/**
 * Created by Sebastian Ramsland on 30.04.2014.
 */
import java.sql.*;

public class Maintest {

    public Maintest() {
    }

    public static void main(String[] args) throws SQLException {
        int duID = 1;
        String pNo = "02030350504";
        int cID = 1;

        Maintest test = new Maintest();

            // Oppretter bolig
            DwellingUnit du = new DwellingUnit(duID);

            // Henter boligens verdier
            du.startQuery();

            // Endrer felt
            du.updateStringValue("dwelling_type", "rekkehus");
            du.updateBooleanValue("available", true);
            du.updateIntValue("monthly_price", 5000);

            // Sender oppdatering til databasen
            du.commitChanges();

            ///////////////////////////////////////////////////

            // Oppretter person
            Person person = new Person(pNo);

            // Henter personens verdier
            person.startQuery();

            // Endrer felt
            person.updateStringValue("firstname","Lolita");
            person.updateIntValue("annual_revenue", 99950);
            person.updateBooleanValue("is_broker", false);

            // Sender oppdatering til databasen
            person.commitChanges();


            // Oppretter kontrakt
            Contract contract = new Contract(cID);

            // Henter kontraktens verdier
            contract.startQuery();

            System.out.println("Kontraktgyldighet: " + contract.checkValidation());
    }
}
