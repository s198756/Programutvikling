package GUI.Files;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.event.*;
import java.net.URL;
import java.sql.SQLException;



/**
 * Created by Thomas Newman, s198753 on 05.05.2014.
 */

public class Tabs extends JFrame
{

    JFrame frame = new JFrame();
    JTabbedPane tabbedPane = new JTabbedPane();
    //PersonUITest pUI = new PersonUITest();
    //BoligVindu bv = new BoligVindu();
    ContractUI cUI;
    FindContract fc;
    FindPerson fp;
    FindResidence fr;
    PersonUI pUI;
    ResidenceUI rUI;

    public Tabs() throws SQLException
    {

        cUI = new ContractUI();
        fp = new FindPerson();
        pUI = new PersonUI();
        fr = new FindResidence();
        rUI = new ResidenceUI();
        fc = new FindContract();


        tabbedPane.add("Finn Kontrakt", fc);
        frame.getContentPane();

        tabbedPane.add("Kontrakt", cUI);
        frame.getContentPane();

        tabbedPane.add("Finn Person", fp );
        frame.getContentPane();

        tabbedPane.add("Person", pUI);
        frame.getContentPane();

        tabbedPane.add("Finn Bolig", fr);
        frame.getContentPane();

        tabbedPane.add("Bolig", rUI);
        frame.getContentPane();

        frame.getContentPane().add(tabbedPane);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);


        Toolkit verktøykasse = Toolkit.getDefaultToolkit();
        Dimension skjermdimensjon = verktøykasse.getScreenSize();
        int bredde = skjermdimensjon.width;
        int høyde = skjermdimensjon.height;

        //Setter bredde og høyde. Lar plattformen velge plassering.
        frame.setSize(bredde / 4, høyde / 4);
        frame.setLocationByPlatform(true);

        // Konstruktør som skal legge inn program Icon, virker ikke.
        String bildefil = "Files/Bilder/PrIcon.svg";
        URL kilde = Tabs.class.getResource(bildefil);
        if (kilde != null)
        {
            ImageIcon bilde = new ImageIcon(kilde);
            Image ikon = bilde.getImage();
            frame.setIconImage(ikon);
        }}

        public static void main(String[] args) throws SQLException
        {
            new Tabs();
        }





    }



