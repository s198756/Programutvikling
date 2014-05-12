/**
 * Created by Sebastian Ramsland on 01.05.2014.
 */

import com.sun.rowset.CachedRowSetImpl;
import javax.sql.rowset.FilteredRowSet;
import java.sql.*;
import java.util.Date;

public class Contract {

    // Tabellnavn
    private static final String TABLENAME = "contract";

    // Generell informasjon
    private int contractID;                             // Kontrakt ID
    private int dwellingUnitID;                         // Bolig ID
    private String renter;                              // Personnummeret til leietaker
    private String broker;                              // Personnummeret til megler
    private boolean isValid;                            // Er kontrakten gyldig?
    private boolean hasPaidDepositum;                   // Er depositum innbetalt?
    private boolean isSignedByRenter;                   // Er kontrakten signert av leietaker?
    private boolean isSignedByBroker;                   // Er kontrakten signert av megler?
    private Date inEffectDate;                          // Startdato for angitt kontraktperiode
    private Date expirationDate;                        // Sluttdato for angitt kontraktperiode

    // Timestamps
    private Timestamp created;
    private Timestamp lastModified;

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

    // Konstruktør som oppretter et nytt CachedRowSet som inneholder ALLE registrerte kontrakter fra databasen
    public Contract () {
        // Oppretter nytt SQLInterface
        dbInterface = new SQLInterface();

        // Utfører SQL-query
        if (!dbInterface.execQuery(
                "SELECT contract_id, dwelling_unit_id, renter, broker, valid, paid_depositum, signed_by_renter, signed_by_broker, in_effect_date, expiration_date, created, last_modified FROM contract")) {

            // Stenger programmet ved feil
            System.exit(1);
        }

        // Lager CachedRowSet av SQL-queryen
        try {
            cachedRowSet = new CachedRowSetImpl();
            cachedRowSet = dbInterface.getRowSet();
            cachedRowSet.setTableName(TABLENAME);
            int [] keys = {1};
            cachedRowSet.setKeyColumns(keys);
            cachedRowSet.first();
            System.out.println("Konstruktør... \t CurrentNowNumber: " + currentRowNumber + "\t Rowset.Rownumber: " + cachedRowSet.getRow());

            // Nåværende rowSet er av typen "Cached"
            rowSetTypeIsFiltered = false;
        }
        catch (SQLException se) {
            System.out.println("Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage());
        }
    }

    // Konstruktør som tar imot eksisterende SQLInterface, CachedRowSet og radnummer (et ufiltrert søk)
    public Contract (SQLInterface dbInt, CachedRowSetImpl crs, int rowID) throws SQLException {
        // Setter inn SQLInterface
        dbInterface = dbInt;

        cachedRowSet = crs;
        cachedRowSet.setTableName(TABLENAME);
        int [] keys = {1};
        cachedRowSet.setKeyColumns(keys);

        // Setter inn mottatt radnummer
        currentRowNumber = rowID;

        // Setter pekeren på radnummeret
        jumpToContract(rowID);

        // Nåværende rowSet er av typen "Cached"
        rowSetTypeIsFiltered = false;
    }

    // Konstruktør som tar imot eksisterende SQLInterface, FilteredRowSet og radnummer (et filtrert søk)
    public Contract (SQLInterface dbInt, FilteredRowSet frs, int rowID) throws SQLException {
        // Setter inn SQLInterface
        dbInterface = dbInt;

        // Setter inn FilteredRowSet og tabellnavn
        filteredRowSet = frs;
        filteredRowSet.setTableName(TABLENAME);
        int [] keys = {1};
        cachedRowSet.setKeyColumns(keys);

        // Setter inn mottatt radnummer
        currentRowNumber = rowID;

        // Setter pekeren på radnummeret
        jumpToContract(rowID);

        // Nåværende rowSet er av typen "Filtered"
        rowSetTypeIsFiltered = true;
    }

    // Innlasting av kontrakt med nåværende radnummer
    public void refreshValues() throws SQLException {
        if (rowSetTypeIsFiltered) {
            try {
                // Hopper til riktig rad
                cachedRowSet.absolute(currentRowNumber);

                // Leser av verdier

                // Generell informasjon om kontrakt
                contractID = filteredRowSet.getInt("contract_id");
                dwellingUnitID = filteredRowSet.getInt("dwelling_unit_id");
                renter = filteredRowSet.getString("renter");
                broker = filteredRowSet.getString("broker");
                isValid = filteredRowSet.getBoolean("valid");
                hasPaidDepositum = filteredRowSet.getBoolean("paid_depositum");
                isSignedByRenter = filteredRowSet.getBoolean("signed_by_renter");
                isSignedByBroker = filteredRowSet.getBoolean("signed_by_broker");
                inEffectDate = filteredRowSet.getDate("in_effect_date");
                expirationDate = filteredRowSet.getDate("expiration_date");

                // Timestamps
                created = filteredRowSet.getTimestamp("created");
                lastModified = filteredRowSet.getTimestamp("last_modified");

            } catch (SQLException se) {
                System.out.println("Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage());
            }
        }
        else {
            try {
                // Hopper til riktig rad
                cachedRowSet.absolute(currentRowNumber);

                // Leser av verdier

                // Generell informasjon om kontrakt
                contractID = cachedRowSet.getInt("contract_id");
                dwellingUnitID = cachedRowSet.getInt("dwelling_unit_id");
                renter = cachedRowSet.getString("renter");
                broker = cachedRowSet.getString("broker");
                isValid = cachedRowSet.getBoolean("valid");
                hasPaidDepositum = cachedRowSet.getBoolean("paid_depositum");
                isSignedByRenter = cachedRowSet.getBoolean("signed_by_renter");
                isSignedByBroker = cachedRowSet.getBoolean("signed_by_broker");
                inEffectDate = cachedRowSet.getDate("in_effect_date");
                expirationDate = cachedRowSet.getDate("expiration_date");

                // Timestamps
                created = cachedRowSet.getTimestamp("created");
                lastModified = cachedRowSet.getTimestamp("last_modified");

            } catch (SQLException se) {
                System.out.println("Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage());
            }
        }

        // Printer ut verdier
        System.out.println("Kontrakt ID:" + contractID);
        System.out.println("Leietaker:" + renter);
        System.out.println("Megler:" + broker);
        System.out.println("Gyldig:" + isValid);
        System.out.println("Innbetalt depositum:" + hasPaidDepositum);
        System.out.println("Signert av leietaker:" + isSignedByRenter);
        System.out.println("Signert av megler:" + isSignedByBroker);
        System.out.println("Startdato:" + inEffectDate);
        System.out.println("Sluttdato:" + expirationDate);
        System.out.println("\n /////// END OF CONTRACT //////// \n");
    }

    // Innlasting av kontrakt med et spesifisert radnummer
    public void jumpToContract(int rowNo) throws SQLException {
        currentRowNumber = rowNo;
        previousRowNumber -= currentRowNumber - 1;
        nextRowNumber += currentRowNumber + 1;
        String messageJumpToContract = "Forsøker å hente ut verdier til kontrakten på rad " + currentRowNumber + ".\n";
        try {
            System.out.println(messageJumpToContract);
            refreshValues();
        } catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Søk opp kontrakt med kontrakt ID (unik verdi)
    public void findContractWithID(int cID) throws SQLException {

        String messageWhenFindContractWithID = "Kontrakten ble funnet. Flytter pekeren til kontrakten og henter verdier. \n";
        String messageWhenCouldNotFindContractWithID = "Fant ikke kontrakten med den angitte kontrakt-IDen. \n";

        if (rowSetTypeIsFiltered) {
            try {
                // Hopper først til toppen av tabellen
                filteredRowSet.beforeFirst();

                // Søker igjennom rowSet
                while (filteredRowSet.next()) {
                    if (cID == filteredRowSet.getInt("contract_id")) {
                        System.out.println(messageWhenFindContractWithID);
                        jumpToContract(filteredRowSet.getRow());
                        return;
                    }
                }
                System.out.println(messageWhenCouldNotFindContractWithID);
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
                    if(cID == cachedRowSet.getInt("contract_id")) {
                        System.out.println(messageWhenFindContractWithID);
                        jumpToContract(cachedRowSet.getRow());
                        return;
                    }
                }
                System.out.println(messageWhenCouldNotFindContractWithID);

            } catch (SQLException e){
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
    }

    // Innlasting av neste kontrakt
    public void nextContract() throws SQLException {
        currentRowNumber += 1;
        previousRowNumber = currentRowNumber - 1;
        nextRowNumber = currentRowNumber + 1;
        String messageWhenNextContract = "Hopper til neste kontrakt. \n";

        if (rowSetTypeIsFiltered) {
            try {
                System.out.println(messageWhenNextContract);
                filteredRowSet.next();
                refreshValues();
            } catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
        else {
            try {
                System.out.println(messageWhenNextContract);
                cachedRowSet.next();
                refreshValues();
            } catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
    }

    // Innlasting av forrige kontrakt
    public void previousContract() throws SQLException {
        currentRowNumber -= 1;
        previousRowNumber = currentRowNumber - 1;
        nextRowNumber = currentRowNumber + 1;
        String messageWhenPreviousContract = "Hopper til forrige kontrakt. \n";

        if (rowSetTypeIsFiltered) {
            try {
                System.out.println(messageWhenPreviousContract);
                filteredRowSet.previous();
                refreshValues();
            } catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
        else {
            try {
                System.out.println(messageWhenPreviousContract);
                cachedRowSet.previous();
                refreshValues();
            } catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
    }

    // Flytter pekeren til en innsettingsrad. Må kalles opp ved opprettelse av ny kontrakt.
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
                System.out.println("Move to insert row... \t CurrentNowNumber: " + currentRowNumber + "\t Rowset.Rownumber: " + cachedRowSet.getRow());
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

                System.out.println("State: " + e.getSQLState() + "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
        else {
            try {
                cachedRowSet.insertRow();
                System.out.println(messageWhenInsertRow);
                System.out.println("Insert row... \t CurrentNowNumber: " + currentRowNumber + "\t Rowset.Rownumber: " + cachedRowSet.getRow());
            } catch (SQLException e){
                System.out.println("State: " + e.getSQLState() + "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
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

    // Oppdateringsmetode for Automatiske verdier
    public void updateAuto(String columnName) throws SQLException {

        String messageWhenUpdateNull = "Automatisk verdi oppdatert. \n";

        if (rowSetTypeIsFiltered) {
            try {
                // Oppdaterer felt til FilteredRowset
                filteredRowSet.updateNull(columnName);
                System.out.println(messageWhenUpdateNull);

            } catch (SQLException s) {
                System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
            }
        }
        else {
            try {
                // Oppdaterer felt til CachedRowSet
                cachedRowSet.updateNull(columnName);
                System.out.println(messageWhenUpdateNull);

            } catch (SQLException s) {
                System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
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
                System.out.println("Update int value... \t CurrentNowNumber: " + currentRowNumber + "\t Rowset.Rownumber: " + cachedRowSet.getRow());


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

    // Returnerer "true" dersom kravene til å oppnå en gyldig kontrakt oppfylles.
    public boolean checkValidation() throws SQLException {

        Person verifyPersons = new Person();
        DwellingUnit verifyDwellingUnit = new DwellingUnit();

        // Henter ut nødvendig informasjon om kontraktens megler
        verifyPersons.findPersonWithPersonNo(broker);
        boolean brokerIsBroker = verifyPersons.getIsBroker();
        String brokerPersonNo = verifyPersons.getPersonNo();

        // Henter ut nødvendig informasjon om kontraktens leietaker
        verifyPersons.findPersonWithPersonNo(renter);
        String renterPersonNo = verifyPersons.getPersonNo();

        // Henter ut nødvendig informasjon om kontraktens bolig
        verifyDwellingUnit.findDwellingUnitWithID(dwellingUnitID);
        String propertyOwner = verifyDwellingUnit.getPropertyOwner();

        // Oppretter nåværende dato
        Date now = new Date();

        if (!brokerIsBroker) {
            System.out.println("FEIL: Angitt megler i kontrakten er ingen megler. \n");
            return false;
        }

        else if (renterPersonNo.equals(propertyOwner)) {
            System.out.println("Feil: Leietakeren kan ikke leie sin egen bolig. \n");
            return false;
        }

        else if (renterPersonNo.equals(brokerPersonNo)) {
            System.out.println("FEIL: Leietakeren kan ikke være samme person som megleren. \n");
            return false;
        }

        else if (!isSignedByRenter) {
            System.out.println("FEIL: Kontrakten er ikke signert av leietaker. \n");
            return false;
        }

        else if (!isSignedByBroker) {
            System.out.println("FEIL: Kontrakten er ikke signert av megler. \n");
            return false;
        }

        else if(!now.after(inEffectDate) || !now.before(expirationDate)) {
            System.out.println("FEIL: Dagens dato er ikke innenfor kontraktperioden. \n");
            return false;
        }

        else {
            return true;
        }
    }

    // Oppdaterer kontraktfeltet "valid" i databasen til korrekt status.
    public void setContractValidation() throws SQLException {
        if (checkValidation() && isValid) {
            System.out.println("Kontrakten er allerede gyldig. Metoden foretar seg ingen endringer.");
            return;
        }
        else if (checkValidation()) {
            if (rowSetTypeIsFiltered){
                try {
                    // Oppdaterer felt
                    filteredRowSet.moveToCurrentRow();
                    filteredRowSet.updateBoolean("valid", true);
                    System.out.println("Boolean-verdi oppdatert. \n");
                    filteredRowSet.updateRow();
                    System.out.println("Oppdatering av raden ble lagret i cache. \n");
                }
                catch (SQLException s) {
                    System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
                }
            }
            else {
                try {
                    // Oppdaterer felt
                    cachedRowSet.moveToCurrentRow();
                    cachedRowSet.updateBoolean("valid", true);
                    System.out.println("Boolean-verdi oppdatert. \n");
                    cachedRowSet.updateRow();
                    System.out.println("Oppdatering av raden ble lagret i cache. \n");
                } catch (SQLException s) {
                    System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
                }
            }

            // Sender oppdatering til databasen
            acceptChanges();
            System.out.println("Kontrakten er nå oppdatert til status: Gyldig.");

            return;
        }

        else if (!checkValidation() && !this.isValid) {
            System.out.println("Kontrakten er allerede ugyldig. Metoden foretar seg ingen endringer.");
            return;
        }

        else
        {
            if (rowSetTypeIsFiltered) {
                try {
                    // Oppdaterer felt
                    filteredRowSet.moveToCurrentRow();
                    filteredRowSet.updateBoolean("valid", false);
                    System.out.println("Boolean-verdi oppdatert. \n");
                    filteredRowSet.updateRow();
                    System.out.println("Oppdatering av raden ble lagret i cache. \n");
                }
                catch (SQLException s) {
                    System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
                }
            }
            else {
                try {
                    // Oppdaterer felt
                    cachedRowSet.moveToCurrentRow();
                    cachedRowSet.updateBoolean("valid", false);
                    System.out.println("Boolean-verdi oppdatert. \n");
                    cachedRowSet.updateRow();
                    System.out.println("Oppdatering av raden ble lagret i cache. \n");
                } catch (SQLException s) {
                    System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
                }
            }

            // Sender oppdatering til databasen
            acceptChanges();
            System.out.println("Kontrakten er nå oppdatert til status: Ugyldig.");
        }
    }

    // Oppdaterer kontraktens boligfelt "available" til korrekt status.
    public void setDwellingUnitAvailability() throws SQLException {

        // Oppretter kontraktens bolig
        DwellingUnit du = new DwellingUnit();
        du.findDwellingUnitWithID(dwellingUnitID);

        if (checkValidation() && !du.getIsAvailable()) {
            System.out.println("Boligen har allerede status OPPTATT.");
            return;
        }

        else if (checkValidation() && du.getIsAvailable()) {
            try {
                du.moveToCurrentRow();
                du.updateBooleanValue("available", false);
                du.acceptChanges();
                System.out.println("Endret status på boligen tilknyttet kontrakten til: OPPTATT");
            }
            catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
            return;
        }
        else if (!checkValidation() && du.getIsAvailable()) {
            System.out.println("Boligen har allerede status LEDIG.");
            return;
        }
        else {
            try {
                du.moveToCurrentRow();
                du.updateBooleanValue("available", true);
                du.acceptChanges();
                System.out.println("Endret status på boligen tilknyttet kontrakten til: LEDIG");
            }
            catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
    }

    public int getCurrentRowNumber() {
        return currentRowNumber;
    }

    public int getContractID()  {
        return contractID;
    }

    public String getRenter() {
        return renter;
    }

    public String getBroker() {
        return broker;
    }

    public boolean getIsValid()  {
        return isValid;
    }

    public boolean getHasPaidDepositum()  {
        return hasPaidDepositum;
    }

    public boolean getIsSignedByRenter()  {
        return isSignedByRenter;
    }

    public boolean getIsSignedByBroker()  {
        return isSignedByBroker;
    }

    public Date getInEffectDate() {
        return inEffectDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public Timestamp getCreatedDate()  {
        return created;
    }

    public Timestamp getLastModifiedDate()  {
        return lastModified;
    }
}