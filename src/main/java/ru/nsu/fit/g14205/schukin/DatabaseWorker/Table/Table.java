package ru.nsu.fit.g14205.schukin.DatabaseWorker.Table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private final StringProperty name;
    private ObservableList<MyTableColumn> columns;
    private ObservableList<MyTableRow> rows;
    private String pkConstraintName = null;
    private List<MyTableColumn> primaryKeys = new ArrayList<>();

    public Table(String name) {
        this.name = new SimpleStringProperty(name);
        columns = FXCollections.observableArrayList();
        rows = FXCollections.observableArrayList();
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

    public void addColumn(String name, boolean isNullable, boolean isPrimaryKey, String type, String defaultValue) {
        MyTableColumn column = new MyTableColumn (name, isNullable, isPrimaryKey, type, defaultValue);
        columns.add(column);

        if (isPrimaryKey)
            primaryKeys.add(column);
    }

    public ObservableList<MyTableColumn> getColumns() {
        return columns;
    }

    public void addRow(MyTableRow row) {
        rows.add(row);
    }

    public ObservableList<MyTableRow> getRows() {
        return rows;
    }

    public String getColumnType(String columnName) {
        for (MyTableColumn column : columns) {
            if (column.getName() == columnName)
                return column.getType();
        }

        return null;
    }

    public String getColumnType(Integer index) {
        return columns.get(index).getType();
    }

    public Integer getColumnIndex(String columnName) {
        int index = 0;
        for (MyTableColumn column : columns) {
            if (column.getName().equals(columnName))
                return index;

            index++;
        }

        return -1;
    }

    public String getColumnName(int index) {
        return columns.get(index).getName();
    }

    public void deleteRow(MyTableRow myTableRow) {
        rows.remove(myTableRow);
    }

    public void setColumnUniqueConstraint(String columnName, boolean value) {
        for (MyTableColumn column : columns) {
            if (column.getName().equals(columnName)) {
                column.setIsUnique(value);
                break;
            }
        }
    }

    public void addColumnPkConstraint(List<String> columns, String pkName) {
        for (MyTableColumn column : this.columns) {
            if (columns.contains(column.getName())) {
                column.setIsPrimaryKey(true);
                primaryKeys.add(column);
            }
        }

        pkConstraintName = pkName;
    }

    public void setColumnFk(String columnName, String fkTable, String fkColumn, String fkName) {
        for (MyTableColumn column : this.columns) {
            if (column.getName().equals(columnName)) {
                column.addForeignKey(fkTable, fkColumn, fkName);
                return;
            }
        }
    }

    public void dropColumn(String columnName) {
        for (MyTableColumn column : columns) {
            if (column.getName().equals(columnName)) {
                int index = columns.indexOf(column);
                if (column.getIsPrimaryKey())
                    primaryKeys.remove(column);

                columns.remove(column);

                for (MyTableRow row :rows) {
                    row.deleteData(index);
                }

                return;
            }
        }
    }

    public void renameColumn(String oldName, String newName) {
        for (MyTableColumn column : columns) {
            if (column.getName().equals(oldName)) {
                column.setName(newName);
                return;
            }
        }
    }

    public void setColumnType(String columnName, String type) {
        for (MyTableColumn column : columns) {
            if (column.getName().equals(columnName)) {
                column.setType(type);
                return;
            }
        }
    }

    public void setColumnDefault(String columnName, String defaultValue) {
        for (MyTableColumn column : columns) {
            if (column.getName().equals(columnName)) {
                column.setDefaultValue(defaultValue);
                return;
            }
        }
    }

    public void setColumnNotNull(String columnName, boolean value) {
        for (MyTableColumn column : columns) {
            if (column.getName().equals(columnName)) {
                column.setIsNullable(!value);
                return;
            }
        }
    }

    public void clearPrimaryKeys() {
        primaryKeys.clear();

        for (MyTableColumn column : columns) {
            column.setIsPrimaryKey(false);
        }

        pkConstraintName = null;
    }

    public List<MyTableColumn> getPrimaryKeys() {
        return primaryKeys;
    }

    public String getPkConstraintName() {
        return pkConstraintName;
    }
}
