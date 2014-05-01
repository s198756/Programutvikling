/**
 * Created by Sebastian Ramsland on 30.04.2014.
 */
import java.sql.*;

public class DwellingUnitMain {

    public DwellingUnitMain() {
        DwellingUnit t;
    }

    public static void main(String[] args) throws SQLException {
        int duID = 1;
        DwellingUnitMain test = new DwellingUnitMain();
        
        try {
            DwellingUnit f = new DwellingUnit(duID);

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
    }
}
