package ideamc.giftpack.dataer.sqlite;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ideamc.giftpack.GiftPackMain;
import ideamc.giftpack.dataer.Data;
import ideamc.giftpack.dataer.OptionalTypeAdapter;
import ideamc.giftpack.error.DataError;
import ideamc.giftpack.error.SaveDataError;
import ideamc.giftpack.utils.GiftPack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author xiantiao
 * @date 2024/4/30
 * GiftPack
 */
public class SQLiter implements Data {
    private static final GiftPackMain instance = GiftPackMain.getInstance();
    private static final Logger logger = GiftPackMain.getInstance().getLogger();
    private static final String TABLE_giftpack;
    private final String dbFile;
    private DataSource dataSource;
    public DataSource getDataSource() {
        return dataSource;
    }

    static {
        TABLE_giftpack = "CREATE TABLE \"main\".\"giftpack\" (\"uid\" integer NOT NULL PRIMARY KEY AUTOINCREMENT,\"name\" TEXT(32),\"itemstack\" TEXT,\"lore\" TEXT,\"creator\" TEXT,\"inventory\" TEXT,\"time\" integer);";
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public SQLiter(String dbFile) {
        // 创建 HikariCP 配置
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:"+dbFile);
        config.setMaximumPoolSize(500); // 设置连接池的最大连接数

        // 创建数据源
        dataSource = new HikariDataSource(config);

        this.dbFile = dbFile;
    }

    @Override
    public GiftPack getGiftPack(int i) {
        return null;
    }

    @Override
    public int size() throws DataError {
        Statement stmt;
        ResultSet rs;
        Connection connection;
        try {
            connection = getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery( "SELECT uid FROM main.giftpack" );
        } catch (SQLException e) {
            throw new DataError(e);
        }

        int size;
        try {
            size = rs.getFetchSize();
            connection.close();
        } catch (SQLException e) {
            throw new DataError(e);
        }

        return size;
    }

    @Override
    public int size(UUID uuid) throws DataError {
        Statement stmt;
        ResultSet rs;
        Connection connection = null;
        try {
            connection = getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery( "SELECT uid FROM main.giftpack WHERE creator = "+uuid);
        } catch (SQLException e) {
            throw new DataError(e);
        }

        int size;
        try {
            size = rs.getFetchSize();
        } catch (SQLException e) {
            throw new DataError(e);
        }

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return size;
    }

    @Override
    public int saveGiftPack(GiftPack giftPack) {
        Map<Integer, ItemStack> intTtemMap = new HashMap<>();
        Inventory giftPackInventory = giftPack.getInventory();

        int i = 0;
        for (ItemStack itemStack : giftPackInventory.getContents()) {
            if (itemStack != null) {
                intTtemMap.put(i,itemStack);
            }
            i++;
        }

        String json = intTtemMap.toString();
        logger.info("json "+intTtemMap);

        Statement stmt = null;

        final long timeMillis = System.currentTimeMillis();

        Connection connection;
        try {
            connection = getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            connection.setAutoCommit(false); // 关闭自动提交模式

            String sql = "INSERT INTO main.giftpack (name, itemstack, lore, creator, inventory, time) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "name");
            preparedStatement.setString(2, "itemstack");
            preparedStatement.setString(3, "lore");
            preparedStatement.setString(4, "creator");
            preparedStatement.setString(5, json); // 假设 json 是你的 JSON 数据
            preparedStatement.setLong(6, timeMillis); // 假设 timeMillis 是你的时间戳

            preparedStatement.executeUpdate(); // 执行插入操作

            logger.info("executeUpdate "+timeMillis);

            connection.commit(); // 提交事务
        } catch (SQLException e) {
            try {
                connection.rollback(); // 回滚事务
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e); // 抛出运行时异常
        } finally {
            try {
                connection.setAutoCommit(true); // 恢复自动提交模式
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            connection = getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int uid;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT uid FROM main.giftpack WHERE time = " + timeMillis+";" );
            logger.info("executeQuery "+timeMillis);
            logger.info(rs.toString());
            if (rs.getFetchSize() != 1) {
                try {
                    throw new SaveDataError(SaveDataError.SaveDataErrorType.CanNotGetUid);
                } catch (SaveDataError e) {
                    throw new RuntimeException(e);
                }
            }

            uid = rs.getInt(0);

            stmt.close();
            connection.commit();
        } catch (SQLException e) {
            try {
                throw new DataError(e);
            } catch (DataError ex) {
                throw new RuntimeException(ex);
            }
        }

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return uid;
    }

    @Override
    public void saveGiftPack(GiftPack giftPack, int uid) {

    }

    @Override
    public void initialization() {
        logger.info("initialization SQLiter...");

        {
            logger.info("initialization SQLiter table giftpack...");
            Statement stmt;

            Connection connection = null;
            try {
                connection = getDataSource().getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                stmt = connection.createStatement();
                stmt.executeUpdate(TABLE_giftpack);
                stmt.close();
                connection.close();
            } catch (SQLException e) {
                if (!"[SQLITE_ERROR] SQL error or missing database (table \"giftpack\" already exists)".equals(e.getMessage())) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    throw new RuntimeException(e);
                }
            }

            logger.info("initialization SQLiter table giftpack ok!");

            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        logger.info("initialization SQLiter ok!");

    }



    @Override
    public void close() {

    }

    /**
     * 将对象序列化为 JSON 字符串
     * @param obj 要序列化的对象
     * @return 对象的 JSON 字符串表示形式
     */
    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }

    /**
     * 将 JSON 字符串反序列化为对象
     * @param json JSON 字符串
     * @param clazz 对象的类类型
     * @param <T> 对象的类型
     * @return JSON 字符串表示的对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }
}

