package GUI.Files;

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

    // Angir rowsettets antall rader
    private int totalAmountOfRows;

    // Angir toppen av rowset
    private static final int TOP_OF_ROWSET = 1;

    // Initialiserer String som viser programmeldinger til vinduene.
    private String infoText;

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

        // Setter inn CachedRowSet fra SQL-queryen
        try {
            cachedRowSet = new CachedRowSetImpl();
            cachedRowSet = dbInterface.getCachedRowSet();
            cachedRowSet.setTableName(TABLENAME);
            int [] keys = {1, 9, 10};
            cachedRowSet.setKeyColumns(keys);

            // Hopper til nederste rad for å finne antall rader
            cachedRowSet.last();

            // Henter ut antall rader
            totalAmountOfRows = cachedRowSet.getRow();

            // Hopper til øverste rad
            cachedRowSet.first();

            // Nåværende rowSet er av typen "Cached"
            rowSetTypeIsFiltered = false;
        } catch (SQLException se) {
            infoText = "Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage();
        }
    }

    // Konstruktør som tar imot eksisterende SQLInterface, radnummer og rowset-type. Henter fram FilteredRowSet eller CachedRowSet fra SQLInterface.
    public Person (SQLInterface dbInt, int rowID, boolean isFiltered) throws SQLException {
        // Setter inn SQLInterface
        dbInterface = dbInt;

        if (isFiltered) {
            filteredRowSet = dbInterface.getFilteredRowSet();
            filteredRowSet.setTableName(TABLENAME);
            int [] keys = {1};
            filteredRowSet.setKeyColumns(keys);

            // Hopper til nederste rad for å finne antall rader
            filteredRowSet.last();

            // Henter ut antall rader
            totalAmountOfRows = filteredRowSet.getRow();

            // Setter pekeren på radnummeret
            jumpToPerson(rowID);

            // Nåværende rowSet er av typen "Filtered"
            rowSetTypeIsFiltered = true;
        } else {
            cachedRowSet = dbInterface.getCachedRowSet();
            cachedRowSet.setTableName(TABLENAME);
            int [] keys = {1};
            cachedRowSet.setKeyColumns(keys);

            // Hopper til nederste rad for å finne antall rader
            cachedRowSet.last();

            // Henter ut antall rader
            totalAmountOfRows = cachedRowSet.getRow();

            // Setter pekeren på radnummeret
            jumpToPerson(rowID);

            // Nåværende rowSet er av typen "Cached"
            rowSetTypeIsFiltered = false;
        }
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
                infoText = "Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage();
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
                infoText = "Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage();
            }
        }

        // Innhenting av stedsinformasjon
        Location location = new Location();
        location.refreshValues(zipCode);

        area = location.getArea();
        township = location.getTownship();
        county = location.getCounty();
        category = location.getCategory();
    }

    // Innlasting av person med et spesifisert radnummer
    public boolean jumpToPerson(int rowNo) throws SQLException {
        currentRowNumber = rowNo;
        previousRowNumber -= currentRowNumber - 1;
        nextRowNumber += currentRowNumber + 1;
        String messageJumpToPerson = "Hentet ut informasjon om personen";
        try {
            infoText = messageJumpToPerson;
            refreshValues();
            return true;
        } catch (SQLException e){
            infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
            return false;
        }
    }

    // Søk opp person med personnummer (unik verdi)
    public boolean findPersonWithPersonNo(String pNo) throws SQLException {

        String messageWhenFindPerson = "Personen ble funnet. Flytter pekeren til personen og henter verdier. \n";
        String messageWhenCouldNotFindPersonWithPersonNo = "Fant ikke personen med det angitte personnummeret. \n";

        if (rowSetTypeIsFiltered) {
            try {
                // Hopper først til toppen av tabellen
                filteredRowSet.beforeFirst();

                // Søker igjennom rowSet
                while (filteredRowSet.next()) {
                    if (pNo.equals(filteredRowSet.getString("person_no"))) {
                        infoText = messageWhenFindPerson;
                        jumpToPerson(filteredRowSet.getRow());
                        return true;
                    }
                }
                infoText = messageWhenCouldNotFindPersonWithPersonNo;
                return false;
            } catch (SQLException e) {
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
                return false;
            }
        }

        else {
            try {
                // Hopper først til toppen av tabellen
                cachedRowSet.beforeFirst();

                // Søker igjennom tabellen
                while(cachedRowSet.next()) {
                    if(pNo.equals(cachedRowSet.getString("person_no"))) {
                        infoText = messageWhenFindPerson;
                        jumpToPerson(cachedRowSet.getRow());
                        return true;
                    }
                }
                infoText = messageWhenCouldNotFindPersonWithPersonNo;
                return false;

            } catch (SQLException e){
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
                return false;
            }
        }
    }

    // Søk opp person med telefonnummer (unik verdi)
    public boolean findPersonWithTelephoneNo(long telNo) throws SQLException {

        String messageWhenFindPerson = "Personen ble funnet. Flytter pekeren til personen og henter verdier. \n";
        String messageWhenCouldNotFindPersonWithTelephoneNo = "Fant ikke personen med det angitte telefonnummeret. \n";

        if (rowSetTypeIsFiltered) {
            try {
                // Hopper først til toppen av tabellen
                filteredRowSet.beforeFirst();

                // Søker igjennom tabellen
                while (filteredRowSet.next()) {
                    if (telNo == filteredRowSet.getLong("telephone")) {
                        infoText = messageWhenFindPerson;
                        jumpToPerson(filteredRowSet.getRow());
                        return true;
                    }
                }
                infoText = messageWhenCouldNotFindPersonWithTelephoneNo;
                return false;
            } catch (SQLException e) {
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
                return false;
            }
        }
        else {
            try {
                // Hopper først til toppen av tabellen
                cachedRowSet.first();

                // Søker igjennom tabellen
                while (cachedRowSet.next()) {
                    if (telNo == cachedRowSet.getLong("telephone")) {
                        infoText = messageWhenFindPerson;
                        jumpToPerson(cachedRowSet.getRow());
                        return true;
                    }
                }
                infoText = messageWhenCouldNotFindPersonWithTelephoneNo;
                return false;
            } catch (SQLException e) {
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
                return false;
            }
        }
    }

    // Søk opp person med mailadresse (unik verdi)
    public boolean findPersonWithEMailAddress(String mail) throws SQLException {

        String messageWhenFindPerson = "Personen ble funnet. Flytter pekeren til personen og henter verdier. \n";
        String messageWhenCouldNotFindPersonWithEmail = "Fant ikke personen med den angitte mailadressen. \n";

        if (rowSetTypeIsFiltered) {
            try {
                // Hopper først til toppen av tabellen
                filteredRowSet.beforeFirst();

                // Søker igjennom tabellen
                while(filteredRowSet.next()) {
                    if(mail.equals(filteredRowSet.getString("email"))) {
                        infoText = messageWhenFindPerson;
                        jumpToPerson(filteredRowSet.getRow());
                        return true;
                    }
                }
                infoText = messageWhenCouldNotFindPersonWithEmail;
                return false;
            } catch (SQLException e){
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
                return false;
            }
        }
        else {
            try {
                // Hopper først til toppen av tabellen
                cachedRowSet.beforeFirst();

                // Søker igjennom tabellen
                while(cachedRowSet.next()) {
                    if(mail.equals(cachedRowSet.getString("email"))) {
                        infoText = messageWhenFindPerson;
                        jumpToPerson(cachedRowSet.getRow());
                        return true;
                    }
                }
                infoText = messageWhenCouldNotFindPersonWithEmail;
                return false;
            } catch (SQLException e){
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
                return false;
            }
        }
    }

    // Innlasting av neste person. Kontrollerer også om pekeren står på nederste rad.
    public void nextPerson() throws SQLException {

        String messageWhenNextPerson = "Fant fram til neste person. \n";
        String messageWhenLastPerson = "Du har nådd bunnen av listen. \n";

        if (rowSetTypeIsFiltered) {
            // Sjekker om pekeren står på siste rad
            if (filteredRowSet.getRow() == totalAmountOfRows) {
                infoText = messageWhenLastPerson;
            }
            else {
                currentRowNumber += 1;
                previousRowNumber = currentRowNumber - 1;
                nextRowNumber = currentRowNumber + 1;

                // Hopper til neste rad
                try {
                    filteredRowSet.next();
                    infoText = messageWhenNextPerson;
                    refreshValues();
                } catch (SQLException e) {
                    infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
                }
            }
        }
        else {
            // Sjekker om pekeren står på siste rad
            if (cachedRowSet.getRow() == totalAmountOfRows) {
                infoText = messageWhenLastPerson;
            }
            else {
                currentRowNumber += 1;
                previousRowNumber = currentRowNumber - 1;
                nextRowNumber = currentRowNumber + 1;

                // Hopper til neste rad
                try {
                    cachedRowSet.next();
                    infoText = messageWhenNextPerson;
                    refreshValues();
                } catch (SQLException e) {
                    infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
                }
            }
        }
    }

    // Innlasting av forrige person. Kontrollerer også om pekeren står på øverste rad.
    public void previousPerson() throws SQLException {

        String messageWhenPreviousPerson = "Fant fram til forrige person. \n";
        String messageWhenFirstPerson = "Du har nådd toppen av listen. \n";

        if (rowSetTypeIsFiltered) {
            // Sjekker om pekeren står på øverste rad
            if (filteredRowSet.getRow() == TOP_OF_ROWSET) {
                infoText = messageWhenFirstPerson;
            }
            else {
                currentRowNumber -= 1;
                previousRowNumber = currentRowNumber - 1;
                nextRowNumber = currentRowNumber + 1;

                // Hopper til neste rad
                try {
                    filteredRowSet.previous();
                    infoText = messageWhenPreviousPerson;
                    refreshValues();
                } catch (SQLException e) {
                    infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
                }
            }
        }
        else {
            // Sjekker om pekeren står på øverste rad
            if (cachedRowSet.getRow() == TOP_OF_ROWSET) {
                infoText = messageWhenFirstPerson;
            }
            else {
                currentRowNumber -= 1;
                previousRowNumber = currentRowNumber - 1;
                nextRowNumber = currentRowNumber + 1;

                // Hopper til neste rad
                try {
                    cachedRowSet.previous();
                    infoText = messageWhenPreviousPerson;
                    refreshValues();
                } catch (SQLException e) {
                    infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
                }
            }
        }
    }

    // Flytter pekeren til en innsettingsrad. Må brukes ved opprettelse av ny person.
    public void moveToInsertRow() throws SQLException {

        String messageWhenMoveToInsertRow = "Fyll ut de påkrevde feltene og trykk 'Lagre' for å opprette en ny person. \n";

        if (rowSetTypeIsFiltered) {
            try {
                filteredRowSet.moveToInsertRow();
                infoText = messageWhenMoveToInsertRow;
            } catch (SQLException e) {
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
            }
        }
        else {
            try {
                cachedRowSet.moveToInsertRow();
                infoText = messageWhenMoveToInsertRow;
            } catch (SQLException e) {
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
            }
        }
    }

    // Setter inn nåværende innsettingsrad. Pekeren må befinne seg på en innsettingsrad.
    public void insertRow() throws SQLException {

        String messageWhenInsertRow = "Raden ble lagret i cache. \n";

        if (rowSetTypeIsFiltered) {
            try {
                filteredRowSet.insertRow();
                infoText = messageWhenInsertRow;
            } catch (SQLException e){
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
            }
        }
        else {
            try {
                cachedRowSet.insertRow();
                infoText = messageWhenInsertRow;
            } catch (SQLException e){
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
            }
        }
    }

    // Pekeren flyttes til nåværende rad. Har kun effekt om pekeren er på en innsettingsrad.
    public void moveToCurrentRow() throws SQLException {

        String messageWhenMovedToRow = "Peker flyttet til nåværende rad. \n";

        if (rowSetTypeIsFiltered) {
            try {
                filteredRowSet.moveToCurrentRow();
                infoText = messageWhenMovedToRow;
            } catch (SQLException e) {
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
            }
        }
        else {
            try {
                cachedRowSet.moveToCurrentRow();
                infoText = messageWhenMovedToRow;

            } catch (SQLException e) {
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
            }
        }
    }

    // Kansellerer alle oppdateringer som er lagret i Rowsetet.
    public void cancelUpdates() throws SQLException {

        String messageWhenCancelUpdates = "Alle endringer ble avbrutt. \n";

        if (rowSetTypeIsFiltered) {
            try {
                filteredRowSet.cancelRowUpdates();
                infoText = messageWhenCancelUpdates;
            } catch (SQLException e){
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
            }
        }
        else {
            try {
                cachedRowSet.cancelRowUpdates();
                infoText = messageWhenCancelUpdates;
            } catch (SQLException e){
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
            }
        }
    }

    // Oppdateringsmetode for Automatiske verdier
    public void updateAuto(String columnName) throws SQLException {
        if (rowSetTypeIsFiltered) {
            try {
                // Oppdaterer felt til FilteredRowset
                filteredRowSet.updateNull(columnName);
            } catch (SQLException s) {
                infoText = "Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage();
            }
        }
        else {
            try {
                // Oppdaterer felt til CachedRowSet
                cachedRowSet.updateNull(columnName);
            } catch (SQLException s) {
                infoText = "Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage();
            }
        }
    }

    // Oppdateringsmetode for Stringverdier
    public void updateStringValue(String columnName, String value) throws SQLException {

        if (rowSetTypeIsFiltered) {
            try {
                // Oppdaterer felt til FilteredRowset
                filteredRowSet.updateString(columnName, value);
            } catch (SQLException s) {
                infoText = "Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage();
            }
        }
        else {
            try {
                // Oppdaterer felt til CachedRowSet
                cachedRowSet.updateString(columnName, value);
            } catch (SQLException s) {
                infoText = "Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage();
            }
        }
    }

    // Oppdateringsmetode for Intverdier
    public void updateIntValue(String columnName, int value) throws SQLException {

        if (rowSetTypeIsFiltered) {
            try {
                // Oppdaterer felt til FilteredRowSet
                filteredRowSet.updateInt(columnName, value);
            } catch (SQLException s) {
                infoText = "Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage();
            }
        }
        else {
            try {
                // Oppdaterer felt til CachedRowSet
                cachedRowSet.updateInt(columnName, value);
            } catch (SQLException s) {
                infoText = "Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage();
            }
        }
    }

    // Oppdateringsmetode for Longverdier
    public void updateLongValue(String columnName, long value) throws SQLException {

        if (rowSetTypeIsFiltered) {
            try {
                // Oppdaterer felt til FilteredRowSet
                cachedRowSet.updateLong(columnName, value);
            } catch (SQLException s) {
                infoText = "Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage();
            }
        }
        else {
            try {
                // Oppdaterer felt til CachedRowSet
                cachedRowSet.updateLong(columnName, value);
            } catch (SQLException s) {
                infoText = "Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage();
            }
        }
    }

    // Oppdateringsmetode for Booleanverdier
    public void updateBooleanValue(String columnName, boolean value) throws SQLException {

        if (rowSetTypeIsFiltered) {
            try {
                // Oppdaterer felt til FilteredRowSet
                cachedRowSet.updateBoolean(columnName, value);
            } catch (SQLException s) {
                infoText = "Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage();
            }
        }
        else {
            try {
                // Oppdaterer felt til CachedRowSet
                cachedRowSet.updateBoolean(columnName, value);
            } catch (SQLException s) {
                infoText = "Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage();
            }
        }
    }

    // Oppdaterer nåværende rad
    public void updateRow() throws SQLException {

        String messageWhenUpdateRow = "Oppdatering av raden ble lagret i cache. \n";

        if (rowSetTypeIsFiltered) {
            try {
                filteredRowSet.updateRow();
                infoText = messageWhenUpdateRow;
            } catch (SQLException e){
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
            }
        }
        else {
            try {
                cachedRowSet.updateRow();
                infoText = messageWhenUpdateRow;
            } catch (SQLException e) {
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
            }
        }
    }

    // Sletter nåværende rad
    public void deleteRow() throws SQLException {

        String messageWhenDeleteRow = "Raden ble slettet fra cache. \n";

        if (rowSetTypeIsFiltered) {
            try {
                filteredRowSet.deleteRow();
                totalAmountOfRows -= 1;
                infoText = messageWhenDeleteRow;
            } catch (SQLException e) {
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
            }
        }
        else {
            try {
                cachedRowSet.deleteRow();
                totalAmountOfRows -= 1;
                infoText = messageWhenDeleteRow;
            } catch (SQLException e) {
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
            }
        }
    }

    // Sender de oppdaterte feltene til databasen
    public boolean acceptChanges() {

        String messageWhenAcceptChanges = "Alle endringer ble sendt til databasen. \n";
        String errorMessageWhenAcceptChanges = "FEIL: Kunne ikke sende endringer til databasen. \n";

        if (rowSetTypeIsFiltered) {
            if (dbInterface.commitToDatabase(filteredRowSet)) {
                infoText = messageWhenAcceptChanges;
                return true;
            } else {
                infoText = errorMessageWhenAcceptChanges;
                return false;
            }
        }
        else {
            if (dbInterface.commitToDatabase(cachedRowSet)) {
                infoText = messageWhenAcceptChanges;
                return true;
            } else {
                infoText = errorMessageWhenAcceptChanges;
                return false;
            }
        }
    }

    public void last() throws SQLException {
        try {
            if (rowSetTypeIsFiltered) {
                filteredRowSet.last();
            }
            else {
                cachedRowSet.last();
            }
        } catch (SQLException e) {
            infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
        }
    }

    public int getCurrentRowNumber() {
        return currentRowNumber;
    }

    public String getPersonNo() {
        return personNo;
    }

    public String getFullName() {
        return firstname + " " + getMiddleName() + " " + surname;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getMiddleName() {
        if (middlename == null) {
            return "";
        }
        else {
            return middlename;
        }
    }

    public String getInfoText() {
        return infoText;
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