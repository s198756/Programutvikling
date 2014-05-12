/**
 * Created by Sebastian Ramsland on 06.05.2014.
 */

import com.sun.rowset.CachedRowSetImpl;
import javax.sql.rowset.FilteredRowSet;
import java.sql.*;

public class SQLInterface {

    // Innloggingsinformasjon
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://sebastianramsla3.mysql.domeneshop.no/sebastianramsla3";
    private static final String DB_USER = "sebastianramsla3";
    private static final String DB_PASSWORD = "pjW8iUnH";
    private static final String DB_SCHEMA = "sebastianramsla3";

    private CachedRowSetImpl crs;

    public SQLInterface() {
    }

    public boolean execQuery(String query) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;

        try {
            Class.forName(DB_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            conn.setAutoCommit(false);  // Need to disable auto-commit for CachedRowSet

            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);

            // Oppretter CahedRowSet og setter inn verdier fra et resultSet.
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);


            // Stenger DB-tilkobling
            conn.close();

            return true;

        } catch (SQLException se) {
            System.out.println("Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage());
            return false;

        } catch (Exception e) {
            System.out.println("\tLocalizedMessage: " + e.getLocalizedMessage());
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
            conn.setAutoCommit(false);  // Behøver å hindre "auto-commit" ved bruk av CachedRowSet

            // Send endringer og steng deretter tilkoblingen til databasen
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

    public boolean commitToDatabase(FilteredRowSet frs) {
        Connection conn = null;

        try {
            Class.forName(DB_DRIVER).newInstance();
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            conn.setAutoCommit(false);  // Behøver å hindre "auto-commit" ved bruk av FilteredRowSet
            conn.setReadOnly(false);
            conn.setSchema(DB_SCHEMA);

            // Send endringer og steng deretter tilkoblingen til databasen
            frs.acceptChanges(conn);
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