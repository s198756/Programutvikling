package GUI.Files;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.event.*;


/**
 * Created by ThomN on 05.05.2014.
 */

public class Tabs
{
    JFrame frame = new JFrame();
    JTabbedPane tabbedPane = new JTabbedPane();
    LeieTakerPanel lp = new LeieTakerPanel();
    BoligPanel bp = new BoligPanel();
    KontraktPanel kp = new KontraktPanel();

    public Tabs()
    {
        tabbedPane.add("Leietaker", lp);
        tabbedPane.add("Bolig", bp);
        tabbedPane.add("Kontrakt", kp);
        frame.getContentPane().add(tabbedPane);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setShape(1200, 720);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Tabs();
            }
        });
    }
}


/*public class Tabs extends JPanel
{
    public Tabs()
    {
        super(new GridLayout(1, 1));
        ImageIcon icon = new ImageIcon(getClass().getResource("bilder/middle.gif"));
        JTabbedPane tab = new JTabbedPane();

        JComponent tab1 = getLeietaker();
        tab.addTab("Leietaker", icon, tab1, "Leietaker vindu");

        JComponent tab2 = getBolig();
        tab.addTab("Bolig", icon, tab2, "Bolig vindu");

        JComponent tab3 = getKontrakt();
        tab.addTab("Kontrakt", icon, tab3, "Kontrakt vindu");

        tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        add(tab);*/

