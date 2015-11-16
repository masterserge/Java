package ru.nsu.ccfit.serdyukov.dbclient.gui;

import ru.nsu.ccfit.serdyukov.dbclient.sql.QueryBuilder;
import ru.nsu.ccfit.serdyukov.dbclient.util.Column;
import ru.nsu.ccfit.serdyukov.dbclient.util.DataType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddColumnWindow extends JDialog {

    private MainWindow mainWindow;

    private JPanel columnPanel;
    private JPanel addCancelPanel;

    private DataTypeComboBoxActionListener dtcbal = new DataTypeComboBoxActionListener();
    private NextCancelActionListener ncal = new NextCancelActionListener();

    private static final String NOT_SPECIFIED = "NOT_SPECIFIED";
    private static final String NUMBER = "NUMBER";
    private static final String VARCHAR2 = "VARCHAR2";
    private static final String DATE = "DATE";

    private String tableName;

    AddColumnWindow(MainWindow mainWindow) {
        super();
        this.mainWindow = mainWindow;
        tableName = mainWindow.getNameOfSelectedTable();
        constructAddColumnFrame();
    }

    private void constructAddColumnFrame() {

        Container pane = getContentPane();

        columnPanel = new JPanel(new GridLayout(0, 5));
        columnPanel.add(new JLabel("Column name"));
        columnPanel.add(new JLabel("Column type"));
        columnPanel.add(new JLabel("Precision"));
        columnPanel.add(new JLabel("Scale"));
        columnPanel.add(new JLabel("Not null"));

        String[] dataTypes = { NOT_SPECIFIED, NUMBER, VARCHAR2, DATE};

        columnPanel.add(new JTextField(10));
        JComboBox comboBox = new JComboBox(dataTypes);
        comboBox.addActionListener(dtcbal);
        columnPanel.add(comboBox);
        JTextField textField = new JTextField(5);
        textField.setVisible(false);
        columnPanel.add(textField);
        textField = new JTextField(5);
        textField.setVisible(false);
        columnPanel.add(textField);
        columnPanel.add(new JCheckBox());

        pane.add(columnPanel, BorderLayout.CENTER);

        addCancelPanel = new JPanel();

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(ncal);
        addCancelPanel.add(cancelButton, BorderLayout.WEST);

        JButton nextButton = new JButton("Add");
        nextButton.setActionCommand("add");
        nextButton.addActionListener(ncal);
        addCancelPanel.add(nextButton, BorderLayout.EAST);

        pane.add(addCancelPanel, BorderLayout.SOUTH);
    }

    private class DataTypeComboBoxActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            JComboBox cb = (JComboBox)e.getSource();
            String dataType = (String)cb.getSelectedItem();

            int componentIndex = getComponentIndex(cb);

            if (dataType.equals(NOT_SPECIFIED) || dataType.equals(DATE)) {
                resetAndDisable((JTextField) columnPanel.getComponent(componentIndex + 1));
                resetAndDisable((JTextField) columnPanel.getComponent(componentIndex + 2));
            }
            else if (dataType.equals(NUMBER)) {
                resetAndEnable((JTextField) columnPanel.getComponent(componentIndex + 1));
                resetAndEnable((JTextField) columnPanel.getComponent(componentIndex + 2));
            }
            else if (dataType.equals(VARCHAR2)) {
                resetAndDisable((JTextField) columnPanel.getComponent(componentIndex + 1));
                resetAndEnable((JTextField) columnPanel.getComponent(componentIndex + 2));
            }
        }

        private JTextField resetAndDisable(JTextField textField) {
            textField.setText("");
            textField.setVisible(false);
            return textField;
        }

        private JTextField resetAndEnable(JTextField textField) {
            textField.setText("");
            textField.setVisible(true);
            return textField;
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

    private class NextCancelActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();

            if (actionCommand.equals("cancel")) {
                AddColumnWindow.this.dispose();
            }
            else if (actionCommand.equals("add")) {

                Column column = parseColumn();
                try {
                    ResultSet rs = QueryBuilder.addColumnAndReturnUpdatedTable(
                            mainWindow.connection,
                            mainWindow.getNameOfSelectedTable(),
                            mainWindow.tableOfResultsModel,
                            column);
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

    private Column parseColumn() {
        Column column = new Column();

        int i = 0;

        JTextField columnName = (JTextField) columnPanel.getComponent((i+1)*5);
        column.name = columnName.getText();

        JComboBox dataType = (JComboBox)columnPanel.getComponent((i+1)*5 + 1);
        column.type = DataType.valueOf((String) dataType.getSelectedItem());

        try {
            JTextField precision = (JTextField)columnPanel.getComponent((i+1)*5 + 2);
            column.precision = Integer.valueOf(precision.getText());
        }
        catch (NumberFormatException e) {
            column.precision = 0;
        }

        try {
            JTextField scale = (JTextField)columnPanel.getComponent((i+1)*5 + 3);
            column.scale = Integer.valueOf(scale.getText());
        } catch (NumberFormatException e) {
            column.scale = 0;
        }

        JCheckBox notNull = (JCheckBox) columnPanel.getComponent((i+1)*5 + 4);
        column.notNull = (notNull.isSelected());

        return column;
    }
}