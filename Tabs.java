package GUI.Files;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.event.*;
import java.net.URL;


/**
 * Created by ThomN on 05.05.2014.
 */

public class Tabs extends JFrame
{
    JFrame frame = new JFrame();
    JTabbedPane tabbedPane = new JTabbedPane();
    //LeieTakerVindu lp = new LeieTakerVindu();
    BoligVindu bp = new BoligVindu();
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

        String bildefil = "bilder/PrIcon.svg";
        URL kilde = Tabs.class.getResource(bildefil);
        if (kilde != null) {
            ImageIcon bilde = new ImageIcon(kilde);
            Image ikon = bilde.getImage();
            frame.setIconImage(ikon);
        }

       // tabbedPane.add("Leietaker", lp);
        //tabbedPane.add("Bolig", bp);
        tabbedPane.add("Kontrakt", kp);
        frame.getContentPane().add(tabbedPane);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 720);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);







    }
}



