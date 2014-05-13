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
    PersonUITest pUI = new PersonUITest();
    //BoligVindu bv = new BoligVindu();
    KontraktPanel kp = new KontraktPanel();


    public Tabs()
    {
        Toolkit verktøykasse = Toolkit.getDefaultToolkit();
        Dimension skjermdimensjon = verktøykasse.getScreenSize();
        int bredde = skjermdimensjon.width;
        int høyde = skjermdimensjon.height;

        //Setter bredde og høyde. Lar plattformen velge plassering.
        frame.setSize(bredde / 4, høyde / 4);
        frame.setLocationByPlatform(true);

        // Konstruktør som skal legge inn program Icon
        String bildefil = "Files/Bilder/PrIcon.svg";
        URL kilde = Tabs.class.getResource(bildefil);
        if (kilde != null) {
            ImageIcon bilde = new ImageIcon(kilde);
            Image ikon = bilde.getImage();
            frame.setIconImage(ikon);
        }










        //tabbedPane.add("Person", pUI);
        //tabbedPane.add("Bolig", bv);
        tabbedPane.add("Kontrakt", kp);
        frame.getContentPane().add(tabbedPane);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);


    }





}



