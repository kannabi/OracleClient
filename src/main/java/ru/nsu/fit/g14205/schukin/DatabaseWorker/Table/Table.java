package ru.nsu.fit.g14205.schukin.DatabaseWorker.Table;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Table {
    @Getter
    private final String name;
    @Getter
    private List<MyTableColumn> columns = new ArrayList<>();
    @Getter
    private List<MyTableRow> rows = new ArrayList<>();
    private String pkConstraintName = null;
    private List<MyTableColumn> primaryKeys = new ArrayList<>();

    public Table(String name) {
        this.name = name.toUpperCase();
    }

    public void addColumn(String name, boolean isNullable, boolean isPrimaryKey, String type, String defaultValue) {
        MyTableColumn column = new MyTableColumn (name, isNullable, isPrimaryKey, type, defaultValue);
        columns.add(column);

        if (isPrimaryKey)
            primaryKeys.add(column);
    }

    public void addRow(MyTableRow row) {
        rows.add(row);
    }

    public MyTableRow getRow(int index){
        return rows.get(index);
    }
    public String getColumnType(String columnName) {
        for (MyTableColumn column : columns) {
            if (column.getName().equals(columnName))
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

    public MyTableColumn getColumn(String name){
        for (MyTableColumn column : columns)
            if (column.getName().equals(name))
                return column;

        return null;
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
