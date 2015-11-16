package ru.nsu.ccfit.serdyukov.dbclient.util;

public class Column {
    public String name;
    public DataType type;
    public int precision;
    public int scale;
    public boolean notNull;
    public boolean isPK;
    public boolean isFK;
    public String referenceTable;
    public String referenceColumn;
}
