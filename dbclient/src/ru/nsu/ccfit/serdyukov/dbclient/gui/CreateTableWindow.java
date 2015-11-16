package ru.nsu.ccfit.serdyukov.dbclient.gui;

import ru.nsu.ccfit.serdyukov.dbclient.res.Strings;
import ru.nsu.ccfit.serdyukov.dbclient.sql.CreateTableQueryBuilder;
import ru.nsu.ccfit.serdyukov.dbclient.sql.QueryBuilder;
import ru.nsu.ccfit.serdyukov.dbclient.util.Column;
import ru.nsu.ccfit.serdyukov.dbclient.util.DataType;
import ru.nsu.ccfit.serdyukov.dbclient.util.Table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class CreateTableWindow extends JDialog{

    private static final String NOT_SPECIFIED = "NOT_SPECIFIED";
    private static final String NUMBER = "NUMBER"; 
    private static final String VARCHAR2 = "VARCHAR2"; 
    private static final String DATE = "DATE";
    
    private int numberOfColumns;
    private Connection connection;
    private MainWindow mainWindow;

    private DataTypeComboBoxActionListener dtcbal = new DataTypeComboBoxActionListener();
    private NextCancelActionListener ncal = new NextCancelActionListener();
    private ColumnsFromTableExtracter cfte = new ColumnsFromTableExtracter();
    private BackCreateActionListener bcal = new BackCreateActionListener();

    private JPanel tableNamePanel;
    private JPanel columnsPanel;
    private JPanel nextCancelPanel;
    
    private JPanel columnsFKPanel;
    private JPanel backCreatePanel;

    private Table createdTable;
    private String errorMessage;

    public CreateTableWindow(MainWindow mainWindow, int numberOfColumns) {
        super();
        this.numberOfColumns = numberOfColumns;
        this.connection = mainWindow.connection;
        this.mainWindow = mainWindow;
        createColumnsWindow();
    }

    private void createColumnsWindow() {
        Container pane = getContentPane();

        tableNamePanel = new JPanel();
        tableNamePanel.add(new JLabel(Strings.TABLE_NAME));
        tableNamePanel.add(new JTextField(12));
        
        pane.add(tableNamePanel, BorderLayout.NORTH);

        columnsPanel = new JPanel(new GridLayout(0, 6));
        columnsPanel.add(new JLabel("Column name"));
        columnsPanel.add(new JLabel("Column type"));
        columnsPanel.add(new JLabel("Precision"));
        columnsPanel.add(new JLabel("Scale"));
        columnsPanel.add(new JLabel("Not null"));
        columnsPanel.add(new JLabel("PK"));

        String[] dataTypes = { NOT_SPECIFIED, NUMBER, VARCHAR2, DATE};
        
        for (int i = 0; i < numberOfColumns; ++i) {
            columnsPanel.add(new JTextField(10));
            JComboBox comboBox = new JComboBox(dataTypes);
            comboBox.addActionListener(dtcbal);
            columnsPanel.add(comboBox);
            JTextField textField = new JTextField(5);
            textField.setVisible(false);
            columnsPanel.add(textField);
            textField = new JTextField(5);
            textField.setVisible(false);
            columnsPanel.add(textField);
            columnsPanel.add(new JCheckBox());
            columnsPanel.add(new JCheckBox());
        }
        pane.add(columnsPanel, BorderLayout.CENTER);
        
        nextCancelPanel = new JPanel();
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(ncal);
        nextCancelPanel.add(cancelButton, BorderLayout.WEST);

        JButton nextButton = new JButton("Next");
        nextButton.setActionCommand("next");
        nextButton.addActionListener(ncal);
        nextCancelPanel.add(nextButton, BorderLayout.EAST);


        pane.add(nextCancelPanel, BorderLayout.SOUTH);
    }
        
    private class DataTypeComboBoxActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            JComboBox cb = (JComboBox)e.getSource();
            String dataType = (String)cb.getSelectedItem();

            int componentIndex = getComponentIndex(cb);
            
            if (dataType.equals(NOT_SPECIFIED) || dataType.equals(DATE)) {
                resetAndDisable((JTextField) columnsPanel.getComponent(componentIndex + 1));
                resetAndDisable((JTextField) columnsPanel.getComponent(componentIndex + 2));
            }
            else if (dataType.equals(NUMBER)) {
                resetAndEnable((JTextField) columnsPanel.getComponent(componentIndex + 1));
                resetAndEnable((JTextField) columnsPanel.getComponent(componentIndex + 2));
            }
            else if (dataType.equals(VARCHAR2)) {
                resetAndDisable((JTextField) columnsPanel.getComponent(componentIndex + 1));
                resetAndEnable((JTextField) columnsPanel.getComponent(componentIndex + 2));
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
                CreateTableWindow.this.dispose();
            }
            else if (actionCommand.equals("next")) {
                createdTable = parseTable();
                if (tableIsValid(createdTable)) {
                    System.out.println("Valid");
                    createFKWindow();
                }
                else {
                    JOptionPane.showMessageDialog(CreateTableWindow.this, errorMessage);
                }
            }
        }
    }

    private Table parseTable() {

        Table table = new Table();
        table.name = ((JTextField)tableNamePanel.getComponent(1)).getText();

        Column columns[] = new Column[numberOfColumns];
        for (int i = 0; i < numberOfColumns; ++i) {

            columns[i] = new Column();

            JTextField columnName = (JTextField) columnsPanel.getComponent((i+1)*6);
            columns[i].name = columnName.getText();

            JComboBox dataType = (JComboBox)columnsPanel.getComponent((i+1)*6 + 1);
            columns[i].type = DataType.valueOf((String)dataType.getSelectedItem());

            try {
                JTextField precision = (JTextField)columnsPanel.getComponent((i+1)*6 + 2);
                columns[i].precision = Integer.valueOf(precision.getText());
            }
            catch (NumberFormatException e) {
                columns[i].precision = 0;
            }

            try {
                JTextField scale = (JTextField)columnsPanel.getComponent((i+1)*6 + 3);
                columns[i].scale = Integer.valueOf(scale.getText());
            } catch (NumberFormatException e) {
                columns[i].scale = 0;
            }

            JCheckBox notNull = (JCheckBox) columnsPanel.getComponent((i+1)*6 + 4);
            columns[i].notNull = (notNull.isSelected());

            JCheckBox isPK  = (JCheckBox) columnsPanel.getComponent((i+1)*6 + 5);
            columns[i].isPK = (isPK.isSelected());
        }
        table.columns = columns;

        return table;
    }

    private boolean tableIsValid(Table createdTable) {

        if (createdTable.name == null|| createdTable.name.equals("")) {
            errorMessage = Strings.INVALID_TABLE_NAME;
            return false;
        }
        for (int i = 0; i < createdTable.columns.length; ++i) {
            Column column = createdTable.columns[i];

            if (column.name == null || column.name.equals("")) {
                errorMessage = Strings.INVALID_COLUMN_NAME;
                return false;
            }
            if (column.type == DataType.NOT_SPECIFIED) {
                errorMessage = Strings.INVALID_COLUMN_TYPE;
                return false;
            }
        }
        return true;
    }

    private void createFKWindow() {
        Container rootPane = getContentPane();
        rootPane.removeAll();

        columnsFKPanel = new JPanel(new GridLayout(0, 4));

        columnsFKPanel.add(new JLabel("Column name"));
        columnsFKPanel.add(new JLabel("FK"));
        columnsFKPanel.add(new JLabel("Reference table"));
        columnsFKPanel.add(new JLabel("Reference column"));

        String[] tables = null;
        try {
            tables = QueryBuilder.getTableNames(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < numberOfColumns; ++i) {
            columnsFKPanel.add(new JLabel(createdTable.columns[i].name));
            columnsFKPanel.add(new JCheckBox());
            JComboBox comboBox = new JComboBox(tables);
            comboBox.addActionListener(cfte);
            columnsFKPanel.add(comboBox);
            columnsFKPanel.add(new JComboBox());
        }

        rootPane.add(columnsFKPanel, BorderLayout.CENTER);

        backCreatePanel = new JPanel();

        JButton backButton = new JButton("Back");
        backButton.setActionCommand("back");
        backButton.addActionListener(bcal);
        backCreatePanel.add(backButton, BorderLayout.WEST);

        JButton createButton = new JButton("Create");
        createButton.setActionCommand("create");
        createButton.addActionListener(bcal);
        backCreatePanel.add(createButton, BorderLayout.EAST);

        rootPane.add(backCreatePanel, BorderLayout.SOUTH);

        setSize(getPreferredSize());
        repaint();
    }

    private class ColumnsFromTableExtracter implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            JComboBox cb = (JComboBox)e.getSource();
            String tableName = (String)cb.getSelectedItem();
            String columns[] = null;
            try {
                columns = QueryBuilder.selectColumnsFromTable(connection, tableName);
            } catch (SQLException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            int componentIndex = getComponentIndex(cb);
            JComboBox columnsBox = (JComboBox)columnsFKPanel.getComponent(componentIndex + 1);
            columnsBox.removeAllItems();
            for (String column : columns) {
                columnsBox.addItem(column);
            }
        }
    }

    private class BackCreateActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            System.out.println(actionCommand);

            if (actionCommand.equals("back")) {
                recreateColumnsWindow();
            }
            else if (actionCommand.equals("create")) {
                parseTableFK(createdTable);
                if (tableFKIsValid(createdTable)) {
                    System.out.println("Almost Created");
                    try {
                        CreateTableQueryBuilder.createTable(connection, createdTable);
                        mainWindow.reloadAndShowTables();
                        mainWindow.resetTableListSelection();
                        CreateTableWindow.this.dispose();
                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(CreateTableWindow.this, e1.getMessage());
                    }
                }
                else {
                    JOptionPane.showMessageDialog(CreateTableWindow.this, errorMessage);
                }
            }
        }
    }

    private void parseTableFK(Table createdTable) {
        for (int i = 0; i < numberOfColumns; ++i) {
            Column column = createdTable.columns[i];

            JCheckBox checkBox = (JCheckBox) columnsFKPanel.getComponent((i + 1) * 4 + 1);
            column.isFK = checkBox.isSelected();
            System.out.println(column.isFK);

            JComboBox comboBoxTable = (JComboBox) columnsFKPanel.getComponent((i + 1) * 4 + 2);
            column.referenceTable = (String) comboBoxTable.getSelectedItem();
            System.out.println(column.referenceTable);

            JComboBox comboBoxColumn = (JComboBox) columnsFKPanel.getComponent((i + 1) * 4 + 3);
            column.referenceColumn = (String) comboBoxColumn.getSelectedItem();
            System.out.println(column.referenceColumn);

        }
    }

    private boolean tableFKIsValid(Table createdTable) {
        for (int i = 0; i < numberOfColumns; ++i) {
            Column column = createdTable.columns[i];
            if ((column.isFK) && (column.referenceTable == null||column.referenceColumn == null)) {
                errorMessage = Strings.INVALID_FK;
                return false;
            }
        }
        return true;
    }

    private void recreateColumnsWindow() {
        Container pane = getContentPane();
        pane.removeAll();

        pane.add(tableNamePanel, BorderLayout.NORTH);
        pane.add(columnsPanel, BorderLayout.CENTER);
        pane.add(nextCancelPanel, BorderLayout.SOUTH);

        setSize(getPreferredSize());
        repaint();
    }
}
