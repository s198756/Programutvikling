/**
 * Created by Sebastian Ramsland on 01.05.2014.
 */

import java.sql.*;
import java.util.Date;

public class Contract {

    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://sebastianramsla3.mysql.domeneshop.no/sebastianramsla3";
    private static final String DB_USER = "sebastianramsla3";
    private static final String DB_PASSWORD = "pjW8iUnH";

    // Generell informasjon
    int contractID;                             // Kontrakt ID
    int dwellingUnitID;                         // Bolig ID
    long renter;                                // Personnummeret til leietaker
    long broker;                                // Personnummeret til megler
    boolean isValid;                            // Er kontrakten gyldig?
    boolean hasPaidDepositum;                   // Er depositum innbetalt?
    boolean isSignedByRenter;                   // Er kontrakten signert av leietaker?
    boolean isSignedByBroker;                   // Er kontrakten signert av megler?
    Date inEffectDate;                          // Startdato for angitt kontraktperiode
    Date expirationDate;                        // Sluttdato for angitt kontraktperiode

    // Timestamps
    Date created;
    Date lastModified;

    public Contract (int cID) throws SQLException {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName(DB_DRIVER);
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String sql = "SELECT * FROM contract WHERE contract_id = ?";

            pst = con.prepareStatement(sql);

            pst.setLong(1, cID);

            rs = pst.executeQuery();

            while (rs.next()) {

                // Generell informasjon om person
                contractID = rs.getInt("contract_id");
                dwellingUnitID = rs.getInt("dwelling_unit_id");
                renter = rs.getLong("renter");
                broker = rs.getLong("broker");
                isValid = rs.getBoolean("valid");
                hasPaidDepositum = rs.getBoolean("paid_depositum");
                isSignedByRenter = rs.getBoolean("signed_by_renter");
                isSignedByBroker = rs.getBoolean("signed_by_broker");
                inEffectDate = rs.getDate("in_effect_date");
                expirationDate = rs.getDate("expiration_date");

                // Timestamps
                created = rs.getDate("created");
                lastModified = rs.getDate("last_modified");

                System.out.println("Kontrakt ID:" + contractID);
                System.out.println("Leietaker:" + renter);
                System.out.println("Megler:" + broker);
                System.out.println("Gyldig:" + isValid);
                System.out.println("Innbetalt depositum:" + hasPaidDepositum);
                System.out.println("Signert av leietaker:" + isSignedByRenter);
                System.out.println("Signert av megler:" + isSignedByBroker);
                System.out.println("Startdato:" + inEffectDate);
                System.out.println("Sluttdato:" + expirationDate);
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

    public int getContractID()  {
        return contractID;
    }

    public long getRenter() {
        return renter;
    }

    public long getBroker() {
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

    public Date getCreatedDate()  {
        return created;
    }

    public Date getLastModifiedDate()  {
        return lastModified;
    }

    public boolean validateContract() throws SQLException {
    // Metode som oppdaterer valid-feltet i databasen tilhørende riktig kontrakt ID, dersom kravene utfylles.
    // Returnerer true dersom oppdateringen blir gjennomført - eller hvis feltet allerede er "valid".

        if (this.isValid)
            return true;
        else
        {
            Person vBroker = new Person(broker);
            Person vRenter = new Person(renter);
            DwellingUnit vDwellingUnit = new DwellingUnit(dwellingUnitID);
            Date now = new Date();

            if (!vBroker.getIsBroker()) {
                System.out.println("Feil: Angitt megler i kontrakten er ingen megler.");
                return false;
            }

            else if (vRenter.getPersonNo() == vDwellingUnit.getPropertyOwner()) {
                System.out.println("Feil: Leietakeren kan ikke leie sin egen bolig.");
                return false;
            }

            else if (vRenter.getPersonNo() == vBroker.getPersonNo()) {
                System.out.println("Feil: Leietakeren kan ikke også være megleren.");
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
                System.out.println("Feil: Nåværende dato er ikke innenfor kontraktperioden.");
                return false;
            }

            else {
                Connection con = null;
                PreparedStatement pst = null;
                ResultSet rs = null;

                String sql = "UPDATE contract SET valid = ? WHERE contract_id = ?";

                // Kobler til databasen og oppdaterer valid-feltet til "True"
                try {
                    con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                    pst = con.prepareStatement(sql);

                    pst.setBoolean(1, true);
                    pst.setInt(2, contractID);

                    pst.executeUpdate();

                    System.out.println("Update utført!");

                    pst.close();
                    con.close();
                }

                catch (SQLException e) {
                    System.out.println(e.getMessage());
                    return false;
                }

                finally {

                    if (pst != null) {
                        pst.close();
                    }

                    if (con != null) {
                        con.close();
                    }
                }
                return true;
            }
        }
    }
}