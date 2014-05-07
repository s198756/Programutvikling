package Superiore;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Programmert av Carl P Reinsnes, s198756
 */
public class Boligvindu extends JPanel implements ActionListener{
    Boligtabell t;                      //Et objekt av boligtabellen
    DwellingUnit dwelling;              //Objekt av DwellingUnit-klassen
    JButton opprett;                    //Knapp for å opprette ny bolig
    JButton visAlle;                    //Knapp for å vise alle boliger i databasen
    JButton search;                     //Knapp for å søke etter en eller flere boliger i tabellen
    JTextField eierpNo;                 //Tekstfelt hvor man  skal skrive inn personnummeret til eieren. Denne må allerede eksistere!
    JTextField btype;                   //Tekstfelt hvor man skriver inn boligtype. Hvis man skriver inn noe annet enn "bofellesskap", "leilighet", "hybel",
                                        //"rekkehus" eller "villa", blir det ikke godkjent som verdi.
    JTextField kvadratm;                //Antall kvadratmeter; størrelsen på boligen
    JTextField gate;                    //gatenavn
    JTextField gateno;                  //gatenummer
    JTextField zipcode;                 //postnummer: dersom det ikke er et reelt, norsk postnummer, blir ikke verdien godkjent
    JTextField month;                   //månedlig leie-tekstfelt
    JTextField depositum;               //depositum ved utleie
    JTextArea output;                   //utskriftsområde for søk etter id
    JPanel buttonpanel;                 //Jpanel for øverste del av det ytre JPanel
    JPanel outputpanel;                 //outputpanel for utskriftsområdet
    JPanel textpanel;                   //JPanel for tekstfeltene
    JPanel tablepanel;                  //JPanel for tabellen i bunnen

    //Nedenfor er opprettelsen av panelet, og alt som hører til
    public Boligvindu(){
        setLayout(new BorderLayout());
        add(new JScrollPane(output), BorderLayout.SOUTH);
        t = new Boligtabell();
        opprett = new JButton("Opprett ny bolig");
        visAlle = new JButton("Vis alle boliger");
        search = new JButton("Søk");
        eierpNo = new JTextField(10);
        btype = new JTextField(10);
        kvadratm = new JTextField(3);
        gate = new JTextField(15);
        gateno = new JTextField(2);
        zipcode = new JTextField(5);
        month = new JTextField(5);
        depositum = new JTextField(5);
        output = new JTextArea(10, 20);
        opprett.addActionListener(this);
        visAlle.addActionListener(this);
        search.addActionListener(this);
        buttonpanel = new JPanel();
        tablepanel = new JPanel();
        tablepanel.setLayout(new BorderLayout());
        tablepanel.setPreferredSize(new Dimension(300, 300));
        textpanel = new JPanel(new GridLayout(0, 1));
        outputpanel = new JPanel();

        add(buttonpanel, BorderLayout.PAGE_START);
        add(outputpanel, BorderLayout.LINE_END);
        add(textpanel, BorderLayout.CENTER);
        add(tablepanel, BorderLayout.PAGE_END);
        buttonpanel.add(opprett);
        textpanel.add(new JLabel("Eier:   "));
        textpanel.add(eierpNo);
        textpanel.add(new JLabel(("Boligtype:   ")));
        textpanel.add(btype, BorderLayout.CENTER);
        textpanel.add(new JLabel("Kvadratmeter:   "));
        textpanel.add(kvadratm);
        textpanel.add(new JLabel("Gatenavn:   "));
        textpanel.add(gate);
        textpanel.add(new JLabel("Gatenummer:   "));
        textpanel.add(gateno);
        textpanel.add(new JLabel("Postnummer:  "));
        textpanel.add(zipcode);
        textpanel.add(new JLabel("Månedlig leie:  "));
        textpanel.add(month);
        textpanel.add(new JLabel("Depositum:  "));
        textpanel.add(depositum);
        tablepanel.add(search, BorderLayout.PAGE_END);
        outputpanel.add(output, BorderLayout.LINE_END);
        tablepanel.add(visAlle, BorderLayout.PAGE_START);
    }

    //Lytteklasse for panelet
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == opprett){
            settInnNyBolig();
        }
        else if(e.getSource() == visAlle){
            visAlleBoliger();
        }
        else if(e.getSource() == search){
            String choices[] = {"Id", "Adresse"};
            int nr = JOptionPane.showOptionDialog(null, "Hva vil du søke med?", "Boligsøking", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, choices[1]);

            if(nr == 0){
                try {
                    finnBoligVedId();               //I denne metoden får man spørsmålet om man vil med adresse eller id. Trykker man adresse, får man tilhørende
                } catch (SQLException e1) {         //metode finnBoligVedAdresse(), og i tilfellet id, finnBoligVedId. Metodene er beskrevet nedenfor.
                    e1.printStackTrace();
                }
            }
            else if(nr == 1){
                finnBoligVedAdresse();
            }
        }
    }

    public void settInnNyBolig(){
        /*Metode for å sette inn ny bolig. Metoden leser av alle verdiene i tekstfeltet, og oppretter boligen KUN dersom alle tkstfeltene har verdier forskjellige fra 0
         * eller null. Deretter kalles boligtabellobjektets metode for å sette inn en ny bolig i tabellen */

        String eier = eierpNo.getText();
        String bolig = btype.getText();
        int kvadrat = Integer.parseInt(kvadratm.getText());
        String gatenavn = gate.getText();
        int gatenr = Integer.parseInt(gateno.getText());
        int postnr = Integer.parseInt(zipcode.getText());
        int maaned = Integer.parseInt(month.getText());
        int dep = Integer.parseInt(depositum.getText());

        if(eier != null && bolig != null && kvadrat != 0 && gatenavn != null && gatenr != 0 && postnr != 0 && maaned != 0 && dep != 0){
        t.settInnNyBoligIBoliglisten(eier, bolig, kvadrat, gatenavn, gatenr, postnr, maaned, dep);
        }

        eierpNo.setText("");
        btype.setText("");
        kvadratm.setText("");
        gate.setText("");
        gateno.setText("");
        zipcode.setText("");
        month.setText("");
        depositum.setText("");
    }

    public void visAlleBoliger(){
        /*Når man trykker på knappen for denne metoden, opprettes en JTable i bunnpanelet der det tilsendte objektet fra Boligtabellobjektet blir DefaultTableModel til
        * JTable. Den har metoden revalidate(), som hele tiden oppdaterer den, og gjør at tabellene ikke blir lagt oppå hverandre.*/

        DefaultTableModel def = t.visAltIBoligtabellen();
        final JTable table = new JTable(def);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scroll = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setViewportView(table);
        tablepanel.add(scroll, BorderLayout.CENTER);
        tablepanel.revalidate();
    }

    public void finnBoligVedAdresse(){
        /*Denne metoden bruker en inputdialog for å hente en adresseverdi som legges inn i defaulttablemodellen som metoden fra boligtabellklassen
         * sender. Denne opprettes i en ny jtable som legges i tablepanelet. blir oppdatert hele tiden av revalidate(). */

        String adresse = JOptionPane.showInputDialog(null, "Skriv inn gatenavn: ");
        DefaultTableModel adr = t.finnBoligVedAASkriveInnAdresse(adresse);
        JTable table = new JTable(adr);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scroll = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setViewportView(table);
        tablepanel.add(scroll, BorderLayout.CENTER);
        tablepanel.revalidate();
    }

    public void finnBoligVedId() throws SQLException {
        String identity = JOptionPane.showInputDialog(null, "Skriv inn bolig-id: ");
        int id = Integer.parseInt(identity);

        dwelling = new DwellingUnit(id);
        String info = "Bolig-ID: " + dwelling.getDwellingUnitID() + "\nEier: " + dwelling.getPropertyOwner() +
                        "\nBoligtype: " + dwelling.getDwellingType() + "\nStørrelse: " + dwelling.getSize() + " kvadratmeter" +
                        "\nAdresse: " + dwelling.getStreet() + " " + dwelling.getStreetNo() + "\nPostnummer: " + dwelling.getZipCode() +
                        "\nMånedlig leie: " + dwelling.getMonthlyPrice() + "\nDepositum: " + dwelling.getDepositumPrice();
        output.setText(info);
    }

    /*public static void main(String[]args){
        Boligvindu vindu = new Boligvindu();
        vindu.setDefaultCloseOperation(EXIT_ON_CLOSE);

        vindu.pack();
        vindu.setVisible(true);*/
    }
//}
