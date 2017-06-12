package ru.nsu.fit.g14205.schukin.DatabaseWorker;

import javafx.util.Pair;
import ru.nsu.fit.g14205.schukin.DatabaseWorker.Table.MyTableColumn;
import ru.nsu.fit.g14205.schukin.DatabaseWorker.Table.MyTableRow;
import ru.nsu.fit.g14205.schukin.DatabaseWorker.Table.Table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by kannabi on 10.06.2017.
 */
public interface DatabaseWorkerInterface {
    boolean login(String ip,
                  String port,
                  String login,
                  String password,
                  String schema);

    boolean connectionEstablished();

    void closeConnection() throws SQLException;

    void updateTables() throws SQLException;

    List<String> getTablesNames() throws SQLException;

    Table getTable(String tableName) throws SQLException;

    void initTable(Table table) throws SQLException;

    void updateTableRow(Table table, MyTableRow oldTableRow, MyTableRow newTableRow) throws SQLException;

//    void deleteTableRow(Table table, MyTableRow myTableRow) throws SQLException;
    void deleteTableRow(Table table, int rowIndex) throws SQLException;

    void addTableRow(Table table, MyTableRow myTableRow) throws SQLException;

    void dropColumn(Table table, String columnName) throws SQLException;

    void updateColumns(Table table, List<String> columnsNames, List<String> columnsTypes) throws SQLException;

    void addNewColumn(Table table, String columnName, String columnType) throws SQLException;

    void renameColumn(Table table, String oldName, String newName) throws SQLException;

    void changeColumnType(Table table, String columnName, String newType) throws SQLException;

    void updateDefault(Table table, List<String> defaultList) throws SQLException;

    void setColumnDefault(Table table, String columnName, String defaultValue) throws SQLException;

    void updateNotNull(Table table, List<Boolean> notNullList) throws SQLException;

    void setNotNull(Table table, String columnName, boolean value) throws SQLException;

    void updatePrimaryKeys(Table table, List<String> primaryKeys) throws SQLException;

    void dropPrimaryKeys(Table table) throws SQLException;

    void setPrimaryKeys(Table table, List<String> pk) throws SQLException;

    void updateForeignKeys(Table table, List<Pair<String, String>> foreignKeys) throws SQLException;

    void setForeignKey(Table table, MyTableColumn column, Pair<String, String> foreignKey) throws SQLException;

    void dropForeignKey(Table table, MyTableColumn column) throws SQLException;

    void dropTable(Table table) throws SQLException;

    ResultSet workerExecuteQuery(String query) throws SQLException;

    boolean workerExecute(String query) throws SQLException;

    void createNewTable(Table table) throws SQLException;
}
