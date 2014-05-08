package GUI.Files;
import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.*;
import java.util.Date;
import java.sql.SQLException;

/**
 * Created by Thomas Newman, s198753  on 05.05.2014.
 */
public class KontraktPanel extends JFrame implements ActionListener
{
    KontraktTabell t;                          // KontraktTabell objekt
    JButton opprett;
    JButton visAlleK;
    JButton search;
    JButton valider;
    JTextField start;
    JTextField stop;
    JTextField maanedsleie;
    JTextArea leietaker ;
    JTextArea bolig;
    JTextArea kontraktutskrift;
    JPanel headline;        // JPanel objektene inni JFrame
    JPanel nyLeieForhold;
    JPanel info;
    JPanel tabell;


    public KontraktPanel()
    {
        setLayout(new BorderLayout( 5, 5 ));
        add(new JScrollPane(kontraktutskrift), BorderLayout.SOUTH);
        t = new KontraktTabell();
        opprett = new JButton("Opprett Kontrakt");
        visAlleK = new JButton("Vis alle kontrakter");
        search = new JButton("Søk");
        valider = new JButton("Valider");
        start = new JTextField(5);
        stop = new JTextField(5);
        maanedsleie = new JTextField(5);
        leietaker = new JTextArea(320, 40);
        bolig = new JTextArea(320, 40);
        kontraktutskrift = new JTextArea(320, 40);

        headline = new JPanel();
        nyLeieForhold = new JPanel(new GridLayout(0,1));
        info = new JPanel(new GridLayout(0,1));
        tabell = new JPanel();
        tabell.setLayout(new BorderLayout());
        tabell.setPreferredSize(new Dimension(300,300));

        add(headline, BorderLayout.PAGE_START);
        add(nyLeieForhold, BorderLayout.CENTER);
        add(info, BorderLayout.EAST);
        add(tabell, BorderLayout.PAGE_END);
        headline.add(new JLabel("Kontrakt"));

        nyLeieForhold.add(new JLabel("Nytt Leieforhold"));
        nyLeieForhold.add(new JLabel("Start: "));
        nyLeieForhold.add(start);
        nyLeieForhold.add(new JLabel("Stop: "));
        nyLeieForhold.add(stop);
        nyLeieForhold.add(new JLabel("Månedsleie: "));
        nyLeieForhold.add(maanedsleie);
        nyLeieForhold.add(search);
        nyLeieForhold.add(opprett);
        nyLeieForhold.add(valider);
        nyLeieForhold.add(new JLabel("Kontrakt Utskrift: "));
        nyLeieForhold.add(kontraktutskrift);

        info.add(new JLabel("Leietaker: "), BorderLayout.NORTH );
        info.add(leietaker);
        info.add(new JLabel("Bolig: "));
        info.add(bolig);

        tabell.add(visAlleK, BorderLayout.PAGE_START);




    }






    public static void main(String args[])
    {
        KontraktPanel kp = new KontraktPanel();
        kp.setSize(1200, 800);
        kp.setDefaultCloseOperation(EXIT_ON_CLOSE);
        kp.setResizable(true);
        kp.setVisible(true);

    }


        public void actionPerformed(ActionEvent e )
        {
            /*if( e.getSource() == opprett )
            {
                settInnNyKontrakt();
            }
            if (e.getSource() == search )
            {
                String ID == "ID";
                int nr = JOptionPane.showInputDialog(null, "")
            }*/

        }
    }

