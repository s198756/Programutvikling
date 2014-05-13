/**
 * Created by Sebastian Ramsland on 11.05.2014.
 */

import com.sun.rowset.CachedRowSetImpl;
import javax.sql.rowset.FilteredRowSet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class PersonUI {
    JFrame personFrame;
    JLabel fullNameLabel;
    JTable contractsTable;
    JTable dwellingUnitsTable;
    JButton nextButton;
    JButton previousButton;
    JButton searchButton;
    JButton saveButton;
    JButton cancelButton;
    JButton closeButton;
    JButton createNewButton;
    JButton findDwellingUnitButton;
    JButton deleteButton;
    JLabel firstNameLabel;
    JLabel middleNameLabel;
    JLabel surNameLabel;
    JLabel personNoLabel;
    JLabel maritalStatusLabel;
    JLabel brokerLabel;
    JLabel telephoneLabel;
    JLabel emailLabel;
    JLabel streetLabel;
    JLabel streetNoLabel;
    JLabel zipCodeLabel;
    JLabel areaLabel;
    JLabel townshipLabel;
    JLabel countyLabel;
    JLabel annualRevenueLabel;
    JLabel passedCreditCheckLabel;
    JLabel smokerLabel;
    JLabel housepetsLabel;
    JLabel handicapAccommLabel;
    JLabel infoTextLabel;
    JTextField personNoField;
    JTextField firstNameField;
    JTextField middleNameField;
    JTextField surNameField;
    JTextField telephoneField;
    JTextField emailField;
    JTextField streetField;
    JTextField streetNoField;
    JTextField zipCodeField;
    JTextField areaField;
    JTextField townshipField;
    JTextField countyField;
    JTextField annualRevenueField;
    JComboBox maritalStatusComboBox;
    JCheckBox brokerCheckBox;
    JCheckBox passedCreditCheckBox;
    JCheckBox smokerCheckBox;
    JCheckBox housepetsCheckBox;
    JCheckBox handicapAccommCheckBox;
    Person person;
    boolean insertMode;

    public static void main(String[] args) throws SQLException {
        PersonUI personPanel = new PersonUI();
    }

    // Konstruktør som oppretter ny personliste over alle personer. Viser personen øverst på lista.
    public PersonUI() throws SQLException {

        // Oppretter ny personliste
        person = new Person();
        try {
            // Henter verdier
            person.refreshValues();
        } catch (SQLException e) {
        }


        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                } catch (InstantiationException ex) {
                } catch (IllegalAccessException ex) {
                } catch (UnsupportedLookAndFeelException ex) {
                }


                personFrame = new JFrame("Person");
                personFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                personFrame.setResizable(true);
                personFrame.setLayout(new BorderLayout());
                personFrame.add(new MainPanel());
                personFrame.setPreferredSize(new Dimension(1280, 720));
                personFrame.pack();

                // Midtstiller vinduet
                personFrame.setLocationRelativeTo(null);

                personFrame.setVisible(true);

                // Oppdaterer feltene
                updateFields();
            }
        });
    }

    // Konstruktør som tar imot et personnummer og viser denne personen først i vinduet.
    public PersonUI(String pNo) throws SQLException {

        // Oppretter ny personliste over alle personer og søker opp personen med det spesifiserte personnummeret
        person = new Person();

        try {
            person.findPersonWithPersonNo(pNo);
        } catch (SQLException e) {
        }

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                } catch (InstantiationException ex) {
                } catch (IllegalAccessException ex) {
                } catch (UnsupportedLookAndFeelException ex) {
                }


                personFrame = new JFrame("Person");
                personFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                personFrame.setResizable(false);
                personFrame.setLayout(new BorderLayout());
                personFrame.add(new MainPanel());
                personFrame.setPreferredSize(new Dimension(1280, 720));
                personFrame.pack();

                // Midtstiller vinduet
                personFrame.setLocationRelativeTo(null);

                personFrame.setVisible(true);

                // Oppdaterer feltene
                updateFields();
            }
        });
    }

    // Konstruktør som tar imot SQLInterface, FilteredRowSet og Radnummer.
    public PersonUI(SQLInterface dbInt, FilteredRowSet frs, int rowID) throws SQLException {

        // Henter den cachede personlista og hopper til det spesifiserte radnummeret
        person = new Person(dbInt, frs, rowID);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                } catch (InstantiationException ex) {
                } catch (IllegalAccessException ex) {
                } catch (UnsupportedLookAndFeelException ex) {
                }


                personFrame = new JFrame("Person");
                personFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                personFrame.setResizable(false);
                personFrame.setLayout(new BorderLayout());
                personFrame.add(new MainPanel());
                personFrame.setPreferredSize(new Dimension(1280, 720));
                personFrame.pack();

                // Midtstiller vinduet
                personFrame.setLocationRelativeTo(null);

                personFrame.setVisible(true);

                // Oppdaterer feltene
                updateFields();
            }
        });
    }

    // Konstruktør som tar imot SQLInterface, CachedRowSet og Radnummer.
    public PersonUI(SQLInterface dbInt, CachedRowSetImpl crs, int rowID) throws SQLException {

        // Henter den cachede personlista og hopper til det spesifiserte radnummeret
        person = new Person(dbInt, crs, rowID);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                } catch (InstantiationException ex) {
                } catch (IllegalAccessException ex) {
                } catch (UnsupportedLookAndFeelException ex) {
                }


                personFrame = new JFrame("Person");
                personFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                personFrame.setResizable(false);
                personFrame.setLayout(new BorderLayout());
                personFrame.add(new MainPanel());
                personFrame.setPreferredSize(new Dimension(1280, 720));
                personFrame.pack();

                // Midtstiller vinduet
                personFrame.setLocationRelativeTo(null);

                personFrame.setVisible(true);

                // Oppdaterer feltene
                updateFields();
            }
        });
    }

    // Hovedpanel som samler alle underpaneler.
    private class MainPanel extends JPanel {
        public MainPanel() {

            setPreferredSize(new Dimension(1100, 600));
            setVisible(true);

            // Layout: Hovedpanel for personUI
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.ipady = 0;
            c.weightx = 0.0;
            c.gridwidth = 2;
            c.gridx = 0;
            c.gridy = 0;
            add(new HeaderPanel(), c);

            c.ipady = 0;
            c.weightx = 0.0;
            c.gridwidth = 1;
            c.gridheight = 2;
            c.gridx = 0;
            c.gridy = 1;
            add(new DataFieldScrollPanel(), c);

            c.ipady = 0;
            c.weightx = 0.0;
            c.gridwidth = 1;
            c.gridx = 1;
            c.gridy = 2;
            add(new SidePanel(), c);

            c.ipady = 0;
            c.weightx = 0.0;
            c.gridwidth = 2;
            c.gridx = 0;
            c.gridy = 4;
            add(new StatusbarPanel(), c);
        }
    }

    // Overskriften på vinduet. Viser fullt navn på nåværende person.
    private class HeaderPanel extends JPanel {
        public HeaderPanel() {
            // Headerpanel som viser fullt navn
            setPreferredSize(new Dimension(1250, 50));

            // Innhold i HeaderPanel:
            fullNameLabel = new JLabel();
            fullNameLabel.setFont(new Font("Serif", Font.PLAIN, 30));

            // Layout
            setLayout(new FlowLayout(FlowLayout.LEFT));
            add(fullNameLabel);
            setVisible(true);
        }
    }

    // Hovedpanel for personens datafelt med scrollfunksjon.
    private class DataFieldScrollPanel extends JScrollPane {
        DataFieldScrollPanel() {
            // Scrollpanel container for personfelt
            setPreferredSize(new Dimension(950, 600));

            // Ramme
            setBorder(BorderFactory.createTitledBorder("Informasjon"));

            // Viewport
            setViewportView(new DataFieldPanel());

            // Layout: Datafeltpanel
            ScrollPaneLayout mainLayout = new ScrollPaneLayout();
            setLayout(mainLayout);

            setVisible(true);
        }
    }

    // Underpanel til DataFieldScrollPanel som samler panelene Personalia, Contact og Misc.
    private class DataFieldPanel extends JPanel {
        public DataFieldPanel() {
            setPreferredSize(new Dimension());
            // Hovedpanel for datafeltene
            add(new PersonaliaPanel());
            add(new ContactPanel());
            add(new MiscPanel());

            GridLayout dataFieldPanelLayout = new GridLayout(3, 1);

            setLayout(dataFieldPanelLayout);
            setPreferredSize(new Dimension(900, 700));


            setVisible(true);
        }
    }

    // Sidepanel som samler sammen panelene ContractScrollPanel, DwelingUnitScrollPanel og ControlPanel.
    private class SidePanel extends JPanel {
        public SidePanel() {
            setPreferredSize(new Dimension(300, 600));

            GridLayout sidepanelLayout = new GridLayout(3, 1);
            setLayout(sidepanelLayout);

            add(new ContractScrollPanel());
            add(new DwellingUnitScrollPanel());
            add(new ControlPanel());
        }
    }

    // Underpanel til DataFieldPanel. Viser personaliainformasjon.
    private class PersonaliaPanel extends JPanel {
        public PersonaliaPanel() {
            // Ramme
            setBorder(BorderFactory.createTitledBorder("Personalia"));
            setPreferredSize(new Dimension(900, 300));
            GridLayout personaliaPanelLayout = new GridLayout(4, 3);
            personaliaPanelLayout.setHgap(50);
            setLayout(personaliaPanelLayout);

            // Innhold i Personalia (personnummer, navn, sivilstatus, megler)
            firstNameLabel = new JLabel("Fornavn *");
            middleNameLabel = new JLabel("Mellomnavn");
            surNameLabel = new JLabel("Etternavn *");
            personNoLabel = new JLabel("Personnummer *");
            maritalStatusLabel = new JLabel("Sivilstatus ");
            brokerLabel = new JLabel("Megler");
            personNoField = new JTextField(11);
            firstNameField = new JTextField(45);
            middleNameField = new JTextField(45);
            surNameField = new JTextField(45);

            maritalStatusComboBox = new JComboBox();
            maritalStatusComboBox.setModel(new DefaultComboBoxModel(new String[]{"alene", "samboer", "gift", "skilt", "enke"}));
            brokerCheckBox = new JCheckBox();

            add(firstNameLabel);
            add(middleNameLabel);
            add(surNameLabel);

            add(firstNameField);
            add(middleNameField);
            add(surNameField);

            add(personNoLabel);
            add(maritalStatusLabel);
            add(brokerLabel);

            add(personNoField);
            add(maritalStatusComboBox);
            add(brokerCheckBox);
        }
    }

    // Underpanel til DataFieldPanel. Viser kontaktinformasjon.
    private class ContactPanel extends JPanel {
        public ContactPanel() {
            // Ramme
            setBorder(BorderFactory.createTitledBorder("Kontakt"));
            setPreferredSize(new Dimension(900, 500));
            GridLayout contactPanelLayout = new GridLayout(0, 3);
            contactPanelLayout.setHgap(50);
            setLayout(contactPanelLayout);

            // Innhold i Kontakt (telefon, email, adresse)
            telephoneLabel = new JLabel("Telefonnummer *");
            emailLabel = new JLabel("Epost *");
            streetLabel = new JLabel("Gateadresse *");
            streetNoLabel = new JLabel("Gatenummer *");
            zipCodeLabel = new JLabel("Postnummer *");
            areaLabel = new JLabel("Poststed");
            townshipLabel = new JLabel("Kommune");
            countyLabel = new JLabel("Fylke");
            telephoneField = new JTextField();
            emailField = new JTextField();
            streetField = new JTextField();
            streetNoField = new JTextField();
            zipCodeField = new JTextField();
            areaField = new JTextField();
            townshipField = new JTextField();
            countyField = new JTextField();

            areaField.setEditable(false);
            townshipField.setEditable(false);
            countyField.setEditable(false);

            add(streetLabel);
            add(streetNoLabel);
            add(zipCodeLabel);

            add(streetField);
            add(streetNoField);
            add(zipCodeField);

            add(areaLabel);
            add(townshipLabel);
            add(countyLabel);

            add(areaField);
            add(townshipField);
            add(countyField);

            add(telephoneLabel);
            add(emailLabel);
            add(new JLabel(""));

            add(telephoneField);
            add(emailField);
            add(new JLabel(""));
        }
    }

    // Underpanel til DataFieldPanel. Viser diverse informasjon. Inntekt, røyker, husdyr osv.
    private class MiscPanel extends JPanel {
        public MiscPanel() {
            // Ramme
            setBorder(BorderFactory.createTitledBorder("Diverse"));
            setPreferredSize(new Dimension(900, 400));
            GridLayout contactPanelLayout = new GridLayout(4, 3);
            setLayout(contactPanelLayout);

            annualRevenueLabel = new JLabel("Årsinntekt");
            passedCreditCheckLabel = new JLabel("Passert kreditsjekk");
            smokerLabel = new JLabel("Røyker");
            housepetsLabel = new JLabel("Husdyr");
            handicapAccommLabel = new JLabel("Behov for handicaptilpasning");
            annualRevenueField = new JTextField();
            String annualRevenue = String.valueOf(person.getAnnualRevenue());
            annualRevenueField.setText(annualRevenue);
            passedCreditCheckBox = new JCheckBox();
            smokerCheckBox = new JCheckBox();
            housepetsCheckBox = new JCheckBox();
            handicapAccommCheckBox = new JCheckBox();

            add(annualRevenueLabel);
            add(new JLabel(""));
            add(passedCreditCheckLabel);

            add(annualRevenueField);
            add(new JLabel(""));
            add(passedCreditCheckBox);

            add(smokerLabel);
            add(housepetsLabel);
            add(handicapAccommLabel);

            add(smokerCheckBox);
            add(housepetsCheckBox);
            add(handicapAccommCheckBox);
        }
    }

    // Scrollpanel som inneholder en tabell med kundens kontrakter.
    private class ContractScrollPanel extends JScrollPane {
        public ContractScrollPanel() {
            // Ramme
            setBorder(BorderFactory.createTitledBorder("Kontrakter"));
            setPreferredSize(new Dimension(300, 200));

            // Viewport
            setViewportView(contractsTable);

            // Innhold i ContractsPanel:
            contractsTable = new JTable();

            setVisible(true);
        }
    }

    // Scrollpanel som inneholder en tabell med kundens utleieboliger.
    private class DwellingUnitScrollPanel extends JScrollPane {
        public DwellingUnitScrollPanel() {
            // Ramme

            setBorder(BorderFactory.createTitledBorder("Utleide boliger"));
            setPreferredSize(new Dimension(300, 200));

            /*
            // Innhold i DwellingUnitsPanel
            Boligtabell boligtabell = new Boligtabell();

            DefaultTableModel d = boligtabell.visEierensBoliger(person.getPersonNo());

            Final JTable table = new JTable(d) {

                for (int row = 0; row < getRowCount(); row++) {
                    Object o = getValueAt(row, columnCount);
                    if (o != null){
                        return o.getClass();
                    }
                    return Object.class;
                }

            };

            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            */


            // Viewport
            setViewportView(dwellingUnitsTable);
            setVisible(true);
        }
    }

    // Kontrollpanel. Tar hånd om alle knappene. Navigering, lagring, ny person etc.
    private class ControlPanel extends JPanel implements ActionListener {
        public ControlPanel() {
            // Ramme
            setBorder(BorderFactory.createTitledBorder("Kontrollpanel"));
            setPreferredSize(new Dimension(300, 200));

            // Innhold i Kontrollpanel
            nextButton = new JButton("Neste");
            previousButton = new JButton("Forrige");
            saveButton = new JButton("Lagre");
            cancelButton = new JButton("Avbryt");
            deleteButton = new JButton("Slett");
            searchButton = new JButton("Søk");
            closeButton = new JButton("Lukk");
            createNewButton = new JButton("Ny");
            findDwellingUnitButton = new JButton("Finn bolig");

            nextButton.addActionListener(this);
            previousButton.addActionListener(this);
            searchButton.addActionListener(this);
            cancelButton.addActionListener(this);
            saveButton.addActionListener(this);
            closeButton.addActionListener(this);
            deleteButton.addActionListener(this);
            createNewButton.addActionListener(this);
            findDwellingUnitButton.addActionListener(this);

            // Layout: Kontrollpanel
            GridLayout controlLayout = new GridLayout(3, 3, 25, 25);
            setLayout(controlLayout);
            add(previousButton);
            add(nextButton);
            add(searchButton);
            add(createNewButton);
            add(saveButton);
            add(deleteButton);
            add(cancelButton);
            add(findDwellingUnitButton);
            add(closeButton);
            setVisible(true);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == nextButton) {
                try {
                    if (insertMode) {
                        person.moveToCurrentRow();
                    }
                    person.nextPerson();
                    updateFields();
                } catch (SQLException sql) {
                }
            } else if (e.getSource() == previousButton) {
                try {
                    if (insertMode) {
                        person.moveToCurrentRow();
                    }
                    person.previousPerson();
                    updateFields();
                } catch (SQLException sql) {
                }
            } else if (e.getSource() == searchButton) {
                try {
                    String identity = JOptionPane.showInputDialog("Søk på personnummer");
                    person.findPersonWithPersonNo(identity);
                    updateFields();
                } catch (SQLException s) {

                }
            } else if (e.getSource() == cancelButton) {
                try {
                    if (insertMode) {
                        person.moveToCurrentRow();
                    }
                    person.cancelUpdates();
                    updateFields();
                } catch (SQLException sql) {
                }
            } else if(e.getSource() == createNewButton) {
                try {
                    clearFields();
                    insertMode = true;
                    person.moveToInsertRow();
                    updateInfotext();
                } catch (SQLException sql) {
                }
            }else if(e.getSource() == deleteButton) {
                try {
                    person.deleteRow();
                    updateInfotext();
                    person.acceptChanges();
                    updateFields();
                } catch (SQLException sql) {
                }
            } else if (e.getSource() == saveButton) {
                try {
                    if (insertMode) {
                        saveNewPerson();
                    }
                    else {
                        updatePerson();
                    }
                } catch (SQLException sql) {
                  }
            } else {
                personFrame.setVisible(false);
            }
        }
    }

    // Statusbar som viser informative programmeldinger
    private class StatusbarPanel extends JPanel {
        public StatusbarPanel() {
            // Ramme
            setPreferredSize(new Dimension(1250, 30));
            setLayout(new FlowLayout(FlowLayout.RIGHT));

            String infoText = person.getInfoText();

            infoTextLabel = new JLabel("test");
            infoTextLabel.setForeground(Color.black);
            add(infoTextLabel);
            setVisible(true);
        }
    }

    // Metode som henter alle nødvendige get-verdier fra Person-objektet og oppdaterer tilhørende datafelt i vinduet.
    private void updateFields() {
        fullNameLabel.setText(person.getFullName());
        firstNameField.setText(person.getFirstName());
        middleNameField.setText(person.getMiddleName());
        surNameField.setText(person.getSurName());
        personNoField.setText(person.getPersonNo());
        streetField.setText(person.getStreet());
        streetNoField.setText(person.getStreetNo());
        String zipCode = String.valueOf(person.getZipCode());
        zipCodeField.setText(zipCode);
        areaField.setText(person.getArea());
        townshipField.setText(person.getTownship());
        countyField.setText(person.getCounty());
        String telephoneNo = String.valueOf(person.getTelephoneNo());
        telephoneField.setText(telephoneNo);
        emailField.setText(person.getEmail());
        String annualRevenue = String.valueOf(person.getAnnualRevenue());
        annualRevenueField.setText(annualRevenue);
        smokerCheckBox.setSelected(person.getIsSmoker());
        housepetsCheckBox.setSelected(person.getHasHousepets());
        passedCreditCheckBox.setSelected(person.getHasPassedCreditCheck());
        handicapAccommCheckBox.setSelected(person.getNeedsHandicapAccommodation());
        maritalStatusComboBox.setSelectedItem(person.getMaritalStatus());

        infoTextLabel.setText(person.getInfoText());
    }

    // Metode som henter Personklassens infotekst og oppdaterer statusfeltet i bunnen av vinduet.
    private void updateInfotext() {
        infoTextLabel.setText(person.getInfoText());
    }

    // Metode som tømmer alle datafeltene. Brukes i forbindelse med opprettelse av ny person.
    private void clearFields() {
        fullNameLabel.setText(null);
        firstNameField.setText(null);
        middleNameField.setText(null);
        surNameField.setText(null);
        personNoField.setText(null);
        streetField.setText(null);
        streetNoField.setText(null);
        zipCodeField.setText(null);
        areaField.setText(null);
        townshipField.setText(null);
        countyField.setText(null);
        telephoneField.setText(null);
        emailField.setText(null);
        annualRevenueField.setText(null);
        passedCreditCheckBox.setSelected(false);
        smokerCheckBox.setSelected(false);
        housepetsCheckBox.setSelected(false);
        handicapAccommCheckBox.setSelected(false);
        maritalStatusComboBox.setSelectedItem("alene");
    }

    private void saveNewPerson() throws SQLException {
        try {
            // Henter fram personnummeret for å hente fram vedkommende etterpå
            String currentPersonNo = personNoField.getText();

            // Oppdaterer feltene
            person.updateStringValue("person_no", personNoField.getText());
            person.updateStringValue("firstname", firstNameField.getText());
            person.updateStringValue("middlename", middleNameField.getText());
            person.updateStringValue("surname", surNameField.getText());
            person.updateBooleanValue("is_broker", brokerCheckBox.isSelected());
            // Caster combobox til String
            String maritalStatus = maritalStatusComboBox.getSelectedItem().toString();
            person.updateStringValue("marital_status", maritalStatus);
            person.updateStringValue("street", streetField.getText());
            person.updateStringValue("street_no", streetNoField.getText());
            person.updateStringValue("zip_code", zipCodeField.getText());
            person.updateLongValue("telephone", Long.parseLong(telephoneField.getText()));
            person.updateStringValue("email", emailField.getText());
            person.updateIntValue("annual_revenue", Integer.parseInt(annualRevenueField.getText()));
            person.updateBooleanValue("passed_credit_check", passedCreditCheckBox.isSelected());
            person.updateBooleanValue("smoker", smokerCheckBox.isSelected());
            person.updateBooleanValue("housepets", housepetsCheckBox.isSelected());
            person.updateBooleanValue("handicap_accomm", handicapAccommCheckBox.isSelected());

            // Inserter raden
            person.insertRow();

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateInfotext();

            // Flytter peker vekk fra innsettingsrad
            person.moveToCurrentRow();

            // Sender endringer til databasen
            person.acceptChanges();

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateInfotext();

            // Henter fram personen som ble opprettet
            person.findPersonWithPersonNo(currentPersonNo);

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateFields();
        }
        catch (SQLException e) {
            infoTextLabel.setText("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }


    private void updatePerson() throws SQLException {
        try {
            // Flytter til nåværende rad. En forsikring i tilfelle pekeren står på en innsettingsrad.
            person.moveToCurrentRow();

            // Oppdaterer feltene
            person.updateStringValue("person_no", personNoField.getText());
            person.updateStringValue("firstname", firstNameField.getText());
            person.updateStringValue("middlename", middleNameField.getText());
            person.updateStringValue("surname", surNameField.getText());
            person.updateBooleanValue("is_broker", brokerCheckBox.isSelected());
            // Caster combobox til String
            String maritalStatus = maritalStatusComboBox.getSelectedItem().toString();
            person.updateStringValue("marital_status", maritalStatus);
            person.updateStringValue("street", streetField.getText());
            person.updateStringValue("street_no", streetNoField.getText());
            person.updateStringValue("zip_code", zipCodeField.getText());
            person.updateLongValue("telephone", Long.parseLong(telephoneField.getText()));
            person.updateStringValue("email", emailField.getText());
            person.updateIntValue("annual_revenue", Integer.parseInt(annualRevenueField.getText()));
            person.updateBooleanValue("passed_credit_check", passedCreditCheckBox.isSelected());
            person.updateBooleanValue("smoker", smokerCheckBox.isSelected());
            person.updateBooleanValue("housepets", housepetsCheckBox.isSelected());
            person.updateBooleanValue("handicap_accomm", handicapAccommCheckBox.isSelected());

            // Oppdaterer raden
            person.updateRow();

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateInfotext();

            // Sender endringer til databasen
            person.acceptChanges();

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateInfotext();

            // Henter fram de nye verdiene til personen
            person.refreshValues();
            updateFields();
        }
        catch (SQLException e) {
            infoTextLabel.setText("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }
}
