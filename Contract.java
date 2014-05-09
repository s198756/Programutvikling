/**
 * Created by Sebastian Ramsland on 01.05.2014.
 */

import com.sun.rowset.CachedRowSetImpl;
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

    // Oppretter SQLInterface
    private SQLInterface dbInterface = new SQLInterface();

    // Oppretter CachedRowSet og radnummer
    private CachedRowSetImpl cachedRowSet;
    private int currentRowNumber = 0;
    private int nextRowNumber;
    private int previousRowNumber;

    public Contract () {

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
            System.out.println("Etter konstruktør: " + getCurrentRowNumber() + " CachedRow: " + cachedRowSet.getRow());
        }
        catch (SQLException se) {
            System.out.println("Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage());
        }
    }

    // Innlasting av person med nåværende radnummer
    public void refreshValues() throws SQLException {
        try {
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


        System.out.println("Etter refreshValues: " + getCurrentRowNumber() + " CachedRow: " + cachedRowSet.getRow());
    }

    // Innlasting av kontrakt med et spesifisert radnummer
    public void jumpToContract(int rowNo) throws SQLException {
        currentRowNumber = rowNo;
        previousRowNumber -= currentRowNumber - 1;
        nextRowNumber += currentRowNumber + 1;
        try {
            cachedRowSet.absolute(currentRowNumber);
            refreshValues();
        }
        catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Innlasting av neste kontrakt
    public void nextContract() throws SQLException {
        currentRowNumber += 1;
        previousRowNumber = currentRowNumber - 1;
        nextRowNumber = currentRowNumber + 1;
        try {
            System.out.println("Hopper til neste kontrakt. \n");
            cachedRowSet.next();
            refreshValues();
        }
        catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Innlasting av forrige kontrakt
    public void previousContract() throws SQLException {
        currentRowNumber -= 1;
        previousRowNumber = currentRowNumber - 1;
        nextRowNumber = currentRowNumber + 1;
        try {
            System.out.println("Hopper tilbake til forrige kontrakt. \n");
            cachedRowSet.previous();
            refreshValues();
        }
        catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Flytter pekeren til en innsettingsrad. Må kalles opp ved opprettelse av ny kontrakt.
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

    // Søk opp kontrakt
    public void findContract(int cID) throws SQLException {
        try {
            // Søker igjennom tabellen
            while(cachedRowSet.next()) {
                if(cID == cachedRowSet.getInt("contract_id")) {
                    System.out.println("Kontrakten ble funnet. Flytter pekeren til kontrakten og henter verdier");
                    jumpToContract(cachedRowSet.getRow());
                    return;
                }
            }
            System.out.println("Fant ikke kontrakten med den angitte nummeret.");
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

    // Returnerer "true" dersom kravene til å oppnå en gyldig kontrakt oppfylles.
    public boolean checkValidation() throws SQLException {

        try {
            // Oppretter kontraktens megler
            Person verifyBroker = new Person();
            verifyBroker.findPerson(broker);

            // Oppretter kontraktens leietaker
            Person verifyRenter = new Person();
            verifyRenter.findPerson(renter);

            // Oppretter kontraktens bolig
            DwellingUnit vDwellingUnit = new DwellingUnit();
            vDwellingUnit.findDwellingUnit(dwellingUnitID);

            // Oppretter nåværende dato
            Date now = new Date();

            if (!verifyBroker.getIsBroker()) {
                System.out.println("Feil: Angitt megler i kontrakten er ingen megler.");
                return false;
            }

            else if (verifyRenter.getPersonNo().equals(vDwellingUnit.getPropertyOwner())) {
                System.out.println("Feil: Leietakeren kan ikke leie sin egen bolig.");
                return false;
            }

            else if (verifyRenter.getPersonNo().equals(verifyBroker.getPersonNo())) {
                System.out.println("Feil: Leietakeren kan ikke være samme person som megleren.");
                return false;
            }

            else if (!isSignedByRenter) {
                System.out.println("Feil: Kontrakten er ikke signert av leietaker.");
                return false;
            }

            else if (!isSignedByBroker) {
                System.out.println("Feil: Kontrakten er ikke signert av megler.");
                return false;
            }

            else if(!now.after(inEffectDate) || !now.before(expirationDate)) {
                System.out.println("Feil: Dagens dato er ikke innenfor kontraktperioden.");
                return false;
            }

            else {
                return true;
            }
        }
        catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
        return false;
    }

    // Oppdaterer kontraktfeltet "valid" i databasen til korrekt status.
    public void setContractValidation() throws SQLException {
        if (checkValidation() && this.isValid) {
            System.out.println("Kontrakten er allerede gyldig. Metoden foretar seg ingen endringer.");
            return;
        }
        else if (checkValidation()) {
            try {
                // Oppdaterer felt
                cachedRowSet.moveToCurrentRow();
                cachedRowSet.updateBoolean("valid", true);
                System.out.println("Boolean-verdi oppdatert. \n");
                cachedRowSet.updateRow();
                System.out.println("Oppdatering av raden ble lagret i cache. \n");
            }
            catch (SQLException s) {
                System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
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
            try {
                // Oppdaterer felt
                cachedRowSet.moveToCurrentRow();
                cachedRowSet.updateBoolean("valid", false);
                System.out.println("Boolean-verdi oppdatert. \n");
                cachedRowSet.updateRow();
                System.out.println("Oppdatering av raden ble lagret i cache. \n");
            }
            catch (SQLException s) {
                System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
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
        du.findDwellingUnit(dwellingUnitID);

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