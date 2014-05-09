/**
 * Created by Sebastian Ramsland on 30.04.2014.
 */

import com.sun.rowset.CachedRowSetImpl;
import java.sql.*;

public class DwellingUnit {

    // Tabellnavn
    private static final String TABLENAME = "dwelling_unit";

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
    private String category;                            // Beskrivelse av postnummer
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

    // Hva er tillat?
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

    // Oppretter CachedRowSet og radnummer
    private CachedRowSetImpl cachedRowSet;
    private int currentRowNumber = 0;
    private int nextRowNumber;
    private int previousRowNumber;


    public DwellingUnit () {

        // Utfører SQL-query
        if (!dbInterface.execQuery(
                "SELECT dwelling_unit_id, available, property_owner, dwelling_type, size, street, street_no, zip_code, monthly_price, depositum, incl_warmup, incl_warmwater, incl_internet, incl_tv, incl_electricity, incl_furniture, incl_elec_appliance, allow_housepets, allow_smokers, property_size, amount_bedroom, amount_bathroom, amount_terrace, amount_balcony, amount_private_parking, elevator, handicap_accomm, created, last_modified FROM dwelling_unit")) {

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

            // Generell informasjon om bolig
            dwellingUnitID = cachedRowSet.getInt("dwelling_unit_id");
            isAvailable = cachedRowSet.getBoolean("available");
            propertyOwner = cachedRowSet.getString("property_owner");
            dwellingType = cachedRowSet.getString("dwelling_type");
            size = cachedRowSet.getInt("size");
            street = cachedRowSet.getString("street");
            streetNo = cachedRowSet.getString("street_no");
            zipCode = cachedRowSet.getInt("zip_code");
            monthlyPrice = cachedRowSet.getInt("monthly_price");
            depositumPrice = cachedRowSet.getInt("depositum");

            // Inkludert i leiepris
            inclusiveWarmup = cachedRowSet.getBoolean("incl_warmup");
            inclusiveWarmWater = cachedRowSet.getBoolean("incl_warmwater");
            inclusiveInternet = cachedRowSet.getBoolean("incl_internet");
            inclusiveCableTV = cachedRowSet.getBoolean("incl_tv");
            inclusiveElectricity = cachedRowSet.getBoolean("incl_electricity");
            inclusiveFurniture = cachedRowSet.getBoolean("incl_furniture");
            inclusiveElectricAppliances = cachedRowSet.getBoolean("incl_elec_appliance");

            // Hva er ikke tillat
            allowHousepets = cachedRowSet.getBoolean("allow_housepets");
            allowSmokers = cachedRowSet.getBoolean("allow_smokers");

            // Detaljert informasjon
            propertySize = cachedRowSet.getInt("property_size");
            amountOfBedrooms = cachedRowSet.getInt("amount_bedroom");
            amountOfBathrooms = cachedRowSet.getInt("amount_bathroom");
            amountOfTerraces = cachedRowSet.getInt("amount_terrace");
            amountOfBalconies = cachedRowSet.getInt("amount_balcony");
            amountOfPrivateParking = cachedRowSet.getInt("amount_private_parking");
            hasElevator = cachedRowSet.getBoolean("elevator");
            hasHandicapAccommodation = cachedRowSet.getBoolean("handicap_accomm");

            // Timestamps
            created = cachedRowSet.getTimestamp("created");
            lastModified = cachedRowSet.getTimestamp("last_modified");

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
        System.out.println("Kategori: " + category);
        System.out.println("Månedspris: " + monthlyPrice);
        System.out.println("Depositum: " + depositumPrice);
        System.out.println("\n /////// END OF DWELLING UNIT //////// \n");

        System.out.println("Etter refreshValues: " + getCurrentRowNumber() + " CachedRow: " + cachedRowSet.getRow());
    }

    // Innlasting av bnolig med et spesifisert radnummer
    public void jumpToDwellingUnit(int rowNo) throws SQLException {
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

    // Innlasting av neste bolig
    public void nextDwellingUnit() throws SQLException {
        currentRowNumber += 1;
        previousRowNumber = currentRowNumber - 1;
        nextRowNumber = currentRowNumber + 1;
        try {
            System.out.println("Hopper til neste bolig. \n");
            cachedRowSet.next();
            refreshValues();
        }
        catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Innlasting av forrige bolig
    public void previousDwellingUnit() throws SQLException {
        currentRowNumber -= 1;
        previousRowNumber = currentRowNumber - 1;
        nextRowNumber = currentRowNumber + 1;
        try {
            System.out.println("Hopper tilbake til forrige bolig. \n");
            cachedRowSet.previous();
            refreshValues();
        }
        catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Flytter pekeren til en innsettingsrad. Må kalles opp ved opprettelse av ny bolig.
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

    // Søk opp bolig
    public void findDwellingUnit(int duID) throws SQLException {
        try {
            // Søker igjennom tabellen
            while(cachedRowSet.next()) {
                if(duID == cachedRowSet.getInt("dwelling_unit_id")) {
                    System.out.println("Boligen ble funnet. Flytter pekeren til boligen og henter verdier");
                    jumpToDwellingUnit(cachedRowSet.getRow());
                    return;
                }
            }
            System.out.println("Fant ikke boligen med det angitte nummeret.");
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
