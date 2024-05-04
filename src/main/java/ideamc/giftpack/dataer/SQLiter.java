package ideamc.giftpack.dataer;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ideamc.giftpack.GiftPackMain;
import ideamc.giftpack.api.Gift;
import ideamc.giftpack.api.GiftPackData;
import ideamc.giftpack.utils.DefaultGift;
import ideamc.giftpack.utils.ItemStackSerialiser;
import ideamc.giftpack.error.DataError;
import ideamc.giftpack.utils.DefaultGiftPack;
import org.bukkit.inventory.ItemStack;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author xiantiao
 * @date 2024/4/30
 * GiftPack
 */
public class SQLiter implements GiftPackData {
    private static final GiftPackMain instance = GiftPackMain.getInstance();
    private static final String TABLE_giftpack;
    private DataSource dataSource;
    public DataSource getDataSource() {
        return dataSource;
    }

    static {
        TABLE_giftpack = "CREATE TABLE \"main\".\"giftpack\" (\"uid\" integer NOT NULL PRIMARY KEY AUTOINCREMENT,\"name\" TEXT(32),\"itemstack\" TEXT,\"creator\" TEXT,\"inventory\" TEXT,\"time\" integer);";
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public SQLiter() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:"+instance.getDataFolder()+"/data.db");
        config.setMaximumPoolSize(1); // 设置连接池的最大连接数
        dataSource = new HikariDataSource(config);
    }

    public SQLiter(String s) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:"+s);
        config.setMaximumPoolSize(1); // 设置连接池的最大连接数
        dataSource = new HikariDataSource(config);
    }


    @Override
    public DefaultGiftPack getGiftPack(int uid) {
        try {
            Connection connection = getDataSource().getConnection();

            Statement stmt = connection.createStatement();
            connection.setAutoCommit(false);
            String sql = "SELECT name,itemstack,creator,inventory FROM giftpack WHERE uid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(uid));

            ResultSet rs = preparedStatement.executeQuery();

            rs.next();

            ItemStack itemStack = ItemStackSerialiser.toItemStack(rs.getString("itemstack"));
            UUID creator = UUID.fromString(rs.getString("creator"));
            ItemStack[] itemStacks = ItemStackSerialiser.toItemStacks(rs.getString("inventory"));

            stmt.close();
            connection.commit();
            connection.close();

            DefaultGiftPack giftPack = new DefaultGiftPack(itemStack,creator);
            giftPack.setUid(uid);

            int i = 0;
            while (i<itemStacks.length) {
                ItemStack itemStack1 = itemStacks[i];
                if (itemStack1 != null) giftPack.getItemRewards().setItem(i,itemStack1);
                i++;
            }
            return giftPack;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ItemStack getDisplayItemstackOfUid(int uid){
        try {
            Connection connection = getDataSource().getConnection();

            connection.setAutoCommit(false);
            String sql = "SELECT itemstack FROM giftpack WHERE uid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(uid));

            ResultSet rs = preparedStatement.executeQuery();

            rs.next();

            ItemStack itemStack = ItemStackSerialiser.toItemStack(rs.getString("itemstack"));

            connection.commit();
            connection.close();

            return itemStack;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getDisplayNameOfUid(int uid) {
        try {
            Connection connection = getDataSource().getConnection();

            connection.setAutoCommit(false);
            String sql = "SELECT name FROM giftpack WHERE uid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(uid));

            ResultSet rs = preparedStatement.executeQuery();

            rs.next();

            String name = rs.getString("name");

            connection.commit();
            connection.close();

            return name;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Gift[] getGiftOfUid(int startUid, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity can not be 0 or less than 0");
        if (startUid < 0) throw new IllegalArgumentException("start can not less than 0");

        Connection connection;
        PreparedStatement preparedStatement;

        try {
            connection = getDataSource().getConnection();
            connection.setAutoCommit(false);

            if (startUid == 0) {
                String sql = "SELECT uid,name,itemstack FROM giftpack LIMIT ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, String.valueOf(quantity));
            } else {
                String sql = "SELECT uid,name,itemstack, FROM giftpack WHERE rowid >= (SELECT rowid FROM giftpack WHERE uid = ?) LIMIT ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, String.valueOf(startUid));
                preparedStatement.setString(2, String.valueOf(quantity));
            }

            ResultSet rs = preparedStatement.executeQuery();

            List<Gift> giftList = new ArrayList<>();
            while (rs.next()) {
                ItemStack itemStack = new ItemStack(ItemStackSerialiser.toItemStack(rs.getString("itemstack")));
                giftList.add(new DefaultGift(itemStack,rs.getString("name"),rs.getInt("uid")));
            }

            connection.commit();
            connection.close();

            return giftList.toArray(new Gift[0]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        try {
            Connection connection = getDataSource().getConnection();

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT uid FROM main.giftpack" );

            int size = 0;
            while (rs.next()) {
                size++;
            }

            stmt.close();
            rs.close();
            connection.close();
            return size;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size(UUID uuid) throws DataError {
        Statement stmt;
        ResultSet rs;
        Connection connection;
        try {
            connection = getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            String sql = "SELECT uid FROM main.giftpack WHERE creator = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            rs = preparedStatement.executeQuery();
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
    public int saveGiftPack(DefaultGiftPack giftPack, int uid) {
        final long timeMillis = System.currentTimeMillis();

        Connection connection;
        try {
            connection = getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            connection.setAutoCommit(false); // 关闭自动提交模式


            String sql;
            PreparedStatement preparedStatement;
            if (uid < 1) {
                sql = "INSERT INTO main.giftpack (itemstack, creator, inventory, time) VALUES (?, ?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, ItemStackSerialiser.toJson(giftPack.getDisplayItemStack()));
                preparedStatement.setString(2, giftPack.getCreator().toString());
                preparedStatement.setString(3, ItemStackSerialiser.toJson(giftPack.getItemRewards().getContents())); // 假设 json 是你的 JSON 数据
                preparedStatement.setLong(4, timeMillis); // 假设 timeMillis 是你的时间戳
            } else {
                sql = "INSERT INTO main.giftpack (\"uid\", itemstack, creator, inventory, time) VALUES (?, ?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, String.valueOf(uid));
                preparedStatement.setString(2, ItemStackSerialiser.toJson(giftPack.getDisplayItemStack()));
                preparedStatement.setString(3, giftPack.getCreator().toString());
                preparedStatement.setString(4, ItemStackSerialiser.toJson(giftPack.getItemRewards().getContents())); // 假设 json 是你的 JSON 数据
                preparedStatement.setLong(5, timeMillis); // 假设 timeMillis 是你的时间戳
            }

            preparedStatement.executeUpdate(); // 执行插入操作

            connection.commit(); // 提交事务

            preparedStatement.close();
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

        if (uid < 1) {
            return getUid(timeMillis);
        } return uid;
    }
    
    private int getUid(long timeMillis) {
        Connection connection;
        try {
            connection = getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            Statement stmt = connection.createStatement();
            connection.setAutoCommit(false);
            String sql = "SELECT * FROM giftpack WHERE time = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(timeMillis));


            ResultSet rs = preparedStatement.executeQuery();

            rs.next();

            // 处理查询结果
            int uid = rs.getInt("uid");
            connection.commit();
            connection.close();
            stmt.close();
            return uid;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialization() {
        {
            Statement stmt;

            Connection connection;
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
                try {
                    connection.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                if (!"[SQLITE_ERROR] SQL error or missing database (table \"giftpack\" already exists)".equals(e.getMessage())) {
                    throw new RuntimeException(e);
                }
            }

        }

    }

    @Override
    public void close() {
        dataSource = null;
    }
}

