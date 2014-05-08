/**
 * Created by sebastianramsland on 30.04.14.
 */

import com.sun.rowset.CachedRowSetImpl;
import java.sql.*;

public class Person {

    // Generell informasjon
    private String personNo;                            // Personnummer
    private boolean isBroker;                           // Er personen megler?
    private String firstname;                           // Fornavn
    private String middlename;                          // Mellomnavn
    private String surname;                             // Etternavn
    private String street;                              // Gateadresse
    private String streetNo;                            // Gatenummer (med mulighet for oppgangsbokstav)
    private int zipCode;                                // Postnummer
    private String area;                                // Poststed
    private String township;                            // Kommune
    private String county;                              // Fylke
    private long telephoneNo;                           // Telefonnummer
    private String email;                               // Epostadresse
    private int annualRevenue;                          // Årsinntekt
    private boolean hasPassedCreditCheck;               // Har personen bestått kredittsjekk?
    private boolean hasHousepets;                       // Har personen husdyr?
    private boolean isSmoker;                           // Røyker personen?
    private String maritalStatus;                       // Sivilstatus
    private boolean needsHandicapAccommodation;         // Trenger personen handicaptilpasning?

    // Timestamps
    private Timestamp createdDate;
    private Timestamp lastModifiedDate;

    // Oppretter SQLInterface
    private SQLInterface dbInterface = new SQLInterface();
    private CachedRowSetImpl crs;

    public Person (String pNo) {
        personNo = pNo;
    }

    // Metode som innhenter alle dataverdier tilhørende personen
    public void startQuery() throws SQLException {

        // create SQLInterface object
        // execute query
        if (!dbInterface.execQuery(
                "SELECT person_no, is_broker, firstname, middlename, surname, street, street_no, zip_code, area, township, county, telephone, email, annual_revenue, passed_credit_check, housepets, smoker, marital_status, handicap_accomm, created, last_modified FROM person_all_fields WHERE person_no = " + personNo)) {

            // exception caught, halt execution
            System.exit(1);
        }

        // create CachedRowSet
        try {
            crs = new CachedRowSetImpl();
            crs = dbInterface.getRowSet();

            while (crs.next()) {

                // Generell informasjon om person
                personNo = crs.getString(1);
                isBroker = crs.getBoolean(2);
                firstname = crs.getString(3);
                middlename = crs.getString(4);
                surname = crs.getString(5);

                // Kontaktinformasjon
                street = crs.getString(6);
                streetNo = crs.getString(7);
                zipCode = crs.getInt(8);
                area = crs.getString(9);
                township = crs.getString(10);
                county = crs.getString(11);
                telephoneNo = crs.getLong(12);
                email = crs.getString(13);

                // Informasjon tilknyttet til boligsøk
                annualRevenue = crs.getInt(14);
                hasPassedCreditCheck = crs.getBoolean(15);
                hasHousepets = crs.getBoolean(16);
                isSmoker = crs.getBoolean(17);
                maritalStatus = crs.getString(18);
                needsHandicapAccommodation = crs.getBoolean(19);

                // Timestamps
                createdDate = crs.getTimestamp(20);
                lastModifiedDate = crs.getTimestamp(21);

                System.out.println("Personnummer: " + personNo);
                System.out.println("Megler: " + isBroker);
                System.out.println("Fornavn: " + firstname);
                System.out.println("Mellomnavn: " + middlename);
                System.out.println("Etternavn: " + surname);
                System.out.println("Gate: " + street);
                System.out.println("Nummer: " + streetNo);
                System.out.println("Postnummer: " + zipCode);
                System.out.println("Poststed: " + area);
                System.out.println("Kommune: " + township);
                System.out.println("Fylke: " + county);
                System.out.println("Telefon: " + telephoneNo);
                System.out.println("Epost: " + email);
                System.out.println("Årsinntekt: " + annualRevenue);
                System.out.println("Bestått kreditsjekk: " + hasPassedCreditCheck);
                System.out.println("Husdyr: " + hasHousepets);
                System.out.println("Røyker: " + isSmoker);
                System.out.println("Sivilstatus: " + maritalStatus);
                System.out.println("Handicaptilpasning: " + needsHandicapAccommodation);
                System.out.println("Opprettet: " + createdDate);
                System.out.println("Sist endret: " + lastModifiedDate);
                System.out.println("\n /////// END OF PERSON //////// \n");
            }
        } catch (SQLException se) {
            System.out.println("SQL Exception Error ved innhenting av data.");
        }
    }

    // Oppdateringsmetode for Stringverdier
    public void updateStringValue(String columnName, String value) throws SQLException {
        try {
            // Hopper til øverste rad
            crs.first();

            // Oppdaterer felt
            crs.updateString(columnName, value);
            System.out.println("UpdateString...");
            crs.updateRow();
            System.out.println("UpdateRow...");

        }
        catch (SQLException s) {
            System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
        }
    }

    // Oppdateringsmetode for Intverdier
    public void updateIntValue(String columnName, int value) throws SQLException {
        try {
            // Hopper til øverste rad
            crs.first();

            // Oppdaterer felt
            crs.updateInt(columnName, value);
            System.out.println("UpdateInt...");
            crs.updateRow();
            System.out.println("UpdateRow...");

        }
        catch (SQLException s) {
            System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
        }
    }

    // Oppdateringsmetode for Booleanverdier
    public void updateBooleanValue(String columnName, boolean value) throws SQLException {
        try {
            // Hopper til øverste rad
            crs.first();

            // Oppdaterer felt
            crs.updateBoolean(columnName, value);
            System.out.println("UpdateBoolean...");
            crs.updateRow();
            System.out.println("UpdateRow...");

        }
        catch (SQLException s) {
            System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
        }
    }

    // Sender de oppdaterte feltene til databasen
    public void commitChanges() {
        if (dbInterface.commitToDatabase(crs)) {
            System.out.println("Successfully committed to the database.");
        }
        else {
            System.out.println("Error: Could NOT commit to the database.");
        }
    }

    public String getPersonNo() {
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

    public String getArea() {
        return area;
    }

    public String getTownship() {
        return township;
    }

    public String getCounty() {
        return county;
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

    public Timestamp getCreatedDate()  {
        return createdDate;
    }

    public Timestamp getLastModifiedDate()  {
        return lastModifiedDate;
    }
}