import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author xiantiao
 * @date 2024/4/30
 * GiftPack
 */

public class SQLiteJDBC {
    public static void main( String args[] )
    {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:C:\\code\\java\\IdeaMC\\Idea\\GiftPack\\src\\main\\resources\\data.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery( "SELECT uid FROM main.giftpack" );

            int size = 0;
            while (rs.next()) {
                size++;
            }

            System.out.println(size);

            stmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }


    static boolean is(String t) {
        Connection connection = SQLiterServer.create();
        boolean Return = false;
        Statement stmt;
        try {
            Class.forName("org.sqlite.JDBC");
            stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery( "SELECT 1 FROM "+t+";" );
            Return = rs.next();
            rs.close();
            stmt.close();
            connection.close();
        } catch ( Exception ignored) {}
        return Return;
    }
}
class SQLiterServer {
    static Connection create() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:C:\\code\\java\\IdeaMC\\Idea\\GiftPack\\src\\main\\resources\\data.db");
        } catch ( Exception e ) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }
}