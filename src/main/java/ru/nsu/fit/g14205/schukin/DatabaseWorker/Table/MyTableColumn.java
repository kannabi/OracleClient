package ru.nsu.fit.g14205.schukin.DatabaseWorker.Table;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MyTableColumn {
    private StringProperty name;
    private BooleanProperty isNullable;
    private BooleanProperty isPrimaryKey;
    private BooleanProperty isUnique;
    private String type;
    private String defaultValue;
    private String fkTable;
    private String fkColumn;
    private String fkName;

    public MyTableColumn(String name, boolean isNullable, boolean isPrimaryKey, String type, String defaultValue) {
        this.name = new SimpleStringProperty(name);
        this.isNullable = new SimpleBooleanProperty(isNullable);
        this.isPrimaryKey = new SimpleBooleanProperty(isPrimaryKey);
        this.isUnique = new SimpleBooleanProperty(false);
        this.type = type;
        this.defaultValue = defaultValue;
        fkTable = null;
        fkColumn = null;
        fkName = null;

    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public Boolean getIsNullable() {
        return isNullable.get();
    }

    public void setIsNullable(Boolean isNullable) {
        this.isNullable.set(isNullable);
    }

    public BooleanProperty isNullableProperty() {
        return isNullable;
    }

    public Boolean getIsPrimaryKey() {
        return isPrimaryKey.get();
    }

    public void setIsPrimaryKey(Boolean isNullable) {
        this.isPrimaryKey.set(isNullable);
    }

    public BooleanProperty isPrimaryKeyProperty() {
        return isPrimaryKey;
    }

    public void setType(String type) {
        this.type = type;

        return;
    }

    public String getType() {
        return type;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;

        return;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setIsUnique(boolean value) {
        isUnique.setValue(value);

        return;
    }

    public boolean getIsUnique() {
        return isUnique.get();
    }

    public void addForeignKey(String fkTable, String fkColumn, String fkName) {
        this.fkColumn = fkColumn;
        this.fkTable = fkTable;
        this.fkName = fkName;

        return;
    }

    public String getFkTable() {
        return fkTable;
    }

    public String getFkColumn() {
        return fkColumn;
    }

    public String getFkName() {
        return fkName;
    }

    public void clearForeignKey() {
        fkTable = null;
        fkColumn = null;
        fkName = null;
    }
}
