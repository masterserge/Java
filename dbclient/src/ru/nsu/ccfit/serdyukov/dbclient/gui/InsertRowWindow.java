package ru.nsu.ccfit.serdyukov.dbclient.gui;

import ru.nsu.ccfit.serdyukov.dbclient.res.Strings;
import ru.nsu.ccfit.serdyukov.dbclient.sql.QueryBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InsertRowWindow extends JDialog {

    private MainWindow mainWindow;
    private ArrayList<JTextField> fieldValues;

    InsertRowWindow(MainWindow mainWindow) {
        super();
        this.mainWindow = mainWindow;
        constructInsertRowFrame();
    }

    private void constructInsertRowFrame() {
        //Two columns and so many rows as necessary
        setLayout(new GridLayout(0, 2));

        int selectedIndex = mainWindow.getIndexOfSelectedTable();
        if (selectedIndex == -1) {
            return;
        }

        constructFieldValues();

        JButton insertButton = new JButton(Strings.INSERT_ROW);
        insertButton.addActionListener(new InsertListener());
        add(insertButton);
    }

    private void constructFieldValues() {

        int columnCount = mainWindow.getColumnCountInTableOfResults();

        fieldValues = new ArrayList<JTextField>
                (columnCount);
        String columnName;
        for (int i = 0; i < columnCount; ++i) {

            columnName = mainWindow.tableOfResultsModel.getColumnName(i);
            add(new JLabel(columnName));
            JTextField value = new JTextField();
            fieldValues.add(value);
            add(value);
        }
    }


    private class InsertListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                ResultSet rs = QueryBuilder.insertRowAndReturnUpdatedTable(
                        mainWindow.connection,
                        mainWindow.getNameOfSelectedTable(),
                        mainWindow.tableOfResultsModel,
                        fieldValues);
                mainWindow.showQueryResult(rs);
                mainWindow.errorLabel.setText("");
                dispose();
            } catch (SQLException e1) {
                mainWindow.errorLabel.setText(e1.getMessage());
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}