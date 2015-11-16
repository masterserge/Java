package ru.nsu.ccfit.serdyukov.dbclient.gui;

import ru.nsu.ccfit.serdyukov.dbclient.sql.QueryBuilder;
import ru.nsu.ccfit.serdyukov.dbclient.util.FK;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateFKWindow extends JDialog {

    private MainWindow mainWindow;

    private JPanel columnPanel;

    private ColumnsFromTableExtracter cfte = new ColumnsFromTableExtracter();
    private CreateActionListener cal = new CreateActionListener();

    private String tableName;

    CreateFKWindow(MainWindow mainWindow) {
        super();
        this.mainWindow = mainWindow;
        tableName = mainWindow.getNameOfSelectedTable();
        constructCreateFKFrame();
    }

    private void constructCreateFKFrame() {

        Container pane = getContentPane();
        columnPanel = new JPanel(new GridLayout(0, 5));

        String[] columns = null;
        try {
            columns = QueryBuilder.selectColumnsFromTable(mainWindow.connection, tableName);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String[] tables = null;
        try {
            tables = QueryBuilder.getTableNames(mainWindow.connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JComboBox comboBox = new JComboBox(columns);
        columnPanel.add(comboBox);

        comboBox = new JComboBox(tables);
        comboBox.addActionListener(cfte);
        columnPanel.add(comboBox);

        columnPanel.add(new JComboBox());

        JTextField conName = new JTextField("Con_Name");
        columnPanel.add(conName);

        JButton createButton = new JButton("Create");
        createButton.addActionListener(cal);
        columnPanel.add(createButton);

        pane.add(columnPanel, BorderLayout.CENTER);
    }

    private class ColumnsFromTableExtracter implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            JComboBox cb = (JComboBox)e.getSource();
            String tableName = (String)cb.getSelectedItem();
            String columns[] = null;
            try {
                columns = QueryBuilder.selectColumnsFromTable(mainWindow.connection, tableName);
            } catch (SQLException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            int componentIndex = getComponentIndex(cb);
            JComboBox columnsBox = (JComboBox)columnPanel.getComponent(componentIndex + 1);
            columnsBox.removeAllItems();
            for (String column : columns) {
                columnsBox.addItem(column);
            }
        }
    }

    private int getComponentIndex(Component component) {
        if (component != null && component.getParent() != null) {
            Container c = component.getParent();
            for (int i = 0; i < c.getComponentCount(); i++) {
                if (c.getComponent(i) == component)
                    return i;
            }
        }
        return -1;
    }

    private class CreateActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            FK fk = parseFK();

            try {
                ResultSet rs = QueryBuilder.addFKAndReturnUpdatedTable(
                        mainWindow.connection,
                        mainWindow.getNameOfSelectedTable(),
                        mainWindow.tableOfResultsModel,
                        fk);
                mainWindow.showQueryResult(rs);
                mainWindow.errorLabel.setText("");
                dispose();
            } catch (SQLException e1) {
                mainWindow.errorLabel.setText(e1.getMessage());
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private FK parseFK() {
        String columnName = (String) ((JComboBox)columnPanel.getComponent(0)).getSelectedItem();
        String conName = ((JTextField)columnPanel.getComponent(3)).getText();
        String refTable = (String) ((JComboBox)columnPanel.getComponent(1)).getSelectedItem();
        String refColumn = (String) ((JComboBox)columnPanel.getComponent(2)).getSelectedItem();

        return new FK(columnName, conName, refTable, refColumn);
    }
}