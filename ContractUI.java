package GUI.Files;

/**
 * Created by Sebastian Ramsland on 13.05.2014.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Date;
import java.sql.Timestamp;
import java.text.*;

public class ContractUI extends JPanel {

    private static final String COULD_NOT_FIND_CONTRACT_MSG = "Kunne ikke finne kontrakten med ID: ";
    private static final String COULD_NOT_FIND_PERSON_MSG = "Kunne ikke finne personen med det angitte personnummeret";
    private static final String COULD_NOT_FIND_DWELLING_UNIT_MSG = "Kunne ikke finne bolig med den angitte IDen";
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
    JButton createNewButton;
    JButton validateButton;
    JButton bringRenterButton;
    JButton bringDwellingUnitButton;
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

    // Oppretter kontrakt, bolig og personliste.
    Contract contract;
    Person person;
    DwellingUnit dwellingUnit;

    // Angir om Rowset er i en innsettingsfase av ny rad.
    boolean insertMode;

    // Konstruktør som oppretter lister over alle kontrakter, personer og boliger. Viser kontrakten som er øverst på lista.
    public ContractUI() throws SQLException {

        // Oppretter lister
        contract = new Contract();
        person = new Person();
        dwellingUnit = new DwellingUnit();
        MainPanel main = new MainPanel();
        main.setPreferredSize(new Dimension(1280, 720));
        add(main);

        try {
            // Henter verdier
            contract.refreshValues();
        } catch (SQLException e) {
        }

        // Oppdaterer alle fields/verdier
        updateContractFields();
        updateDwellingUnitFields();
        updateRenterFields();
    }

    // Konstruktør som tar imot allerede opprettede lister.
    public ContractUI(Contract c, Person p, DwellingUnit d) throws SQLException {
        //
        contract = c;
        person = p;
        dwellingUnit = d;
        MainPanel main = new MainPanel();
        main.setPreferredSize(new Dimension(1280, 720));
        add(main);

        try {
            // Henter verdier
            contract.refreshValues();
        } catch (SQLException e) {
        }

        // Oppdaterer alle fields/verdier
        updateContractFields();
        updateDwellingUnitFields();
        updateRenterFields();
    }

    // Konstruktør som oppretter lister over alle kontrakter, personer og boliger.
    // Tar imot èn kontrakt ID og viser denne kontrakten først i vinduet.
    public ContractUI(int cID) throws SQLException {

        // Oppretter lister over alle kontrakter, personer og boliger
        contract = new Contract();
        person = new Person();
        dwellingUnit = new DwellingUnit();
        MainPanel main = new MainPanel();
        main.setPreferredSize(new Dimension(1280, 720));
        add(main);

        try {
            contract.findContractWithID(cID);
        } catch (SQLException e) {
        }

        // Oppdaterer alle fields/verdier
        updateContractFields();
        updateDwellingUnitFields();
        updateRenterFields();
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
            GridLayout headerPanelLayout = new GridLayout(1, 2);
            setLayout(headerPanelLayout);

            // Innhold i HeaderPanel:
            contractLabel = new JLabel();
            contractLabel.setFont(new Font("Serif", Font.PLAIN, 25));
            validLabel = new JLabel();

            // Layout
            setLayout(new FlowLayout(FlowLayout.LEFT));
            add(contractLabel);
            add(validLabel);
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
            add(new RelationPanel());
            add(new DetailsPanel());

            GridLayout dataFieldPanelLayout = new GridLayout(2, 1);

            setLayout(dataFieldPanelLayout);
            setPreferredSize(new Dimension(900, 300));


            setVisible(true);
        }
    }

    // Sidepanel som samler sammen panelene ContractScrollPanel, DwellingUnitScrollPanel og ControlPanel.
    private class SidePanel extends JPanel {
        public SidePanel() {
            setPreferredSize(new Dimension(300, 600));

            GridLayout sidepanelLayout = new GridLayout(3, 1);
            setLayout(sidepanelLayout);

            add(new DwellingUnitScrollPanel());
            add(new RenterScrollPanel());
            add(new ControlPanel());
        }
    }

    // Underpanel til DataFieldPanel. Viser relasjonsinformasjon.
    private class RelationPanel extends JPanel implements ActionListener {
        public RelationPanel() {
            // Ramme
            setBorder(BorderFactory.createTitledBorder("Relasjoner"));
            setPreferredSize(new Dimension(900, 300));
            GridLayout personaliaPanelLayout = new GridLayout(4, 3);
            personaliaPanelLayout.setHgap(70);
            personaliaPanelLayout.setVgap(10);
            setLayout(personaliaPanelLayout);

            // Relasjonsinnhold i kontrakten (leietaker, bolig, megler)
            contractIDLabel = new JLabel("Kontrakt ID (automatisk tildelt)");
            dwellingUnitIDLabel = new JLabel("Bolig ID *");
            renterLabel = new JLabel("Personnummer Leietaker *");
            brokerLabel = new JLabel("Personnummer Megler *");
            contractIDField = new JTextField(5);
            dwellingUnitIDField = new JTextField(5);
            renterField = new JTextField(11);
            brokerField = new JTextField(11);
            bringRenterButton = new JButton("Vis Person >>");
            bringDwellingUnitButton = new JButton("Vis Bolig >>");

            contractIDField.setEditable(false);

            add(contractIDLabel);
            add(dwellingUnitIDLabel);
            add(new JLabel("")); // Tom celle

            add(contractIDField);
            add(dwellingUnitIDField);
            add(bringDwellingUnitButton);

            add(brokerLabel);
            add(renterLabel);
            add(new JLabel("")); // Tom celle

            add(brokerField);
            add(renterField);
            add(bringRenterButton);

            bringRenterButton.addActionListener(this);
            bringDwellingUnitButton.addActionListener(this);

        }


        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == bringRenterButton) {
                try {
                    if(!person.findPersonWithPersonNo(renterField.getText()))
                        JOptionPane.showMessageDialog(null, COULD_NOT_FIND_PERSON_MSG);
                    else
                        updateRenterFields();
                } catch (SQLException sql) {
                }
            } else {
                try {
                    if(!dwellingUnit.findDwellingUnitWithID(Integer.parseInt(dwellingUnitIDField.getText())))
                        JOptionPane.showMessageDialog(null, COULD_NOT_FIND_DWELLING_UNIT_MSG);
                    else
                        updateDwellingUnitFields();
                } catch (SQLException sql){
                }
            }
        }
    }

    // Underpanel til DataFieldPanel. Viser kontaktdetaljer.
    private class DetailsPanel extends JPanel {
        public DetailsPanel() {
            // Ramme
            setBorder(BorderFactory.createTitledBorder("Detaljer"));
            setPreferredSize(new Dimension(900, 500));
            GridLayout contactPanelLayout = new GridLayout(4, 3);
            contactPanelLayout.setHgap(50);
            setLayout(contactPanelLayout);

            // Innhold i Kontakt (telefon, email, adresse)
            inEffectDateLabel = new JLabel("Start (åå-mm-dd) *");
            expirationDateLabel = new JLabel("Slutt (åå-mm-dd) *");
            signedByRenterLabel = new JLabel("Signert av leietaker");
            signedByBrokerLabel = new JLabel("Signert av megler");
            paidDepositumLabel = new JLabel ("Depositum innbetalt");
            inEffectDateField = new JTextField();
            expirationDateField = new JTextField();
            signedByRenterCheckBox = new JCheckBox();
            signedByBrokerCheckBox = new JCheckBox();
            paidDepositumCheckBox = new JCheckBox();

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

    // Underpanel til sidepanelet. Viser informasjon om kontraktens leietaker.
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

    // Underpanel til sidepanelet. Viser informasjon om kontraktens bolig.
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

    // Kontrollpanel. Tar hånd om alle knappene. Navigering, lagring, ny kontrakt etc.
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
            searchButton = new JButton("Søk");
            createNewButton = new JButton("Ny");
            validateButton = new JButton("Validèr");

            nextButton.addActionListener(this);
            previousButton.addActionListener(this);
            searchButton.addActionListener(this);
            cancelButton.addActionListener(this);
            saveButton.addActionListener(this);
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
            add(cancelButton);
            add(validateButton);
            setVisible(true);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == nextButton) {
                try {
                    if (insertMode) {
                        contract.moveToCurrentRow();
                    }
                    contract.nextPerson();
                    updateContractFields();
                    updateInfotext();
                    updateRenterFields();
                    updateDwellingUnitFields();
                    System.out.println("Etter next: " + contract.getCurrentRowNumber());
                } catch (SQLException sql) {
                }
            } else if (e.getSource() == previousButton) {
                try {
                    if (insertMode) {
                        contract.moveToCurrentRow();
                    }
                    contract.previousPerson();
                    updateContractFields();
                    updateRenterFields();
                    updateDwellingUnitFields();
                    System.out.println("Etter previous: " + contract.getCurrentRowNumber());
                } catch (SQLException sql) {
                }
            } else if (e.getSource() == searchButton) {
                try {
                    int input = Integer.parseInt(JOptionPane.showInputDialog("Søk på kontrakt ID"));
                    if (!contract.findContractWithID(input)) {
                        JOptionPane.showMessageDialog(null, COULD_NOT_FIND_CONTRACT_MSG + input);
                    }
                    updateContractFields();
                    System.out.println("Etter søk: " + contract.getCurrentRowNumber());
                } catch (SQLException s) {

                }
            } else if (e.getSource() == cancelButton) {
                try {
                    if (insertMode) {
                        contract.moveToCurrentRow();
                    }
                    contract.cancelUpdates();
                    updateContractFields();
                    updateRenterFields();
                    updateDwellingUnitFields();
                    updateInfotext();
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
            } else if (e.getSource() == saveButton) {
                try {
                    if (insertMode) {
                        saveNewContract();
                    }
                    else {
                        changeContract();
                    }
                } catch (SQLException sql) {
                }
            } else if (e.getSource() == validateButton) {
                try {
                    setCorrectContractValidation();
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

    // Metode som henter alle nødvendige get-verdier fra en kontrakt og oppdaterer tilhørende datafelt i vinduet.
    private void updateContractFields() {
        if (contract.getIsValid()) {
            validLabel.setText("GYLDIG");
            validLabel.setForeground(Color.GREEN);
        }
        else {
            validLabel.setText("UGYLDIG");
            validLabel.setForeground(Color.RED);
        }
        contractLabel.setText("Kontrakt ID: " + contract.getContractID());
        String contractID = String.valueOf(contract.getContractID());
        contractIDField.setText(contractID);
        String dwellingUnitID = String.valueOf(contract.getDwellingUnitID());
        dwellingUnitIDField.setText(dwellingUnitID);
        renterField.setText(contract.getRenter());
        brokerField.setText(contract.getBroker());

        // Oppretter datoformat
/*
        Format date = new SimpleDateFormat("yyyy-MM-dd");

        String inEffectDate = date.format(contract.getInEffectDate());
        String expirationDate = date.format(contract.getExpirationDate());

        inEffectDateField.setText(inEffectDate);
        expirationDateField.setText(expirationDate);

        signedByRenterCheckBox.setSelected(contract.getIsSignedByRenter());
        signedByBrokerCheckBox.setSelected(contract.getIsSignedByBroker());
        */
    }

    private void updateRenterFields() throws SQLException {
        // Henter ut informasjon om leietaker
        try {
            person.findPersonWithPersonNo(renterField.getText());
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

    private void updateDwellingUnitFields() throws SQLException {
        // Henter ut informasjon om bolig
        try {
            dwellingUnit.findDwellingUnitWithID(Integer.parseInt(dwellingUnitIDField.getText()));
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

        // Tømmer Kontraktfeltene
        contractLabel.setText("Ny leiekontrakt");
        contractIDField.setText(null);
        dwellingUnitIDField.setText(null);
        renterField.setText(null);
        brokerField.setText(null);
        inEffectDateField.setText(null);
        expirationDateField.setText(null);
        signedByRenterCheckBox.setSelected(false);
        signedByBrokerCheckBox.setSelected(false);

        // Tømmer Leietakerfeltene
        renterFullNameValue.setText(null);
        renterTelephoneNoValue.setText(null);
        renterEmailValue.setText(null);
        renterAnnualRevenueValue.setText(null);
        renterHasHousepetsCheckBox.setSelected(false);
        renterIsSmokerCheckBox.setSelected(false);
        renterHasPassedCreditCheckBox.setSelected(false);
        renterNeedsHandicapAccommCheckBox.setSelected(false);

        //Tømmer Boligfeltene
        dwellingUnitStreetValue.setText(null);
        dwellingUnitStreetNoValue.setText(null);
        dwellingUnitZipCodeValue.setText(null);
        dwellingUnitTypeValue.setText(null);
        dwellingUnitMonthlyPriceValue.setText(null);
        dwellingUnitDepositumPriceValue.setText(null);
        dwellingUnitHasElevatorCheckBox.setSelected(false);
        dwellingUnitIsAvailableCheckBox.setSelected(false);
        dwellingUnitHasHandicapAccommCheckBox.setSelected(false);
    }

    private void saveNewContract() throws SQLException {
        try {
            // Oppdaterer feltene
            contract.updateAuto("contract_id");
            contract.updateBooleanValue("valid", false);
            int dwellingUnitID = Integer.parseInt(dwellingUnitIDField.getText());
            contract.updateIntValue("dwelling_unit_id", dwellingUnitID);
            contract.updateStringValue("renter", renterField.getText());
            contract.updateStringValue("broker", renterField.getText());
            contract.updateBooleanValue("paid_depositum", false);
            contract.updateBooleanValue("signed_by_renter", signedByRenterCheckBox.isSelected());
            contract.updateBooleanValue("signed_by_broker", signedByBrokerCheckBox.isSelected());

            Timestamp inEffectDate = Timestamp.valueOf(inEffectDateField.getText() + " 00:00:00");
            Timestamp expirationDate = Timestamp.valueOf(expirationDateField.getText() + " 00:00:00");

            contract.updateTimestampValue("in_effect_date", inEffectDate);
            contract.updateTimestampValue("expiration_date", expirationDate);

            // Inserter raden
            contract.insertRow();

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateInfotext();

            // Flytter peker vekk fra innsettingsrad
            contract.moveToCurrentRow();

            // Sender endringer til databasen
            if (!contract.acceptChanges()) {
                JOptionPane.showMessageDialog(null, "Kunne ikke sende endringer til databasen");
            }

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateInfotext();

            // Henter fram nederste person
            contract.last();
            contract.refreshValues();

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateContractFields();
        }
        catch (SQLException e) {
            infoTextLabel.setText("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    private void changeContract() throws SQLException {
        try {
            // Flytter til nåværende rad. En forsikring i tilfelle pekeren står på en innsettingsrad.
            contract.moveToCurrentRow();

            // Oppdaterer feltene
            int dwellingUnitID = Integer.parseInt(dwellingUnitIDField.getText());
            contract.updateIntValue("dwelling_unit_id", dwellingUnitID);
            contract.updateStringValue("renter", renterField.getText());
            contract.updateStringValue("broker", brokerField.getText());
            contract.updateBooleanValue("signed_by_renter", signedByRenterCheckBox.isSelected());
            contract.updateBooleanValue("signed_by_broker", signedByBrokerCheckBox.isSelected());

            Timestamp inEffectDate = Timestamp.valueOf(inEffectDateField.getText() + " 00:00:00");
            Timestamp expirationDate = Timestamp.valueOf(expirationDateField.getText() + " 00:00:00");

            contract.updateTimestampValue("in_effect_date", inEffectDate);
            contract.updateTimestampValue("expiration_date", expirationDate);

            // Oppdaterer raden
            contract.updateRow();
            System.out.println("Etter updaterow: " + contract.getCurrentRowNumber());

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateInfotext();

            // Sender endringer til databasen
            if (!contract.acceptChanges()) {
                JOptionPane.showMessageDialog(null, "Kunne ikke sende endringer til databasen");
            }

            // Oppdaterer infotekst for å vise eventuelle statusmeldinger
            updateInfotext();

            // Finner tilbake til kontrakten som ble lagret
            if (!contract.findContractWithID(Integer.parseInt(contractIDField.getText()))) {
                JOptionPane.showMessageDialog(null, COULD_NOT_FIND_CONTRACT_MSG + contractIDField.getText());
            }

            System.out.println("Etter findContractWithID: " + contract.getCurrentRowNumber());

            // Henter fram de nye verdiene til personen
            contract.refreshValues();
            updateContractFields();
        }
        catch (SQLException e) {
            infoTextLabel.setText("Error code: " + e.getErrorCode() + "\tLocalizedMessage: " + e.getLocalizedMessage());
        }
    }

    // Returnerer "true" dersom kravene til å oppnå en gyldig kontrakt oppfylles.
    public boolean checkValidation() throws SQLException {

        // Henter ut nødvendig informasjon om kontraktens megler
        person.findPersonWithPersonNo(contract.getBroker());
        boolean brokerIsBroker = person.getIsBroker();

        // Henter ut nødvendig informasjon om kontraktens leietaker
        person.findPersonWithPersonNo(contract.getRenter());

        // Oppretter nåværende dato
        Date today = new java.util.Date();
        Timestamp now = new Timestamp(today.getTime());

        String validationErrorMessage;
        String contractValid = "Kontrakten utfyller alle krav for å bli gyldig.";

        if (!brokerIsBroker) {
            validationErrorMessage = "Angitt megler i kontrakten er ingen megler.";
            JOptionPane.showMessageDialog(null, validationErrorMessage, "Valideringsfeil", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        else if (contract.getRenter().equals(dwellingUnit.getPropertyOwner())) {
            validationErrorMessage = "Leietakeren kan ikke leie sin egen bolig.";
            JOptionPane.showMessageDialog(null, validationErrorMessage, "Valideringsfeil", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        else if (contract.getRenter().equals(contract.getBroker())) {
            validationErrorMessage = "Leietakeren kan ikke være samme person som megleren.";
            JOptionPane.showMessageDialog(null, validationErrorMessage, "Valideringsfeil", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        else if (!contract.getIsSignedByRenter()) {
            validationErrorMessage = "Kontrakten er ikke signert av leietaker.";
            JOptionPane.showMessageDialog(null, validationErrorMessage, "Valideringsfeil", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        else if (!contract.getIsSignedByBroker()) {
            validationErrorMessage = "Kontrakten er ikke signert av megler.";
            JOptionPane.showMessageDialog(null, validationErrorMessage, "Valideringsfeil", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        else if (!now.after(contract.getInEffectDate()) || !now.before(contract.getExpirationDate())) {
            validationErrorMessage = "Dagens dato er ikke innenfor kontraktperioden.";
            JOptionPane.showMessageDialog(null, validationErrorMessage, "Valideringsfeil", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        else {
            infoTextLabel.setText(contractValid);
            return true;
        }
    }

    // Oppdaterer feltet "valid" i kontrakten til korrekt status.
    public boolean setCorrectContractValidation() throws SQLException {
        boolean contractCanBeSetValid = checkValidation();

        if (contractCanBeSetValid) {
            try {
                // Oppdaterer felt
                contract.moveToCurrentRow();
                contract.updateBooleanValue("valid", true);
                contract.updateRow();

                // Sender oppdatering til databasen
                contract.acceptChanges();
                infoTextLabel.setText("Kontrakten har nå status: Gyldig.");

                // Setter pekeren tilbake til kontrakten
                if(!contract.findContractWithID(Integer.parseInt(contractIDField.getText()))) {
                    JOptionPane.showMessageDialog(null, COULD_NOT_FIND_CONTRACT_MSG + contractIDField.getText());
                    return false;
                }

                updateContractFields();

                // Oppdaterer boligens status til "Opptatt";
                setDwellingUnitAvailability();
                return true;

            } catch (SQLException s) {
                infoTextLabel.setText("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
                return false;
            }
        }

        else
        {
            try {
                // Oppdaterer felt
                contract.moveToCurrentRow();
                contract.updateBooleanValue("valid", false);
                contract.updateRow();

                // Sender oppdatering til databasen
                contract.acceptChanges();
                infoTextLabel.setText("Kontrakten har nå status: Ugyldig.");

                // Setter pekeren tilbake til kontrakten
                if(!contract.findContractWithID(Integer.parseInt(contractIDField.getText()))) {
                    JOptionPane.showMessageDialog(null, COULD_NOT_FIND_CONTRACT_MSG + contractIDField.getText());
                    return false;
                }

                updateContractFields();
                return true;

            } catch (SQLException s) {
                infoTextLabel.setText("Error code: " + s.getErrorCode() + "\tLocalizedMessage: " + s.getLocalizedMessage());
                return false;
            }
        }
    }

    // Oppdaterer kontraktens boligfelt "available" til korrekt status
    public boolean setDwellingUnitAvailability() throws SQLException {

        // Finner fram til boligen som er tilknyttet kontrakten
        if (!dwellingUnit.findDwellingUnitWithID(contract.getDwellingUnitID())) {
            JOptionPane.showMessageDialog(null, COULD_NOT_FIND_DWELLING_UNIT_MSG);
            return false;
        }
        else {
            // Sjekker om kontrakten er gyldig
            if (contract.getIsValid()) {
                // Oppdaterer boligens "available"-felt til false (opptatt)
                dwellingUnit.moveToCurrentRow();
                dwellingUnit.updateBooleanValue("available", false);
                dwellingUnit.updateRow();
                dwellingUnit.acceptChanges();

                // Setter peker tilbake til boligen
                if (!dwellingUnit.findDwellingUnitWithID(contract.getDwellingUnitID())) {
                    JOptionPane.showMessageDialog(null, COULD_NOT_FIND_DWELLING_UNIT_MSG);
                    return false;
                }
            }
            updateDwellingUnitFields();
            return true;
        }
    }
}
