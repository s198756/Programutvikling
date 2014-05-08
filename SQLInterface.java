/**
 * Created by Sebastian Ramsland on 06.05.2014.
 */
import com.sun.rowset.CachedRowSetImpl;
import java.sql.*;

public class SQLInterface {

    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://sebastianramsla3.mysql.domeneshop.no/sebastianramsla3";
    private static final String DB_USER = "sebastianramsla3";
    private static final String DB_PASSWORD = "pjW8iUnH";

    private CachedRowSetImpl crs;

    public SQLInterface() {
    }

    public boolean execQuery(String query) {
        Statement stmt = null;
        Connection conn = null;
        ResultSet resultSet = null;

        try {
            Class.forName(DB_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            conn.setAutoCommit(false);  // Need to disable auto-commit for CachedRowSet

            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);

            // create CachedRowSet and populate
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);

            // note that the connection is being closed
            conn.close();

            return true;

        } catch (SQLException se) {
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    public CachedRowSetImpl getRowSet() {
        return crs;
    }

    public boolean commitToDatabase(CachedRowSetImpl crs) {
        Connection conn = null;

        try {
            Class.forName(DB_DRIVER).newInstance();
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            conn.setAutoCommit(false);  // Need to disable auto-commit for CachedRowSet

            // propagate changes and close connection
            crs.acceptChanges(conn);
            conn.close();

            return true;

        } catch (SQLException se) {
            System.out.println(se.getLocalizedMessage());
            return false;

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
}