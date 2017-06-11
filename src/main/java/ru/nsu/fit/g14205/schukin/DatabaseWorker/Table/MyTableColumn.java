package ru.nsu.fit.g14205.schukin.DatabaseWorker.Table;


import javafx.beans.property.BooleanProperty;
import lombok.Data;

@Data
public class MyTableColumn {
    private String name;
    private Boolean isNullable;
    private Boolean isPrimaryKey;
    private Boolean isUnique;
    private String type;
    private String defaultValue;
    private String fkTable;
    private String fkColumn;
    private String fkName;

    public MyTableColumn(String name, boolean isNullable, boolean isPrimaryKey, String type, String defaultValue) {
        this.name = name;
        this.isNullable = isNullable;
        this.isPrimaryKey = isPrimaryKey;
        this.isUnique = false;
        this.type = type;
        this.defaultValue = defaultValue;
        fkTable = null;
        fkColumn = null;
        fkName = null;

    }

    public void addForeignKey(String fkTable, String fkColumn, String fkName) {
        this.fkColumn = fkColumn;
        this.fkTable = fkTable;
        this.fkName = fkName;
    }

    public void clearForeignKey() {
        fkTable = null;
        fkColumn = null;
        fkName = null;
    }
}
