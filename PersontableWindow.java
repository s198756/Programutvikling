/**
 * Created by Carl Reinsnes.
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.sql.SQLException;

public class PersontableWindow extends JFrame implements ActionListener{
    Persontable p;                      //Et objekt av persontabellen

    Person person;               //objekt av Person-klassen: denne klassen inneholder en metode for å opprette og
                                // fylle et Cachedrowset med databaseverdier på bakgrunn av en eksisterende person i
                                // databasen. ved opprettelse blir det unike personnummeret sendt med, og det er dette
                                // som oppretter objektet. Person inneholder oppdateringsmetoder, og også
                                // get-metoder for alle databaseverdier.

    JButton visAlle;                    //Knapp for å vise alle personer i databasen
    JButton search;                     //Knapp for å søke etter boliger i tabellen
    JTextArea output;                   //utskriftsområde for muselytteren
    JPanel buttonpanel;                 //Jpanel for øverste del av det ytre JPanel
    JPanel outputpanel;                 //outputpanel for utskriftsområdet
    JPanel tablepanel;                  //JPanel for tabellen i bunnen

    //Nedenfor er opprettelsen av panelet, og alt som hører til
    public PersontableWindow(){
        JFrame personvindu = new JFrame();
        personvindu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        personvindu.setResizable(true);
        personvindu.setLayout(new BorderLayout());
        personvindu.setPreferredSize(new Dimension(1280, 720));
        personvindu.pack();
        personvindu.setVisible(true);

        personvindu.add(new JScrollPane(output), BorderLayout.SOUTH);
        p = new Persontable();
        person = new Person();
        visAlle = new JButton("Vis alle personer");
        search = new JButton("Søk");
        output = new JTextArea(20, 20);
        visAlle.addActionListener(this);
        search.addActionListener(this);
        buttonpanel = new JPanel();
        tablepanel = new JPanel();
        tablepanel.setLayout(new BorderLayout());
        tablepanel.setPreferredSize(new Dimension(300, 300));
        outputpanel = new JPanel();

        personvindu.add(buttonpanel, BorderLayout.PAGE_START);              //de forskjellige panelene legges til
                                                                            // forskjellige deler av
        personvindu.add(outputpanel, BorderLayout.LINE_END);                //det ytre panelet og får faste plasser
                                                                            //med borderlayout
        personvindu.add(tablepanel, BorderLayout.CENTER);
        buttonpanel.add(visAlle);
        buttonpanel.add(search);
        outputpanel.add(output, BorderLayout.LINE_END);

    }

    //Lytteklasse for panelet
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == visAlle){
            DefaultTableModel alt = p.showeverythingInPersontable();
            showTable(alt);
        }
        else if(e.getSource() == search){
            String choices[] = {"Etternavn"}; //spør om å søke etter
            int nr = JOptionPane.showOptionDialog(null, "Hva vil du søke med?", "Personsøk",
                                   JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);

            if(nr == 0){
                String person = JOptionPane.showInputDialog(null, "Skriv inn etternavn: ");
                DefaultTableModel adr = p.findPersonBySurname(person);
                showTable(adr);
            }
        }
    }

    public void showTable(DefaultTableModel def){
        /*Når man trykker på en av knappene, opprettes det for hvert tilfelle DefaultTableModel-er som blir sendt til
        * visTabell(), som oppretter et JTable med det tilsendte DefaultTableModel-et*/

         tablepanel.removeAll();                //gjør at tabellene ikke blir lagt oppå hverandre ved gjentatte
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
        table.setModel(def);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        table.addMouseListener(new MouseAdapter() {                             //muselytter for tabellen
            public void mouseClicked(java.awt.event.MouseEvent e){
                if(e.getClickCount() == 1){
                    JTable target = (JTable)e.getSource();
                    int row = target.getSelectedRow();
                    int column = 0;

                    Object identity = target.getValueAt(row, column);    //objektet får personnummer-verdi
                    String pNo = (String)identity;                      //castes til string

                    try {
                        person.findPersonWithPersonNo(pNo);                 //finner personen
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                    //Info-streng
                    String info = "Personnummer: " + person.getPersonNo() + "\nNavn: " + person.getFirstName() +
                            " " + person.getSurName() +
                            "\nTelefonnummer: " + person.getTelephoneNo() + "\nEmail: " + person.getEmail();


                    output.setText(info);       //infostrengen skrives ut

                }
            }
        });

        //oppretter scrollpane
        JScrollPane scroll = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setViewportView(table);
        tablepanel.add(scroll, BorderLayout.CENTER);
        tablepanel.revalidate();                        //revaliderer tabellen hele tiden
        tablepanel.repaint();                           //gjør tabellen mindre sensitiv ved klikk
    }

    public static void main(String[]args){
        PersontableWindow vindu = new PersontableWindow();
    }

}
