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

public class Kontraktvindu extends JFrame implements ActionListener{
    Kontrakttabell k;                      //Et objekt av boligtabellen

    Contract contract;               //objekt av DwellingUnit-klassen: denne klassen inneholder en metode for å opprette og fylle et Cachedrowset
    //med databaseverdier på bakgrunn av en eksisterende bolig i databasen. ved opprettelse blir den unike id-en sendt
    //med, og det er dette som oppretter objektet. DwellingUnit inneholder oppdateringsmetoder, men man MÅ kalle startQuery() for å
    //kunne unngå nullpointerexception. Inneholder også get-metoder for alle databaseverdier.

    JButton visAlle;                    //Knapp for å vise alle boliger i databasen
    JButton search;                     //Knapp for å søke etter en eller flere boliger i tabellen
    JTextArea output;                   //utskriftsområde for søk etter id
    JPanel buttonpanel;                 //Jpanel for øverste del av det ytre JPanel
    JPanel outputpanel;                 //outputpanel for utskriftsområdet
    JPanel tablepanel;                  //JPanel for tabellen i bunnen

    //Nedenfor er opprettelsen av panelet, og alt som hører til
    public Kontraktvindu(){
        JFrame kontraktvindu = new JFrame();
        kontraktvindu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        kontraktvindu.setResizable(true);
        kontraktvindu.setLayout(new BorderLayout());
        kontraktvindu.setPreferredSize(new Dimension(1280, 720));
        kontraktvindu.pack();
        kontraktvindu.setVisible(true);

        kontraktvindu.add(new JScrollPane(output), BorderLayout.SOUTH);
        k = new Kontrakttabell();
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

        kontraktvindu.add(buttonpanel, BorderLayout.PAGE_START);              //de forskjellige panelene legges til forskjellige deler av
        kontraktvindu.add(outputpanel, BorderLayout.LINE_END);                //det ytre panelet og får faste plasser ed borderlayout
        kontraktvindu.add(tablepanel, BorderLayout.CENTER);
        buttonpanel.add(visAlle);
        buttonpanel.add(search);
        outputpanel.add(output, BorderLayout.LINE_END);

    }

    //Lytteklasse for panelet
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == visAlle){
            DefaultTableModel alt = k.visAltIKontrakttabellen();
            visTabell(alt);
        }
        else if(e.getSource() == search){
            String choices[] = {"Bolig-ID"};
            int nr = JOptionPane.showOptionDialog(null, "Hva vil du søke med?", "Boligsøking", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);

            if(nr == 0){
                String adresse = JOptionPane.showInputDialog(null, "Skriv inn bolig-ID: ");
                int id = Integer.parseInt(adresse);
                DefaultTableModel adr = k.finnKontraktVedAASkriveInnBoligId(id);
                visTabell(adr);
            }
        }
    }

    public void visTabell(DefaultTableModel def){
        /*Når man trykker på knappen for denne metoden, opprettes en JTable i bunnpanelet der det tilsendte objektet fra Boligtabellobjektet blir DefaultTableModel til
        * JTable. Den har metoden revalidate(), som hele tiden oppdaterer den, og gjør at tabellene ikke blir lagt oppå hverandre.*/
        tablepanel.removeAll();

        final JTable table = new JTable(){
            public Class getColumnClass(int column){                    //Denne metoden returnerer klassene til
                for(int row = 0; row < getRowCount(); row++){           //de forskjellige tabellene.
                    Object o = getValueAt(row, column);                 //Dermed får man skrevne datostempler, checkbox for booleanverdier osv

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

                    Object identity = target.getValueAt(row, column);
                    int id = (Integer)identity;

                    try {
                        contract.findContractWithID(id);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }


                    String info = "Kontrakt-ID: " + contract.getContractID() + "\nBolig-ID: " + contract.getDwellingUnitID() +
                            "\nLeietaker: " + contract.getRenter() + "\nMegler: " + contract.getBroker() +
                            "\nOpprettet: " + contract.getCreatedDate() +
                            "\nGyldig til: " + contract.getExpirationDate();


                    output.setText(info);

                }
            }
        });

        JScrollPane scroll = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setViewportView(table);
        tablepanel.add(scroll, BorderLayout.CENTER);
        tablepanel.revalidate();
        tablepanel.repaint();
    }

    public static void main(String[]args){
        Kontraktvindu vindu = new Kontraktvindu();
    }
}

