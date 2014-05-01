/**
 * Created by sebastianramsland on 30.04.14.
 */

import java.sql.*;
import java.util.Date;

public class Person {

    // JDBC driver name and database URL
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://sebastianramsla3.mysql.domeneshop.no/sebastianramsla3";
    private static final String DB_USER = "sebastianramsla3";
    private static final String DB_PASSWORD = "pjW8iUnH";

    // Generell informasjon
    long personNo;                              // Personnummer
    boolean isBroker;                           // Er personen megler?
    String firstname;                           // Fornavn
    String middlename;                          // Mellomnavn
    String surname;                             // Etternavn
    String street;                              // Gateadresse
    String streetNo;                            // Gatenummer (med mulighet for oppgangsbokstav)
    int zipCode;                                // Postnummer
    long telephoneNo;                           // Telefonnummer
    String email;                               // Epostadresse
    int annualRevenue;                          // Årsinntekt
    boolean hasPassedCreditCheck;               // Har personen bestått kredittsjekk?
    boolean hasHousepets;                       // Har personen husdyr?
    boolean isSmoker;                           // Røyker personen?
    String maritalStatus;                       // Sivilstatus
    boolean needsHandicapAccommodation;         // Trenger personen handicaptilpasning?

    // Timestamps
    Date created;
    Date lastModified;

    public Person (long pNo) throws SQLException {

        PreparedStatement pst = null;
        Connection con = null;
        try {
            Class.forName(DB_DRIVER);
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String sql = "SELECT * FROM person WHERE person_no = ?";

            pst = con.prepareStatement(sql);

            pst.setLong(1, pNo);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                // Generell informasjon om person
                personNo = rs.getLong("person_no");
                isBroker = rs.getBoolean("is_broker");
                firstname = rs.getString("firstname");
                middlename = rs.getString("middlename");
                surname = rs.getString("surname");

                // Kontaktinformasjon
                street = rs.getString("street");
                streetNo = rs.getString("street_no");
                zipCode = rs.getInt("zip_code");
                telephoneNo = rs.getLong("telephone");
                email = rs.getString("email");

                // Informasjon tilknyttet til boligsøk
                annualRevenue = rs.getInt("annual_revenue");
                hasPassedCreditCheck = rs.getBoolean("passed_credit_check");
                hasHousepets = rs.getBoolean("housepets");
                isSmoker = rs.getBoolean("smoker");
                maritalStatus = rs.getString("marital_status");
                needsHandicapAccommodation = rs.getBoolean("handicap_accomm");

                // Timestamps
                created = rs.getDate("created");
                lastModified = rs.getDate("last_modified");

                System.out.println("Personnummer:" + personNo);
                System.out.println("Megler:" + isBroker);
                System.out.println("Fornavn:" + firstname);
                System.out.println("Mellomnavn:" + middlename);
                System.out.println("Etternavn:" + surname);
                System.out.println("Gate:" + street);
                System.out.println("Nummer:" + streetNo);
                System.out.println("Postnummer:" + zipCode);
                System.out.println("Telefon:" + telephoneNo);
                System.out.println("Epost:" + email);
                System.out.println("Årsinntekt:" + annualRevenue);
                System.out.println("Bestått kreditsjekk:" + hasPassedCreditCheck);
                System.out.println("Husdyr:" + hasHousepets);
                System.out.println("Røyker:" + isSmoker);
                System.out.println("Sivilstatus:" + maritalStatus);
                System.out.println("Handicaptilpasning:" + needsHandicapAccommodation);
            }

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

    public long getPersonNo() {
        return personNo;
    }

    public String getFullName() {
        return firstname + " " + middlename + " " + surname;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getMiddleName() {
        return middlename;
    }

    public String getSurName() {
        return surname;
    }

    public boolean getIsBroker() {
        return isBroker;
    }

    public String getFullAddress() {
        return street + " " + streetNo + " " + zipCode;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetNo() {
        return streetNo;
    }

    public int getZipCode() {
        return zipCode;
    }

    public long getTelephoneNo() {
        return telephoneNo;
    }

    public String getEmail() {
        return email;
    }

    public int getAnnualRevenue() {
        return annualRevenue;
    }

    public boolean getHasPassedCreditCheck() {
        return hasPassedCreditCheck;
    }

    public boolean getHasHousepets() {
        return hasHousepets;
    }

    public boolean getIsSmoker() {
        return isSmoker;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public boolean getNeedsHandicapAccommodation() {
        return needsHandicapAccommodation;
    }

    public Date getCreatedDate()  {
        return created;
    }

    public Date getLastModifiedDate()  {
        return lastModified;
    }
}
