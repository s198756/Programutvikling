/**
 * Created by Sebastian Ramsland on 30.04.2014.
 */
//STEP 1. Import required packages
import java.sql.*;
import java.util.Date;

public class DwellingUnit {

    // JDBC driver name and database URL
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://sebastianramsla3.mysql.domeneshop.no/sebastianramsla3";
    private static final String DB_USER = "sebastianramsla3";
    private static final String DB_PASSWORD = "pjW8iUnH";

    // Generell informasjon
    int dwellingUnitID;                         // Unikt ID-nummer for boligen
    boolean isAvailable;                        // Er boligen ledig?
    long propertyOwner;                         // Personnummeret til eier av bolig
    String dwellingType;                        // Boligtype
    int size;                                   // Størrelse i kvadratmeter
    String street;                              // Gateadresse
    String streetNo;                            // Gatenummer (med mulighet for oppgangsbokstav)
    int zipCode;                                // Postnummer
    int monthlyPrice;                           // Månedsleie
    int depositumPrice;                         // Depositumspris

    // Inkludert i leiepris
    boolean inclusiveWarmup;                    // Inklusivt oppvarming/fyring?
    boolean inclusiveWarmWater;                 // Inklusivt varmtvann?
    boolean inclusiveInternet;                  // Inklusivt internett?
    boolean inclusiveCableTV;                   // Inklusivt kabel-TV?
    boolean inclusiveElectricity;               // Inklusivt strøm?
    boolean inclusiveFurniture;                 // Inklusivt møbler?
    boolean inclusiveElectricAppliances;        // Inklusivt hvitevarer?

    // Hva er ikke tillat
    boolean allowHousepets;                     // Tillat husdyr?
    boolean allowSmokers;                       // Tillat røykere?

    // Detaljert informasjon
    int propertySize;                           // Tomtestørrelse (gjelder for villa, rekkehus etc)
    int amountOfBedrooms;                       // Antall soverom
    int amountOfBathrooms;                      // Antall baderom/toalett
    int amountOfTerraces;                       // Antall terasser
    int amountOfBalconies;                      // Antall balkonger
    int amountOfPrivateParking;                 // Antall private parkeringsplasser
    boolean hasElevator;                        // Har boligen heis?
    boolean hasHandicapAccommodation;           // Er boligen tilpasset handicappede?

    // Timestamps
    Date created;
    Date lastModified;


    public DwellingUnit (int duID) throws SQLException {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName(DB_DRIVER);
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String sql = "SELECT * FROM dwelling_unit WHERE dwelling_unit_id = ?";

            pst = con.prepareStatement(sql);

            pst.setInt(1, duID);

            rs = pst.executeQuery();



            while (rs.next()) {

                // Generell informasjon om bolig
                dwellingUnitID = rs.getInt("dwelling_unit_id");
                isAvailable = rs.getBoolean("available");
                propertyOwner = rs.getLong("property_owner");
                dwellingType = rs.getString("dwelling_type");
                size = rs.getInt("size");
                street = rs.getString("street");
                streetNo = rs.getString("street_no");
                zipCode = rs.getInt("zip_code");
                monthlyPrice = rs.getInt("monthly_price");
                depositumPrice = rs.getInt("depositum");

                // Inkludert i leiepris
                inclusiveWarmup = rs.getBoolean("incl_warmup");
                inclusiveWarmWater = rs.getBoolean("incl_warmwater");
                inclusiveInternet = rs.getBoolean("incl_internet");
                inclusiveCableTV = rs.getBoolean("incl_tv");
                inclusiveElectricity = rs.getBoolean("incl_electricity");
                inclusiveFurniture = rs.getBoolean("incl_furniture");
                inclusiveElectricAppliances = rs.getBoolean("incl_elec_appliance");

                // Hva er ikke tillat
                allowHousepets = rs.getBoolean("allow_housepets");
                allowSmokers = rs.getBoolean("allow_smokers");

                // Detaljert informasjon
                propertySize = rs.getInt("property_size");
                amountOfBedrooms = rs.getInt("amount_bedroom");
                amountOfBathrooms = rs.getInt("amount_bathroom");
                amountOfTerraces = rs.getInt("amount_terrace");
                amountOfBalconies = rs.getInt("amount_balcony");
                amountOfPrivateParking = rs.getInt("amount_private_parking");
                hasElevator = rs.getBoolean("elevator");
                hasHandicapAccommodation = rs.getBoolean("handicap_accomm");

                // Timestamps
                created = rs.getDate("created");
                lastModified = rs.getDate("last_modified");


                System.out.println("BoligID:" + dwellingUnitID);
                System.out.println("Ledig:" + isAvailable);
                System.out.println("Boligeier:" + propertyOwner);
                System.out.println("Boligtype:" + dwellingType);
                System.out.println("Størrelse:" + size);
                System.out.println("Gate:" + street);
                System.out.println("Nummer:" + streetNo);
                System.out.println("Postnummer:" + zipCode);
                System.out.println("Månedspris:" + monthlyPrice);
                System.out.println("Depositum:" + depositumPrice);
            }

        }
        catch (Exception e) {
            System.out.println(e);
        }

        finally {

            if (pst != null) {
                pst.close();
            }

            if (con != null) {
                con.close();
            }
        }
    }

    public int getDwellingUnitID() {
        return dwellingUnitID;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public long getPropertyOwner() {
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

    public Date getCreatedDate()  {
        return created;
    }

    public Date getLastModifiedDate()  {
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

    }
}
