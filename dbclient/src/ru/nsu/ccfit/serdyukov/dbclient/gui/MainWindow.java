package ru.nsu.ccfit.serdyukov.dbclient.gui;

import ru.nsu.ccfit.serdyukov.dbclient.res.Strings;
import ru.nsu.ccfit.serdyukov.dbclient.sql.QueryBuilder;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class MainWindow extends JFrame {

    SqlClientMenuBar menuBar;
    JLabel logonStatusLabel;
    DefaultListModel listOfTablesModel;
    JList listOfTables;
    DefaultTableModel tableOfResultsModel;
    JTable tableOfResults;
    JLabel errorLabel;

    MenuBarListener menuBarListener;
    WindowSwitchListener windowSwitchListener;
    TableSelectionListener tableSelectionListener;

    Connection connection;

    public MainWindow(String title) throws HeadlessException {
        super(title);
        initializeListeners();
        MainWindowConstructor.constructMainWindow(this);
    }

    private void initializeListeners() {
        menuBarListener = new MenuBarListener();
        windowSwitchListener = new WindowSwitchListener();
        tableSelectionListener = new TableSelectionListener();
    }

    int getIndexOfSelectedTable() {
        return listOfTables.getSelectedIndex();
    }

    String getNameOfSelectedTable() {
        int index = getIndexOfSelectedTable();
        return (String)listOfTablesModel.get(index);
    }

    int getSelectedRowIndexInTableOfResults() {
        return tableOfResults.getSelectedRow();
    }

    String getValueInResultTable(int row, int column) {
        return (String)tableOfResultsModel.getValueAt(row, column);
    }

    int getColumnCountInTableOfResults() {
        return tableOfResultsModel.getColumnCount();
    }

    private boolean anyTableSelected() {
        int selectedTable = listOfTables.getSelectedIndex();
        return (selectedTable >= 0);
    }

    private boolean ableToLogout() {
        return (connection != null);
    }

    private boolean ableToEditOrDeleteRow() {
        int selectedTable = listOfTables.getSelectedIndex();
        int selectedRow = tableOfResults.getSelectedRow();
        return (selectedTable >= 0) && (selectedRow >= 0);
    }

    private boolean ableToInsertRow() {
        int selectedTable = listOfTables.getSelectedIndex();
        return (selectedTable >= 0);
    }

    private boolean ableToExecuteQuery() {
        return (connection != null);
    }

    private boolean ableToLogon() {
        return (connection == null);
    }

    private void clearResultTable() {
        tableOfResultsModel.setColumnCount(0);
        tableOfResultsModel.setRowCount(0);
    }

    private void clearListOfTables() {
        listOfTablesModel.clear();
    }

    void resetTableListSelection() {
        listOfTables.clearSelection();
    }

    void reloadAndShowTables() throws SQLException {

        ResultSet res = QueryBuilder.getTables(connection);
        listOfTablesModel.clear();

        while (res.next()) {
            String tableName = res.getString("TABLE_NAME");
            if (!tableName.startsWith("BIN$"))
            listOfTablesModel.addElement(res.getString("TABLE_NAME"));
        }
        clearResultTable();
    }

    void showLogonInfo() throws SQLException {
        String userName = QueryBuilder.getUserName(connection);
        logonStatusLabel.setText("You logged on as: " + userName);
        errorLabel.setText("");
    }

    void showQueryResult(ResultSet rs) throws SQLException {

        if (isEmpty(rs)) return;
        clearResultTable();

        ResultSetMetaData rsmd = rs.getMetaData();

        int columnCount = rsmd.getColumnCount();
        for (int i = 1; i <= columnCount; ++i) {
            tableOfResultsModel.addColumn(rsmd.getColumnName(i));
        }
        while (rs.next()) {
            Vector rowData = new Vector();
            for (int i = 1; i <= columnCount; ++i) {
                //We need some walkaround with DateType to correctly remove and update rows
                //Exactly, we need to use Oracle(dd-MMM-yy) format
                if (rsmd.getColumnType(i) == Types.DATE) {
                    Date date = rs.getDate(i);
                    if (date != null) {
                        String dateFormatted = new SimpleDateFormat("dd-MMM-yy").format(date);
                        rowData.add(dateFormatted);
                    }
                } else {
                    rowData.add(rs.getString(i));
                }
            }
            tableOfResultsModel.addRow(rowData);
        }
        rs.getStatement().close();
    }

    //I'd be glad to know if there is better way to know if ResultSet is empty
    //without moving cursor(rs.next()) and using scrollable ResultSet
    private boolean isEmpty(ResultSet rs) {
        try {
            rs.getMetaData();
            return false;
        } catch (SQLException e) {
            return true;
        }
    }



    class MenuBarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            JMenuItem source = (JMenuItem) (e.getSource());
            String sourceString = source.getText();

            if (sourceString.equals(Strings.EXIT)) {
                System.exit(0);
            } else if (sourceString.equals(Strings.LOG_ON)) {
                logon();
            } else if (sourceString.equals(Strings.LOG_OUT)) {
                logout();
            } else if (sourceString.equals(Strings.EXECUTE_QUERY)) {
                executeQuery();
            } else if (sourceString.equals(Strings.INSERT_ROW)) {
                insertRow();
            } else if (sourceString.equals(Strings.DELETE_ROW)) {
                deleteRow();
            } else if (sourceString.equals(Strings.EDIT_ROW)) {
                editRow();
            } else if (sourceString.equals(Strings.DELETE_TABLE)) {
                deleteTable();
            } else if (sourceString.equals(Strings.RENAME_TABLE)) {
                renameTable();
            } else if (sourceString.equals(Strings.CREATE_TABLE)) {
                createTable();
            } else if (sourceString.equals(Strings.DESCRIBE_TABLE)) {
              describeTable();  
            } else if (sourceString.equals(Strings.ADD_COLUMN)) {
                addColumn();
            } else if (sourceString.equals(Strings.DROP_COLUMN)) {
                dropColumn();
            } else if (sourceString.equals(Strings.MODIFY_COLUMN)) {
                modifyColumn();
            } else if (sourceString.equals(Strings.RENAME_COLUMN)) {
                renameColumn();
            } else if (sourceString.equals(Strings.WATCH_CONSTRAINTS)) {
                watchConstraints();
            } else if (sourceString.equals(Strings.DROP_CONSTRAINT)) {
                dropConstraint();
            } else if (sourceString.equals(Strings.CREATE_PK)) {
                createPK();
            } else if (sourceString.equals(Strings.CREATE_FK)) {
                createFK();
            }
        }

        private void createFK() {
            if (!anyTableSelected()) {
                return;
            }
            CreateFKWindow createFKWindow = new CreateFKWindow(MainWindow.this);
            prepareJDialog(createFKWindow);
        }

        private void createPK() {
            if (!anyTableSelected()) {
                return;
            }
            CreatePKWindow createPKWindow = new CreatePKWindow(MainWindow.this);
            prepareJDialog(createPKWindow);
        }

        private void dropConstraint() {
            if (!anyTableSelected()) {
                return;
            }
            DropConstraintWindow dropConstraintWindow = new DropConstraintWindow(MainWindow.this);
            prepareJDialog(dropConstraintWindow);
        }

        private void watchConstraints() {
            if (!anyTableSelected()) {
                return;
            }
            try {
                ResultSet rs = QueryBuilder.getConstraintsForTable(connection,
                        getNameOfSelectedTable());
                showQueryResult(rs);
                errorLabel.setText("");
            } catch (SQLException e1) {
                errorLabel.setText(e1.getMessage());
                e1.printStackTrace();
            }
        }

        private void renameColumn() {
            if (!anyTableSelected()) {
                return;
            }
            RenameColumnWindow renameColumnWindow = new RenameColumnWindow(MainWindow.this);
            prepareJDialog(renameColumnWindow);
        }

        private void modifyColumn() {
            if (!anyTableSelected()) {
                return;
            }
            ModifyColumnWindow modifyColumnWindow = new ModifyColumnWindow(MainWindow.this);
            prepareJDialog(modifyColumnWindow);
        }

        private void dropColumn() {
            if (!anyTableSelected()) {
                return;
            }
            DropColumnWindow dropColumnWindow = new DropColumnWindow(MainWindow.this);
            prepareJDialog(dropColumnWindow);
        }

        private void addColumn() {
            if (!anyTableSelected()) {
                return;
            }

            AddColumnWindow addColumnWindow = new AddColumnWindow(MainWindow.this);
            prepareJDialog(addColumnWindow);
        }

        private void describeTable() {
            if (!anyTableSelected()) {
                return;
            }
            String tableName = getNameOfSelectedTable();
            ResultSet rs;
            try {
                rs = QueryBuilder.describeTable(connection, tableName);
                showQueryResult(rs);
                resetTableListSelection();
            } catch (SQLException e) {
                errorLabel.setText(e.getMessage());
            }
        }

        private void createTable() {
            if (!ableToExecuteQuery()) {
                errorLabel.setText(Strings.CANNOT_EXECUTE_QUERY);
                return;
            }
            int numberOfColumns = Integer.valueOf(JOptionPane.showInputDialog(Strings
                    .NUMBER_OF_COLUMNS));
            CreateTableWindow createTableWindow = new CreateTableWindow(MainWindow.this,
                    numberOfColumns);
            prepareJDialog(createTableWindow);
        }

        private void renameTable() {
            if (anyTableSelected()) {
                String newTableName = JOptionPane.showInputDialog(Strings.ENTER_NEW_TABLE_NAME);
                String oldTableName = getNameOfSelectedTable();
                try {
                    QueryBuilder.renameTable(connection, oldTableName, newTableName);
                    reloadAndShowTables();
                } catch (SQLException e1) {
                    errorLabel.setText(e1.getMessage());
                }
            }
        }

        private void deleteTable() {
            if (anyTableSelected()) {

                String tableName = getNameOfSelectedTable();
                try {
                    QueryBuilder.deleteTable(connection, tableName);
                    reloadAndShowTables();
                } catch (SQLException e1) {
                    errorLabel.setText(e1.getMessage());
                }
            }
        }

        private void editRow() {

            if (!ableToEditOrDeleteRow()) {
                errorLabel.setText(Strings.CANNOT_EDIT_ROW);
                return;
            }

            EditRowWindow editRowWindow = new EditRowWindow(MainWindow.this);
            prepareJDialog(editRowWindow);
        }


        private void deleteRow() {

            if (!ableToEditOrDeleteRow()) {
                errorLabel.setText(Strings.CANNOT_DELETE_ROW);
                return;
            }
            int selectedRow = getSelectedRowIndexInTableOfResults();

            try {
                deleteRowByNumber(selectedRow);
            } catch (SQLException e1) {
                errorLabel.setText(e1.getMessage());
            }
        }

        private void deleteRowByNumber(int selectedRow) throws SQLException {

            try {
                ResultSet rs = QueryBuilder.deleteRowAndReturnUpdatedTable(connection,
                        getNameOfSelectedTable(),
                        tableOfResultsModel, selectedRow);
                showQueryResult(rs);
                errorLabel.setText("");
            } catch (SQLException e1) {
                errorLabel.setText(e1.getMessage());
                e1.printStackTrace();
            }
        }

        private void insertRow() {
            if (!ableToInsertRow()) {
                errorLabel.setText(Strings.CANNOT_INSERT_ROW);
                return;
            }
            InsertRowWindow insertRowWindow = new InsertRowWindow(MainWindow.this);
            prepareJDialog(insertRowWindow);
        }

        private void executeQuery() {
            if (!ableToExecuteQuery()) {
                errorLabel.setText(Strings.CANNOT_EXECUTE_QUERY);
                return;
            }
            QueryWindow queryWindow = new QueryWindow(MainWindow.this);
            prepareJDialog(queryWindow);
        }

        private void logout() {
            if (!ableToLogout()) {
                errorLabel.setText(Strings.CANNOT_LOGOUT);
                return;
            }
            try {
                connection.close();
                connection = null;
                logonStatusLabel.setText(Strings.NOT_LOGED_ON);
                clearListOfTables();
                clearResultTable();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        private void logon() {

            if (!ableToLogon()) {
                errorLabel.setText(Strings.CANNOT_LOGON);
                return;
            }
            LoginWindow loginWindow = new LoginWindow(MainWindow.this);
            prepareJDialog(loginWindow);
        }

        private void prepareJDialog(JDialog dialog) {

            dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dialog.setAlwaysOnTop(true);
            dialog.setLocation(200, 200);

            dialog.pack();
            dialog.addWindowListener(windowSwitchListener);
            dialog.setVisible(true);
        }
    }



    private class WindowSwitchListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {
            MainWindow.this.setEnabled(false);
        }

        @Override
        public void windowClosed(WindowEvent e) {
            MainWindow.this.setEnabled(true);
        }

        @Override
        public void windowClosing(WindowEvent e) {
        }

        @Override
        public void windowIconified(WindowEvent e) {
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
        }

        @Override
        public void windowActivated(WindowEvent e) {
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
        }
    }

    private class TableSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {

            if (!e.getValueIsAdjusting()) {
                int selectedIndex = getIndexOfSelectedTable();

                if (selectedIndex == -1) {
                    return;
                }
                try {
                    String tableName = getNameOfSelectedTable();
                    ResultSet rs = QueryBuilder.selectAllFromTable(connection, tableName);
                    showQueryResult(rs);
                    errorLabel.setText("");
                } catch (SQLException e1) {
                    errorLabel.setText(e1.getMessage());
                    e1.printStackTrace();
                }
            }
        }
    }
}
