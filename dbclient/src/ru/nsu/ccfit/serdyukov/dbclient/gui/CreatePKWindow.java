package ru.nsu.ccfit.serdyukov.dbclient.gui;

import ru.nsu.ccfit.serdyukov.dbclient.sql.QueryBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreatePKWindow extends JDialog {

    private MainWindow mainWindow;

    private JPanel columnPanel;

    private CreateActionListener cal = new CreateActionListener();

    private String tableName;
    private static final int MAX_COLUMN_COUNT = 3;
    private static final String UNSELECTED_COLUMN = "-Select column-";

    CreatePKWindow(MainWindow mainWindow) {
        super();
        this.mainWindow = mainWindow;
        tableName = mainWindow.getNameOfSelectedTable();
        constructCreatePKFrame();
    }

    private void constructCreatePKFrame() {

        Container pane = getContentPane();

        columnPanel = new JPanel(new GridLayout(0, 1));
        
        String[] columns = null;
        try {
            columns = QueryBuilder.selectColumnsFromTable(mainWindow.connection, tableName);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        
        for (int i = 0; i < MAX_COLUMN_COUNT; ++i) {
            JComboBox comboBox = new JComboBox(columns);
            comboBox.insertItemAt(UNSELECTED_COLUMN, 0);
            comboBox.setSelectedIndex(0);
            columnPanel.add(comboBox);
        }

        JTextField conName = new JTextField("Con_Name");
        columnPanel.add(conName);

        JButton createButton = new JButton("Create");
        createButton.setActionCommand("create");
        createButton.addActionListener(cal);
        columnPanel.add(createButton);

        pane.add(columnPanel, BorderLayout.CENTER);
    }

    private class CreateActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {


            String columns[] = parseColumns();
            String conName = getConstraintName();

            try {
                ResultSet rs = QueryBuilder.addPKAndReturnUpdatedTable(
                        mainWindow.connection,
                        mainWindow.getNameOfSelectedTable(),
                        mainWindow.tableOfResultsModel,
                        columns, conName);
                mainWindow.showQueryResult(rs);
                mainWindow.errorLabel.setText("");
                dispose();
            } catch (SQLException e1) {
                mainWindow.errorLabel.setText(e1.getMessage());
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private String getConstraintName() {
        return ((JTextField)columnPanel.getComponent(MAX_COLUMN_COUNT)).getText();
    }

    private String[] parseColumns() {

        int columnCount = 0;

        for (int i = 0; i < MAX_COLUMN_COUNT; ++i) {
            JComboBox columnBox = (JComboBox)columnPanel.getComponent(i);
            String columnName = (String) columnBox.getSelectedItem();
            if (!UNSELECTED_COLUMN.equals(columnName)) {
                columnCount++;
            }
        }

        System.out.println(columnCount);
        String columns[] = new String[columnCount];

        int columnIndex = 0;

        for (int i = 0; i < MAX_COLUMN_COUNT; ++i) {
            JComboBox columnBox = (JComboBox)columnPanel.getComponent(i);
            String columnName = (String) columnBox.getSelectedItem();
            if (!UNSELECTED_COLUMN.equals(columnName)) {
                columns[columnIndex++] = columnName;
            }
        }

        return columns;
    }
}