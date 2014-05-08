/**
 * Created by Sebastian Ramsland on 01.05.2014.
 */

import com.sun.rowset.CachedRowSetImpl;
import java.sql.*;
import java.util.Date;

public class Contract {

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
    private CachedRowSetImpl crs;

    public Contract (int cID) {
        contractID = cID;
    }

    // Metode som innhenter alle dataverdier tilhørende kontrakten
    public void startQuery() throws SQLException {

        // create SQLInterface object
        // execute query
        if (!dbInterface.execQuery(
                "SELECT contract_id, dwelling_unit_id, renter, broker, valid, paid_depositum, signed_by_renter, signed_by_broker, in_effect_date, expiration_date, created, last_modified FROM contract WHERE contract_id = " + contractID)) {

            // exception caught, halt execution
            System.exit(1);
        }

        // create CachedRowSet
        try {
            crs = new CachedRowSetImpl();
            crs = dbInterface.getRowSet();

            while (crs.next()) {
                // Generell informasjon om person
                contractID = crs.getInt("contract_id");
                dwellingUnitID = crs.getInt("dwelling_unit_id");
                renter = crs.getString("renter");
                broker = crs.getString("broker");
                isValid = crs.getBoolean("valid");
                hasPaidDepositum = crs.getBoolean("paid_depositum");
                isSignedByRenter = crs.getBoolean("signed_by_renter");
                isSignedByBroker = crs.getBoolean("signed_by_broker");
                inEffectDate = crs.getDate("in_effect_date");
                expirationDate = crs.getDate("expiration_date");

                // Timestamps
                created = crs.getTimestamp("created");
                lastModified = crs.getTimestamp("last_modified");

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
        }
        catch (SQLException se) {
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

    // Returnerer "true" dersom kravene til å oppnå en gyldig kontrakt oppfylles.
    public boolean checkValidation() {

        // Oppretter kontraktens megler
        Person verifyBroker = new Person(broker);

        // Oppretter kontraktens leietaker
        Person verifyRenter = new Person(renter);

        // Oppretter kontraktens bolig
        DwellingUnit vDwellingUnit = new DwellingUnit(dwellingUnitID);

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

    // Oppdaterer kontraktfeltet "valid" i databasen til korrekt status.
    public void setContractValidation() throws SQLException {
        if (checkValidation() && this.isValid) {
            System.out.println("Kontrakten er allerede gyldig. Metoden foretar seg ingen endringer.");
            return;
        }
        else if (checkValidation()) {
            try {
                // Hopper til øverste rad
                crs.first();

                // Oppdaterer felt
                crs.updateBoolean("valid", true);
                System.out.println("UpdateBoolean...");
                crs.updateRow();
                System.out.println("UpdateRow...");
            }
            catch (SQLException s) {
                System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
            }

            // Sender oppdatering til databasen
            commitChanges();
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
                // Hopper til øverste rad
                crs.first();

                // Oppdaterer felt
                crs.updateBoolean("valid", false);
                System.out.println("UpdateBoolean...");
                crs.updateRow();
                System.out.println("UpdateRow...");
            }
            catch (SQLException s) {
                System.out.println("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
            }

            // Sender oppdatering til databasen
            commitChanges();
            System.out.println("Kontrakten er nå oppdatert til status: Ugyldig.");
        }
    }

    // Oppdaterer kontraktens boligfelt "available" til korrekt status.
    public void setDwellingUnitAvailability() throws SQLException {

        // Oppretter kontraktens bolig
        DwellingUnit du = new DwellingUnit(dwellingUnitID);

        if (checkValidation() && !du.getIsAvailable()) {
            System.out.println("Boligen har allerede status OPPTATT.");
            return;
        }

        else if (checkValidation() && du.getIsAvailable()) {
            try {
                du.updateBooleanValue("available", false);
                du.commitChanges();
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
                du.updateBooleanValue("available", true);
                du.commitChanges();
                System.out.println("Endret status på boligen tilknyttet kontrakten til: LEDIG");
            }
            catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
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

    public CachedRowSetImpl getCachedRowSet() {
        return crs;
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