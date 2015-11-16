package ru.nsu.ccfit.serdyukov.dbclient.sql;

import ru.nsu.ccfit.serdyukov.dbclient.util.Column;
import ru.nsu.ccfit.serdyukov.dbclient.util.FK;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QueryBuilder {

    public static ResultSet getTables(Connection connection) throws SQLException {

        DatabaseMetaData meta = connection.getMetaData();
        ResultSet res = meta.getTables(null, meta.getUserName(), null,
                new String[]{"TABLE"});
        return res;
    }

    public static String[] getTableNames(Connection connection) throws SQLException {

        DatabaseMetaData meta = connection.getMetaData();
        ResultSet res = meta.getTables(null, meta.getUserName(), null,
                new String[]{"TABLE"});
        List<String> tables = new LinkedList<String>();
        while (res.next()) {
            String tableName = res.getString("TABLE_NAME");
            if (!tableName.startsWith("BIN$")) {
                tables.add(tableName);
            }
        }
        return (String[])tables.toArray((new String[0]));
    }


    public static String getUserName(Connection connection) throws SQLException {

        DatabaseMetaData meta = connection.getMetaData();
        return meta.getUserName();
    }

    public static ResultSet insertRowAndReturnUpdatedTable(Connection connection, String tableName,
        DefaultTableModel tableModel, ArrayList<JTextField> fieldValues) throws SQLException {

        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append(tableName);
        query.append(" VALUES (\'");

        for (int i = 0; i < tableModel.getColumnCount(); ++i) {
            query.append(fieldValues.get(i).getText());
            if (i < tableModel.getColumnCount() - 1) {
                query.append("\', \'");
            } else {
                query.append("\'");
            }
        }
        query.append(")");
        System.out.println(query);

        Statement stmt = connection.createStatement();
        stmt.executeQuery(query.toString());
        stmt.close();

        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
        return rs;
    }

    public static ResultSet updateRowAndReturnUpdatedTable(Connection connection, String tableName,
        DefaultTableModel tableModel, ArrayList<JTextField> fieldValues, int selectedRowIndex)
            throws SQLException {

        StringBuilder query = new StringBuilder();
        String fieldValue;

        query.append("UPDATE ");
        query.append(tableName);
        query.append(" SET ");

        for (int i = 0; i < tableModel.getColumnCount(); ++i) {

            fieldValue = fieldValues.get(i).getText();

            if (fieldValue == null) {
                continue;
            }
            query.append("\"").append(tableModel.getColumnName(i)).append("\"");
            query.append("=\'");
            query.append(fieldValue);
            query.append("\'");
            query.append(", ");
        }
        query.delete(query.length() - 2, query.length());


        query.append(" WHERE ");
        for (int i = 0; i < tableModel.getColumnCount(); ++i) {

            fieldValue = (String) tableModel.getValueAt(selectedRowIndex, i);

            if (fieldValue == null) {
                continue;
            }
            query.append("\"").append(tableModel.getColumnName(i)).append("\"");
            query.append("=\'");
            query.append(fieldValue);
            query.append("\'");
            query.append(" AND ");
        }
        query.delete(query.length() - 5, query.length());

        System.out.println(query);

        Statement stmt = connection.createStatement();
        stmt.executeQuery(query.toString());
        stmt.close();

        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
        return rs;
    }

    public static ResultSet deleteRowAndReturnUpdatedTable(Connection connection, String tableName,
        DefaultTableModel tableModel, int selectedRow) throws SQLException {

        StringBuilder query = new StringBuilder();
        String fieldValue;

        query.append("DELETE FROM ");
        query.append(tableName);
        query.append(" WHERE ");

        for (int i = 0; i < tableModel.getColumnCount(); ++i) {

            fieldValue = (String) tableModel.getValueAt(selectedRow, i);

            if (fieldValue == null) {
                continue;
            }
            query.append("\"").append(tableModel.getColumnName(i)).append("\"");
            query.append("=\'");
            query.append(fieldValue);
            query.append("\'");

            query.append(" AND ");
        }
        query.delete(query.length() - 5, query.length());

        Statement stmt = connection.createStatement();
        stmt.executeQuery(query.toString());
        stmt.close();

        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
        return rs;
    }

    public static ResultSet executeQueryAndReturnResult(Connection connection, String query)
            throws SQLException {

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }

    public static ResultSet selectAllFromTable(Connection connection, String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
        return rs;
    }
    public static String[] selectColumnsFromTable(Connection connection,
                                                  String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        String[] columns = new String[columnCount];
        for (int i = 1; i <= columnCount; ++i) {
            columns[i-1] = (rsmd.getColumnName(i));
        }
        return columns;
    }

    public static void deleteTable(Connection connection, String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeQuery("DROP TABLE " + tableName);
    }

    public static void renameTable(Connection connection, String oldTableName, String newTableName) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeQuery("ALTER TABLE " + oldTableName + " RENAME TO " + newTableName);
    }

    public static ResultSet describeTable(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet rsColumns = meta.getColumns(null, null, tableName, null);
        return rsColumns;
    }

    public static ResultSet addColumnAndReturnUpdatedTable(Connection connection, String tableName, DefaultTableModel tableOfResultsModel, Column column) throws SQLException {
        StringBuilder query = new StringBuilder();

        query.append("ALTER TABLE ");
        query.append("\"").append(tableName).append("\" ");
        query.append("ADD (");

        query.append("\"").append(column.name).append("\"").append(" ");

        switch (column.type) {

            case DATE: {
                query.append(column.type).append(" ");
            } break;
            case VARCHAR2: {
                query.append(column.type);
                if (column.scale != 0) {
                    query.append("("+column.scale+")");
                }
                query.append(" ");
            } break;
            case NUMBER: {
                query.append(column.type);
                if (column.precision != 0) {
                    query.append("("+column.precision);
                    if (column.scale != 0) {
                        query.append(","+column.scale+")");
                    }
                    else {
                        query.append(")");
                    }
                }
                query.append(" ");
            } break;
            default: {
                throw new IllegalArgumentException("Invalid DataType");
            }
        }
        if (column.notNull) {
            query.append("NOT NULL");
        }
        query.append(")");
        System.out.println(query);

        Statement stmt = connection.createStatement();
        stmt.executeQuery(query.toString());
        stmt.close();

        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
        return rs;
    }

    public static ResultSet dropColumnAndReturnUpdatedTable(Connection connection, String tableName,
        DefaultTableModel tableOfResultsModel, String column) throws SQLException {

        StringBuilder query = new StringBuilder();

        query.append("ALTER TABLE ");
        query.append("\"").append(tableName).append("\" ");
        query.append("DROP COLUMN" );

        query.append("\"").append(column).append("\"").append(" ");

        System.out.println(query);

        Statement stmt = connection.createStatement();
        stmt.executeQuery(query.toString());
        stmt.close();

        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
        return rs;
    }

    public static ResultSet modifyColumnAndReturnUpdatedTable(Connection connection, String table, DefaultTableModel tableOfResultsModel, Column column) throws SQLException {
        StringBuilder query = new StringBuilder();

        query.append("ALTER TABLE ");
        query.append("\"").append(table).append("\" ");
        query.append("MODIFY (");

        query.append("\"").append(column.name).append("\"").append(" ");

        switch (column.type) {

            case DATE: {
                query.append(column.type).append(" ");
            } break;
            case VARCHAR2: {
                query.append(column.type);
                if (column.scale != 0) {
                    query.append("("+column.scale+")");
                }
                query.append(" ");
            } break;
            case NUMBER: {
                query.append(column.type);
                if (column.precision != 0) {
                    query.append("("+column.precision);
                    if (column.scale != 0) {
                        query.append(","+column.scale+")");
                    }
                    else {
                        query.append(")");
                    }
                }
                query.append(" ");
            } break;
            default: {
                throw new IllegalArgumentException("Invalid DataType");
            }
        }
        if (column.notNull) {
            query.append("NOT NULL");
        }
        query.append(")");
        System.out.println(query);

        Statement stmt = connection.createStatement();
        stmt.executeQuery(query.toString());
        stmt.close();

        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + table);
        return rs;

    }

    public static ResultSet renameColumnAndReturnUpdatedTable(Connection connection,
        String tableName, DefaultTableModel tableOfResultsModel, String oldColumnName, String newColumnName) throws SQLException {
        StringBuilder query = new StringBuilder();

        query.append("ALTER TABLE ");
        query.append("\"").append(tableName).append("\" ");
        query.append("RENAME COLUMN" );

        query.append("\"").append(oldColumnName).append("\"").append(" ");
        query.append("to ").append("\"").append(newColumnName).append("\"");

        System.out.println(query);

        Statement stmt = connection.createStatement();
        stmt.executeQuery(query.toString());
        stmt.close();

        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
        return rs;
    }

    public static ResultSet getConstraintsForTable(Connection connection,
                                                   String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT CONSTRAINT_NAME, " +
                "CONSTRAINT_TYPE, TABLE_NAME, SEARCH_CONDITION, STATUS FROM USER_CONSTRAINTS " +
                "WHERE TABLE_NAME = \'"+tableName+"\' "+
                "ORDER BY TABLE_NAME");
        return rs;
    }

    public static String[] selectConstraintsFromTable(Connection connection, String tableName) throws SQLException {

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT CONSTRAINT_NAME FROM USER_CONSTRAINTS " +
                "WHERE TABLE_NAME = \'"+tableName+"\' "+
                "ORDER BY TABLE_NAME");

        List<String> constraints = new LinkedList<String>();
        while (rs.next()) {
            constraints.add(rs.getString("CONSTRAINT_NAME"));
        }
        return (String[])constraints.toArray((new String[0]));
    }

    public static ResultSet dropConstraintAndReturnUpdatedTable(Connection connection,
                                                                String tableName,
                                                                DefaultTableModel
                                                                        tableOfResultsModel,
                                                                String constraint) throws SQLException {
        StringBuilder query = new StringBuilder();

        query.append("ALTER TABLE ");
        query.append("\"").append(tableName).append("\" ");
        query.append("DROP CONSTRAINT");
        query.append("\"").append(constraint).append("\"");

        System.out.println(query);

        Statement stmt = connection.createStatement();
        stmt.executeQuery(query.toString());
        stmt.close();

        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
        return rs;
    }

    public static ResultSet addPKAndReturnUpdatedTable(Connection connection, String table,
                                                       DefaultTableModel tableOfResultsModel, String[] columns, String conName)
            throws SQLException {
        StringBuilder query = new StringBuilder();

        query.append("ALTER TABLE ");
        query.append("\"").append(table).append("\" ");
        query.append("ADD CONSTRAINT");
        query.append("\"").append(conName).append("\" ");
        query.append("PRIMARY KEY ");

        query.append("(");
        
        for (int i = 0; i < columns.length; ++i) {
            if (i !=0) {
                query.append(",");
            }
            query.append("\"").append(columns[i]).append("\" ");
            
        }
        query.append(")");
        System.out.println(query);

        Statement stmt = connection.createStatement();
        stmt.executeQuery(query.toString());
        stmt.close();

        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + table);
        return rs;
    }

    public static ResultSet addFKAndReturnUpdatedTable(Connection connection,
        String table, DefaultTableModel tableOfResultsModel, FK fk) throws SQLException {

        StringBuilder query = new StringBuilder();

        query.append("ALTER TABLE ");
        query.append("\"").append(table).append("\" ");
        query.append("ADD CONSTRAINT");
        query.append("\"").append(fk.conName).append("\" ");
        query.append("FOREIGN KEY (\""+fk.columnName+"\") REFERENCES ");
        query.append("\""+fk.refTable+"\"");
        query.append("(\""+fk.refColumn+"\")");

        System.out.println(query);

        Statement stmt = connection.createStatement();
        stmt.executeQuery(query.toString());
        stmt.close();

        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + table);
        return rs;

    }
}
