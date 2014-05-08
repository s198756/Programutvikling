/**
 * Created by Sebastian Ramsland on 01.05.2014.
 */

import java.sql.*;

public class PersonUpdate {
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://sebastianramsla3.mysql.domeneshop.no/sebastianramsla3";
    private static final String DB_USER = "sebastianramsla3";
    private static final String DB_PASSWORD = "pjW8iUnH";


    public PersonUpdate (long old_person_no, long new_person_no, boolean is_broker, String firstname, String middlename,
                        String surname, String street, String street_no, int zip_code, int telephone,
                        String email, int annual_revenue, boolean passed_credit_check, boolean housepets,
                        boolean smoker, String marital_status, boolean handicap_accomm) throws SQLException{

        Connection con = null;
        PreparedStatement pst = null;

        try {
            Class.forName(DB_DRIVER);

            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String updatePersonSQL = "UPDATE person SET person_no = ?," +
                                                    "is_broker = ?," +
                                                    "firstname = ?," +
                                                    "middlename = ?," +
                                                    "surname = ?," +
                                                    "street = ?," +
                                                    "street_no = ?," +
                                                    "zip_code = ?," +
                                                    "telephone = ?," +
                                                    "email = ?," +
                                                    "annual_revenue = ?," +
                                                    "passed_credit_check = ?," +
                                                    "housepets = ?," +
                                                    "smoker = ?," +
                                                    "marital_status = ?," +
                                                    "handicap_accomm = ?" +
                                "WHERE person_no = ?";

            pst = con.prepareStatement(updatePersonSQL);
            con.setAutoCommit(false);

            pst.setLong(1, new_person_no);
            pst.setBoolean(2, is_broker);
            pst.setString(3, firstname);
            pst.setString(4, middlename);
            pst.setString(5, surname);
            pst.setString(6, street);
            pst.setString(7, street_no);
            pst.setInt(8, zip_code);
            pst.setInt(9, telephone);
            pst.setString(10, email);
            pst.setInt(11, annual_revenue);
            pst.setBoolean(12, passed_credit_check);
            pst.setBoolean(13, housepets);
            pst.setBoolean(14, smoker);
            pst.setString(15, marital_status);
            pst.setBoolean(16, handicap_accomm);
            pst.setLong(17, old_person_no);
            pst.addBatch();

            int[] count = pst.executeBatch();

            con.commit();

            pst.close();
            con.close();
        }

        catch (Exception e) {
            System.out.println(e);
        }

        finally {
            if (pst != null) {
                pst.close();
            }

            if (con != null) {
                con.close();
            }
        }
    }
}


