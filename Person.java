/**
 * Created by sebastianramsland on 30.04.14.
 */

import com.sun.rowset.CachedRowSetImpl;
import java.sql.*;

public class Person {

    // Tabellnavn
    private static final String TABLENAME = "person";

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
    private String category;                            // Beskrivelse av postnummer
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

    // Oppretter CachedRowSet og radnummer
    private CachedRowSetImpl cachedRowSet;
    private int currentRowNumber = 1;
    private int nextRowNumber;
    private int previousRowNumber;

    public Person () {

        // Utfører SQL-query
        if (!dbInterface.execQuery(
                "SELECT person_no, is_broker, firstname, middlename, surname, street, street_no, zip_code, telephone, email, annual_revenue, passed_credit_check, housepets, smoker, marital_status, handicap_accomm, created, last_modified FROM person")) {

            // Stenger programmet ved feil
            System.exit(1);
        }

        // Lager CachedRowSet av SQL-queryen
        try {
            cachedRowSet = new CachedRowSetImpl();
            cachedRowSet = dbInterface.getRowSet();
            cachedRowSet.setTableName(TABLENAME);
            cachedRowSet.first();
        }
        catch (SQLException se) {
            System.out.println("Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage());
        }
    }

    // Innlasting av person med nåværende radnummer
    public void refreshValues() throws SQLException {
        try {
            // Hopper til riktig rad
            cachedRowSet.absolute(currentRowNumber);

            // Leser av verdier

            // Generell informasjon om person
            personNo = cachedRowSet.getString("person_no");
            isBroker = cachedRowSet.getBoolean("is_broker");
            firstname = cachedRowSet.getString("firstname");
            middlename = cachedRowSet.getString("middlename");
            surname = cachedRowSet.getString("surname");

            // Kontaktinformasjon
            street = cachedRowSet.getString("street");
            streetNo = cachedRowSet.getString("street_no");
            zipCode = cachedRowSet.getInt("zip_code");
            telephoneNo = cachedRowSet.getLong("telephone");
            email = cachedRowSet.getString("email");

            // Informasjon tilknyttet til boligsøk
            annualRevenue = cachedRowSet.getInt("annual_revenue");
            hasPassedCreditCheck = cachedRowSet.getBoolean("passed_credit_check");
            hasHousepets = cachedRowSet.getBoolean("housepets");
            isSmoker = cachedRowSet.getBoolean("smoker");
            maritalStatus = cachedRowSet.getString("marital_status");
            needsHandicapAccommodation = cachedRowSet.getBoolean("handicap_accomm");

            // Timestamps
            createdDate = cachedRowSet.getTimestamp("created");
            lastModifiedDate = cachedRowSet.getTimestamp("last_modified");

        } catch (SQLException se) {
            System.out.println("Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage());
        }

        // Innhenting av stedsinformasjon
        Location location = new Location();
        location.refreshValues(zipCode);

        area = location.getArea();
        township = location.getTownship();
        county = location.getCounty();
        category = location.getCategory();


        // Printer ut verdier
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
        System.out.println("Kategori: " + category);
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

    // Innlasting av person med et spesifisert radnummer
    public void jumpToPerson(int rowNo) throws SQLException {
        currentRowNumber = rowNo;
        previousRowNumber -= currentRowNumber - 1;
        nextRowNumber += currentRowNumber + 1;
        try {
            refreshValues();
        }
        catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Innlasting av neste person
    public void nextPerson() throws SQLException {
        currentRowNumber += 1;
        previousRowNumber = currentRowNumber - 1;
        nextRowNumber = currentRowNumber + 1;
        try {
            System.out.println("Hopper til neste person. \n");
            cachedRowSet.next();
            refreshValues();
        }
        catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Innlasting av forrige person
    public void previousPerson() throws SQLException {
        currentRowNumber -= 1;
        previousRowNumber = currentRowNumber - 1;
        nextRowNumber = currentRowNumber + 1;
        try {
            System.out.println("Hopper tilbake til forrige person. \n");
            cachedRowSet.previous();
            refreshValues();
        }
        catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Flytter pekeren til en innsettingsrad. Må kalles opp ved opprettelse av ny person.
    public void moveToInsertRow() throws SQLException {
        try {
            cachedRowSet.moveToInsertRow();
            System.out.println("Peker flyttet til innsettingsrad. \n");
        }
        catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Setter inn nåværende innsettingsrad. Pekeren må befinne seg på en innsettingsrad.
    public void insertRow() throws SQLException {
        try {
            cachedRowSet.insertRow();
            System.out.println("Raden ble lagret i cache. \n");
        }
        catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Pekeren flyttes til nåværende rad. Har kun effekt om pekeren er på en innsettingsrad.
    public void moveToCurrentRow() throws SQLException {
        try {
            cachedRowSet.moveToCurrentRow();
            System.out.println("Peker flyttet til nåværende rad. \n");
        }
        catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Kansellerer alle oppdateringer
    public void cancelUpdates() throws SQLException {
        try {
            cachedRowSet.cancelRowUpdates();
            System.out.println("Alle endringer lagret i cachen ble fjernet. \n");
        }
        catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Søk opp person
    public void findPerson(String pNo) throws SQLException {
        try {
            // Hopper til toppen av tabellen
            cachedRowSet.first();

            // Søker igjennom tabellen
            while(cachedRowSet.next()) {
                if(pNo.equals(cachedRowSet.getString("person_no"))) {
                    System.out.println("Personen ble funnet. Flytter pekeren til personen og henter verdier \n");
                    jumpToPerson(cachedRowSet.getRow());
                    return;
                }
            }
            System.out.println("Fant ikke personen med det angitte personnummeret.");
            return;
        }
        catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Oppdateringsmetode for Stringverdier
    public void updateStringValue(String columnName, String value) throws SQLException {
        try {
            // Oppdaterer felt
            cachedRowSet.updateString(columnName, value);
            System.out.println("String-verdi oppdatert. \n");

        }
        catch (SQLException s) {
            System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
        }
    }

    // Oppdateringsmetode for Intverdier
    public void updateIntValue(String columnName, int value) throws SQLException {
        try {
            // Oppdaterer felt
            cachedRowSet.updateInt(columnName, value);
            System.out.println("Int-verdi oppdatert. \n");

        }
        catch (SQLException s) {
            System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
        }
    }

    // Oppdateringsmetode for Booleanverdier
    public void updateBooleanValue(String columnName, boolean value) throws SQLException {
        try {
            // Oppdaterer felt
            cachedRowSet.updateBoolean(columnName, value);
            System.out.println("Boolean-verdi oppdatert. \n");

        }
        catch (SQLException s) {
            System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
        }
    }

    // Oppdaterer nåværende rad
    public void updateRow() throws SQLException {
        try {
            cachedRowSet.updateRow();
            System.out.println("Oppdatering av raden ble lagret i cache. \n");
        }
        catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Sletter nåværende rad
    public void deleteRow() throws SQLException {
        try {
            cachedRowSet.deleteRow();
            System.out.println("Raden ble slettet fra cache. \n");
        }
        catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Sender de oppdaterte feltene til databasen
    public void acceptChanges() throws SQLException {
        if (dbInterface.commitToDatabase(cachedRowSet)) {
            System.out.println("Alle endringer i cache ble sendt til databasen.");
        }
        else {
            System.out.println("Feil: Kunne ikke sende cache-endringer til databasen. \n");
        }
    }

    public int getCurrentRowNumber() {
        return currentRowNumber;
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

    public String getCategory()
    {
        return category;
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