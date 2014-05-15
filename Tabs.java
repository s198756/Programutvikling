/**
 * Created by Thomas Newman, s198753 on 05.05.2014.
 */

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class Tabs extends JFrame {
    JFrame frame = new JFrame();
    JTabbedPane tabbedPane = new JTabbedPane();

    ContractUI cUI;
    ContracttableWindow fc;
    PersonUI pUI;
    PersontableWindow fp;
    DwellingUnitUI rUI;
    DwellingtableWindow fr;

    public Tabs() throws SQLException {

        Person person = new Person();
        DwellingUnit dwellingUnit = new DwellingUnit();
        Contract contract = new Contract();

        cUI = new ContractUI(contract, person, dwellingUnit);
        fp = new PersontableWindow();
        pUI = new PersonUI(person);
        fr = new DwellingtableWindow();
        rUI = new DwellingUnitUI(dwellingUnit);
        fc = new ContracttableWindow();

        tabbedPane.add("Finn Person", fp );
        frame.getContentPane();

        tabbedPane.add("Person", pUI);
        frame.getContentPane();

        tabbedPane.add("Finn Bolig", fr);
        frame.getContentPane();

        tabbedPane.add("Bolig", rUI);
        frame.getContentPane();

        tabbedPane.add("Finn Kontrakt", fc);
        frame.getContentPane();

        tabbedPane.add("Kontrakt", cUI);
        frame.getContentPane();

        frame.getContentPane().add(tabbedPane);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1280, 720));
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws SQLException {
        new Tabs();
    }
}



