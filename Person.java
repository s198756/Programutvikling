/**
 * Created by sebastianramsland on 30.04.14.
 */

import com.sun.rowset.CachedRowSetImpl;
import javax.sql.rowset.FilteredRowSet;
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

    // Initialiserer SQLInterface
    private SQLInterface dbInterface;

    // Initialiserer FilteredRowSet
    private FilteredRowSet filteredRowSet;

    // Initialiserer CachedRowSet
    private CachedRowSetImpl cachedRowSet;

    // Initialiserer sjekk om klassens RowSet er av typen "Cached" eller "Filtered"
    private boolean rowSetTypeIsFiltered;

    // Oppretter radnummer (til pekeren)
    private int currentRowNumber = 1;
    private int nextRowNumber;
    private int previousRowNumber;

    // Konstruktør som oppretter et nytt CachedRowSet som inneholder ALLE registrerte personer fra databasen
    public Person () {
        // Oppretter nytt SQLInterface
        dbInterface = new SQLInterface();

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

            // Nåværende rowSet er av typen "Cached"
            rowSetTypeIsFiltered = false;
        } catch (SQLException se) {
            System.out.println("Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage());
        }
    }

    // Konstruktør som tar imot eksisterende SQLInterface, CachedRowSet og radnummer (et ufiltrert søk)
    public Person (SQLInterface dbInt, CachedRowSetImpl crs, int rowID) throws SQLException {
        // Setter inn SQLInterface
        dbInterface = dbInt;

        cachedRowSet = crs;
        cachedRowSet.setTableName(TABLENAME);

        // Setter inn mottatt radnummer
        currentRowNumber = rowID;

        // Setter pekeren på radnummeret
        jumpToPerson(rowID);

        // Nåværende rowSet er av typen "Cached"
        rowSetTypeIsFiltered = false;
    }

    // Konstruktør som tar imot eksisterende SQLInterface, FilteredRowSet og radnummer (et filtrert søk)
    public Person (SQLInterface dbInt, FilteredRowSet frs, int rowID) throws SQLException {
        // Setter inn SQLInterface
        dbInterface = dbInt;

        // Setter inn FilteredRowSet og tabellnavn
        filteredRowSet = frs;
        filteredRowSet.setTableName(TABLENAME);

        // Setter inn mottatt radnummer
        currentRowNumber = rowID;

        // Setter pekeren på radnummeret
        jumpToPerson(rowID);

        // Nåværende rowSet er av typen "Filtered"
        rowSetTypeIsFiltered = true;
    }

    // Innlasting av person med nåværende radnummer
    public void refreshValues() throws SQLException {
        if (rowSetTypeIsFiltered) {
            try {
                // Hopper til riktig rad
                filteredRowSet.absolute(currentRowNumber);

                // Leser av verdier

                // Generell informasjon
                personNo = filteredRowSet.getString("person_no");
                isBroker = filteredRowSet.getBoolean("is_broker");
                firstname = filteredRowSet.getString("firstname");
                middlename = filteredRowSet.getString("middlename");
                surname = filteredRowSet.getString("surname");

                // Kontaktinformasjon
                street = filteredRowSet.getString("street");
                streetNo = filteredRowSet.getString("street_no");
                zipCode = filteredRowSet.getInt("zip_code");
                telephoneNo = filteredRowSet.getLong("telephone");
                email = filteredRowSet.getString("email");

                // Informasjon tilknyttet til boligsøk
                annualRevenue = filteredRowSet.getInt("annual_revenue");
                hasPassedCreditCheck = filteredRowSet.getBoolean("passed_credit_check");
                hasHousepets = filteredRowSet.getBoolean("housepets");
                isSmoker = filteredRowSet.getBoolean("smoker");
                maritalStatus = filteredRowSet.getString("marital_status");
                needsHandicapAccommodation = filteredRowSet.getBoolean("handicap_accomm");

                // Timestamps
                createdDate = filteredRowSet.getTimestamp("created");
                lastModifiedDate = filteredRowSet.getTimestamp("last_modified");

            } catch (SQLException se) {
                System.out.println("Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage());
            }
        }
        else {
            try {
                // Hopper til riktig rad
                cachedRowSet.absolute(currentRowNumber);

                // Leser av verdier

                // Generell informasjon
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
        String messageJumpToPerson = "Forsøker å hente ut verdier til personen på rad " + currentRowNumber + ".\n";
        try {
            System.out.println(messageJumpToPerson);
            refreshValues();
        } catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Søk opp person med personnummer (unik verdi)
    public void findPersonWithPersonNo(String pNo) throws SQLException {

        String messageWhenFindPerson = "Personen ble funnet. Flytter pekeren til personen og henter verdier. \n";
        String messageWhenCouldNotFindPersonWithPersonNo = "Fant ikke personen med det angitte personnummeret. \n";

        if (rowSetTypeIsFiltered) {
            try {
                // Hopper først til toppen av tabellen
                filteredRowSet.beforeFirst();

                // Søker igjennom rowSet
                while (filteredRowSet.next()) {
                    if (pNo.equals(filteredRowSet.getString("person_no"))) {
                        System.out.println(messageWhenFindPerson);
                        jumpToPerson(filteredRowSet.getRow());
                        return;
                    }
                }
                System.out.println(messageWhenCouldNotFindPersonWithPersonNo);
            } catch (SQLException e) {
                    System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }

        else {
            try {
                // Hopper først til toppen av tabellen
                cachedRowSet.beforeFirst();

                // Søker igjennom tabellen
                while(cachedRowSet.next()) {
                    if(pNo.equals(cachedRowSet.getString("person_no"))) {
                        System.out.println(messageWhenFindPerson);
                        jumpToPerson(cachedRowSet.getRow());
                        return;
                    }
                }
                System.out.println(messageWhenCouldNotFindPersonWithPersonNo);

            } catch (SQLException e){
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
    }

    // Søk opp person med telefonnummer (unik verdi)
    public void findPersonWithTelephoneNo(long telNo) throws SQLException {

        String messageWhenFindPerson = "Personen ble funnet. Flytter pekeren til personen og henter verdier. \n";
        String messageWhenCouldNotFindPersonWithTelephoneNo = "Fant ikke personen med det angitte telefonnummeret. \n";

        if (rowSetTypeIsFiltered) {
            try {
                // Hopper først til toppen av tabellen
                filteredRowSet.beforeFirst();

                // Søker igjennom tabellen
                while (filteredRowSet.next()) {
                    if (telNo == filteredRowSet.getLong("telephone")) {
                        System.out.println(messageWhenFindPerson);
                        jumpToPerson(filteredRowSet.getRow());
                        return;
                    }
                }
                System.out.println(messageWhenCouldNotFindPersonWithTelephoneNo);
            } catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
        else {
            try {
                // Hopper først til toppen av tabellen
                cachedRowSet.first();

                // Søker igjennom tabellen
                while (cachedRowSet.next()) {
                    if (telNo == cachedRowSet.getLong("telephone")) {
                        System.out.println(messageWhenFindPerson);
                        jumpToPerson(cachedRowSet.getRow());
                        return;
                    }
                }
                System.out.println(messageWhenCouldNotFindPersonWithTelephoneNo);
            } catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
    }

    // Søk opp person med mailadresse (unik verdi)
    public void findPersonWithEMailAddress(String mail) throws SQLException {

        String messageWhenFindPerson = "Personen ble funnet. Flytter pekeren til personen og henter verdier. \n";
        String messageWhenCouldNotFindPersonWithEmail = "Fant ikke personen med den angitte mailadressen. \n";

        if (rowSetTypeIsFiltered) {
            try {
                // Hopper først til toppen av tabellen
                filteredRowSet.beforeFirst();

                // Søker igjennom tabellen
                while(filteredRowSet.next()) {
                    if(mail.equals(filteredRowSet.getString("email"))) {
                        System.out.println(messageWhenFindPerson);
                        jumpToPerson(filteredRowSet.getRow());
                        return;
                    }
                }
                System.out.println(messageWhenCouldNotFindPersonWithEmail);
            } catch (SQLException e){
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
        else {
            try {
                // Hopper først til toppen av tabellen
                cachedRowSet.beforeFirst();

                // Søker igjennom tabellen
                while(cachedRowSet.next()) {
                    if(mail.equals(cachedRowSet.getString("email"))) {
                        System.out.println(messageWhenFindPerson);
                        jumpToPerson(cachedRowSet.getRow());
                        return;
                    }
                }
                System.out.println(messageWhenCouldNotFindPersonWithEmail);
            } catch (SQLException e){
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
    }

    // Innlasting av neste person
    public void nextPerson() throws SQLException {
        currentRowNumber += 1;
        previousRowNumber = currentRowNumber - 1;
        nextRowNumber = currentRowNumber + 1;
        String messageWhenNextPerson = "Hopper til neste person. \n";

        if (rowSetTypeIsFiltered) {
            try {
                System.out.println(messageWhenNextPerson);
                filteredRowSet.next();
                refreshValues();
            } catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
        else {
            try {
                System.out.println(messageWhenNextPerson);
                cachedRowSet.next();
                refreshValues();
            } catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
    }

    // Innlasting av forrige person
    public void previousPerson() throws SQLException {
        currentRowNumber -= 1;
        previousRowNumber = currentRowNumber - 1;
        nextRowNumber = currentRowNumber + 1;
        String messageWhenPreviousPerson = "Hopper tilbake til forrige person. \n";

        if (rowSetTypeIsFiltered) {
            try {
                System.out.println(messageWhenPreviousPerson);
                filteredRowSet.previous();
                refreshValues();
            } catch (SQLException e){
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
        else {
            try {
                System.out.println(messageWhenPreviousPerson);
                cachedRowSet.previous();
                refreshValues();
            } catch (SQLException e){
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
    }

    // Flytter pekeren til en innsettingsrad. Må kalles opp ved opprettelse av ny person.
    public void moveToInsertRow() throws SQLException {

        String messageWhenMoveToInsertRow = "Peker flyttet til innsettingsrad. \n";

        if (rowSetTypeIsFiltered) {
            try {
                filteredRowSet.moveToInsertRow();
                System.out.println(messageWhenMoveToInsertRow);
            } catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
        else {
            try {
                cachedRowSet.moveToInsertRow();
                System.out.println(messageWhenMoveToInsertRow);
            } catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
    }

    // Setter inn nåværende innsettingsrad. Pekeren må befinne seg på en innsettingsrad.
    public void insertRow() throws SQLException {

        String messageWhenInsertRow = "Raden ble lagret i cache. \n";

        if (rowSetTypeIsFiltered) {
            try {
                filteredRowSet.insertRow();
                System.out.println(messageWhenInsertRow);
            } catch (SQLException e){
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
        else {
            try {
                cachedRowSet.insertRow();
                System.out.println(messageWhenInsertRow);
            } catch (SQLException e){
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
    }

    // Pekeren flyttes til nåværende rad. Har kun effekt om pekeren er på en innsettingsrad.
    public void moveToCurrentRow() throws SQLException {

        String messageWhenMovedToRow = "Peker flyttet til nåværende rad. \n";

        if (rowSetTypeIsFiltered) {
            try {
                filteredRowSet.moveToCurrentRow();
                System.out.println(messageWhenMovedToRow);
            } catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
        else {
            try {
                cachedRowSet.moveToCurrentRow();
                System.out.println(messageWhenMovedToRow);
            } catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
    }

    // Kansellerer alle oppdateringer
    public void cancelUpdates() throws SQLException {

        String messageWhenCancelUpdates = "Alle endringer lagret i cachen ble fjernet. \n";

        if (rowSetTypeIsFiltered) {
            try {
                filteredRowSet.cancelRowUpdates();
                System.out.println(messageWhenCancelUpdates);
            } catch (SQLException e){
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
        else {
            try {
                cachedRowSet.cancelRowUpdates();
                System.out.println(messageWhenCancelUpdates);
            } catch (SQLException e){
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
    }

    // Oppdateringsmetode for Stringverdier
    public void updateStringValue(String columnName, String value) throws SQLException {

        String messageWhenUpdateStringValue = "String-verdi oppdatert. \n";

        if (rowSetTypeIsFiltered) {
            try {
                // Oppdaterer felt til FilteredRowset
                filteredRowSet.updateString(columnName, value);
                System.out.println(messageWhenUpdateStringValue);

            } catch (SQLException s) {
                System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
            }
        }
        else {
            try {
                // Oppdaterer felt til CachedRowSet
                cachedRowSet.updateString(columnName, value);
                System.out.println(messageWhenUpdateStringValue);

            } catch (SQLException s) {
                System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
            }
        }
    }

    // Oppdateringsmetode for Intverdier
    public void updateIntValue(String columnName, int value) throws SQLException {

        String messageWhenUpdateIntValue = "Int-verdi oppdatert. \n";

        if (rowSetTypeIsFiltered) {
            try {
                // Oppdaterer felt til FilteredRowSet
                filteredRowSet.updateInt(columnName, value);
                System.out.println(messageWhenUpdateIntValue);

            } catch (SQLException s) {
                System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
            }
        }
        else {
            try {
                // Oppdaterer felt til CachedRowSet
                cachedRowSet.updateInt(columnName, value);
                System.out.println(messageWhenUpdateIntValue);

            } catch (SQLException s) {
                System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
            }
        }
    }

    // Oppdateringsmetode for Booleanverdier
    public void updateBooleanValue(String columnName, boolean value) throws SQLException {

        String messageWhenUpdateBooleanValue = "Boolean-verdi oppdatert. \n";

        if (rowSetTypeIsFiltered) {
            try {
                // Oppdaterer felt til FilteredRowSet
                cachedRowSet.updateBoolean(columnName, value);
                System.out.println(messageWhenUpdateBooleanValue);

            } catch (SQLException s) {
                System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
            }
        }
        else {
            try {
                // Oppdaterer felt til CachedRowSet
                cachedRowSet.updateBoolean(columnName, value);
                System.out.println(messageWhenUpdateBooleanValue);

            } catch (SQLException s) {
                System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
            }
        }
    }

    // Oppdaterer nåværende rad
    public void updateRow() throws SQLException {

        String messageWhenUpdateRow = "Oppdatering av raden ble lagret i cache. \n";

        if (rowSetTypeIsFiltered) {
            try {
                filteredRowSet.updateRow();
                System.out.println(messageWhenUpdateRow);
            } catch (SQLException e){
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
        else {
            try {
                cachedRowSet.updateRow();
                System.out.println(messageWhenUpdateRow);
            } catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
    }

    // Sletter nåværende rad
    public void deleteRow() throws SQLException {

        String messageWhenDeleteRow = "Raden ble slettet fra cache. \n";

        if (rowSetTypeIsFiltered) {
            try {
                filteredRowSet.deleteRow();
                System.out.println(messageWhenDeleteRow);
            } catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
        else {
            try {
                cachedRowSet.deleteRow();
                System.out.println(messageWhenDeleteRow);
            } catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
    }

    // Sender de oppdaterte feltene til databasen
    public void acceptChanges() throws SQLException {

        String messageWhenAcceptChanges = "Alle endringer i cache ble sendt til databasen. \n";
        String errorMessageWhenAcceptChanges = "FEIL: Kunne ikke sende cache-endringer til databasen. \n";

        if (rowSetTypeIsFiltered) {
            if (dbInterface.commitToDatabase(filteredRowSet)) {
                System.out.println(messageWhenAcceptChanges);
            } else {
                System.out.println(errorMessageWhenAcceptChanges);
            }
        }
        else {
            if (dbInterface.commitToDatabase(cachedRowSet)) {
                System.out.println(messageWhenAcceptChanges);
            } else {
                System.out.println(errorMessageWhenAcceptChanges);
            }
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