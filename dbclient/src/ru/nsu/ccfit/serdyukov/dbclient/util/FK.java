package ru.nsu.ccfit.serdyukov.dbclient.util;

public class FK {
    public FK(String columnName, String conName, String refTable, String refColumn) {
        this.columnName = columnName;
        this.conName = conName;
        this.refTable = refTable;
        this.refColumn = refColumn;
    }

    public String columnName;
    public String conName;
    public String refTable;
    public String refColumn;
}
