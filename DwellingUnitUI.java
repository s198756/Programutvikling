/**
 * Created by Dragon on 13.05.14.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class DwellingUnitUI extends JPanel {

    // Advarsler
    private static final String COULD_NOT_FIND_DWELLING_UNIT_MSG = "Kunne ikke finne bolig med den angitte IDen";

    JFrame dwellingFrame;
    JLabel adressLabel;
    JButton searchButton;
    JButton saveButton;
    JButton cancelButton;
    JButton nextButton;
    JButton previousButton;
    JButton createNewButton;
    JButton deleteButton;
    JLabel ownerLabel;
    JLabel typeLabel;
    JLabel streetnameLabel;
    JLabel IDLabel;
    JLabel sizeLabel;
    JLabel availableLabel;
    JLabel monthlyLabel;
    JLabel depositLabel;
    JLabel warmupLabel;
    JLabel warmwaterLabel;
    JLabel internetCheckLabel;
    JLabel TVLabel;
    JLabel electricityLabel;
    JLabel furnitureLabel;
    JLabel elecapplianceLabel;
    JLabel housepetsLabel;
    JLabel smokeLabel;
    JLabel elevatorLabel;
    JLabel handicapLabel;
    JLabel streetNoLabel;
    JLabel zipCodeLabel;
    JLabel areaLabel;
    JLabel townshipLabel;
    JLabel countyLabel;
    JLabel eiendomLabel;
    JLabel soveromLabel;
    JLabel badLabel;
    JLabel terraseLabel;
    JLabel balkongLabel;
    JLabel parkeringLabel;
    JLabel lagtTilLabel;
    JLabel infoTextLabel;
    JTextField ownerField;
    JTextField sizeField;
    JTextField streetField;
    JTextField streetNoField;
    JTextField zipCodeField;
    JTextField areaField;
    JTextField townshipField;
    JTextField countyField;
    JTextField depositField;
    JTextField monthlyField;
    JTextField propertyField;
    JTextField soveromField;
    JTextField badField;
    JTextField terrasseField;
    JTextField balkongField;
    JTextField parkeringField;
    JTextField lagtTilField;
    JComboBox type;
    JCheckBox availableCheckBox;
    JCheckBox warmupCheckBox;
    JCheckBox warmwaterCheckBox;
    JCheckBox internetCheckBox;
    JCheckBox TVCheckBox;
    JCheckBox electricityCheckBox;
    JCheckBox furnitureCheckBox;
    JCheckBox elecappCheckBox;
    JCheckBox housepetsCheckBox;
    JCheckBox smokeCheckBox;
    int dwellingUnitID;

    // Initialiserer boligliste.
    DwellingUnit dwellingUnit;

    // Angir om Rowset er i en innsettingsfase av ny rad.
    boolean insertMode = false;

    // Konstruktør som oppretter ny boligliste over alle boliger. Viser boligen øverst på lista.
    public DwellingUnitUI() throws SQLException {

        // Oppretter ny personliste
        dwellingUnit = new DwellingUnit();

        MainPanel main = new MainPanel();
        main.setPreferredSize(new Dimension(1280, 670));
        add(main);

        try {
            // Henter verdier
            dwellingUnit.refreshValues();
        } catch (SQLException e) {
            infoTextLabel.setText("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }

        // Oppdaterer feltene
        updateDwellingUnitFields();
    }

    // Konstruktør som tar imot et allerede opprettet Bolig-objekt
    public DwellingUnitUI(DwellingUnit d) {
        //
        dwellingUnit = d;

        MainPanel main = new MainPanel();
        main.setPreferredSize(new Dimension(1280, 670));
        add(main);

        try {
            // Henter verdier
            dwellingUnit.refreshValues();
        } catch (SQLException e) {
            infoTextLabel.setText("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }

    }

    // Konstruktør som tar imot en bolig ID, oppretter en ny boligliste og viser denne boligen først i vinduet.
    public DwellingUnitUI(int dID) throws SQLException {

        // Oppretter ny boligliste over alle boliger og søker opp boligen med den spesifiserte IDen
        dwellingUnit = new DwellingUnit();

        MainPanel main = new MainPanel();
        main.setPreferredSize(new Dimension(1280, 670));
        add(main);

        try {
            dwellingUnit.findDwellingUnitWithID(dID);
        } catch (SQLException e) {
            infoTextLabel.setText("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Hovedpanel som samler alle underpaneler.
    private class MainPanel extends JPanel {
        public MainPanel() {

            setPreferredSize(new Dimension(1100, 600));
            setVisible(true);

            // Layout: Hovedpanel for ContractUI
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

    // Overskriften på panelet. Viser kortfattig informasjon om nåværende bolig.
    private class HeaderPanel extends JPanel {
        public HeaderPanel() {
            // Headerpanel som viser fullt navn
            setPreferredSize(new Dimension(1250, 50));

            // Innhold i HeaderPanel:
            adressLabel = new JLabel();
            adressLabel.setFont(new Font("Serif", Font.PLAIN, 30));

            // Layout
            setLayout(new FlowLayout(FlowLayout.LEFT));
            add(adressLabel);
            setVisible(true);
        }
    }

    // Hovedpanel for boligens datafelt med scrollfunksjon.
    private class DataFieldScrollPanel extends JScrollPane {
        DataFieldScrollPanel() {
            // Scrollpanel container for boligfelt
            setPreferredSize(new Dimension(950, 550));

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
            add(new MainInfoPanel());
            add(new IncludedPanel());
            add(new AdditionalPanel());

            GridLayout dataFieldPanelLayout = new GridLayout(4, 4);

            setLayout(dataFieldPanelLayout);
            setPreferredSize(new Dimension(900, 750));

            setVisible(true);
        }
    }

    // Sidepanel som samler sammen panelene ContractScrollPanel, DwelingUnitScrollPanel og ControlPanel.
    private class SidePanel extends JPanel {
        public SidePanel() {
            setPreferredSize(new Dimension(300, 550));

            GridLayout sidepanelLayout = new GridLayout(3, 1);
            setLayout(sidepanelLayout);

            add(new ControlPanel());
        }
    }

    // Underpanel til DataFieldPanel. Viser personaliainformasjon.
    private class MainInfoPanel extends JPanel {
        public MainInfoPanel() {
            // Ramme
            setBorder(BorderFactory.createTitledBorder("Hovedinformasjon"));
            setPreferredSize(new Dimension(900, 300));
            GridLayout personaliaPanelLayout = new GridLayout(8, 3);
            personaliaPanelLayout.setHgap(50);
            setLayout(personaliaPanelLayout);

            // Innhold i Personalia (personnummer, navn, sivilstatus, megler)
            ownerLabel = new JLabel("Eier *");
            IDLabel = new JLabel("ID ");
            streetnameLabel = new JLabel("Adressenavn *");
            streetNoLabel = new JLabel("Adressenummer *");
            typeLabel = new JLabel("Boligtype *");
            sizeLabel = new JLabel("Kvadratmeter *");
            availableLabel = new JLabel("Ledig");
            depositLabel = new JLabel("Depositum *");
            monthlyLabel = new JLabel("Månedlig leie *");
            sizeField = new JTextField(4);
            ownerField = new JTextField(11);
            streetField = new JTextField(45);
            streetNoField = new JTextField(4);
            depositField = new JTextField(10);
            monthlyField = new JTextField(10);
            zipCodeLabel = new JLabel("Postnummer *");
            areaLabel = new JLabel("Poststed");
            townshipLabel = new JLabel("Kommune");
            countyLabel = new JLabel("Fylke");
            zipCodeField = new JTextField(7);
            areaField = new JTextField();
            townshipField = new JTextField();
            countyField = new JTextField();

            areaField.setEditable(false);
            townshipField.setEditable(false);
            countyField.setEditable(false);

            type = new JComboBox();
            type.setModel(new DefaultComboBoxModel(new String[]{"villa", "rekkehus", "bofellesskap", "leilighet", "hybel"}));
            availableCheckBox = new JCheckBox();

            add(ownerLabel);
            add(depositLabel);
            add(monthlyLabel);

            add(ownerField);
            add(depositField);
            add(monthlyField);

            add(streetnameLabel);
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

            add(availableLabel);
            add(typeLabel);
            add(sizeLabel);

            add(availableCheckBox);
            add(type);
            add(sizeField);
        }
    }

    // Underpanel til DataFieldPanel. Viser kontaktinformasjon.
    private class IncludedPanel extends JPanel {
        public IncludedPanel() {
            // Ramme
            setBorder(BorderFactory.createTitledBorder("Inkludert"));
            setPreferredSize(new Dimension(900, 700));
            GridLayout contactPanelLayout = new GridLayout(8, 3);
            contactPanelLayout.setHgap(50);
            setLayout(contactPanelLayout);

            warmupLabel = new JLabel("Oppvarming ");
            warmwaterLabel = new JLabel("Varmtvann ");
            internetCheckLabel = new JLabel("Internett ");
            TVLabel = new JLabel("TV ");
            electricityLabel = new JLabel("Elektrisitet ");
            furnitureLabel = new JLabel("Møbler ");
            elecapplianceLabel = new JLabel("Hvitevarer ");
            housepetsLabel = new JLabel("Husdyr ");
            smokeLabel = new JLabel("Røyking ");
            elevatorLabel = new JLabel("Heis" );
            handicapLabel = new JLabel("Handicap ");
            warmupCheckBox = new JCheckBox();
            warmwaterCheckBox = new JCheckBox();
            internetCheckBox = new JCheckBox();
            TVCheckBox = new JCheckBox();
            electricityCheckBox = new JCheckBox();
            furnitureCheckBox = new JCheckBox();
            elecappCheckBox = new JCheckBox();
            housepetsCheckBox = new JCheckBox();
            smokeCheckBox = new JCheckBox();
            // Innhold i Inkludert (telefon, email, adresse)

            add(warmupLabel);
            add(warmwaterLabel);
            add(internetCheckLabel);

            add(warmupCheckBox);
            add(warmwaterCheckBox);
            add(internetCheckBox);

            add(TVLabel);
            add(electricityLabel);
            add(furnitureLabel);

            add(TVCheckBox);
            add(electricityCheckBox);
            add(furnitureCheckBox);

            add(elecapplianceLabel);
            add(housepetsLabel);
            add(smokeLabel);

            add(elecappCheckBox);
            add(housepetsCheckBox);
            add(smokeCheckBox);
        }
    }

    // Underpanel til DataFieldPanel. Viser diverse informasjon. Inntekt, røyker, husdyr osv.
    private class AdditionalPanel extends JPanel {
        public AdditionalPanel() {
            // Ramme
            setBorder(BorderFactory.createTitledBorder("Tilleggsinformasjon"));
            setPreferredSize(new Dimension(900, 400));
            GridLayout contactPanelLayout = new GridLayout(6, 3);
            contactPanelLayout.setHgap(50);
            setLayout(contactPanelLayout);

            eiendomLabel = new JLabel("Størrelse på eiendom ");
            soveromLabel = new JLabel("Antall soverom ");
            badLabel = new JLabel("Antall bad ");
            terraseLabel = new JLabel("Antall terrasser ");
            balkongLabel = new JLabel("Antall balkonger ");
            parkeringLabel = new JLabel("Antall parkeringsplasser ");
            lagtTilLabel = new JLabel("Lagt til ");
            propertyField = new JTextField(4);
            soveromField = new JTextField(4);
            badField = new JTextField(4);
            terrasseField = new JTextField(4);
            balkongField = new JTextField(4);
            parkeringField = new JTextField(4);
            lagtTilField = new JTextField(4);

            add(eiendomLabel);
            add(soveromLabel);
            add(badLabel);

            add(propertyField);
            add(soveromField);
            add(badField);

            add(terraseLabel);
            add(balkongLabel);
            add(parkeringLabel);

            add(terrasseField);
            add(balkongField);
            add(parkeringField);

            add(lagtTilLabel);
            add(new JLabel(""));
            add(new JLabel(""));

            add(lagtTilField);
        }
    }

    // Kontrollpanel. Tar hånd om alle knappene. Navigering, lagring, ny person etc.
    private class ControlPanel extends JPanel implements ActionListener {
        public ControlPanel() {
            // Ramme
            setBorder(BorderFactory.createTitledBorder("Kontrollpanel"));
            setPreferredSize(new Dimension(300, 200));

            // Innhold i Kontrollpanel
            saveButton = new JButton("Lagre");
            cancelButton = new JButton("Avbryt");
            deleteButton = new JButton("Slett");
            searchButton = new JButton("Søk");
            nextButton = new JButton("Neste");
            createNewButton = new JButton("Ny");
            previousButton = new JButton("Forrige");

            searchButton.addActionListener(this);
            cancelButton.addActionListener(this);
            saveButton.addActionListener(this);
            nextButton.addActionListener(this);
            deleteButton.addActionListener(this);
            createNewButton.addActionListener(this);
            previousButton.addActionListener(this);

            // Layout: Kontrollpanel
            GridLayout controlLayout = new GridLayout(3, 3, 25, 25);
            setLayout(controlLayout);
            add(searchButton);
            add(createNewButton);
            add(saveButton);
            add(deleteButton);
            add(cancelButton);
            add(nextButton);
            add(previousButton);
            setVisible(true);
        }

        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == nextButton){
                try {
                    if (insertMode) {
                        dwellingUnit.moveToCurrentRow();
                    }
                    dwellingUnit.nextPerson();
                    dwellingUnit.refreshValues();
                    updateDwellingUnitFields();
                } catch (SQLException sql) {
                    infoTextLabel.setText("Error code: " + sql.getErrorCode() + "\tLocalizedMessage: " + sql.getLocalizedMessage());
                }
            }
            else if (e.getSource() == searchButton) {
                try {
                    String identity = JOptionPane.showInputDialog("Søk etter ID: ");
                    int id = Integer.parseInt(identity);
                    dwellingUnit.findDwellingUnitWithID(id);
                    updateDwellingUnitFields();
                } catch (SQLException sql) {
                    infoTextLabel.setText("Error code: " + sql.getErrorCode() + "\tLocalizedMessage: " + sql.getLocalizedMessage());
                }

            } else if (e.getSource() == cancelButton) {
                try {
                    if (insertMode) {
                        dwellingUnit.moveToCurrentRow();
                        insertMode = false;
                    }
                    dwellingUnit.cancelUpdates();
                    updateDwellingUnitFields();
                    insertMode = false;
                } catch (SQLException sql) {
                    infoTextLabel.setText("Error code: " + sql.getErrorCode() + "\tLocalizedMessage: " + sql.getLocalizedMessage());
                }
            } else if(e.getSource() == createNewButton) {
                try {
                    clearFields();
                    dwellingUnit.moveToInsertRow();
                    insertMode = true;
                    adressLabel.setText(null);
                    updateInfotext();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }else if(e.getSource() == deleteButton) {
                try {
                    dwellingUnit.deleteRow();
                    dwellingUnit.acceptChanges();
                    dwellingUnit.previousDwellingUnit();
                    updateDwellingUnitFields();
                } catch (SQLException sql) {
                    infoTextLabel.setText("Error code: " + sql.getErrorCode() + "\tLocalizedMessage: " + sql.getLocalizedMessage());
                }
            } else if (e.getSource() == saveButton) {
                try {
                    if (insertMode) {
                        saveNewDwellingUnit();
                    }
                    else {
                        changeDwellingUnit();
                    }
                } catch (SQLException sql) {
                    infoTextLabel.setText("Error code: " + sql.getErrorCode() + "\tLocalizedMessage: " + sql.getLocalizedMessage());
                }
            }else if (e.getSource() == previousButton){
                try {
                    if (insertMode) {
                        dwellingUnit.moveToCurrentRow();
                    }
                    dwellingUnit.previousDwellingUnit();
                    updateDwellingUnitFields();
                } catch (SQLException sql) {
                    infoTextLabel.setText("Error code: " + sql.getErrorCode() + "\tLocalizedMessage: " + sql.getLocalizedMessage());
                }
            }
            }
        }


    // Statusbar som viser informative programmeldinger
    private class StatusbarPanel extends JPanel {
        public StatusbarPanel() {
            // Ramme
            setPreferredSize(new Dimension(1250, 30));
            setLayout(new FlowLayout(FlowLayout.RIGHT));

            infoTextLabel = new JLabel();
            infoTextLabel.setForeground(Color.black);
            add(infoTextLabel);
            setVisible(true);
        }
    }

    // Metode som henter alle nødvendige get-verdier fra Person-objektet og oppdaterer tilhørende datafelt i vinduet.
    private void updateDwellingUnitFields() {
        dwellingUnitID = dwellingUnit.getDwellingUnitID();
        adressLabel.setText("Bolig-ID " + dwellingUnit.getDwellingUnitID() + ", " + dwellingUnit.getStreet() + " " + dwellingUnit.getStreetNo());
        ownerField.setText(dwellingUnit.getPropertyOwner());
        String size = String.valueOf(dwellingUnit.getSize());
        sizeField.setText(size);
        streetField.setText(dwellingUnit.getStreet());
        streetNoField.setText(dwellingUnit.getStreetNo());
        String zipCode = String.valueOf(dwellingUnit.getZipCode());
        zipCodeField.setText(zipCode);
        areaField.setText(dwellingUnit.getArea());
        townshipField.setText(dwellingUnit.getTownship());
        countyField.setText(dwellingUnit.getCounty());
        String deposit = String.valueOf(dwellingUnit.getDepositumPrice());
        depositField.setText(deposit);
        String monthly = String.valueOf(dwellingUnit.getMonthlyPrice());
        monthlyField.setText(monthly);
        availableCheckBox.setSelected(dwellingUnit.getIsAvailable());
        warmupCheckBox.setSelected(dwellingUnit.getInclusiveWarmup());
        warmwaterCheckBox.setSelected(dwellingUnit.getInclusiveWarmWater());
        internetCheckBox.setSelected(dwellingUnit.getInclusiveInternet());
        TVCheckBox.setSelected(dwellingUnit.getInclusiveCableTV());
        electricityCheckBox.setSelected(dwellingUnit.getInclusiveElectricity());
        furnitureCheckBox.setSelected(dwellingUnit.getInclusiveFurniture());
        elecappCheckBox.setSelected(dwellingUnit.getInclusiveElectricAppliances());
        housepetsCheckBox.setSelected(dwellingUnit.getAllowHousepets());
        smokeCheckBox.setSelected(dwellingUnit.getAllowSmokers());
        type.setSelectedItem(dwellingUnit.getDwellingType());
        String property = String.valueOf(dwellingUnit.getPropertySize());
        propertyField.setText(property);
        String soverom = String.valueOf(dwellingUnit.getAmountOfBedrooms());
        soveromField.setText(soverom);
        String bad = String.valueOf(dwellingUnit.getAmountOfBathrooms());
        badField.setText(bad);
        String terrasse = String.valueOf(dwellingUnit.getAmountOfTerraces());
        terrasseField.setText(terrasse);
        String balkong = String.valueOf(dwellingUnit.getAmountOfBalconies());
        balkongField.setText(balkong);
        String parkering = String.valueOf(dwellingUnit.getAmountOfPrivateParking());
        parkeringField.setText(parkering);
    }

    // Metode som henter Personklassens infotekst og oppdaterer statusfeltet i bunnen av vinduet.
    private void updateInfotext() {
        infoTextLabel.setText(dwellingUnit.getInfoText());
    }

    // Metode som tømmer alle datafeltene. Brukes i forbindelse med opprettelse av ny bolig.
    private void clearFields() {
        ownerField.setText(null);
        sizeField.setText(null);
        streetField.setText(null);
        streetNoField.setText(null);
        depositField.setText(null);
        monthlyField.setText(null);
        zipCodeField.setText(null);
        areaField.setText(null);
        townshipField.setText(null);
        countyField.setText(null);
        availableCheckBox.setSelected(false);
        warmupCheckBox.setSelected(false);
        warmwaterCheckBox.setSelected(false);
        internetCheckBox.setSelected(false);
        TVCheckBox.setSelected(false);
        electricityCheckBox.setSelected(false);
        furnitureCheckBox.setSelected(false);
        elecappCheckBox.setSelected(false);
        housepetsCheckBox.setSelected(false);
        smokeCheckBox.setSelected(false);
        type.setSelectedItem("hybel");
        propertyField.setText(null);
        soveromField.setText(null);
        badField.setText(null);
        terrasseField.setText(null);
        balkongField.setText(null);
        parkeringField.setText(null);
        lagtTilField.setText(null);
    }

    private void saveNewDwellingUnit() throws SQLException {
        try {
            // Oppdaterer feltene
            dwellingUnit.updateBooleanValue("available", availableCheckBox.isSelected());
            dwellingUnit.updateStringValue("property_owner", ownerField.getText());
            String boligtype = type.getSelectedItem().toString();
            dwellingUnit.updateStringValue("dwelling_type", boligtype);
            int size = Integer.valueOf(sizeField.getText());
            dwellingUnit.updateIntValue("size", size);
            int zip = Integer.valueOf(zipCodeField.getText());
            dwellingUnit.updateIntValue("zip_code", zip);
            dwellingUnit.updateStringValue("street", streetField.getText());
            dwellingUnit.updateStringValue("street_no", streetNoField.getText());
            int monthly = Integer.valueOf(monthlyField.getText());
            dwellingUnit.updateIntValue("monthly_price", monthly);
            int deposit = Integer.valueOf(depositField.getText());
            dwellingUnit.updateStringValue("depositum", depositField.getText());
            dwellingUnit.updateBooleanValue("incl_warmup", warmupCheckBox.isSelected());
            dwellingUnit.updateBooleanValue("incl_warmwater", warmwaterCheckBox.isSelected());
            dwellingUnit.updateBooleanValue("incl_internet", internetCheckBox.isSelected());
            dwellingUnit.updateBooleanValue("incl_tv", TVCheckBox.isSelected());
            dwellingUnit.updateBooleanValue("incl_electricity", electricityCheckBox.isSelected());
            dwellingUnit.updateBooleanValue("incl_furniture", furnitureCheckBox.isSelected());
            dwellingUnit.updateBooleanValue("incl_elec_appliance", elecappCheckBox.isSelected());
            dwellingUnit.updateBooleanValue("allow_housepets", housepetsCheckBox.isSelected());
            dwellingUnit.updateBooleanValue("allow_smokers", smokeCheckBox.isSelected());
            dwellingUnit.updateBooleanValue("elevator", true);
            dwellingUnit.updateBooleanValue("handicap_accomm", true);
            int property = Integer.valueOf(propertyField.getText());
            dwellingUnit.updateIntValue("property_size", property);
            int bed = Integer.valueOf(soveromField.getText());
            dwellingUnit.updateIntValue("amount_bedroom", bed);
            int bath = Integer.valueOf(badField.getText());
            dwellingUnit.updateIntValue("amount_bathroom", bath);
            int terrace = Integer.valueOf(terrasseField.getText());
            dwellingUnit.updateIntValue("amount_terrace", terrace);
            int balcony = Integer.valueOf(balkongField.getText());
            dwellingUnit.updateIntValue("amount_balcony", balcony);
            int parking = Integer.valueOf(parkeringField.getText());
            dwellingUnit.updateIntValue("amount_private_parking", parking);

            // Inserter raden
            dwellingUnit.insertRow();

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateInfotext();

            // Flytter peker vekk fra innsettingsrad
            dwellingUnit.moveToCurrentRow();
            dwellingUnit.acceptChanges();

            // Sender endringer til databasen
            /*if (!dwellingUnit.acceptChanges()) {
                JOptionPane.showMessageDialog(null, "Kunne ikke sende endringer til databasen");
            }*/

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateInfotext();

            insertMode = false;
            // Henter fram nederste person
            dwellingUnit.last();
            dwellingUnit.refreshValues();

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateDwellingUnitFields();
        }
        catch (SQLException e) {
            infoTextLabel.setText("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }

    }

    private void changeDwellingUnit() throws SQLException {
        try {
            // Flytter til nåværende rad. En forsikring i tilfelle pekeren står på en innsettingsrad.
            dwellingUnit.moveToCurrentRow();

            dwellingUnit.updateBooleanValue("available", availableCheckBox.isSelected());
            dwellingUnit.updateStringValue("property_owner", ownerField.getText());
            String boligtype = type.getSelectedItem().toString();
            dwellingUnit.updateStringValue("dwelling_type", boligtype);
            dwellingUnit.updateStringValue("size", sizeField.getText());
            dwellingUnit.updateStringValue("street", streetField.getText());
            dwellingUnit.updateStringValue("street_no", streetNoField.getText());
            dwellingUnit.updateIntValue("zip_code", Integer.parseInt(zipCodeField.getText()));
            dwellingUnit.updateIntValue("monthly_price", Integer.parseInt(monthlyField.getText()));
            dwellingUnit.updateIntValue("depositum", Integer.parseInt(depositField.getText()));
            dwellingUnit.updateBooleanValue("incl_warmup", warmupCheckBox.isSelected());
            dwellingUnit.updateBooleanValue("incl_warmwater", warmwaterCheckBox.isSelected());
            dwellingUnit.updateBooleanValue("incl_internet", internetCheckBox.isSelected());
            dwellingUnit.updateBooleanValue("incl_tv", TVCheckBox.isSelected());
            dwellingUnit.updateBooleanValue("incl_electricity", electricityCheckBox.isSelected());
            dwellingUnit.updateBooleanValue("incl_furniture", furnitureCheckBox.isSelected());
            dwellingUnit.updateBooleanValue("incl_elec_appliance", elecappCheckBox.isSelected());
            dwellingUnit.updateBooleanValue("allow_housepets", housepetsCheckBox.isSelected());
            dwellingUnit.updateBooleanValue("allow_smokers", smokeCheckBox.isSelected());
            dwellingUnit.updateIntValue("property_size", Integer.parseInt(propertyField.getText()));
            dwellingUnit.updateIntValue("amount_bedroom", Integer.parseInt(soveromField.getText()));
            dwellingUnit.updateIntValue("amount_bathroom", Integer.parseInt(badField.getText()));
            dwellingUnit.updateIntValue("amount_terrace", Integer.parseInt(terrasseField.getText()));
            dwellingUnit.updateIntValue("amount_balcony", Integer.parseInt(balkongField.getText()));
            dwellingUnit.updateIntValue("amount_private_parking", Integer.parseInt(parkeringField.getText()));

            // Oppdaterer raden
            dwellingUnit.updateRow();
            System.out.println("Etter updaterow: " + dwellingUnit.getCurrentRowNumber());

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateInfotext();

            // Sender endringer til databasen
            if (!dwellingUnit.acceptChanges()) {
                JOptionPane.showMessageDialog(null, "Kunne ikke sende endringer til databasen");
            }

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateInfotext();

            // Finner tilbake til kontrakten som ble lagret
            if (!dwellingUnit.findDwellingUnitWithID(dwellingUnitID)) {
                JOptionPane.showMessageDialog(null, COULD_NOT_FIND_DWELLING_UNIT_MSG + dwellingUnitID);
            }

            // Henter fram de nye verdiene til personen
            dwellingUnit.refreshValues();
            updateDwellingUnitFields();
        }
        catch (SQLException e) {
            infoTextLabel.setText("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }
}



