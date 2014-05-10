/**
 * Created by Sebastian Ramsland on 30.04.2014.
 */

import com.sun.rowset.CachedRowSetImpl;
import javax.sql.rowset.FilteredRowSet;
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

    // Konstruktør som oppretter et nytt CachedRowSet som inneholder ALLE registrerte boliger fra databasen
    public DwellingUnit () {
        // Oppretter nytt SQLInterface
        dbInterface = new SQLInterface();

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
            cachedRowSet.first();

            // Nåværende rowSet er av typen "Cached"
            rowSetTypeIsFiltered = false;
        }
        catch (SQLException se) {
            System.out.println("Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage());
        }
    }

    // Konstruktør som tar imot eksisterende SQLInterface, CachedRowSet og radnummer (et ufiltrert søk)
    public DwellingUnit (SQLInterface dbInt, CachedRowSetImpl crs, int rowID) throws SQLException {
        // Setter inn SQLInterface
        dbInterface = dbInt;

        cachedRowSet = crs;
        cachedRowSet.setTableName(TABLENAME);

        // Setter inn mottatt radnummer
        currentRowNumber = rowID;

        // Setter pekeren på radnummeret
        jumpToDwellingUnit(rowID);

        // Nåværende rowSet er av typen "Cached"
        rowSetTypeIsFiltered = false;
    }

    // Konstruktør som tar imot eksisterende SQLInterface, FilteredRowSet og radnummer (et filtrert søk)
    public DwellingUnit (SQLInterface dbInt, FilteredRowSet frs, int rowID) throws SQLException {
        // Setter inn SQLInterface
        dbInterface = dbInt;

        // Setter inn FilteredRowSet og tabellnavn
        filteredRowSet = frs;
        filteredRowSet.setTableName(TABLENAME);

        // Setter inn mottatt radnummer
        currentRowNumber = rowID;

        // Setter pekeren på radnummeret
        jumpToDwellingUnit(rowID);

        // Nåværende rowSet er av typen "Filtered"
        rowSetTypeIsFiltered = true;
    }

    // Innlasting av bolig med nåværende radnummer
    public void refreshValues() throws SQLException {
        if (rowSetTypeIsFiltered) {
            try {
                // Leser av verdier

                // Generell informasjon om bolig
                dwellingUnitID = filteredRowSet.getInt("dwelling_unit_id");
                isAvailable = filteredRowSet.getBoolean("available");
                propertyOwner = filteredRowSet.getString("property_owner");
                dwellingType = filteredRowSet.getString("dwelling_type");
                size = filteredRowSet.getInt("size");
                street = filteredRowSet.getString("street");
                streetNo = filteredRowSet.getString("street_no");
                zipCode = filteredRowSet.getInt("zip_code");
                monthlyPrice = filteredRowSet.getInt("monthly_price");
                depositumPrice = filteredRowSet.getInt("depositum");

                // Inkludert i leiepris
                inclusiveWarmup = filteredRowSet.getBoolean("incl_warmup");
                inclusiveWarmWater = filteredRowSet.getBoolean("incl_warmwater");
                inclusiveInternet = filteredRowSet.getBoolean("incl_internet");
                inclusiveCableTV = filteredRowSet.getBoolean("incl_tv");
                inclusiveElectricity = filteredRowSet.getBoolean("incl_electricity");
                inclusiveFurniture = filteredRowSet.getBoolean("incl_furniture");
                inclusiveElectricAppliances = filteredRowSet.getBoolean("incl_elec_appliance");

                // Hva er ikke tillat
                allowHousepets = filteredRowSet.getBoolean("allow_housepets");
                allowSmokers = filteredRowSet.getBoolean("allow_smokers");

                // Detaljert informasjon
                propertySize = filteredRowSet.getInt("property_size");
                amountOfBedrooms = filteredRowSet.getInt("amount_bedroom");
                amountOfBathrooms = filteredRowSet.getInt("amount_bathroom");
                amountOfTerraces = filteredRowSet.getInt("amount_terrace");
                amountOfBalconies = filteredRowSet.getInt("amount_balcony");
                amountOfPrivateParking = filteredRowSet.getInt("amount_private_parking");
                hasElevator = filteredRowSet.getBoolean("elevator");
                hasHandicapAccommodation = filteredRowSet.getBoolean("handicap_accomm");

                // Timestamps
                created = filteredRowSet.getTimestamp("created");
                lastModified = filteredRowSet.getTimestamp("last_modified");

            } catch (SQLException se) {
                System.out.println("Error code: " + se.getErrorCode() + "\tLocalizedMessage: " + se.getLocalizedMessage());
            }
        }
        else {
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
    }

    // Innlasting av bolig med et spesifisert radnummer
    public void jumpToDwellingUnit(int rowNo) throws SQLException {
        currentRowNumber = rowNo;
        previousRowNumber -= currentRowNumber - 1;
        nextRowNumber += currentRowNumber + 1;
        String messageJumpToDwellingUnit = "Forsøker å hente ut verdier til boligen på rad " + currentRowNumber + ".\n";
        try {
            System.out.println(messageJumpToDwellingUnit);
            refreshValues();
        } catch (SQLException e){
            System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Søk opp bolig med bolig ID (unik verdi)
    public void findDwellingUnitWithID(int duID) throws SQLException {

        String messageWhenFindDwellingUnitWithID = "Boligen ble funnet. Flytter pekeren til boligen og henter verdier. \n";
        String messageWhenCouldNotFindDwellingUnitWithID = "Fant ikke boligen med den angitte bolig IDen. \n";

        if (rowSetTypeIsFiltered) {
            try {
                // Hopper først til toppen av tabellen
                filteredRowSet.beforeFirst();

                // Søker igjennom rowSet
                while (filteredRowSet.next()) {
                    if (duID == filteredRowSet.getInt("dwelling_unit_id")) {
                        System.out.println(messageWhenFindDwellingUnitWithID);
                        jumpToDwellingUnit(filteredRowSet.getRow());
                        return;
                    }
                }
                System.out.println(messageWhenCouldNotFindDwellingUnitWithID);
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
                    if(duID == cachedRowSet.getInt("dwelling_unit_id")) {
                        System.out.println(messageWhenFindDwellingUnitWithID);
                        jumpToDwellingUnit(cachedRowSet.getRow());
                        return;
                    }
                }
                System.out.println(messageWhenCouldNotFindDwellingUnitWithID);

            } catch (SQLException e){
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
    }

    // Innlasting av neste bolig
    public void nextDwellingUnit() throws SQLException {
        currentRowNumber += 1;
        previousRowNumber = currentRowNumber - 1;
        nextRowNumber = currentRowNumber + 1;
        String messageWhenNextDwellingUnit = "Hopper til neste bolig. \n";

        if (rowSetTypeIsFiltered) {
            try {
                System.out.println(messageWhenNextDwellingUnit);
                filteredRowSet.next();
                refreshValues();
            } catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
        else {
            try {
                System.out.println(messageWhenNextDwellingUnit);
                cachedRowSet.next();
                refreshValues();
            } catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
    }

    // Innlasting av forrige bolig
    public void previousDwellingUnit() throws SQLException {
        currentRowNumber -= 1;
        previousRowNumber = currentRowNumber - 1;
        nextRowNumber = currentRowNumber + 1;
        String messageWhenPreviousDwellingUnit = "Hopper til forrige bolig. \n";

        if (rowSetTypeIsFiltered) {
            try {
                System.out.println(messageWhenPreviousDwellingUnit);
                filteredRowSet.previous();
                refreshValues();
            } catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
        else {
            try {
                System.out.println(messageWhenPreviousDwellingUnit);
                cachedRowSet.previous();
                refreshValues();
            } catch (SQLException e) {
                System.out.println("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
            }
        }
    }

    // Flytter pekeren til en innsettingsrad. Må kalles opp ved opprettelse av ny bolig.
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
}
