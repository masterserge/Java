package ru.nsu.ccfit.serdyukov.dbclient.gui;

import ru.nsu.ccfit.serdyukov.dbclient.res.Strings;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainWindowConstructor {
    public static void constructMainWindow(MainWindow mainWindow) {

        addMenuBar(mainWindow);
        addComponents(mainWindow);
    }

    private static void addMenuBar(MainWindow mainWindow) {
        SqlClientMenuBar menuBar = new SqlClientMenuBar(mainWindow.menuBarListener);
        mainWindow.menuBar = menuBar;
        mainWindow.setJMenuBar(menuBar);
    }

    private static void addComponents(MainWindow mainWindow) {
        Container pane = mainWindow.getContentPane();
        pane.setLayout(new GridBagLayout());

        addLogonStatusLabel(mainWindow);
        addListOfTables(mainWindow);
        addResultTable(mainWindow);
        addErrorLabel(mainWindow);
    }

    private static void addLogonStatusLabel(MainWindow mainWindow) {
        Container pane = mainWindow.getContentPane();
        GridBagConstraints c = new GridBagConstraints();

        mainWindow.logonStatusLabel = new JLabel(Strings.NOT_LOGED_ON, JLabel.CENTER);

        c.weightx = 1.0;
        c.weighty = 0.15;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;

        //pane.add(mainWindow.logonStatusLabel, c);
    }

    private static void addListOfTables(MainWindow mainWindow) {
        Container pane = mainWindow.getContentPane();
        GridBagConstraints c = new GridBagConstraints();

        mainWindow.listOfTablesModel = new DefaultListModel();

        mainWindow.listOfTables = new JList();
        mainWindow.listOfTables.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainWindow.listOfTables.addListSelectionListener(mainWindow.tableSelectionListener);
        mainWindow.listOfTables.setModel(mainWindow.listOfTablesModel);
        mainWindow.listOfTables.setLayoutOrientation(JList.VERTICAL);

        c.weightx = 0.25;
        c.weighty = 0.85;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;

        JScrollPane listPane = new JScrollPane(mainWindow.listOfTables);
        pane.add(listPane, c);
    }

    private static void addResultTable(MainWindow mainWindow) {
        Container pane = mainWindow.getContentPane();
        GridBagConstraints c = new GridBagConstraints();

        mainWindow.tableOfResultsModel = new DefaultTableModel();

        mainWindow.tableOfResults = new JTable(mainWindow.tableOfResultsModel);
        mainWindow.tableOfResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        c.weightx = 0.75;
        c.weighty = 0.75;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;

        JScrollPane scrollPane = new JScrollPane(mainWindow.tableOfResults);
        pane.add(scrollPane, c);
    }

    private static void addErrorLabel(MainWindow mainWindow) {
        Container pane = mainWindow.getContentPane();
        GridBagConstraints c = new GridBagConstraints();

        mainWindow.errorLabel = new JLabel("", JLabel.CENTER);

        c.weightx = 0.75;
        c.weighty = 0.10;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 2;

        pane.add(mainWindow.errorLabel, c);
    }
}
