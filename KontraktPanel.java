package GUI.Files;
import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.Date;
import java.sql.SQLException;

/**
 * Created by Thomas Newman, s198753  on 05.05.2014.
 */
public class KontraktPanel extends JPanel implements ActionListener
{
    KontraktTabell t;            // KontraktTabell objekt
    DwellingUnit dwelling;       // DwellingUnit objekt
    Contract contract;

    JButton opprett;
    JButton visAlleK;
    JButton search;
    JButton valider;

    JTextField start;
    JTextField stop;
    JTextField maanedsleie;

    JTextArea leietaker ;       //Utskrifts områder
    JTextArea bolig;
    JTextArea kontraktutskrift;

    JPanel headline;        // JPaneler for GUI
    JPanel nyLeieForhold;
    JPanel info;
    JPanel tabell;


    public KontraktPanel()
    {
        setLayout(new BorderLayout( 5, 5 ));
        add(new JScrollPane(kontraktutskrift), BorderLayout.EAST);
        add(new JScrollPane(bolig), BorderLayout.CENTER);
        add(new JScrollPane(leietaker), BorderLayout.CENTER);
        t = new KontraktTabell();
        opprett = new JButton("Opprett Kontrakt");
        visAlleK = new JButton("Vis alle kontrakter");
        search = new JButton("Søk");
        valider = new JButton("Valider");
        start = new JTextField(5);
        stop = new JTextField(5);
        maanedsleie = new JTextField(5);
        leietaker = new JTextArea(320, 50);
        bolig = new JTextArea(320, 50);
        kontraktutskrift = new JTextArea(320, 100);


        visAlleK.addActionListener(this);
        search.addActionListener(this);
        valider.addActionListener(this);
        start.addActionListener(this);
        stop.addActionListener(this);
        maanedsleie.addActionListener(this);

        headline = new JPanel();
        nyLeieForhold = new JPanel(new GridLayout(0,1));
        nyLeieForhold.setPreferredSize(new Dimension(500,300));
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






    /*public static void main(String args[])
    {
        KontraktPanel kp = new KontraktPanel();
        kp.setSize(1200, 800);
        kp.setDefaultCloseOperation(EXIT_ON_CLOSE);
        kp.setResizable(true);
        kp.pack();
        kp.setVisible(true);

    }*/

        // Lytteklasse for panelet
        public void actionPerformed(ActionEvent e )
        {
            if( e.getSource() == opprett )
            {
                //settInnNyKontrakt();
            }
            if (e.getSource() == search )
            {
                String choices[] = {"Kontrakt-ID", "Bolig-ID"};
                int nr = JOptionPane.showOptionDialog(null, "Hva vil du søke med?", "Kontraktsøking",
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, choices[1]);

                if(nr == 0)
                {
                    try{
                        finnKontraktVedKontraktID();
                    }catch (SQLException e1){
                        e1.printStackTrace();
                    }
                }
                else if(nr == 1){
                    try {
                        finnKontraktVedBoligID();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            if(e.getSource() == visAlleK){
                visAlleKontrakter();
            }

        }

        public void visAlleKontrakter()
        {
            DefaultTableModel def = t.visAltIKontrakttabellen();
            JTable table = new JTable(def);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);




        }


        public void finnKontraktVedBoligID() throws SQLException
        {

            String BID = JOptionPane.showInputDialog(null, "Skriv inn IDnr: ");
            int bid = Integer.parseInt(BID);

            dwelling = new DwellingUnit();
            dwelling.findDwellingUnitWithID(bid);

            String info = "Kontrakt-ID: " + contract.getContractID() + "Bolig-ID: " + dwelling.getDwellingUnitID() +
                    "\nEier: " + dwelling.getPropertyOwner() + "\nBoligtype: " +
                    dwelling.getDwellingType() +"\nStørrelse: " + dwelling.getSize() +
                    " kvadratmeter" + "\nAdresse: " + dwelling.getStreet() + " " + dwelling.getStreetNo() +
                    "\nPostnummer: " + dwelling.getZipCode() + "\nMånedlig leie: " + dwelling.getMonthlyPrice() +
                    "\nDepositum: " + dwelling.getDepositumPrice();




            bolig.setText(info);

        }

        public void finnKontraktVedKontraktID() throws SQLException
        {
            String KID = JOptionPane.showInputDialog(null, "Skriv inn IDnr: ");
            int kid = Integer.parseInt(KID);

            contract = new Contract();
            contract.findContractWithID(kid);
            dwelling = new DwellingUnit();
            dwelling.findDwellingUnitWithID(kid);
            //person = new Person();


            String info = "Kontrakt-ID: " + contract.getContractID() + "\nBolig-ID" + dwelling.getDwellingUnitID() +"\nLeietaker: " + contract.getRenter() +
                    "\nMegler: " + contract.getBroker() + "\nValider? " + contract.getIsValid() +
                    "\nBetalt Depositum: " + contract.getHasPaidDepositum() + "\nSignert av Leietaker: " +
                    contract.getIsSignedByRenter()+ "\nSignert av Megler: " + contract.getIsSignedByBroker() +
                    "\nStart dato: " + contract.getInEffectDate() + "\nStop dato: " + contract.getExpirationDate();

            kontraktutskrift.setText(info);

        }

    /*Contract contract = new Contract();
    contract.moveToInsertRow();
    contract.updateAuto("contract_id");
    contract.updateIntValue("dwelling_unit_id", 3);
    contract.updateStringValue("renter", "23453211232");
    contract.updateStringValue("broker", "04120492213" );
    contract.updateBooleanValue("valid", false);
    contract.updateBooleanValue("paid_depositum", false);
    contract.updateBooleanValue("signed_by_renter", false);
    contract.updateBooleanValue("signed_by_broker", false);
    contract.updateStringValue("in_effect_date", "2014-03-03");
    contract.updateStringValue("expiration_date", "2015-03-03");
    contract.insertRow(); contract.moveToCurrentRow();
    contract.acceptChanges();*/


    }

