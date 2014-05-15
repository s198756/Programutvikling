/**
 * Created by Sebastian Ramsland on 01.05.2014.
 */

import com.sun.rowset.CachedRowSetImpl;
import com.sun.rowset.FilteredRowSetImpl;
import java.sql.*;
import java.sql.Date;

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
    private Timestamp inEffectDate;                     // Startdato for angitt kontraktperiode
    private Timestamp expirationDate;                   // Sluttdato for angitt kontraktperiode

    // Timestamps
    private Timestamp created;
    private Timestamp lastModified;

    // Initialiserer SQLInterface
    private SQLInterface dbInterface;

    // Initialiserer FilteredRowSet
    private FilteredRowSetImpl filteredRowSet;

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

        // Setter inn CachedRowSet fra SQL-queryen
        try {
            cachedRowSet = new CachedRowSetImpl();
            cachedRowSet = dbInterface.getCachedRowSet();
            cachedRowSet.setTableName(TABLENAME);
            int [] keys = {1};
            cachedRowSet.setKeyColumns(keys);

            // Hopper til nederste rad for å finne antall rader
            cachedRowSet.last();

            // Henter ut antall rader
            totalAmountOfRows = cachedRowSet.getRow();

            // Hopper tilbake til øverste rad
            cachedRowSet.first();

            // Nåværende rowSet er av typen "Cached"
            rowSetTypeIsFiltered = false;
        }
        catch (SQLException se) {
            infoText = "Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage();
        }
    }

    // Konstruktør som tar imot eksisterende SQLInterface, radnummer og rowset-type. Henter fram FilteredRowSet eller CachedRowSet fra SQLInterface.
    public Contract (SQLInterface dbInt, int rowID, boolean isFiltered) throws SQLException {
        // Setter inn SQLInterface
        dbInterface = dbInt;

        if (isFiltered) {
            filteredRowSet = new FilteredRowSetImpl();
            filteredRowSet = dbInterface.getFilteredRowSet();
            filteredRowSet.setTableName(TABLENAME);
            int [] keys = {1};
            filteredRowSet.setKeyColumns(keys);

            // Hopper til nederste rad for å finne antall rader
            filteredRowSet.last();

            // Henter ut antall rader
            totalAmountOfRows = filteredRowSet.getRow();

            // Setter pekeren på radnummeret
            jumpToContract(rowID);

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
            jumpToContract(rowID);

            // Nåværende rowSet er av typen "Cached"
            rowSetTypeIsFiltered = false;
        }
    }

    // Innlasting av kontrakt med nåværende radnummer
    public void refreshValues() throws SQLException {
        if (rowSetTypeIsFiltered) {
            try {
                // Hopper til riktig rad
                cachedRowSet.absolute(cachedRowSet.getRow());

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
                inEffectDate = filteredRowSet.getTimestamp("in_effect_date");
                expirationDate = filteredRowSet.getTimestamp("expiration_date");

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
                cachedRowSet.absolute(cachedRowSet.getRow());

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
                inEffectDate = cachedRowSet.getTimestamp("in_effect_date");
                expirationDate = cachedRowSet.getTimestamp("expiration_date");

                // Timestamps
                created = cachedRowSet.getTimestamp("created");
                lastModified = cachedRowSet.getTimestamp("last_modified");

            } catch (SQLException se) {
                infoText = "Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage();
            }
        }
    }

    // Innlasting av kontrakt med et spesifisert radnummer
    public boolean jumpToContract(int rowNo) throws SQLException {

        if (rowSetTypeIsFiltered)  {
            previousRowNumber -= filteredRowSet.getRow() - 1;
            nextRowNumber += filteredRowSet.getRow() + 1;
        }
        else {
            previousRowNumber -= cachedRowSet.getRow() - 1;
            nextRowNumber += cachedRowSet.getRow() + 1;
        }

        String messageJumpToContract = "Hentet ut informasjon om kontrakten.";
        try {
            infoText = messageJumpToContract;
            refreshValues();
            return true;
        } catch (SQLException e){
            infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
            return false;
        }
    }

    // Søk opp kontrakt med kontrakt ID (unik verdi)
    // Returnerer false dersom kontrakten ikke ble funnet.
    public boolean findContractWithID(int cID) throws SQLException {

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
                        return true;
                    }
                }
                infoText = messageWhenCouldNotFindContractWithID;
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
                    if(cID == cachedRowSet.getInt("contract_id")) {
                        infoText = messageWhenFindContractWithID;
                        jumpToContract(cachedRowSet.getRow());
                        return true;
                    }
                }
                infoText = messageWhenCouldNotFindContractWithID;
                return false;
            } catch (SQLException e){
                infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
                return false;
            }
        }
    }

    // Innlasting av neste person
    public void nextPerson() throws SQLException {

        String messageWhenNextContract = "Fant fram til neste kontrakt. \n";
        String messageWhenLastContract = "Du har nådd bunnen av listen. \n";

        if (rowSetTypeIsFiltered) {
            // Sjekker om pekeren står på siste rad
            if (filteredRowSet.getRow() == totalAmountOfRows) {
                infoText = messageWhenLastContract;
            }
            else {
                previousRowNumber = filteredRowSet.getRow() - 1;
                nextRowNumber = filteredRowSet.getRow() + 1;

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
            if (cachedRowSet.getRow() == totalAmountOfRows) {
                infoText = messageWhenLastContract;
            }
            else {
                previousRowNumber = cachedRowSet.getRow() - 1;
                nextRowNumber = cachedRowSet.getRow() + 1;

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
                previousRowNumber = filteredRowSet.getRow() - 1;
                nextRowNumber = filteredRowSet.getRow() + 1;

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
                previousRowNumber = cachedRowSet.getRow() - 1;
                nextRowNumber = cachedRowSet.getRow() + 1;

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

        String messageWhenMoveToInsertRow = "Fyll ut de påkrevde feltene og trykk 'Lagre' for å opprette en ny kontrakt. \n";

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

        String messageWhenCancelUpdates = "Alle endringer lagret i cache ble fjernet. \n";

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


    // Oppdateringsmetode for Timestampverdier
    public void updateTimestampValue(String columnName, Timestamp value) throws SQLException {

        if (rowSetTypeIsFiltered) {
            try {
                // Oppdaterer felt til FilteredRowset
                filteredRowSet.updateTimestamp(columnName, value);
            } catch (SQLException s) {
                infoText = "Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage();
            }
        }
        else {
            try {
                // Oppdaterer felt til CachedRowSet
                cachedRowSet.updateTimestamp(columnName, value);
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
    public boolean acceptChanges() throws SQLException {

        String messageWhenAcceptChanges = "Endringer ble sendt til databasen. \n";
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
                System.out.println("Etter acceptchanges: " + cachedRowSet.getRow());
                return true;
            } else {
                infoText = errorMessageWhenAcceptChanges;
                return false;
            }
        }
    }

    // Hopper til nederste rad
    public void last() throws SQLException {
        try {
            if (rowSetTypeIsFiltered) {
                // Flytter peker til nederste rad
                filteredRowSet.last();

                // Setter nåværende nadnummer lik nederste rad
                previousRowNumber = filteredRowSet.getRow() -1;
                nextRowNumber = filteredRowSet.getRow() + 1;

                // Henter verdier fra nåværende rad
                refreshValues();
            }
            else {
                // Flytter peker til nederste rad
                cachedRowSet.last();

                // Setter nåværende nadnummer lik nederste rad
                previousRowNumber = cachedRowSet.getRow() -1;
                nextRowNumber = cachedRowSet.getRow() + 1;

                // Henter verdier fra nåværende rad
                refreshValues();
            }
        } catch (SQLException e) {
            infoText = "Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage();
        }
    }

    public int getTotalAmountOfRows() {
        return totalAmountOfRows;
    }

    public String getInfoText() {
        return infoText;
    }

    public int getCurrentRowNumber() throws SQLException {
        if (rowSetTypeIsFiltered)
            return filteredRowSet.getRow();
        else
            return cachedRowSet.getRow();
    }

    public int getContractID()  {
        return contractID;
    }

    public int getDwellingUnitID() {
        return dwellingUnitID;
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

    public Timestamp getInEffectDate() {
        return inEffectDate;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public Timestamp getCreatedDate()  {
        return created;
    }

    public Timestamp getLastModifiedDate()  {
        return lastModified;
    }
}