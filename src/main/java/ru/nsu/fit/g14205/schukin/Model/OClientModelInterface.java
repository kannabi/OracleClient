package ru.nsu.fit.g14205.schukin.Model;

import ru.nsu.fit.g14205.schukin.DatabaseWorker.DatabaseWorkerInterface;
import ru.nsu.fit.g14205.schukin.Presenter.OClientPresenterInterface;
import ru.nsu.fit.g14205.schukin.View.OClientViewInterface;

import java.util.List;

/**
 * Created by kannabi on 10.06.2017.
 */
public interface OClientModelInterface {
    void setView(OClientViewInterface view);

    void setPresenter(OClientPresenterInterface presenter);

    void setDatabaseWorker(DatabaseWorkerInterface worker);

    boolean loginDatabase(String ip,
                          String port,
                          String login,
                          String password,
                          String schema);

    List<String> getTablesName();

    List<String> getTableColumnsName(String tableName);

    List<List<String>> getTableRows(String tableName);

    List<Object> executeQeury(String query);

    String deleteRow(int rowIndex);

    String addRow(List<String> data);

    String updateRow(int index, List<String> oldData, List<String> newData);

    List<List<Object>> getTableMetaData();

    List<List<Object>> getTableForeignKeys();

    String deleteField(String name);

    String addField(String name, String type);

    void setNotNull(String columnName, boolean state);

    void updatePrimaryKeys(List<String> pks);

    void updateDefaulValues(List<String> values);

    void updateColumnType(String columnName, String newType);

    void renameColumn (String oldName, String newName);

    void deleteForeignKey(String name);

    void addForeignKey(String name, String fkTable, String fkColumn, String fkName);

    void dropTable();

    void createTable(String name, List<List<Object>> columns, List<List<String>> foreignKeys);
}
