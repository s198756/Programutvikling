/**
 * Created by Sebastian Ramsland on 13.05.2014.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class ContractUI {
    JFrame contractFrame;
    JLabel contractLabel;
    JLabel contractIDLabel;
    JLabel dwellingUnitIDLabel;
    JLabel renterLabel;
    JLabel brokerLabel;
    JLabel validLabel;
    JLabel paidDepositumLabel;
    JLabel signedByRenterLabel;
    JLabel signedByBrokerLabel;
    JLabel inEffectDateLabel;
    JLabel expirationDateLabel;
    JButton nextButton;
    JButton previousButton;
    JButton searchButton;
    JButton saveButton;
    JButton cancelButton;
    JButton closeButton;
    JButton createNewButton;
    JButton validateButton;
    JButton deleteButton;
    JLabel infoTextLabel;
    JTextField contractIDField;
    JTextField dwellingUnitIDField;
    JTextField renterField;
    JTextField brokerField;
    JTextField inEffectDateField;
    JTextField expirationDateField;
    JCheckBox signedByRenterCheckBox;
    JCheckBox signedByBrokerCheckBox;
    JCheckBox paidDepositumCheckBox;
    JCheckBox validCheckBox;

    // Kontraktens bolig
    JLabel dwellingUnitTypeLabel;
    JLabel dwellingUnitStreetLabel;
    JLabel dwellingUnitStreetNoLabel;
    JLabel dwellingUnitZipCodeLabel;
    JLabel dwellingUnitMonthlyPriceLabel;
    JLabel dwellingUnitDepositumPriceLabel;
    JLabel dwellingUnitIsAvailableLabel;
    JLabel dwellingUnitHasElevatorLabel;
    JLabel dwellingUnitHasHandicapAccommLabel;

    JLabel dwellingUnitTypeValue;
    JLabel dwellingUnitStreetValue;
    JLabel dwellingUnitStreetNoValue;
    JLabel dwellingUnitZipCodeValue;
    JLabel dwellingUnitMonthlyPriceValue;
    JLabel dwellingUnitDepositumPriceValue;
    JCheckBox dwellingUnitIsAvailableCheckBox;
    JCheckBox dwellingUnitHasElevatorCheckBox;
    JCheckBox dwellingUnitHasHandicapAccommCheckBox;

    // Leietaker
    JLabel renterFullnameLabel;
    JLabel renterEmailLabel;
    JLabel renterTelephoneNoLabel;
    JLabel renterAnnualRevenueLabel;
    JLabel renterHasPassedCreditCheckLabel;
    JLabel renterIsSmokerLabel;
    JLabel renterHasHousepetsLabel;
    JLabel renterNeedsHandicapAccommLabel;

    JLabel renterFullNameValue;
    JLabel renterEmailValue;
    JLabel renterTelephoneNoValue;
    JLabel renterAnnualRevenueValue;
    JCheckBox renterHasPassedCreditCheckBox;
    JCheckBox renterIsSmokerCheckBox;
    JCheckBox renterHasHousepetsCheckBox;
    JCheckBox renterNeedsHandicapAccommCheckBox;

    // Font
    Font bold = new Font("Courier", Font.BOLD,11);

    Contract contract;
    Person person;
    DwellingUnit dwellingUnit;

    boolean insertMode;

    public static void main(String[] args) throws SQLException {
        ContractUI contractPanel = new ContractUI();
    }

    // Konstruktør som oppretter lister over alle kontrakter, personer og boliger. Viser kontrakten som er øverst på lista.
    public ContractUI() throws SQLException {

        // Oppretter lister
        contract = new Contract();
        person = new Person();
        dwellingUnit = new DwellingUnit();

        try {
            // Henter verdier
            contract.refreshValues();
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


                contractFrame = new JFrame("Contract");
                contractFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                contractFrame.setResizable(true);
                contractFrame.setLayout(new BorderLayout());
                contractFrame.add(new MainPanel());
                contractFrame.setPreferredSize(new Dimension(1280, 720));
                contractFrame.pack();

                // Midtstiller vinduet
                contractFrame.setLocationRelativeTo(null);

                contractFrame.setVisible(true);

                // Oppdaterer feltene
                updateFields();

                try {
                    updateDwellingUnit();
                    updateRenter();
                } catch (SQLException e) {

                }
            }
        });
    }

    // Konstruktør som tar imot allerede opprettede lister.
    public ContractUI(Contract c, Person p, DwellingUnit d) {
        //
        contract = c;
        person = p;
        dwellingUnit = d;
        try {
            // Henter verdier
            contract.refreshValues();
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

                contractFrame = new JFrame("Contract");
                contractFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                contractFrame.setResizable(true);
                contractFrame.setLayout(new BorderLayout());
                contractFrame.add(new MainPanel());
                contractFrame.setPreferredSize(new Dimension(1280, 720));
                contractFrame.pack();

                // Midtstiller vinduet
                contractFrame.setLocationRelativeTo(null);

                contractFrame.setVisible(true);

                // Oppdaterer feltene
                updateFields();
                try {
                    updateRenter();
                    updateDwellingUnit();
                } catch (SQLException s) {

                }
            }
        });
    }

    // Konstruktør som oppretter lister over alle kontrakter, personer og boliger.
    // Tar imot èn kontrakt ID og viser denne kontrakten først i vinduet.
    public ContractUI(int cID) throws SQLException {

        // Oppretter lister over alle kontrakter, personer og boliger
        contract = new Contract();
        person = new Person();
        dwellingUnit = new DwellingUnit();;

        try {
            contract.findContractWithID(cID);
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


                contractFrame = new JFrame("Contract");
                contractFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                contractFrame.setResizable(false);
                contractFrame.setLayout(new BorderLayout());
                contractFrame.add(new MainPanel());
                contractFrame.setPreferredSize(new Dimension(1280, 720));
                contractFrame.pack();

                // Midtstiller vinduet
                contractFrame.setLocationRelativeTo(null);

                contractFrame.setVisible(true);

                // Oppdaterer feltene
                updateFields();

                try {
                    updateDwellingUnit();
                    updateRenter();
                } catch (SQLException e) {

                }
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

    // Overskriften på vinduet. Viser kontrakt IDen.
    private class HeaderPanel extends JPanel {
        public HeaderPanel() {
            // Headerpanel som viser fullt navn
            setPreferredSize(new Dimension(1250, 50));

            // Innhold i HeaderPanel:
            contractLabel = new JLabel();
            contractLabel.setFont(new Font("Serif", Font.PLAIN, 30));

            // Layout
            setLayout(new FlowLayout(FlowLayout.LEFT));
            add(contractLabel);
            setVisible(true);
        }
    }

    // Hovedpanel for kontraktens datafelt med scrollfunksjon.
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
            add(new DatePanel());
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

            add(new RenterScrollPanel());
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
            contractIDLabel = new JLabel("Kontrakt ID (automatisk verdi)");
            dwellingUnitIDLabel = new JLabel("Bolig ID *");
            renterLabel = new JLabel("Personnummer Leietaker *");
            brokerLabel = new JLabel("Personnummer Megler *");
            validLabel = new JLabel("Gyldig");
            contractIDField = new JTextField(5);
            dwellingUnitIDField = new JTextField(5);
            renterField = new JTextField(11);
            brokerField = new JTextField(11);
            validCheckBox = new JCheckBox();

            contractIDField.setEditable(false);

            add(contractIDLabel);
            add(dwellingUnitIDLabel);
            add(validLabel);

            add(contractIDField);
            add(dwellingUnitIDField);
            add(validCheckBox);

            add(renterLabel);
            add(brokerLabel);
            add(new JLabel(""));

            add(renterField);
            add(brokerField);
            add(new JLabel(""));
        }
    }

    // Underpanel til DataFieldPanel. Viser kontaktinformasjon.
    private class DatePanel extends JPanel {
        public DatePanel() {
            // Ramme
            setBorder(BorderFactory.createTitledBorder("Kontakt"));
            setPreferredSize(new Dimension(900, 500));
            GridLayout contactPanelLayout = new GridLayout(4, 3);
            contactPanelLayout.setHgap(50);
            setLayout(contactPanelLayout);

            // Innhold i Kontakt (telefon, email, adresse)
            inEffectDateLabel = new JLabel("Start på kontraktperiode *");
            expirationDateLabel = new JLabel("Slutt på kontraktperiode *");
            signedByRenterLabel = new JLabel("Signert av leietaker *");
            signedByBrokerLabel = new JLabel("Signert av megler *");
            inEffectDateField = new JTextField();
            expirationDateField = new JTextField();
            signedByRenterCheckBox = new JCheckBox();
            signedByBrokerCheckBox = new JCheckBox();

            add(inEffectDateLabel);
            add(expirationDateLabel);
            add(new JLabel(""));

            add(inEffectDateField);
            add(expirationDateField);
            add(new JLabel(""));

            add(signedByRenterLabel);
            add(signedByBrokerLabel);
            add(new JLabel(""));

            add(signedByRenterCheckBox);
            add(signedByBrokerCheckBox);
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


        }
    }

    // Scrollpanel som inneholder en tekstboks med leietakerens informasjon.
    private class RenterScrollPanel extends JScrollPane {
        public RenterScrollPanel() {
            // Ramme
            setBorder(BorderFactory.createTitledBorder("Leietaker"));
            setPreferredSize(new Dimension(300, 200));

            // Viewport
            setViewportView(new RenterPanel());
            setVisible(true);
        }
    }

    // Scrollpanel som inneholder en tekstboks med informasjon om kontraktens bolig.
    private class DwellingUnitScrollPanel extends JScrollPane {
        public DwellingUnitScrollPanel() {
            // Ramme

            setBorder(BorderFactory.createTitledBorder("Tilknyttet bolig"));
            setPreferredSize(new Dimension(300, 200));

            // Viewport
            setViewportView(new DwellingUnitPanel());
            setVisible(true);
        }
    }

    // Underpanel til sidepanelet. Viser informasjon om leietaker.
    private class RenterPanel extends JPanel {
        public RenterPanel() {
            // Ramme
            setPreferredSize(new Dimension(200, 200));
            GridLayout contactPanelLayout = new GridLayout(8, 2);
            setLayout(contactPanelLayout);

            renterFullnameLabel = new JLabel("Navn");
            renterFullnameLabel.setFont(bold);
            renterEmailLabel = new JLabel("Epost");
            renterEmailLabel.setFont(bold);
            renterTelephoneNoLabel = new JLabel("Telefon");
            renterTelephoneNoLabel.setFont(bold);
            renterAnnualRevenueLabel = new JLabel("Årsinntekt");
            renterAnnualRevenueLabel.setFont(bold);
            renterHasPassedCreditCheckLabel = new JLabel("Bestått kreditsjekk");
            renterHasPassedCreditCheckLabel.setFont(bold);
            renterHasHousepetsLabel = new JLabel("Husdyr");
            renterHasHousepetsLabel.setFont(bold);
            renterIsSmokerLabel = new JLabel("Røyker");
            renterIsSmokerLabel.setFont(bold);
            renterNeedsHandicapAccommLabel = new JLabel("Handicaptilpasning");
            renterNeedsHandicapAccommLabel.setFont(bold);

            renterFullNameValue = new JLabel();
            renterEmailValue = new JLabel();
            renterTelephoneNoValue = new JLabel();
            renterAnnualRevenueValue = new JLabel();
            renterHasPassedCreditCheckBox = new JCheckBox();
            renterHasPassedCreditCheckBox.setEnabled(false);
            renterHasHousepetsCheckBox = new JCheckBox();
            renterHasHousepetsCheckBox.setEnabled(false);
            renterIsSmokerCheckBox = new JCheckBox();
            renterIsSmokerCheckBox.setEnabled(false);
            renterNeedsHandicapAccommCheckBox = new JCheckBox();
            renterNeedsHandicapAccommCheckBox.setEnabled(false);

            add(renterFullnameLabel);
            add(new JLabel(""));

            add(renterFullNameValue);
            add(new JLabel(""));

            add(renterTelephoneNoLabel);
            add(renterEmailLabel);

            add(renterTelephoneNoValue);
            add(renterEmailValue);

            add(renterAnnualRevenueLabel);
            add(renterHasPassedCreditCheckLabel);

            add(renterAnnualRevenueValue);
            add(renterHasPassedCreditCheckBox);

            add(renterIsSmokerLabel);
            add(renterHasHousepetsLabel);

            add(renterIsSmokerCheckBox);
            add(renterHasHousepetsCheckBox);
        }
    }

    // Underpanel til sidepanelet. Viser informasjon om leietaker.
    private class DwellingUnitPanel extends JPanel {
        public DwellingUnitPanel() {
            // Ramme
            setPreferredSize(new Dimension(200, 200));
            GridLayout contactPanelLayout = new GridLayout(6, 3);
            setLayout(contactPanelLayout);

            dwellingUnitTypeLabel = new JLabel("Boligtype");
            dwellingUnitTypeLabel.setFont(bold);
            dwellingUnitStreetLabel = new JLabel("Gateadresse");
            dwellingUnitStreetLabel.setFont(bold);
            dwellingUnitStreetNoLabel = new JLabel("Gatenummer");
            dwellingUnitStreetNoLabel.setFont(bold);
            dwellingUnitZipCodeLabel = new JLabel("Postnummer");
            dwellingUnitZipCodeLabel.setFont(bold);
            dwellingUnitMonthlyPriceLabel = new JLabel("Månedsleie");
            dwellingUnitMonthlyPriceLabel.setFont(bold);
            dwellingUnitDepositumPriceLabel = new JLabel("Depositum");
            dwellingUnitDepositumPriceLabel.setFont(bold);
            dwellingUnitHasElevatorLabel = new JLabel("Heis");
            dwellingUnitHasElevatorLabel.setFont(bold);
            dwellingUnitIsAvailableLabel = new JLabel("Ledig");
            dwellingUnitIsAvailableLabel.setFont(bold);
            dwellingUnitHasHandicapAccommLabel = new JLabel("Tilpasset handikappede");
            dwellingUnitHasHandicapAccommLabel.setFont(bold);

            dwellingUnitTypeValue = new JLabel();
            dwellingUnitStreetNoValue = new JLabel();
            dwellingUnitStreetValue = new JLabel();
            dwellingUnitStreetNoValue = new JLabel();
            dwellingUnitZipCodeValue = new JLabel();
            dwellingUnitMonthlyPriceValue = new JLabel();
            dwellingUnitDepositumPriceValue = new JLabel();
            dwellingUnitHasElevatorCheckBox = new JCheckBox();
            dwellingUnitHasElevatorCheckBox.setEnabled(false);
            dwellingUnitIsAvailableCheckBox = new JCheckBox();
            dwellingUnitIsAvailableCheckBox.setEnabled(false);
            dwellingUnitHasHandicapAccommCheckBox = new JCheckBox();
            dwellingUnitHasHandicapAccommCheckBox.setEnabled(false);


            add(dwellingUnitStreetLabel);
            add(dwellingUnitStreetNoLabel);
            add(dwellingUnitZipCodeLabel);

            add(dwellingUnitStreetValue);
            add(dwellingUnitStreetNoValue);
            add(dwellingUnitZipCodeValue);

            add(dwellingUnitTypeLabel);
            add(dwellingUnitMonthlyPriceLabel);
            add(dwellingUnitDepositumPriceLabel);

            add(dwellingUnitTypeValue);
            add(dwellingUnitMonthlyPriceValue);
            add(dwellingUnitDepositumPriceValue);

            add(dwellingUnitIsAvailableLabel);
            add(dwellingUnitHasElevatorLabel);
            add(dwellingUnitHasHandicapAccommLabel);

            add(dwellingUnitIsAvailableCheckBox);
            add(dwellingUnitHasElevatorCheckBox);
            add(dwellingUnitHasHandicapAccommCheckBox);
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
            validateButton = new JButton("Validèr");

            nextButton.addActionListener(this);
            previousButton.addActionListener(this);
            searchButton.addActionListener(this);
            cancelButton.addActionListener(this);
            saveButton.addActionListener(this);
            closeButton.addActionListener(this);
            deleteButton.addActionListener(this);
            createNewButton.addActionListener(this);
            validateButton.addActionListener(this);

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
            add(validateButton);
            add(closeButton);
            setVisible(true);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == nextButton) {
                try {
                    if (insertMode) {
                        contract.moveToCurrentRow();
                    }
                    contract.nextPerson();
                    updateFields();
                    updateRenter();
                    updateDwellingUnit();
                } catch (SQLException sql) {
                }
            } else if (e.getSource() == previousButton) {
                try {
                    if (insertMode) {
                        contract.moveToCurrentRow();
                    }
                    contract.previousPerson();
                    updateFields();
                    updateRenter();
                    updateDwellingUnit();
                } catch (SQLException sql) {
                }
            } else if (e.getSource() == searchButton) {
                try {
                    int identity = Integer.parseInt(JOptionPane.showInputDialog("Søk på kontrakt ID"));
                    contract.findContractWithID(identity);
                    updateFields();
                } catch (SQLException s) {

                }
            } else if (e.getSource() == cancelButton) {
                try {
                    if (insertMode) {
                        contract.moveToCurrentRow();
                    }
                    contract.cancelUpdates();
                    updateFields();
                } catch (SQLException sql) {
                }
            } else if(e.getSource() == createNewButton) {
                try {
                    clearFields();
                    insertMode = true;
                    contract.moveToInsertRow();
                    updateInfotext();
                } catch (SQLException sql) {
                }
            }else if(e.getSource() == deleteButton) {
                try {
                    contract.deleteRow();
                    updateInfotext();
                    contract.acceptChanges();
                    updateFields();
                } catch (SQLException sql) {
                }
            } else if (e.getSource() == saveButton) {
                try {
                    if (insertMode) {
                        saveNewContract();
                    }
                    else {
                        updateContract();
                    }
                } catch (SQLException sql) {
                }
            } else if (e.getSource() == validateButton) {
                try {
                    contract.setContractValidation();
                    updateInfotext();
                } catch (SQLException sql) {
                }
            } else {
                contractFrame.setVisible(false);
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

    // Metode som henter alle nødvendige get-verdier fra Kontrakt-objektet og oppdaterer tilhørende datafelt i vinduet.
    private void updateFields() {
        contractLabel.setText("Kontrakt ID: " + contract.getContractID());
        String contractID = String.valueOf(contract.getContractID());
        contractIDField.setText(contractID);
        renterField.setText(contract.getRenter());
        brokerField.setText(contract.getBroker());
        /*
        inEffectDateField.setText(contract.getInEffectDate());
        expirationDateField.setText(contract.getExpirationDate());
        */
        validCheckBox.setSelected(contract.getIsValid());
        signedByRenterCheckBox.setSelected(contract.getIsSignedByRenter());
        signedByBrokerCheckBox.setSelected(contract.getIsSignedByBroker());

        infoTextLabel.setText(contract.getInfoText());
    }

    private void updateRenter() throws SQLException {
        // Henter ut informasjon om leietaker
        try {
            person.findPersonWithPersonNo(contract.getRenter());
        } catch (SQLException s) {
        }
        renterFullNameValue.setText(person.getFullName());
        renterTelephoneNoValue.setText(String.valueOf(person.getTelephoneNo()));
        renterEmailValue.setText(person.getEmail());
        renterAnnualRevenueValue.setText(String.valueOf(person.getAnnualRevenue()));
        renterHasHousepetsCheckBox.setSelected(person.getHasHousepets());
        renterIsSmokerCheckBox.setSelected(person.getIsSmoker());
        renterHasPassedCreditCheckBox.setSelected(person.getHasPassedCreditCheck());
        renterNeedsHandicapAccommCheckBox.setSelected(person.getNeedsHandicapAccommodation());
    }

    private void updateDwellingUnit() throws SQLException {
        // Henter ut informasjon om bolig
        try {
            dwellingUnit.findDwellingUnitWithID(contract.getDwellingUnitID());
        } catch (SQLException s) {
        }
        dwellingUnitStreetValue.setText(dwellingUnit.getStreet());
        dwellingUnitStreetNoValue.setText(dwellingUnit.getStreetNo());
        dwellingUnitZipCodeValue.setText(String.valueOf(dwellingUnit.getZipCode()));
        dwellingUnitTypeValue.setText(dwellingUnit.getDwellingType());
        dwellingUnitMonthlyPriceValue.setText(String.valueOf(dwellingUnit.getMonthlyPrice()));
        dwellingUnitDepositumPriceValue.setText(String.valueOf(dwellingUnit.getDepositumPrice()));
        dwellingUnitHasElevatorCheckBox.setSelected(dwellingUnit.getHasElevator());
        dwellingUnitIsAvailableCheckBox.setSelected(dwellingUnit.getIsAvailable());
        dwellingUnitHasHandicapAccommCheckBox.setSelected(dwellingUnit.getHasHandicapAccommodation());
    }

    // Metode som henter Personklassens infotekst og oppdaterer statusfeltet i bunnen av vinduet.
    private void updateInfotext() {
        infoTextLabel.setText(contract.getInfoText());
    }

    // Metode som tømmer alle datafeltene. Brukes i forbindelse med opprettelse av ny person.
    private void clearFields() {
        contractLabel.setText("Ny kontrakt");
        contractIDField.setText(null);
        renterField.setText(null);
        brokerField.setText(null);
        inEffectDateField.setText(null);
        expirationDateField.setText(null);
        validCheckBox.setSelected(false);
        signedByRenterCheckBox.setSelected(false);
        signedByBrokerCheckBox.setSelected(false);
    }

    private void saveNewContract() throws SQLException {
        try {
            // Henter ut kontrakt ID for å hente fram kontrakten etter opprettelse
            int currentContractID = Integer.parseInt(contractIDField.getText());

            // Oppdaterer feltene
            contract.updateAuto("contract_id");
            contract.updateBooleanValue("valid", validCheckBox.isSelected());
            contract.updateIntValue("dwelling_unit_id", Integer.parseInt(dwellingUnitIDField.getText()));
            contract.updateStringValue("renter", renterField.getText());
            contract.updateStringValue("broker", renterField.getText());
            contract.updateBooleanValue("signed_by_renter", signedByRenterCheckBox.isSelected());
            contract.updateBooleanValue("signed_by_broker", signedByBrokerCheckBox.isSelected());
            contract.updateStringValue("in_effect_date", inEffectDateField.getText());
            contract.updateStringValue("expiration_date", expirationDateField.getText());

            // Inserter raden
            contract.insertRow();

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateInfotext();

            // Flytter peker vekk fra innsettingsrad
            contract.moveToCurrentRow();

            // Sender endringer til databasen
            contract.acceptChanges();

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateInfotext();

            // Henter fram personen som ble opprettet
            contract.findContractWithID(currentContractID);

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateFields();
        }
        catch (SQLException e) {
            infoTextLabel.setText("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    private void updateContract() throws SQLException {
        try {
            // Flytter til nåværende rad. En forsikring i tilfelle pekeren står på en innsettingsrad.
            contract.moveToCurrentRow();

            // Oppdaterer feltene
            contract.updateAuto("contract_id");
            contract.updateBooleanValue("valid", validCheckBox.isSelected());
            contract.updateIntValue("dwelling_unit_id", Integer.parseInt(dwellingUnitIDField.getText()));
            contract.updateStringValue("renter", renterField.getText());
            contract.updateStringValue("broker", renterField.getText());
            contract.updateBooleanValue("signed_by_renter", signedByRenterCheckBox.isSelected());
            contract.updateBooleanValue("signed_by_broker", signedByBrokerCheckBox.isSelected());
            contract.updateStringValue("in_effect_date", inEffectDateField.getText());
            contract.updateStringValue("expiration_date", expirationDateField.getText());

            // Oppdaterer raden
            contract.updateRow();

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateInfotext();

            // Sender endringer til databasen
            contract.acceptChanges();

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateInfotext();

            // Henter fram de nye verdiene til personen
            contract.refreshValues();
            updateFields();
        }
        catch (SQLException e) {
            infoTextLabel.setText("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }
}
