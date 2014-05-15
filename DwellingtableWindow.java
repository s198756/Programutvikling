package GUI.Files;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.sql.SQLException;

/**
 * Programmert av Carl P Reinsnes, s198756
 */
public class DwellingtableWindow extends JFrame implements ActionListener{
    Dwellingtable t;                      //Et objekt av boligtabellen

    DwellingUnit dwelling;               //objekt av DwellingUnit-klassen: denne klassen inneholder en metode for å
                                        // opprette og fylle et Cachedrowset med databaseverdier på bakgrunn av en
                                        // eksisterende bolig i databasen. ved opprettelse blir den unike id-en sendt
                                        //med, og det er dette som oppretter objektet. DwellingUnit inneholder
                                        // oppdateringsmetoder, men man MÅ kalle startQuery() for å
                                        //kunne unngå nullpointerexception. Inneholder også get-metoder for
                                        // alle databaseverdier.

    JButton lukk;                    //Knapp for å lukke vinduet
    JButton visAlle;                    //Knapp for å vise alle boliger i databasen
    JButton search;                     //Knapp for å søke etter en eller flere boliger i tabellen
    JTextArea output;                   //utskriftsområde for søk etter id
    JPanel buttonpanel;                 //Jpanel for øverste del av det ytre JPanel
    JPanel outputpanel;                 //outputpanel for utskriftsområdet
    JPanel tablepanel;                  //JPanel for tabellen i bunnen

    //Nedenfor er opprettelsen av panelet, og alt som hører til
    public DwellingtableWindow(){
        JFrame boligvindu = new JFrame();
        boligvindu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boligvindu.setResizable(true);
        boligvindu.setLayout(new BorderLayout());
        boligvindu.setPreferredSize(new Dimension(1280, 720));
        boligvindu.pack();
        boligvindu.setVisible(true);

        boligvindu.add(new JScrollPane(output), BorderLayout.SOUTH);
        t = new Dwellingtable();
        dwelling = new DwellingUnit();
        lukk = new JButton("Lukk vindu");
        visAlle = new JButton("Vis alle boliger");
        search = new JButton("Søk");
        output = new JTextArea(20, 20);
        lukk.addActionListener(this);
        visAlle.addActionListener(this);
        search.addActionListener(this);
        buttonpanel = new JPanel();
        tablepanel = new JPanel();
        tablepanel.setLayout(new BorderLayout());
        outputpanel = new JPanel();

        boligvindu.add(buttonpanel, BorderLayout.PAGE_START);              //de forskjellige panelene legges til
                                                                            // forskjellige deler av
        boligvindu.add(outputpanel, BorderLayout.LINE_END);                //det ytre panelet og får faste plasser
                                                                            // med borderlayout
        boligvindu.add(tablepanel, BorderLayout.CENTER);
        buttonpanel.add(visAlle);
        buttonpanel.add(search);
        outputpanel.add(output, BorderLayout.LINE_END);

    }

    //Lytteklasse for panelet
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == visAlle){
            DefaultTableModel alt = t.showEverythingInDwellingtable();
            showTable(alt);
        }
        else if(e.getSource() == search){
            String choices[] = {"Adresse"};             //Spør hva man vil søke med: kan lett utvides
            int nr = JOptionPane.showOptionDialog(null, "Hva vil du søke med?", "Boligsøking",
                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);

            if(nr == 0){
                String adresse = JOptionPane.showInputDialog(null, "Skriv inn gatenavn: "); //skriv inn adresse
                DefaultTableModel adr = t.findDwellingUnitByAddress(adresse);          //oppretter
                                                                                            //DefaultTableModel
                showTable(adr);                                 //sendes til tabell-panelet
            }
        }
    }

    public void showTable(DefaultTableModel def){
        /*Når man trykker på en av knappene, opprettes det i hvert tilfelle et spesifikt DefaultTableModel som
        * blir innkommende parameter. Metoden oppretter tabellen med innkommende DefaultTableModel og
        * legger den inn i tabellpanelet*/

         tablepanel.removeAll();                        //gjør at tabellene ikke legger seg oppå hverandre ved gjentatte
                                                        //opprettelser

        final JTable table = new JTable(){
            public Class getColumnClass(int column){                    //Denne metoden returnerer klassene til
                for(int row = 0; row < getRowCount(); row++){           //de forskjellige tabellene.
                    Object o = getValueAt(row, column);                 //Dermed får man skrevne datostempler,
                                                                        // checkbox for booleanverdier osv

                    if(o != null) {
                        return o.getClass();
                    }
                }
                return Object.class;
                }
            };
        table.setModel(def);                      //setter medsendt DefaultTableModel som model for JTable

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        table.addMouseListener(new MouseAdapter() {                             //muselytter for tabellen
            public void mouseClicked(java.awt.event.MouseEvent e){
                if(e.getClickCount() == 1){
                    JTable target = (JTable)e.getSource();
                    int row = target.getSelectedRow();
                    int column = 0;

                        Object identity = target.getValueAt(row, column);       //finner den unike bolig-id
                        int id = (Integer)identity;                             //caster til integer

                    try {
                        dwelling.findDwellingUnitWithID(id);                    //finner bolig
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                    //oppretter tekst-streng med info
                    String info = "Bolig-ID: " + dwelling.getDwellingUnitID() + "\nEier: " +
                                    dwelling.getPropertyOwner() + "\nBoligtype: " + dwelling.getDwellingType() +
                                "\nStørrelse: " + dwelling.getSize() + " kvadratmeter" +
                                "\nAdresse: " + dwelling.getStreet() + " " + dwelling.getStreetNo() +
                                "\nMånedlig leie: " + dwelling.getMonthlyPrice() + "\nDepositum: " +
                                dwelling.getDepositumPrice() + "\nPostnummer: " + dwelling.getZipCode() +
                                "\nPoststed: " + dwelling.getArea() + "\nKommune: " + dwelling.getTownship() +
                                "\nFylke: " + dwelling.getCounty();

                    output.setText(info);       //sender ut ved klikk på en hvilken som helst kolonne

                }
            }
        });

        //oppretter scrollpane
        JScrollPane scroll = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setViewportView(table);
        tablepanel.add(scroll, BorderLayout.CENTER);
        tablepanel.revalidate();                        //revaliderer og fornyer jtable hele tiden
        tablepanel.repaint();                           //database-verdier forblir uendret ved
                                                        //enkeltklikk
    }

    public static void main(String[]args){
        DwellingtableWindow boligvindu = new DwellingtableWindow();

    }
}
