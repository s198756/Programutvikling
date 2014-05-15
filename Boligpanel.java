import com.sun.rowset.CachedRowSetImpl;
import javax.sql.rowset.FilteredRowSet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.Timestamp;
import java.sql.SQLException;

/**
 * Created by Dragon on 13.05.14.
 */

    public class Boligpanel {
        JFrame dwellingFrame;
        JLabel adressLabel;
        JButton searchButton;
        JButton saveButton;
        JButton cancelButton;
        JButton closeButton;
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
        DwellingUnit dwelling;
        boolean insertMode;
        java.sql.Timestamp opprettet;

        public static void main(String[] args) throws SQLException {
            Boligpanel bpanel = new Boligpanel();
        }

        // Konstruktør som oppretter ny personliste over alle personer. Viser personen øverst på lista.
        public Boligpanel() throws SQLException {

            // Oppretter ny personliste
            dwelling = new DwellingUnit();
            try {
                // Henter verdier
                dwelling.refreshValues();
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


                    dwellingFrame = new JFrame("Person");
                    dwellingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    dwellingFrame.setResizable(true);
                    dwellingFrame.setLayout(new BorderLayout());
                    dwellingFrame.add(new MainPanel());
                    dwellingFrame.setPreferredSize(new Dimension(1280, 720));
                    dwellingFrame.pack();

                    // Midtstiller vinduet
                    dwellingFrame.setLocationRelativeTo(null);

                    dwellingFrame.setVisible(true);

                    // Oppdaterer feltene
                    updateFields();
                }
            });
        }

        // Konstruktør som tar imot et personnummer og viser denne personen først i vinduet.
       /* public Boligpanel(String pNo) throws SQLException {

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
*/
        // Konstruktør som tar imot SQLInterface, FilteredRowSet og Radnummer.
        public Boligpanel(SQLInterface dbInt, FilteredRowSet frs, int rowID) throws SQLException {

            // Henter den cachede personlista og hopper til det spesifiserte radnummeret
            dwelling = new DwellingUnit(dbInt, frs, rowID);

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


                    dwellingFrame = new JFrame("Person");
                    dwellingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    dwellingFrame.setResizable(false);
                    dwellingFrame.setLayout(new BorderLayout());
                    dwellingFrame.add(new MainPanel());
                    dwellingFrame.setPreferredSize(new Dimension(1280, 720));
                    dwellingFrame.pack();

                    // Midtstiller vinduet
                    dwellingFrame.setLocationRelativeTo(null);

                    dwellingFrame.setVisible(true);

                    // Oppdaterer feltene
                    updateFields();
                }
            });
        }

        // Konstruktør som tar imot SQLInterface, CachedRowSet og Radnummer.
        public Boligpanel(SQLInterface dbInt, CachedRowSetImpl crs, int rowID) throws SQLException {

            // Henter den cachede personlista og hopper til det spesifiserte radnummeret
            dwelling = new DwellingUnit(dbInt, crs, rowID);

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


                    dwellingFrame = new JFrame("Person");
                    dwellingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    dwellingFrame.setResizable(false);
                    dwellingFrame.setLayout(new BorderLayout());
                    dwellingFrame.add(new MainPanel());
                    dwellingFrame.setPreferredSize(new Dimension(1280, 720));
                    dwellingFrame.pack();

                    // Midtstiller vinduet
                    dwellingFrame.setLocationRelativeTo(null);

                    dwellingFrame.setVisible(true);

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
               // add(new StatusbarPanel(), c);
            }
        }

        // Overskriften på vinduet. Viser fullt navn på nåværende person.
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
                setPreferredSize(new Dimension(300, 600));

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
                closeButton = new JButton("Lukk");
                createNewButton = new JButton("Ny");

                searchButton.addActionListener(this);
                cancelButton.addActionListener(this);
                saveButton.addActionListener(this);
                closeButton.addActionListener(this);
                deleteButton.addActionListener(this);
                createNewButton.addActionListener(this);

                // Layout: Kontrollpanel
                GridLayout controlLayout = new GridLayout(3, 3, 25, 25);
                setLayout(controlLayout);
                add(searchButton);
                add(createNewButton);
                add(saveButton);
                add(deleteButton);
                add(cancelButton);
                add(closeButton);
                setVisible(true);
            }


            public void actionPerformed(ActionEvent e) {
               if (e.getSource() == searchButton) {
                    try {
                        String identity = JOptionPane.showInputDialog("Søk etter ID: ");
                        int id = Integer.parseInt(identity);
                        dwelling.findDwellingUnitWithID(id);
                        updateFields();
                    } catch (SQLException s) {

                    }

                } else if (e.getSource() == cancelButton) {
                    try {
                        if (insertMode) {
                            dwelling.moveToCurrentRow();
                        }
                        dwelling.cancelUpdates();
                        updateFields();
                    } catch (SQLException sql) {
                    }
                } else if(e.getSource() == createNewButton) {
                        clearFields();
                        insertMode = true;
                        adressLabel.setText(null);
                }else if(e.getSource() == deleteButton) {
                    try {
                        dwelling.deleteRow();
                        dwelling.acceptChanges();
                        dwelling.previousDwellingUnit();
                        updateFields();
                    } catch (SQLException sql) {
                    }
                } else if (e.getSource() == saveButton) {
                    try {
                        if (insertMode) {
                            saveNewDwellingUnit();
                        }
                        else {
                            updateDwelling();
                        }
                    } catch (SQLException sql) {
                    }
                } else {
                    dwellingFrame.setVisible(false);
                }
            }
        }

        // Metode som henter alle nødvendige get-verdier fra Person-objektet og oppdaterer tilhørende datafelt i vinduet.
       private void updateFields() {
            adressLabel.setText("Bolig-ID " + dwelling.getDwellingUnitID() + ", " + dwelling.getStreet() + " " + dwelling.getStreetNo());
            ownerField.setText(dwelling.getPropertyOwner());
            String size = String.valueOf(dwelling.getSize());
            sizeField.setText(size);
            streetField.setText(dwelling.getStreet());
            streetNoField.setText(dwelling.getStreetNo());
            String zipCode = String.valueOf(dwelling.getZipCode());
            zipCodeField.setText(zipCode);
            areaField.setText(dwelling.getArea());
            townshipField.setText(dwelling.getTownship());
            countyField.setText(dwelling.getCounty());
            String deposit = String.valueOf(dwelling.getDepositumPrice());
            depositField.setText(deposit);
            String monthly = String.valueOf(dwelling.getMonthlyPrice());
            monthlyField.setText(monthly);
            availableCheckBox.setSelected(dwelling.getIsAvailable());
            warmupCheckBox.setSelected(dwelling.getInclusiveWarmup());
            warmwaterCheckBox.setSelected(dwelling.getInclusiveWarmWater());
            internetCheckBox.setSelected(dwelling.getInclusiveInternet());
            TVCheckBox.setSelected(dwelling.getInclusiveCableTV());
            electricityCheckBox.setSelected(dwelling.getInclusiveElectricity());
            furnitureCheckBox.setSelected(dwelling.getInclusiveFurniture());
            elecappCheckBox.setSelected(dwelling.getInclusiveElectricAppliances());
            housepetsCheckBox.setSelected(dwelling.getAllowHousepets());
            smokeCheckBox.setSelected(dwelling.getAllowSmokers());
            type.setSelectedItem(dwelling.getDwellingType());
            String property = String.valueOf(dwelling.getPropertySize());
            propertyField.setText(property);
            String soverom = String.valueOf(dwelling.getAmountOfBedrooms());
            soveromField.setText(soverom);
            String bad = String.valueOf(dwelling.getAmountOfBathrooms());
            badField.setText(bad);
            String terrasse = String.valueOf(dwelling.getAmountOfTerraces());
            terrasseField.setText(terrasse);
            String balkong = String.valueOf(dwelling.getAmountOfBalconies());
            balkongField.setText(balkong);
            String parkering = String.valueOf(dwelling.getAmountOfPrivateParking());
            parkeringField.setText(parkering);
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
                dwelling.moveToInsertRow();
                dwelling.updateBooleanValue("available", availableCheckBox.isSelected());
                dwelling.updateStringValue("property_owner", ownerField.getText());
                String boligtype = type.getSelectedItem().toString();
                dwelling.updateStringValue("dwelling_type", boligtype);
                dwelling.updateStringValue("size", sizeField.getText());
                dwelling.updateStringValue("street", streetField.getText());
                dwelling.updateStringValue("street_no", streetNoField.getText());
                dwelling.updateStringValue("zip_code", zipCodeField.getText());
                dwelling.updateStringValue("monthly_price", monthlyField.getText());
                dwelling.updateStringValue("depositum", depositField.getText());
                dwelling.updateBooleanValue("incl_warmup", warmupCheckBox.isSelected());
                dwelling.updateBooleanValue("incl_warmwater", warmwaterCheckBox.isSelected());
                dwelling.updateBooleanValue("incl_internet", internetCheckBox.isSelected());
                dwelling.updateBooleanValue("incl_tv", TVCheckBox.isSelected());
                dwelling.updateBooleanValue("incl_electricity", electricityCheckBox.isSelected());
                dwelling.updateBooleanValue("incl_furniture", furnitureCheckBox.isSelected());
                dwelling.updateBooleanValue("incl_elec_appliance", elecappCheckBox.isSelected());
                dwelling.updateBooleanValue("allow_housepets", housepetsCheckBox.isSelected());
                dwelling.updateBooleanValue("allow_smokers", smokeCheckBox.isSelected());
                dwelling.updateBooleanValue("elevator", true);
                dwelling.updateBooleanValue("handicap_accomm", true);
                dwelling.updateIntValue("property_size", Integer.parseInt(propertyField.getText()));
                dwelling.updateStringValue("amount_bedroom", soveromField.getText());
                dwelling.updateStringValue("amount_bathroom", badField.getText());
                dwelling.updateStringValue("amount_balcony", balkongField.getText());
                dwelling.updateStringValue("amount_terrace", terraseLabel.getText());
                dwelling.updateStringValue("amount_private_parking", parkeringField.getText());
                dwelling.insertRow();
                dwelling.moveToCurrentRow();
                dwelling.acceptChanges();

                /*dwelling.jumpToDwellingUnit(dwelling.getTotalAmountOfRows());
                updateFields();*/
            }
            catch (SQLException e) {

            }
        }


        private void updateDwelling() throws SQLException {
            try {
                dwelling.moveToCurrentRow();
                dwelling.updateBooleanValue("available", availableCheckBox.isSelected());
                dwelling.updateStringValue("property_owner", ownerField.getText());
                String boligtype = type.getSelectedItem().toString();
                dwelling.updateStringValue("dwelling_type", boligtype);
                dwelling.updateStringValue("size", sizeField.getText());
                dwelling.updateStringValue("street", streetField.getText());
                dwelling.updateStringValue("street_no", streetNoField.getText());
                dwelling.updateIntValue("zip_code", Integer.parseInt(zipCodeField.getText()));
                dwelling.updateIntValue("monthly_price", Integer.parseInt(monthlyField.getText()));
                dwelling.updateIntValue("depositum", Integer.parseInt(depositField.getText()));
                dwelling.updateBooleanValue("incl_warmup", warmupCheckBox.isSelected());
                dwelling.updateBooleanValue("incl_warmwater", warmwaterCheckBox.isSelected());
                dwelling.updateBooleanValue("incl_internet", internetCheckBox.isSelected());
                dwelling.updateBooleanValue("incl_tv", TVCheckBox.isSelected());
                dwelling.updateBooleanValue("incl_electricity", electricityCheckBox.isSelected());
                dwelling.updateBooleanValue("incl_furniture", furnitureCheckBox.isSelected());
                dwelling.updateBooleanValue("incl_elec_appliance", elecappCheckBox.isSelected());
                dwelling.updateBooleanValue("allow_housepets", housepetsCheckBox.isSelected());
                dwelling.updateBooleanValue("allow_smokers", smokeCheckBox.isSelected());
                dwelling.updateIntValue("property_size", Integer.parseInt(propertyField.getText()));
                dwelling.updateIntValue("amount_bedroom", Integer.parseInt(soveromField.getText()));
                dwelling.updateIntValue("amount_bathroom", Integer.parseInt(badField.getText()));
                dwelling.updateIntValue("amount_terrace", Integer.parseInt(terrasseField.getText()));
                dwelling.updateIntValue("amount_balcony", Integer.parseInt(balkongField.getText()));
                dwelling.updateIntValue("amount_private_parking", Integer.parseInt(parkeringField.getText()));
                /*opprettet = dwelling.getCreatedDate();
                String lagttil = String.valueOf(opprettet);
                dwelling.updateStringValue("created", lagttil);*/

                dwelling.updateRow();
                dwelling.acceptChanges();
                dwelling.refreshValues();
                updateFields();
            }
            catch (SQLException e) {

            }
        }
    }


