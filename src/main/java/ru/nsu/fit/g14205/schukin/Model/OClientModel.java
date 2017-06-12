package ru.nsu.fit.g14205.schukin.Model;

import jdk.nashorn.internal.codegen.ObjectClassGenerator;
import ru.nsu.fit.g14205.schukin.DatabaseWorker.DatabaseWorkerInterface;
import ru.nsu.fit.g14205.schukin.DatabaseWorker.Table.MyTableColumn;
import ru.nsu.fit.g14205.schukin.DatabaseWorker.Table.MyTableRow;
import ru.nsu.fit.g14205.schukin.DatabaseWorker.Table.Table;
import ru.nsu.fit.g14205.schukin.Presenter.OClientPresenterInterface;
import ru.nsu.fit.g14205.schukin.View.OClientViewInterface;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * Created by kannabi on 10.06.2017.
 */
public class OClientModel implements OClientModelInterface {
    OClientPresenterInterface presenter;
    OClientViewInterface view;
    DatabaseWorkerInterface worker;

    Table currentTable = new Table("NULL");

    public OClientModel() {}

    public void setPresenter(OClientPresenterInterface presenter){
        this.presenter = presenter;
    }

    public void setDatabaseWorker(DatabaseWorkerInterface worker){
        this.worker = worker;
        worker.login("localhost", "1251", "kannabii", "kan", "kannabii");
    }

    public void setView(OClientViewInterface view){
        this.view = view;
    }

    public boolean loginDatabase(String ip,
                                 String port,
                                 String login,
                                 String password,
                                 String schema){
        return worker.login(ip, port, login, password, schema);
    }

    public List<String> getTablesName(){
        try {
            return worker.getTablesNames();
        } catch (SQLException e){
            e.printStackTrace();
            return new LinkedList<>();
        }
    }

    public List<String> getTableColumnsName(String tableName){
        try {
            currentTable = worker.getTable(tableName);
//            worker.getTable(tableName).getColumns().forEach(o -> resColumns.add(o.getName()));
            return currentTable.getColumns().stream().
                            map(MyTableColumn::getName).collect(Collectors.toList());
        } catch (SQLException e){
            e.printStackTrace();
            return new LinkedList<>();
        }
    }

    public List<List<String>> getTableRows(String tableName){
        try {
            return worker.getTable(tableName).getRows().stream().
                    map(MyTableRow::getData).collect(Collectors.toList());
        } catch (SQLException e){
            e.printStackTrace();
            return new LinkedList<>();
        }
    }

    public List<Object> executeQeury(String query){
        List<Object> res = new LinkedList<>();

        try {
            ResultSet resultSet = worker.workerExecuteQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();

            res.add("OK");

            List<String> columnsNames = new ArrayList<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++)
                columnsNames.add(metaData.getColumnName(i));

            res.add(columnsNames);

            List<List<String>> rows = new LinkedList<>();
            List<String> row;
            while (resultSet.next()) {
                row = new LinkedList<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++)
                    row.add(resultSet.getObject(i) == null ? null : resultSet.getObject(i).toString());
                rows.add(row);
            }
            res.add(rows);

            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            res.add(e.getMessage());
            return res;
        }
    }

    public String deleteRow(int rowIndex){
        try {
            worker.deleteTableRow(currentTable, rowIndex);
            return "OK";
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String addRow(List<String> data){
        MyTableRow row = new MyTableRow();
        data.forEach(row::addValue);

        try {
            worker.addTableRow(currentTable, row);
            return "OK";
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String updateRow(int index, List<String> oldData, List<String> newData){
        MyTableRow oldRow = new MyTableRow();
        oldData.forEach(oldRow::addValue);
        MyTableRow newRow = new MyTableRow();
                newData.forEach(newRow::addValue);

        try {
            worker.updateTableRow(currentTable, oldRow, newRow);
            return "OK";
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public List<List<Object>> getTableMetaData(){
        List<MyTableColumn> columns = currentTable.getColumns();
        List<List<Object>> metaData = new LinkedList<>();
        List<Object> columnMetaData;
        for (MyTableColumn column : columns){
            columnMetaData = new LinkedList<>();
            columnMetaData.add(column.getName());
            columnMetaData.add(column.getType());
            columnMetaData.add(column.getDefaultValue());
            columnMetaData.add(column.getIsPrimaryKey());
            columnMetaData.add(column.getIsNullable());
            metaData.add(columnMetaData);
        }
        return metaData;
    }

    public List<List<Object>> getTableForeignKeys(){
        List<MyTableColumn> columns = currentTable.getColumns();
        List<List<Object>> foreignKeys = new LinkedList<>();
        List<Object> columnKeys;

        for (MyTableColumn column : columns){
            columnKeys = new LinkedList<>();
            if (column.getFkName() != null) {
                columnKeys.add(column.getName());
                columnKeys.add(column.getFkName());
                columnKeys.add(column.getFkTable());
                columnKeys.add(column.getFkColumn());
                foreignKeys.add(columnKeys);
            }
        }

        return foreignKeys;
    }
}
