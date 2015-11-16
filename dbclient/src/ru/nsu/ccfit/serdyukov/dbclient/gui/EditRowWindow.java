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

public class EditRowWindow extends JDialog {

    private MainWindow mainWindow;
    private ArrayList<JTextField> fieldValues;

    EditRowWindow(MainWindow mainWindow) {
        super();
        this.mainWindow = mainWindow;
        constructEditRowFrame();
    }

    private void constructEditRowFrame() {
        //Two columns and so many rows as necessary
        setLayout(new GridLayout(0, 2));

        int selectedIndex = mainWindow.getIndexOfSelectedTable();
        int selectedRowIndex = mainWindow.getSelectedRowIndexInTableOfResults();

        if (selectedIndex == -1 || selectedRowIndex == -1) {
            return;
        }

        constructFieldValues();

        JButton insertButton = new JButton(Strings.EDIT_ROW);
        insertButton.addActionListener(new InsertListener());
        add(insertButton);
    }

    private void constructFieldValues() {

        int columnCount = mainWindow.getColumnCountInTableOfResults();
        int selectedRowIndex = mainWindow.getSelectedRowIndexInTableOfResults();

        fieldValues = new ArrayList<JTextField>
                (columnCount);
        String columnName;
        for (int i = 0; i < columnCount; ++i) {

            columnName = mainWindow.tableOfResultsModel.getColumnName(i);
            add(new JLabel(columnName));
            JTextField value = new JTextField(mainWindow.getValueInResultTable(selectedRowIndex, i));
            fieldValues.add(value);
            add(value);
        }
    }


    private class InsertListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                ResultSet rs = QueryBuilder.updateRowAndReturnUpdatedTable(
                        mainWindow.connection,
                        mainWindow.getNameOfSelectedTable(),
                        mainWindow.tableOfResultsModel,
                        fieldValues,
                        mainWindow.getSelectedRowIndexInTableOfResults());
                mainWindow.showQueryResult(rs);
                mainWindow.errorLabel.setText("");
                dispose();
            } catch (SQLException e1) {
                mainWindow.errorLabel.setText(e1.getMessage());
                e1.printStackTrace();
            }
        }
    }
}