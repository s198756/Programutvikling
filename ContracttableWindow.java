/**
 * Created by Dragon on 14.05.14.
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.sql.SQLException;

public class ContracttableWindow extends JPanel {
    Contracttable c;                      // Et objekt av boligtabellen

    Contract contract;                    // objekt av Contract-klassen: denne klassen inneholder en metode for å opprette
                                          // og fylle et Cachedrowset med databaseverdier på bakgrunn av en eksisterende
                                          // kontrakt i databasen. ved opprettelse blir den unike id-en sendt
                                          // med, og det er dette som oppretter objektet. Contract inneholder
                                          // oppdateringsmetoder, og også get-metoder for alle databaseverdier.

    JButton visAlle;                      // Knapp for å vise alle kontrakter i databasen
    JButton search;                       // Knapp for å søke etter kontrakter i tabellen
    JTextArea output;                     // utskriftsområde for muselytter
    JPanel buttonpanel;                   // Jpanel for øverste del av det ytre JPanel
    JPanel outputpanel;                   // outputpanel for utskriftsområdet
    JPanel tablepanel;                    // JPanel for tabellen i bunnen

    //Nedenfor er opprettelsen av panelet, og alt som hører til
    public ContracttableWindow(){
        MainPanel main = new MainPanel();
        main.setPreferredSize(new Dimension(1280, 670));
        add(main);
    }

    // Hovedpanel som samler alle underpaneler.
    private class MainPanel extends JPanel implements ActionListener {
        public MainPanel() {
            setPreferredSize(new Dimension(1100, 600));
            setVisible(true);

            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(1280, 720));
            setVisible(true);
            add(new JScrollPane(output), BorderLayout.SOUTH);
            c = new Contracttable();
            contract = new Contract();
            visAlle = new JButton("Vis alle kontrakter");
            search = new JButton("Søk");
            output = new JTextArea(20, 20);
            visAlle.addActionListener(this);
            search.addActionListener(this);
            buttonpanel = new JPanel();
            tablepanel = new JPanel();
            tablepanel.setLayout(new BorderLayout());
            tablepanel.setPreferredSize(new Dimension(300, 300));
            outputpanel = new JPanel();

            add(buttonpanel, BorderLayout.PAGE_START);              // de forskjellige panelene legges til
            // forskjellige deler av
            add(outputpanel, BorderLayout.LINE_END);                // det ytre panelet og får faste plasser
            // med borderlayout
            add(tablepanel, BorderLayout.CENTER);
            buttonpanel.add(visAlle);
            buttonpanel.add(search);
            outputpanel.add(output, BorderLayout.LINE_END);
        }

        //Lytteklasse for panelet
        public void actionPerformed(ActionEvent e){
            if(e.getSource() == visAlle){
                DefaultTableModel alt = c.showEverythingInContracttable();
                showTable(alt);
            }
            else if(e.getSource() == search){
                String choices[] = {"Bolig-ID"};
                int nr = JOptionPane.showOptionDialog(null, "Hva vil du søke med?", "Boligsøking",
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);

                if(nr == 0){
                    String adresse = JOptionPane.showInputDialog(null, "Skriv inn bolig-ID: ");
                    int id = Integer.parseInt(adresse);
                    DefaultTableModel adr = c.findContractByDwellingUnit(id);
                    showTable(adr);
                }
            }
        }
    }

    public void showTable(DefaultTableModel def){
        /*Når man trykker på en av knappene opprettes det i hvert tilfelle et spesifikt DefaultTableModel som sendes
        til showTable(), som oppretter et JTable med det tilsendte DefaultTableModel som modell. Deretter legges
        tabellen inn i tabellpanelet*/

        tablepanel.removeAll();     //gjør at tabellene ikke legger seg oppå hverandre ved gjentatte opprettelser

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
        table.setModel(def);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        table.addMouseListener(new MouseAdapter() {                             //muselytter for tabellen
            public void mouseClicked(java.awt.event.MouseEvent e){
                if(e.getClickCount() == 1){
                    JTable target = (JTable)e.getSource();
                    int row = target.getSelectedRow();
                    int column = 0;

                    Object identity = target.getValueAt(row, column);       //objektet får kontraktens id som verdi
                    int id = (Integer)identity;                             //castes til integer

                    try {
                        contract.findContractWithID(id);                //finner bolig
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                    //tekststreng med info
                    String info = "Kontrakt-ID: " + contract.getContractID() + "\nBolig-ID: " +
                            contract.getDwellingUnitID() +
                            "\nLeietaker: " + contract.getRenter() + "\nMegler: " + contract.getBroker() +
                            "\nOpprettet: " + contract.getCreatedDate() +
                            "\nGyldig til: " + contract.getExpirationDate();


                    output.setText(info);               //skriver ut info

                }
            }
        });

        //legger inn jscrollpane
        JScrollPane scroll = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                                            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setViewportView(table);
        tablepanel.add(scroll, BorderLayout.CENTER);
        tablepanel.revalidate();                    //revaliderer tabellen
        tablepanel.repaint();                       //gjør tabellen mindre klikk-sensitiv
    }
}