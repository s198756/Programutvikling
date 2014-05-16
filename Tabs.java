
/**
 * Created by Thomas Newman, s198753 on 05.05.2014.
 * Formålet med denne klassen er å samle alle JPanels vi har laget og legge dem inn i en JFrame
 */
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.SQLException;



public class Tabs extends JFrame
{
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private ContractUI cUI;
    private PersonUI pUI;
    private DwellingUnitUI dUI;
    private Person person;
    private Contract contract;
    private DwellingUnit dwellingUnit;


    public Tabs() throws SQLException
    {
        //Implementerer de tre klassene
        person = new Person();
        contract = new Contract();
        dwellingUnit = new DwellingUnit();

        // Konstruktør for å sette opp JTabbedPane og legger til content fra JPanels og klasser.
        tabbedPane = new JTabbedPane();
        cUI = new ContractUI(contract, person, dwellingUnit);
        pUI = new PersonUI(person);
        dUI = new DwellingUnitUI(dwellingUnit);

        tabbedPane.addTab("Kontrakt", cUI);
        tabbedPane.addTab("Person", pUI);
        tabbedPane.addTab("Bolig", dUI);

        // Konstruktør som legger til tabbedPane og setter opp JFramens størrelse
        frame = new JFrame("Boligformidling");
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setVisible(true);
        frame.validate();


        // Konstruktør som skal legge inn program ikon.
        String bildefil = "Bilder/PrIcon2.png";
        URL kilde = Tabs.class.getResource(bildefil);
        if (kilde != null) {
            ImageIcon bilde = new ImageIcon(kilde);
            Image ikon = bilde.getImage();
            frame.setIconImage(ikon);
        }
    }

    // Main metode for programmet
    public static void main(String[] args) throws SQLException
    {
        new Tabs();
    }

}
