/**
 * Created by Sebastian Ramsland on 11.05.2014.
 */

import javax.swing.*;
import java.awt.*;

public class PersonUI {
    JFrame personFrame;
    JLabel fullNameLabel;
    JTable contractsTable;
    JTable dwellingUnitsTable;
    Button nextButton;
    JLabel firstNameLabel;
    JLabel middleNameLabel;
    JLabel surNameLabel;
    JLabel personNoLabel;
    JLabel maritalStatusLabel;
    JLabel brokerLabel;
    JLabel telephoneLabel;
    JTextField personNoField;
    JTextField firstNameField;
    JTextField middleNameField;
    JTextField surNameField;
    JComboBox maritalStatusComboBox;
    JCheckBox brokerCheckBox;

    public static void main(String[] args) {
        new PersonUI();
    }

    public PersonUI() {
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
                personFrame.setLayout(new BorderLayout());
                personFrame.add(new MainPanel());
                personFrame.setPreferredSize(new Dimension(1280, 720));
                personFrame.pack();

                // Midtstiller vinduet
                personFrame.setLocationRelativeTo(null);

                personFrame.setVisible(true);
            }
        });
    }

    private class MainPanel extends JPanel {
        public MainPanel() {

            setPreferredSize(new Dimension(1280, 720));
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
            c.gridheight = 3;
            c.gridx = 0;
            c.gridy = 1;
            add(new DataFieldScrollPanel(), c);

            c.ipady = 0;
            c.weightx = 0.0;
            c.gridwidth = 1;
            c.gridx = 1;
            c.gridy = 1;
            add(new ContractScrollPanel(), c);

            c.ipady = 20;
            c.weightx = 0.0;
            c.gridwidth = 1;
            c.gridx = 1;
            c.gridy = 2;
            add(new DwellingUnitScrollPanel(), c);

            c.ipady = 0;
            c.weightx = 0.0;
            c.gridwidth = 1;
            c.gridx = 1;
            c.gridy = 3;
            add(new ControlPanel(), c);
        }
    }

    private class HeaderPanel extends JPanel {
        public HeaderPanel() {
            // Headerpanel som viser fullt navn

            setPreferredSize(new Dimension(1280, 20));

            // Innhold i HeaderPanel:
            fullNameLabel = new JLabel("NAVN NAVNESEN");

            // Layout
            setLayout(new FlowLayout(FlowLayout.LEFT));
            add(fullNameLabel);
            setVisible(true);
        }
    }

    private class DataFieldScrollPanel extends JScrollPane {
        DataFieldScrollPanel() {
            // Scrollpanel container for personfelt
            setPreferredSize(new Dimension(900, 600));

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

    private class DataFieldPanel extends JPanel {
        public DataFieldPanel() {
            // Hovedpanel for datafeltene
            add(new PersonaliaPanel());
            add(new ContactPanel());
            add(new MiscPanel());

            setVisible(true);
        }
    }

    private class PersonaliaPanel extends JPanel {
        public PersonaliaPanel() {
            // Ramme
            setBorder(BorderFactory.createTitledBorder("Personalia"));
            setPreferredSize(new Dimension(900, 300));

            // Innhold i Personalia (personnummer, navn, sivilstatus, megler)
            firstNameLabel = new JLabel("Fornavn");
            middleNameLabel = new JLabel("Mellomnavn");
            surNameLabel = new JLabel("Etternavn");
            personNoLabel = new JLabel("Personnummer");
            maritalStatusLabel = new JLabel("Sivilstatus");
            brokerLabel = new JLabel("Megler");
            personNoField = new JTextField();
            firstNameField = new JTextField();
            middleNameField = new JTextField();
            surNameField = new JTextField();
            maritalStatusComboBox = new JComboBox();
            maritalStatusComboBox.setModel(new DefaultComboBoxModel(new String[] { "Alene", "Samboer", "Gift", "Skilt", "Enke" }));
            brokerCheckBox = new JCheckBox();

            setVisible(true);
        }
    }

    private class ContactPanel extends JPanel {
        public ContactPanel() {
            // Ramme
            setBorder(BorderFactory.createTitledBorder("Kontakt"));
            setPreferredSize(new Dimension(900, 300));

            // Innhold i Kontakt (telefon, email, adresse)
            telephoneLabel = new JLabel("Telefonnummer");
        }
    }

    private class MiscPanel extends JPanel {
        public MiscPanel () {
            // Ramme
            setBorder(BorderFactory.createTitledBorder("Diverse"));
            setPreferredSize(new Dimension(900, 300));

            setVisible(true);
            // Innhold i Diverse (husdyr, røyker, årsinntekt osv.)
        }
    }

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

    private class DwellingUnitScrollPanel extends JScrollPane {
        public DwellingUnitScrollPanel() {
            // Ramme
            setBorder(BorderFactory.createTitledBorder("Utleide boliger"));
            setPreferredSize(new Dimension(300, 200));


            // Innhold i DwellingUnitsPanel
            dwellingUnitsTable = new JTable();

            // Viewport
            setViewportView(dwellingUnitsTable);

            setVisible(true);
        }
    }

    private class ControlPanel extends JPanel {
        public ControlPanel() {
            // Ramme
            setBorder(BorderFactory.createTitledBorder("Kontrollpanel"));
            setPreferredSize(new Dimension(300, 200));

            // Innhold i Kontrollpanel
            nextButton = new Button();

            // Layout: Kontrollpanel
            GridLayout controlLayout = new GridLayout();
            setLayout(controlLayout);

            setVisible(true);
        }
    }

}
