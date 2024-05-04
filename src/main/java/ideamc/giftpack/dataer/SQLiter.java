package ideamc.giftpack.dataer;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ideamc.giftpack.GiftPackMain;
import ideamc.giftpack.api.Gift;
import ideamc.giftpack.api.GiftPack;
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
 *
 * TODO 指定uid的覆盖 去除表中的name列(不再支持直接获取name)
 */
public class SQLiter implements GiftPackData {
    private static final GiftPackMain instance = GiftPackMain.getInstance();
    private static final String TABLE_giftpack;
    private DataSource dataSource;
    public DataSource getDataSource() {
        return dataSource;
    }

    static {
        TABLE_giftpack = "CREATE TABLE \"main\".\"giftpack\" (\"uid\" integer NOT NULL PRIMARY KEY AUTOINCREMENT,\"itemstack\" TEXT,\"creator\" TEXT,\"inventory\" TEXT,\"time\" integer);";
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
    public GiftPack getGiftPack(int uid) {
        Connection connection = null;
        try {
            connection = getDataSource().getConnection();

            Statement stmt = connection.createStatement();
            connection.setAutoCommit(false);
            String sql = "SELECT itemstack,creator,inventory FROM giftpack WHERE uid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(uid));

            ResultSet rs = preparedStatement.executeQuery();

            rs.next();

            UUID creator;
            try {
                creator = UUID.fromString(rs.getString("creator"));
            } catch (RuntimeException e) {
                stmt.close();
                connection.commit();
                connection.close();
                return null;
            }

            ItemStack itemStack = ItemStackSerialiser.toItemStack(rs.getString("itemstack"));
            ItemStack[] itemStacks = ItemStackSerialiser.toItemStacks(rs.getString("inventory"));

            stmt.close();
            connection.commit();
            connection.close();

            GiftPack giftPack = new DefaultGiftPack(itemStack,creator);
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
        } finally {
            if (connection != null) {
                try {
                    // 关闭连接
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public ItemStack getDisplayItemstackOfUid(int uid){
        Connection connection = null;
        try {
            connection = getDataSource().getConnection();

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
        } finally {
            if (connection != null) {
                try {
                    // 关闭连接
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Gift[] getGiftOfUid(int startUid, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity can not be 0 or less than 0");
        if (startUid < 0) throw new IllegalArgumentException("start can not less than 0");

        Connection connection = null;
        PreparedStatement preparedStatement;
        try {
            connection = getDataSource().getConnection();
            connection.setAutoCommit(false);

            if (startUid == 0) {
                String sql = "SELECT uid,name,itemstack FROM giftpack LIMIT ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, String.valueOf(quantity));
            } else {
                String sql = "SELECT uid,itemstack, FROM giftpack WHERE rowid >= (SELECT rowid FROM giftpack WHERE uid = ?) LIMIT ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, String.valueOf(startUid));
                preparedStatement.setString(2, String.valueOf(quantity));
            }

            ResultSet rs = preparedStatement.executeQuery();

            List<Gift> giftList = new ArrayList<>();
            while (rs.next()) {
                ItemStack itemstack = ItemStackSerialiser.toItemStack(rs.getString("itemstack"));
                giftList.add(new DefaultGift(itemstack,itemstack.getItemMeta().getDisplayName(),rs.getInt("uid")));
            }

            connection.commit();
            connection.close();

            return giftList.toArray(new Gift[0]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    // 关闭连接
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int size() {
        Connection connection = null;
        try {
            connection = getDataSource().getConnection();

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT uid FROM main.giftpack" );

            int size = 0;
            while (rs.next()) {
                size++;
            }

            connection.close();
            return size;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    // 关闭连接
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int size(UUID uuid) throws DataError {
        ResultSet rs;
        Connection connection;
        int size;
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
            size = rs.getFetchSize();
            connection.close();
        } catch (SQLException e) {
            throw new DataError(e);
        } finally {
            if (connection != null) {
                try {
                    // 关闭连接
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return size;
    }

    @Override
    public int saveGiftPack(GiftPack giftPack, int uid) {
        if (uid > 0) {
            upload(uid,giftPack);
            return uid;
        }

        final long timeMillis = System.currentTimeMillis();
        Connection connection = null;
        try {
            connection = getDataSource().getConnection();
            connection.setAutoCommit(false); // 关闭自动提交模式

            String sql = "INSERT INTO main.giftpack (\"uid\", itemstack, creator, inventory, time) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, uid);
            preparedStatement.setString(2, ItemStackSerialiser.toJson(giftPack.getDisplayItemStack()));
            preparedStatement.setString(3, giftPack.getCreator().toString());
            preparedStatement.setString(4, ItemStackSerialiser.toJson(giftPack.getItemRewards().getContents())); // 假设 json 是你的 JSON 数据
            preparedStatement.setLong(5, timeMillis); // 假设 timeMillis 是你的时间戳

            preparedStatement.executeUpdate(); // 执行插入操作

            connection.commit(); // 提交事务

            connection.close();
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return getUid(timeMillis);
    }
    private void upload(int uid, GiftPack giftPack) {
        Connection connection = null;
        try {
            connection = getDataSource().getConnection();
            connection.setAutoCommit(false); // 关闭自动提交模式


            String sql = "UPDATE \"main\".\"giftpack\" SET \"itemstack\" = ?, \"creator\" = ?, \"inventory\" = ? WHERE uid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,ItemStackSerialiser.toJson(giftPack.getDisplayItemStack()));
            preparedStatement.setString(2,giftPack.getCreator().toString());
            preparedStatement.setObject(3,ItemStackSerialiser.toJson(giftPack.getItemRewards().getContents()));
            preparedStatement.setInt(4,uid);

            preparedStatement.executeUpdate(); // 执行插入操作

            connection.commit(); // 提交事务

            connection.close();
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private int getUid(long timeMillis) {
        Connection connection = null;
        try {
            connection = getDataSource().getConnection();
            Statement stmt = connection.createStatement();
            connection.setAutoCommit(false);
            String sql = "SELECT uid FROM giftpack WHERE time = ?";
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
        } finally {
            if (connection != null) {
                try {
                    // 关闭连接
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void initialization() {
        {
            Connection connection;
            try {
                connection = getDataSource().getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                Statement stmt = connection.createStatement();
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
            }  finally {
                try {
                    // 关闭连接
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void close() {
        dataSource = null;
    }
}

