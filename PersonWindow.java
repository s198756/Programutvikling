/**
 * Created by sebastianramsland on 05.05.14.
 */

import java.sql.SQLException;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.text.*;
import java.util.Date;
/*
public class PersonWindow extends JPanel implements PropertyChangeListener {

    // LabelStrings
    private static String firstNameString = "Fornavn";
    private static String middleNameString = "Mellomnavn";
    private static String surNameString = "Etternavn";
    private static String personNoString = "Personnummer";
    private static String streetString = "Adresse";
    private static String streetNoString = "Gatenummer";
    private static String zipCodeString = "Postnummer";
    private static String telephoneNoString = "Telefonnummer";
    private static String emailString = "Email";
    private static String annualRevenueString = "Årsinntekt";
    private static String hasPassedCreditCheckString = "Bestått kredittsjekk";
    private static String maritalStatusString = "Sivilstatus";
    private static String isSmokerString = "Røyker";
    private static String hasHousepetsString = "Husdyr";
    private static String needsHandicapAccommString = "Behov for handicaptilpasning";
    private static String createdDateString = "Opprettet";
    private static String lastModifiedDateString = "Sist endret";

    // Verdier
    private String firstName;
    private String middleName;
    private String surName;
    private String personNo;
    private String street;
    private String streetNo;
    private int zipCode;
    private long telephoneNo;
    private String email;
    private int annualRevenue;
    private boolean hasPassedCreditCheck;
    private String maritalStatus;
    private boolean isSmoker;
    private boolean hasHousepets;
    private boolean needsHandicapAccomm;
    private Timestamp createdDate;
    private Timestamp lastModifiedDate;

    // Labels
    private JLabel firstNameLabel;
    private JLabel middleNameLabel;
    private JLabel surNameLabel;
    private JLabel personNoLabel;
    private JLabel streetLabel;
    private JLabel streetNoLabel;
    private JLabel zipCodeLabel;
    private JLabel telephoneNoLabel;
    private JLabel emailLabel;
    private JLabel annualRevenueLabel;
    private JLabel hasPassedCreditCheckLabel;
    private JLabel maritalStatusLabel;
    private JLabel isSmokerLabel;
    private JLabel hasHousepetsLabel;
    private JLabel needsHandicapAccommLabel;
    private JLabel createdDateLabel;
    private JLabel lastModifiedDateLabel;

    // Tekstfelt
    private JTextField firstNameField;
    private JTextField middleNameField;
    private JTextField surNameField;
    private JFormattedTextField personNoField;
    private JTextField streetField;
    private JFormattedTextField streetNoField;
    private JFormattedTextField zipCodeField;
    private JFormattedTextField telephoneNoField;
    private JFormattedTextField emailField;
    private JFormattedTextField annualRevenueField;
    private JFormattedTextField maritalStatusField;
    private JFormattedTextField createdDateField;
    private JFormattedTextField lastModifiedDateField;
    private JCheckBox hasPassedCreditCheckBox;
    private JCheckBox isSmokerBox;
    private JCheckBox hasHousepetsBox;
    private JCheckBox needsHandicapAccommBox;

    // Tekstfelt-formateringer
    private NumberFormat telephoneNoFormat;
    private Format personNoFormat;
    private DateFormat dateFormat;

    /*
    public PersonWindow()
    {
        super(new BorderLayout());
        setUpFormats();

        String pNo = "02030350504";

            Person person = new Person(pNo);
            person.startQuery();

            firstName = person.getFirstName();
            middleName = person.getMiddleName();
            surName = person.getSurName();
            personNo = person.getPersonNo();
            street = person.getStreet();
            streetNo = person.getStreetNo();
            zipCode = person.getZipCode();
            telephoneNo = person.getTelephoneNo();
            email = person.getEmail();
            annualRevenue = person.getAnnualRevenue();
            hasPassedCreditCheck = person.getHasPassedCreditCheck();
            maritalStatus = person.getMaritalStatus();
            isSmoker = person.getIsSmoker();
            hasHousepets = person.getHasHousepets();
            needsHandicapAccomm = person.getNeedsHandicapAccommodation();
            createdDate = person.getCreatedDate();
            lastModifiedDate = person.getLastModifiedDate();


        System.out.println(createdDate);

       //Create the text fields and set them up.
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        createdDateField = new JFormattedTextField(dateFormat);
        createdDateField.addPropertyChangeListener("value", this);

        JPanel testPanel = new JPanel();


        JFrame frame = new JFrame();
        frame.getContentPane().add(testPanel, BorderLayout.CENTER);


        /*
        personNoField = new JFormattedTextField(personNoFormat);
        personNoField.setValue(new Long(personNoLong));
        personNoField.setColumns(11);
        personNoField.addPropertyChangeListener("value", this);
        */


        /*
        firstNameLabel.setLabelFor(firstNameField);
        middleNameLabel.setLabelFor(middleNameField);
        surNameLabel.setLabelFor(surNameField);
        personNoLabel.setLabelFor(personNoField);
        streetLabel.setLabelFor(streetField);
        streetNoLabel.setLabelFor(streetNoField);
        zipCodeLabel.setLabelFor(zipCodeField);
        telephoneNoLabel.setLabelFor(telephoneNoField);
        emailLabel.setLabelFor(emailField);
        annualRevenueLabel.setLabelFor(annualRevenueField);
        hasPassedCreditCheckLabel.setLabelFor(hasPassedCreditCheckBox);
        maritalStatusLabel.setLabelFor(maritalStatusField);
        isSmokerLabel.setLabelFor(isSmokerBox);
        hasHousepetsLabel.setLabelFor(hasHousepetsBox);
        needsHandicapAccommLabel.setLabelFor(needsHandicapAccommBox);
        createdDateLabel.setLabelFor(createdDateField);
        lastModifiedDateLabel.setLabelFor(lastModifiedDateField);
        */




        //Lay out the labels in a panel.
        /*
        JPanel labelPane = new JPanel(new GridLayout(0,1));
        /*
        //labelPane.add(personNoLabel);
        labelPane.add(firstNameLabel);
        labelPane.add(middleNameLabel);
        labelPane.add(surNameLabel);
        labelPane.add(personNoLabel);
        labelPane.add(streetLabel);
        labelPane.add(streetNoLabel);
        labelPane.add(zipCodeLabel);
        labelPane.add(telephoneNoLabel);
        labelPane.add(emailLabel);
        labelPane.add(annualRevenueLabel);
        labelPane.add(hasPassedCreditCheckLabel);
        labelPane.add(maritalStatusLabel);
        labelPane.add(isSmokerLabel);
        labelPane.add(hasHousepetsLabel);
        labelPane.add(needsHandicapAccommLabel);
        labelPane.add(createdLabel);
        labelPane.add(lastModifiedLabel);
        */

        //Layout the text fields in a panel.
        /*
        JPanel fieldPane = new JPanel(new GridLayout(0,1));
        /*
        fieldPane.add(personNoField);
        fieldPane.add(firstNameField);
        fieldPane.add(middleNameField);
        fieldPane.add(surNameField);
        fieldPane.add(personNoField);
        fieldPane.add(streetField);
        fieldPane.add(streetNoField);
        fieldPane.add(zipCodeField);
        fieldPane.add(telephoneNoField);
        fieldPane.add(emailField);
        fieldPane.add(annualRevenueField);
        fieldPane.add(hasPassedCreditCheckBox);
        fieldPane.add(maritalStatusField);
        fieldPane.add(isSmokerBox);
        fieldPane.add(hasHousepetsBox);
        fieldPane.add(needsHandicapAccommBox);
        fieldPane.add(createdField);
        fieldPane.add(lastModifiedField);
        */


        //Put the panels in this panel, labels on left,
        //text fields on right.
        /*
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(labelPane, BorderLayout.CENTER);
        add(fieldPane, BorderLayout.LINE_END);
        */
/*
    }

    /** Called when a field's "value" property changes. */
    /*
    public void propertyChange(PropertyChangeEvent e) {
        Object source = e.getSource();
        /*
        if (source == personNoField) {

        } else if (source == firstNameField) {
            System.out.println(source + " = " + firstName);
        } else if (source == telephoneNoField) {
            System.out.println(source + " = " + telephoneNo);
        }
        else
            System.out.println(source + " = else");

    }

    //Create and set up number formats. These objects also
    //parse numbers input by user.
/*
    private void setUpFormats() {

    }

}
    */