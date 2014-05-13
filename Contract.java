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

    // Angir rowsettets antall rader
    private int amountRows;

    // Angir toppen av rowset
    private static final int TOP_OF_ROWSET = 1;

    // Initialiserer String som viser programmeldinger til vinduene.
    private String infoText;

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

            // Hopper til nederste rad for å finne antall rader
            cachedRowSet.last();

            // Henter ut antall rader
            amountRows = cachedRowSet.getRow();

            // Hopper tilbake til øverste rad
            cachedRowSet.first();

            // Nåværende rowSet er av typen "Cached"
            rowSetTypeIsFiltered = false;
        }
        catch (SQLException se) {
            infoText = "Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage();
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

        // Hopper til nederste rad for å finne antall rader
        cachedRowSet.last();

        // Henter ut antall rader
        amountRows = cachedRowSet.getRow();

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

        // Hopper til nederste rad for å finne antall rader
        cachedRowSet.last();

        // Henter ut antall rader
        amountRows = cachedRowSet.getRow();

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
                infoText = "Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage();
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
                infoText = "Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage();
            }
        }
    }

    // Innlasting av kontrakt med et spesifisert radnummer
    public void jumpToContract(int rowNo) throws SQLException {
        currentRowNumber = rowNo;
        previousRowNumber -= currentRowNumber - 1;
        nextRowNumber += currentRowNumber + 1;
        String messageJumpToContract = "Forsøker å hente ut verdier til kontrakten på rad " + currentRowNumber + ".\n";
        try {
            infoText = messageJumpToContract;
            refreshValues();
        } catch (SQLException e){
            infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
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
                        infoText = messageWhenFindContractWithID;
                        jumpToContract(filteredRowSet.getRow());
                        return;
                    }
                }
                infoText = messageWhenCouldNotFindContractWithID;
            } catch (SQLException e) {
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
            }
        }

        else {
            try {
                // Hopper først til toppen av tabellen
                cachedRowSet.beforeFirst();

                // Søker igjennom tabellen
                while(cachedRowSet.next()) {
                    if(cID == cachedRowSet.getInt("contract_id")) {
                        infoText = messageWhenFindContractWithID;
                        jumpToContract(cachedRowSet.getRow());
                        return;
                    }
                }
                infoText = messageWhenCouldNotFindContractWithID;
            } catch (SQLException e){
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
            }
        }
    }

    // Innlasting av neste person
    public void nextPerson() throws SQLException {

        String messageWhenNextContract = "Fant fram til neste kontrakt. \n";
        String messageWhenLastContract = "Du har nådd bunnen av listen. \n";

        if (rowSetTypeIsFiltered) {
            // Sjekker om pekeren står på siste rad
            if (filteredRowSet.getRow() == amountRows) {
                infoText = messageWhenLastContract;
            }
            else {
                currentRowNumber += 1;
                previousRowNumber = currentRowNumber - 1;
                nextRowNumber = currentRowNumber + 1;

                // Hopper til neste rad
                try {
                    infoText = messageWhenNextContract;
                    filteredRowSet.next();
                    refreshValues();
                } catch (SQLException e) {
                    infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
                }
            }
        }
        else {
            // Sjekker om pekeren står på siste rad
            if (cachedRowSet.getRow() == amountRows) {
                infoText = messageWhenLastContract;
            }
            else {
                currentRowNumber += 1;
                previousRowNumber = currentRowNumber - 1;
                nextRowNumber = currentRowNumber + 1;

                // Hopper til neste rad
                try {
                    infoText = messageWhenNextContract;
                    cachedRowSet.next();
                    refreshValues();
                } catch (SQLException e) {
                    infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
                }
            }
        }
    }

    // Innlasting av forrige kontrakt
    public void previousPerson() throws SQLException {

        String messageWhenPreviousContract = "Fant fram til forrige kontrakt. \n";
        String messageWhenFirstContract = "Du har nådd toppen av listen. \n";

        if (rowSetTypeIsFiltered) {
            // Sjekker om pekeren står på øverste rad
            if (filteredRowSet.getRow() == TOP_OF_ROWSET) {
                infoText = messageWhenFirstContract;
            }
            else {
                currentRowNumber -= 1;
                previousRowNumber = currentRowNumber - 1;
                nextRowNumber = currentRowNumber + 1;

                // Hopper til neste rad
                try {
                    filteredRowSet.previous();
                    infoText = messageWhenPreviousContract;
                    refreshValues();
                } catch (SQLException e) {
                    infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
                }
            }
        }
        else {
            // Sjekker om pekeren står på øverste rad
            if (cachedRowSet.getRow() == TOP_OF_ROWSET) {
                infoText = messageWhenFirstContract;
            }
            else {
                currentRowNumber -= 1;
                previousRowNumber = currentRowNumber - 1;
                nextRowNumber = currentRowNumber + 1;

                // Hopper til neste rad
                try {
                    cachedRowSet.previous();
                    infoText = messageWhenPreviousContract;
                    refreshValues();
                } catch (SQLException e) {
                    infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
                }
            }
        }
    }

    // Flytter pekeren til en innsettingsrad. Må kalles opp ved opprettelse av ny kontrakt.
    public void moveToInsertRow() throws SQLException {

        String messageWhenMoveToInsertRow = "Peker flyttet til innsettingsrad. \n";

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

    // Kansellerer alle oppdateringer
    public void cancelUpdates() throws SQLException {

        String messageWhenCancelUpdates = "Alle endringer lagret i cachen ble fjernet. \n";

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

        String messageWhenUpdateNull = "Automatisk verdi oppdatert. \n";

        if (rowSetTypeIsFiltered) {
            try {
                // Oppdaterer felt til FilteredRowset
                filteredRowSet.updateNull(columnName);
                infoText = messageWhenUpdateNull;
            } catch (SQLException s) {
                infoText = "Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage();
            }
        }
        else {
            try {
                // Oppdaterer felt til CachedRowSet
                cachedRowSet.updateNull(columnName);
                infoText = messageWhenUpdateNull;
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
                infoText = messageWhenDeleteRow;
            } catch (SQLException e) {
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
            }
        }
        else {
            try {
                cachedRowSet.deleteRow();
                infoText = messageWhenDeleteRow;
            } catch (SQLException e) {
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
            }
        }
    }

    // Sender de oppdaterte feltene til databasen
    public void acceptChanges() throws SQLException {

        String messageWhenAcceptChanges = "Alle endringer i cache ble sendt til databasen. \n";
        String errorMessageWhenAcceptChanges = "FEIL: Kunne ikke sende cache-endringer til databasen. \n";

        if (rowSetTypeIsFiltered) {
            if (dbInterface.commitToDatabase(filteredRowSet)) {
                infoText = messageWhenAcceptChanges;
            } else {
                infoText = errorMessageWhenAcceptChanges;
            }
        }
        else {
            if (dbInterface.commitToDatabase(cachedRowSet)) {
                infoText = messageWhenAcceptChanges;
            } else {
                infoText = errorMessageWhenAcceptChanges;
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
            infoText = "FEIL: Angitt megler i kontrakten er ingen megler. \n";
            return false;
        }

        else if (renterPersonNo.equals(propertyOwner)) {
            infoText = "Feil: Leietakeren kan ikke leie sin egen bolig. \n";
            return false;
        }

        else if (renterPersonNo.equals(brokerPersonNo)) {
            infoText = "FEIL: Leietakeren kan ikke være samme person som megleren. \n";
            return false;
        }

        else if (!isSignedByRenter) {
            infoText = "FEIL: Kontrakten er ikke signert av leietaker. \n";
            return false;
        }

        else if (!isSignedByBroker) {
            infoText = "FEIL: Kontrakten er ikke signert av megler. \n";
            return false;
        }

        else if(!now.after(inEffectDate) || !now.before(expirationDate)) {
            infoText = "FEIL: Dagens dato er ikke innenfor kontraktperioden. \n";
            return false;
        }

        else {
            return true;
        }
    }

    // Oppdaterer kontraktfeltet "valid" i databasen til korrekt status.
    public void setContractValidation() throws SQLException {
        if (checkValidation() && isValid) {
            infoText = "Kontrakten er allerede gyldig. Metoden foretar seg ingen endringer.";
            return;
        }
        else if (checkValidation()) {
            if (rowSetTypeIsFiltered){
                try {
                    // Oppdaterer felt
                    filteredRowSet.moveToCurrentRow();
                    filteredRowSet.updateBoolean("valid", true);
                    filteredRowSet.updateRow();
                    infoText = "Gyldighetsstatus til kontrakten ble endret. \n";
                }
                catch (SQLException s) {
                    infoText = "Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage();
                }
            }
            else {
                try {
                    // Oppdaterer felt
                    cachedRowSet.moveToCurrentRow();
                    cachedRowSet.updateBoolean("valid", true);
                    cachedRowSet.updateRow();
                    infoText = "Gyldighetsstatus til kontrakten ble endret. \n";
                } catch (SQLException s) {
                    infoText = "Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage();
                }
            }

            // Sender oppdatering til databasen
            acceptChanges();
            infoText = "Kontrakten er nå oppdatert til status: Gyldig.";

            return;
        }

        else if (!checkValidation() && !this.isValid) {
            infoText = "Kontrakten er allerede ugyldig. Metoden foretar seg ingen endringer.";
            return;
        }

        else
        {
            if (rowSetTypeIsFiltered) {
                try {
                    // Oppdaterer felt
                    filteredRowSet.moveToCurrentRow();
                    filteredRowSet.updateBoolean("valid", false);
                    filteredRowSet.updateRow();
                    infoText = "Oppdatering av raden ble lagret i cache. \n";
                } catch (SQLException s) {
                    infoText = "Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage();
                }
            }
            else {
                try {
                    // Oppdaterer felt
                    cachedRowSet.moveToCurrentRow();
                    cachedRowSet.updateBoolean("valid", false);
                    cachedRowSet.updateRow();
                    infoText = "Oppdatering av raden ble lagret i cache. \n";
                } catch (SQLException s) {
                    infoText = "Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage();
                }
            }

            // Sender oppdatering til databasen
            acceptChanges();
            infoText = "Kontrakten er nå oppdatert til status: Ugyldig.";
        }
    }

    // Oppdaterer kontraktens boligfelt "available" til korrekt status.
    public void setDwellingUnitAvailability() throws SQLException {

        // Oppretter kontraktens bolig
        DwellingUnit du = new DwellingUnit();
        du.findDwellingUnitWithID(dwellingUnitID);

        if (checkValidation() && !du.getIsAvailable()) {
            infoText = "Boligen har allerede status OPPTATT.";
            return;
        }

        else if (checkValidation() && du.getIsAvailable()) {
            try {
                du.moveToCurrentRow();
                du.updateBooleanValue("available", false);
                du.acceptChanges();
                infoText = "Endret status på boligen tilknyttet kontrakten til: OPPTATT";
            }
            catch (SQLException e) {
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
            }
            return;
        }
        else if (!checkValidation() && du.getIsAvailable()) {
            infoText = "Boligen har allerede status LEDIG.";
            return;
        }
        else {
            try {
                du.moveToCurrentRow();
                du.updateBooleanValue("available", true);
                du.acceptChanges();
                infoText = "Endret status på boligen tilknyttet kontrakten til: LEDIG";
            }
            catch (SQLException e) {
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
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