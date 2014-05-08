/**
 * Created by Sebastian Ramsland on 30.04.2014.
 */

import com.sun.rowset.CachedRowSetImpl;
import java.sql.*;

public class DwellingUnit {

    // Generell informasjon
    private int dwellingUnitID;                         // Unikt ID-nummer for boligen
    private boolean isAvailable;                        // Er boligen ledig?
    private String propertyOwner;                       // Personnummeret til eier av bolig
    private String dwellingType;                        // Boligtype
    private int size;                                   // Størrelse i kvadratmeter
    private String street;                              // Gateadresse
    private String streetNo;                            // Gatenummer (med mulighet for oppgangsbokstav)
    private int zipCode;                                // Postnummer
    private String area;                                // Poststed
    private String township;                            // Kommune
    private String county;                              // Fylke
    private int monthlyPrice;                           // Månedsleie
    private int depositumPrice;                         // Depositumspris

    // Inkludert i leiepris
    private boolean inclusiveWarmup;                    // Inklusivt oppvarming/fyring?
    private boolean inclusiveWarmWater;                 // Inklusivt varmtvann?
    private boolean inclusiveInternet;                  // Inklusivt internett?
    private boolean inclusiveCableTV;                   // Inklusivt kabel-TV?
    private boolean inclusiveElectricity;               // Inklusivt strøm?
    private boolean inclusiveFurniture;                 // Inklusivt møbler?
    private boolean inclusiveElectricAppliances;        // Inklusivt hvitevarer?

    // Hva er ikke ønsket?
    private boolean allowHousepets;                     // Tillat husdyr?
    private boolean allowSmokers;                       // Tillat røykere?

    // Detaljert informasjon
    private int propertySize;                           // Tomtestørrelse (gjelder for villa, rekkehus etc)
    private int amountOfBedrooms;                       // Antall soverom
    private int amountOfBathrooms;                      // Antall baderom/toalett
    private int amountOfTerraces;                       // Antall terasser
    private int amountOfBalconies;                      // Antall balkonger
    private int amountOfPrivateParking;                 // Antall private parkeringsplasser
    private boolean hasElevator;                        // Har boligen heis?
    private boolean hasHandicapAccommodation;           // Er boligen tilpasset handicappede?

    // Timestamps
    private Timestamp created;                          // Dato/tidspunkt for opprettelse
    private Timestamp lastModified;                     // Dato/tidspunkt for sist endring

    // Oppretter SQLInterface
    private SQLInterface dbInterface = new SQLInterface();
    private CachedRowSetImpl crs;


    public DwellingUnit (int duID) {
        dwellingUnitID = duID;
    }

    // Metode som innhenter alle dataverdier tilhørende boenheten
    public void startQuery() throws SQLException {

        // create SQLInterface object
        // execute query
        if (!dbInterface.execQuery(
                "SELECT dwelling_unit_id, available, property_owner, dwelling_type, size, street, street_no, zip_code, area, township, county, monthly_price, depositum, incl_warmup, incl_warmwater, incl_internet, incl_tv, incl_electricity, incl_furniture, incl_elec_appliance, allow_housepets, allow_smokers, property_size, amount_bedroom, amount_bathroom, amount_terrace, amount_balcony, amount_private_parking, elevator, handicap_accomm, created, last_modified FROM dwelling_unit_all_fields WHERE dwelling_unit_id = " + dwellingUnitID)) {

            // exception caught, halt execution
            System.exit(1);
        }

        // create CachedRowSet
        try {
            crs = new CachedRowSetImpl();
            crs = dbInterface.getRowSet();

            while (crs.next()) {

                // Generell informasjon om bolig
                dwellingUnitID = crs.getInt("dwelling_unit_id");
                isAvailable = crs.getBoolean("available");
                propertyOwner = crs.getString("property_owner");
                dwellingType = crs.getString("dwelling_type");
                size = crs.getInt("size");
                street = crs.getString("street");
                streetNo = crs.getString("street_no");
                zipCode = crs.getInt("zip_code");
                area = crs.getString("area");
                township = crs.getString("township");
                county = crs.getString("county");
                monthlyPrice = crs.getInt("monthly_price");
                depositumPrice = crs.getInt("depositum");

                // Inkludert i leiepris
                inclusiveWarmup = crs.getBoolean("incl_warmup");
                inclusiveWarmWater = crs.getBoolean("incl_warmwater");
                inclusiveInternet = crs.getBoolean("incl_internet");
                inclusiveCableTV = crs.getBoolean("incl_tv");
                inclusiveElectricity = crs.getBoolean("incl_electricity");
                inclusiveFurniture = crs.getBoolean("incl_furniture");
                inclusiveElectricAppliances = crs.getBoolean("incl_elec_appliance");

                // Hva er ikke tillat
                allowHousepets = crs.getBoolean("allow_housepets");
                allowSmokers = crs.getBoolean("allow_smokers");

                // Detaljert informasjon
                propertySize = crs.getInt("property_size");
                amountOfBedrooms = crs.getInt("amount_bedroom");
                amountOfBathrooms = crs.getInt("amount_bathroom");
                amountOfTerraces = crs.getInt("amount_terrace");
                amountOfBalconies = crs.getInt("amount_balcony");
                amountOfPrivateParking = crs.getInt("amount_private_parking");
                hasElevator = crs.getBoolean("elevator");
                hasHandicapAccommodation = crs.getBoolean("handicap_accomm");


                // Timestamps
                created = crs.getTimestamp("created");
                lastModified = crs.getTimestamp("last_modified");


                System.out.println("BoligID: " + dwellingUnitID);
                System.out.println("Ledig: " + isAvailable);
                System.out.println("Boligeier: " + propertyOwner);
                System.out.println("Boligtype: " + dwellingType);
                System.out.println("Størrelse: " + size);
                System.out.println("Gate: " + street);
                System.out.println("Nummer: " + streetNo);
                System.out.println("Postnummer: " + zipCode);
                System.out.println("Poststed: " + area);
                System.out.println("Kommune: " + township);
                System.out.println("Fylke: " + county);
                System.out.println("Månedspris: " + monthlyPrice);
                System.out.println("Depositum: " + depositumPrice);
                System.out.println("\n /////// END OF DWELLING UNIT //////// \n");
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

    public CachedRowSetImpl getCachedRowSet() {
        return crs;
    }

    public int getDwellingUnitID() {
        return dwellingUnitID;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public String getPropertyOwner() {
        return propertyOwner;
    }

    public String getDwellingType() {
        return dwellingType;
    }

    public int getSize() {
        return size;
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

    public String getArea()  {
        return area;
    }

    public String getTownship()  {
        return township;
    }

    public String getCounty()  {
        return county;
    }

    public int getMonthlyPrice() {
        return monthlyPrice;
    }

    public int getDepositumPrice() {
        return depositumPrice;
    }

    public boolean getInclusiveWarmup() {
        return inclusiveWarmup;
    }

    public boolean getInclusiveWarmWater() {
        return inclusiveWarmWater;
    }

    public boolean getInclusiveInternet() {
        return inclusiveInternet;
    }

    public boolean getInclusiveCableTV() {
        return inclusiveCableTV;
    }

    public boolean getInclusiveElectricity() {
        return inclusiveElectricity;
    }

    public boolean getInclusiveFurniture() {
        return inclusiveFurniture;
    }

    public boolean getInclusiveElectricAppliances() {
        return inclusiveElectricAppliances;
    }

    public boolean getAllowHousepets() {
        return allowHousepets;
    }

    public boolean getAllowSmokers() {
        return allowSmokers;
    }

    public int getPropertySize() {
        return propertySize;
    }

    public int getAmountOfBedrooms() {
        return amountOfBedrooms;
    }

    public int getAmountOfBathrooms() {
        return amountOfBathrooms;
    }

    public int getAmountOfTerraces() {
        return amountOfTerraces;
    }

    public int getAmountOfBalconies() {
        return amountOfBalconies;
    }

    public int getAmountOfPrivateParking() {
        return amountOfPrivateParking;
    }

    public boolean getHasElevator() {
        return hasElevator;
    }

    public boolean getHasHandicapAccommodation() {
        return hasHandicapAccommodation;
    }

    public Timestamp getCreatedDate()  {
        return created;
    }

    public Timestamp getLastModifiedDate()  {
        return lastModified;
    }

    public boolean updateAvailability(boolean newStatus)   {

        if (newStatus == isAvailable) {
            return true;
        }
        else {
            // UPDATE dwelling_unit
            // SET available = newStatus
            // WHERE dwelling_unit_id LIKE duID
        }
        return true;
    }
}
