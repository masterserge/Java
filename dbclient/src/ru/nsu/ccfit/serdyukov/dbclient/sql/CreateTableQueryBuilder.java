package ru.nsu.ccfit.serdyukov.dbclient.sql;

import ru.nsu.ccfit.serdyukov.dbclient.util.Column;
import ru.nsu.ccfit.serdyukov.dbclient.util.Table;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTableQueryBuilder {
    public static void createTable(Connection connection, Table createdTable) throws SQLException {
        StringBuilder query = new StringBuilder();
        
        String tableName = createdTable.name;
        int columnNumber = createdTable.columns.length;
        
        query.append("CREATE TABLE ").append(tableName).append(" (");
        
        for (int i = 0; i < columnNumber; ++i) {
            if (i != 0) {
                query.append(",");
            }
            addColumn(query, createdTable.columns[i]);
        }
        if (hasPK(createdTable)) {
            query.append(",");
            addPK(query, createdTable);
        }
        if (hasFK(createdTable)) {
            addFK(query, createdTable);
        }
        query.append(")");

        System.out.println(query);

        Statement stmt = connection.createStatement();
        stmt.executeQuery(query.toString());
        stmt.close();
    }

    private static void addColumn(StringBuilder query, Column column) {
        query.append("\"").append(column.name).append("\"").append(" ");
        addColumnType(query, column);
        if (column.notNull) {
            query.append("NOT NULL");
        }
    }

    private static void addColumnType(StringBuilder query, Column column) {

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
    }

    private static boolean hasPK(Table createdTable) {
        for (int i = 0; i < createdTable.columns.length; ++i) {
            if (createdTable.columns[i].isPK) {
                return true;
            }
        }
        return false;
    }

    private static void addPK(StringBuilder query, Table createdTable) {
        query.append("CONSTRAINT ").append("\""+createdTable.name+"_PK"+"\"");
        query.append(" PRIMARY KEY ");
        query.append("(");

        boolean firstColumnAdded = true;
        for (int i = 0; i < createdTable.columns.length; ++i) {
            if (createdTable.columns[i].isPK) {
            if (!firstColumnAdded) {
                query.append(",");
            }
            query.append("\"").append(createdTable.columns[i].name).append("\"");
            firstColumnAdded = false;
            }
        }

        query.append(")");
    }

    private static boolean hasFK(Table createdTable) {
        for (int i = 0; i < createdTable.columns.length; ++i) {
            if (createdTable.columns[i].isFK) {
                return true;
            }
        }
        return false;
    }

    private static void addFK(StringBuilder query, Table createdTable) {

        int columnNumber = createdTable.columns.length;

        for (int i = 0; i < columnNumber; ++i) {
            Column column = createdTable.columns[i];
            if (column.isFK) {
                query.append(",");

                query.append("CONSTRAINT ").append("\""+createdTable.name).append("_").append(
                        column.name+"_FK\" ").append("FOREIGN KEY ");

                query.append("(\""+column.name+"\") ");
                query.append("REFERENCES ");
                query.append("\""+column.referenceTable+"\"");
                query.append("(\""+column.referenceColumn+"\") ");
            }
        }
    }
}
