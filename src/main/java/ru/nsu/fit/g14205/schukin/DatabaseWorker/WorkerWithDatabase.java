package ru.nsu.fit.g14205.schukin.DatabaseWorker;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import ru.nsu.fit.g14205.schukin.DatabaseWorker.Table.MyTableColumn;
import ru.nsu.fit.g14205.schukin.DatabaseWorker.Table.MyTableRow;
import ru.nsu.fit.g14205.schukin.DatabaseWorker.Table.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WorkerWithDatabase implements LoginInterface {
    private Connection connection = null;
    private String schema;
    private ObservableList<StringProperty> tablesNames;

//    public boolean login(Pair<String, String> ipPort, Pair<String, String> logPass, String schema) {
    public boolean login(String ip,
                         String port,
                         String login,
                         String password,
                         String schema) {
        try {
            Locale.setDefault(Locale.ENGLISH);
            Class.forName("oracle.jdbc.driver.OracleDriver");

            connection = DriverManager.getConnection(getConnectURL(ip, port, schema), login, password);
//            connection = DriverManager.getConnection(getConnectURL(ipPort, schema), "system", "Ss148819695674");
            tablesNames = FXCollections.observableArrayList();
            updateTables();
            this.schema = schema;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean connectionEstablished() {
        if (connection == null)
            return false;

        try {
            return !connection.isClosed();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public void closeConnection() throws SQLException  {
        connection.close();


    }

    public void updateTables() throws SQLException {
        tablesNames.clear();

        String query = "SELECT TABLE_NAME FROM USER_TABLES WHERE TABLE_NAME NOT LIKE'%$%'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            try {
                tablesNames.add(new SimpleStringProperty(resultSet.getString(1)));
            } catch (SQLException ex) {
                continue;
            }
        }
    }

    public ObservableList<StringProperty> getTablesNames() throws SQLException {
        return tablesNames;
    }

    private String getConnectURL(String ip, String port, String schema) {
        String url = "jdbc:oracle:thin:@" + ip + ":" + port + ":xe";
//        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        return url;
    }

    public Table getTable(String tableName) throws SQLException {
        Table table = new Table(tableName);

        initTable(table);

        return table;
    }

    public void initTable(Table table) throws SQLException {
        initColumns(table);
        setData(table);
        initPrimaryKeys(table);
        initForeignKeys(table);


    }

    private void initColumns(Table table) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resultSet = meta.getColumns(schema, null, table.getName(), "%");

        System.out.println(table.getName());

        while (resultSet.next()) {
            String defaultValue = resultSet.getString(13);
            if (defaultValue != null) {
                defaultValue = defaultValue.replace("\n", "");
                if (defaultValue.indexOf("'") == 0 && defaultValue.lastIndexOf("'") == defaultValue.length() - 2)
                    defaultValue = defaultValue.substring(1, defaultValue.length() - 2);
            } else {
                defaultValue = "NULL";
            }

            boolean isNullable = false;
            if (resultSet.getString(18).equals("YES"))
                isNullable = true;

            if (!resultSet.getString(6).equals("VARCHAR2")) {
                defaultValue = defaultValue.replace("'", "");
            }

            System.out.println("\t\t" + resultSet.getString(4) + ": " + resultSet.getString(6) + ", " + resultSet.getInt(5) + ", " + defaultValue);

            table.addColumn(resultSet.getString(4), isNullable, false, resultSet.getString(6), defaultValue);
        }
    }

    private void setData(Table table) throws SQLException {
        String query = "SELECT * FROM " + table.getName();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        ResultSetMetaData metaData = resultSet.getMetaData();

        while (resultSet.next()) {
            MyTableRow row = new MyTableRow();

            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                if (resultSet.getObject(i) == null) {
                    row.addValue(null);
                } else {
                    row.addValue(resultSet.getObject(i).toString());
                }
            }

            table.addRow(row);
        }
    }

    private void executeQuery() throws SQLException {

    }

    public void updateTableRow(Table table, MyTableRow myTableRow) throws SQLException {
        String updateString = getUpdateString(table, myTableRow);

        Statement statement = connection.createStatement();
        statement.executeUpdate(updateString);
        myTableRow.commitNewData();
    }

    private String getUpdateString(Table table, MyTableRow myTableRow) {
        StringBuilder updateString = new StringBuilder();
        updateString.append("UPDATE " + table.getName() + " SET ");

        for (int i = 0; i < myTableRow.getDataSize() - 1; i++) {
            if (myTableRow.getNewValue(i).get() == null) {
                updateString.append(table.getColumnName(i) + " = NULL, ");
            } else {
                updateString.append(table.getColumnName(i) + " = '" + myTableRow.getNewValue(i).get() + "', ");
            }
        }
        updateString.append(table.getColumnName(myTableRow.getDataSize() - 1) + " = '" + myTableRow.getNewValue(myTableRow.getDataSize() - 1).get() + "' ");

        updateString.append("WHERE ");
        for (int i = 0; i < myTableRow.getDataSize() - 1; i++) {
            if (myTableRow.getValue(i).get() == null) {
                updateString.append(table.getColumnName(i) + " = NULL, ");
            } else {
                updateString.append(table.getColumnName(i) + " = '" + myTableRow.getValue(i).get() + "' AND ");
            }
        }

        updateString.append(table.getColumnName(myTableRow.getDataSize() - 1) + " = '" + myTableRow.getValue(myTableRow.getDataSize() - 1).get() + "' ");

        return updateString.toString();
    }

    public void deleteTableRow(Table table, MyTableRow myTableRow) throws SQLException {
        String deleteString = getDeleteString(table, myTableRow);
        Statement statement = connection.createStatement();
        statement.executeUpdate(deleteString);

        table.deleteRow(myTableRow);
    }

    private String getDeleteString(Table table, MyTableRow myTableRow) {
        StringBuilder deleteString = new StringBuilder();
        deleteString.append("DELETE FROM " + table.getName());

        deleteString.append(" WHERE ");
        for (int i = 0; i < myTableRow.getDataSize() - 1; i++) {
            if (myTableRow.getValue(i).get() == null) {
                deleteString.append(table.getColumnName(i) + " = NULL, ");
            } else {
                deleteString.append(table.getColumnName(i) + " = '" + myTableRow.getValue(i).get() + "' AND ");
            }
        }

        deleteString.append(table.getColumnName(myTableRow.getDataSize() - 1) + " = '" + myTableRow.getValue(myTableRow.getDataSize() - 1).get() + "' ");

        return deleteString.toString();
    }

    public void addTableRow(Table table, MyTableRow myTableRow) throws SQLException {
        String insertString = getInsertString(table, myTableRow);

        Statement statement = connection.createStatement();
        statement.executeUpdate(insertString);

        table.addRow(myTableRow);
    }

    private String getInsertString(Table table, MyTableRow myTableRow) {
        StringBuilder insertString = new StringBuilder("INSERT INTO " + table.getName() + " VALUES (");

        for (int i = 0; i < myTableRow.getDataSize() - 1; i++) {
            insertString.append("'" + myTableRow.getValue(i).getValue() + "', ");
        }

        insertString.append("'" + myTableRow.getValue(myTableRow.getDataSize() - 1).getValue() + "')");

        return insertString.toString();
    }

    private void initPrimaryKeys(Table table) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet resultSet = meta.getPrimaryKeys(schema, null, table.getName());

        List<String> columns = new ArrayList<>();
        String pkName = null;
        while (resultSet.next()) {
            pkName = resultSet.getString("PK_NAME");
            String columnName = resultSet.getString("COLUMN_NAME");

            columns.add(columnName);
        }

        table.addColumnPkConstraint(columns, pkName);
    }

    private void initForeignKeys(Table table) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet foreignKeys = meta.getImportedKeys(connection.getCatalog(), null, table.getName());


        while (foreignKeys.next()) {
            String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
            String pkTableName = foreignKeys.getString("PKTABLE_NAME");
            String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");

            String query = "SELECT constraint_name FROM user_cons_columns WHERE table_name = '" +
                    table.getName() + "' AND column_name = '" + fkColumnName + "' AND position = 1";

            Statement statement = connection.createStatement();
            ResultSet fkNames = statement.executeQuery(query);
            fkNames.next();

            table.setColumnFk(fkColumnName, pkTableName, pkColumnName, fkNames.getString(1));
        }
    }

    public void dropColumn(Table table, String columnName) throws SQLException {
        String dropColumnString = "ALTER TABLE " + table.getName() + " DROP COLUMN " + columnName;
        Statement statement = connection.createStatement();
        statement.executeUpdate(dropColumnString);

        table.dropColumn(columnName);
    }

    public void updateColumns(Table table, List<String> columnsNames, List<String> columnsTypes) throws SQLException {
        if (table.getColumns().size() < columnsNames.size()) {
            for (int i = table.getColumns().size(); i < columnsNames.size(); i++){
                addNewColumn(table, columnsNames.get(i), columnsTypes.get(i));
            }
        }


        for (int i = 0; i < columnsNames.size(); i++) {
            if (!table.getColumns().get(i).getName().equals(columnsNames.get(i))) {
                renameColumn(table, table.getColumnName(i), columnsNames.get(i));
            }

            if (!table.getColumns().get(i).getType().equals(columnsTypes.get(i))) {
                changeColumnType(table, table.getColumnName(i), columnsTypes.get(i));
            }
        }
    }

    public void addNewColumn(Table table, String columnName, String columnType) throws SQLException {
        String query = "ALTER TABLE " + table.getName() + " ADD ( " + columnName + " " + columnType + " )";
        Statement statement = connection.createStatement();
        statement.execute(query);

        table.addColumn(columnName, true, false, columnType, null);
    }

    public void renameColumn(Table table, String oldName, String newName) throws SQLException {
        String query = "ALTER TABLE " + table.getName() + " RENAME COLUMN " + oldName + " TO " + newName;
        Statement statement = connection.createStatement();
        statement.execute(query);

        table.renameColumn(oldName, newName);
    }

    public void changeColumnType(Table table, String columnName, String newType) throws SQLException {
        String query = "ALTER TABLE " + table.getName() + " MODIFY ( " + columnName + " " + newType + " )";
        Statement statement = connection.createStatement();
        statement.execute(query);

        table.setColumnType(columnName, newType);
    }

    public void updateDefault(Table table, List<String> defaultList) throws SQLException {
        for (int i = 0; i < defaultList.size(); i++) {
            if (!defaultList.get(i).equals(table.getColumns().get(i).getDefaultValue())) {
                setColumnDefault(table, table.getColumnName(i), defaultList.get(i));
            }
        }
    }

    public void setColumnDefault(Table table, String columnName, String defaultValue) throws SQLException {
        String query;
        if (defaultValue.equals("")) {
            query = "ALTER TABLE " + table.getName() + " MODIFY ( " + columnName + " DEFAULT NULL)";
        } else {
            query = "ALTER TABLE " + table.getName() + " MODIFY ( " + columnName + " DEFAULT '" + defaultValue + "' )";
        }
        Statement statement = connection.createStatement();
        statement.execute(query);

        table.setColumnDefault(columnName, defaultValue);
    }

    public void updateNotNull(Table table, List<Boolean> notNullList) throws SQLException {
        for (int i = 0; i < notNullList.size(); i++) {
            if (notNullList.get(i) != !table.getColumns().get(i).getIsNullable()) {
                setNotNull(table, table.getColumnName(i), notNullList.get(i));
            }
        }
    }

    public void setNotNull(Table table, String columnName, boolean value) throws SQLException {
        String query;
        if (value == true) {
            query = "ALTER TABLE " + table.getName() + " MODIFY ( " + columnName + " NOT NULL)";
        } else {
            query = "ALTER TABLE " + table.getName() + " MODIFY ( " + columnName + " NULL)";
        }

        Statement statement = connection.createStatement();
        statement.execute(query);

        table.setColumnNotNull(columnName, value);
    }

    public void updatePrimaryKeys(Table table, List<String> primaryKeys) throws SQLException {
        if (table.getPrimaryKeys().size() == 0 && primaryKeys.size() == 0)
            return;

        if (table.getPrimaryKeys().size() != 0 && primaryKeys.size() != 0) {
            dropPrimaryKeys(table);
            setPrimaryKeys(table, primaryKeys);
            return;
        }

        setPrimaryKeys(table, primaryKeys);
    }

    public void dropPrimaryKeys(Table table) throws SQLException {
        String query = "ALTER TABLE " + table.getName() + " DROP PRIMARY KEY";

        Statement statement = connection.createStatement();
        statement.execute(query);

        table.clearPrimaryKeys();
    }

    public void setPrimaryKeys(Table table, List<String> pk) throws SQLException {
        if (pk.size() == 0) {
            dropPrimaryKeys(table);
            return;
        }

        String pkConstraintName = table.getName() + "_pk";
        String query = "ALTER TABLE " + table.getName() + " ADD CONSTRAINT " + pkConstraintName + " PRIMARY KEY ( ";

        for (int i = 0; i < pk.size() - 1; i++) {
            query += pk.get(i) + ", ";
        }

        query += pk.get(pk.size() - 1) + " )";

        Statement statement = connection.createStatement();
        statement.execute(query);

        table.addColumnPkConstraint(pk, pkConstraintName);
    }

    public void updateForeignKeys(Table table, List<Pair<String, String>> foreignKeys) throws SQLException {
        for (int i = 0; i < foreignKeys.size(); i++) {
            Pair<String, String> pair = foreignKeys.get(i);
            MyTableColumn column = table.getColumns().get(i);

            if (!pair.getKey().equals(column.getFkTable()) || !pair.getValue().equals(column.getFkColumn()))
                setForeignKey(table, column, pair);
        }
    }

    public void setForeignKey(Table table, MyTableColumn column, Pair<String, String> foreignKey) throws SQLException {
        if (foreignKey.getKey().equals("") && foreignKey.getValue().equals("") && column.getFkColumn() == null && column.getFkTable() == null)
            return;

        if (foreignKey.getKey().equals("") && foreignKey.getValue().equals("") && column.getFkColumn() != null && column.getFkTable() != null) {
            dropForeignKey(table, column);
            return;
        }

        dropForeignKey(table, column);

        String fkName = table.getName() + "_" + column.getName() + "_fk";
        String query = "ALTER TABLE " + table.getName() + " ADD CONSTRAINT " + fkName + " FOREIGN KEY (" + column.getName()
                + ") REFERENCES " + foreignKey.getKey() + "( " + foreignKey.getValue() + " )";


        Statement statement = connection.createStatement();
        statement.execute(query);

        table.setColumnFk(column.getName(), foreignKey.getKey(), foreignKey.getValue(), fkName);
    }

    public void dropForeignKey(Table table, MyTableColumn column) throws SQLException {
        if (column.getFkColumn() == null && column.getFkTable() == null)
            return;

        String query = "ALTER TABLE " + table.getName() + " DROP CONSTRAINT " + column.getFkName();
        Statement statement = connection.createStatement();
        statement.execute(query);

        column.clearForeignKey();
    }

    public void dropTable(Table table) throws SQLException {
        String query = "DROP TABLE " + table.getName();
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);

        for (int i = 0; i < tablesNames.size(); i++) {
            if (table.getName().equals(tablesNames.get(i).get())) {
                tablesNames.remove(i);
                break;
            }
        }
    }

    public ResultSet workerExecuteQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        return resultSet;
    }

    public void workerExecute(String query) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(query);
    }

    public void createNewTable(Table table) throws SQLException {
        Statement statement = connection.createStatement();
        String query = getCreateTableString(table);
        System.out.println(query);
        statement.executeUpdate(query);
        connection.commit();

        tablesNames.add(new SimpleStringProperty(table.getName().toUpperCase()));
    }

    private String getCreateTableString(Table table) {
        StringBuilder createTableString = new StringBuilder("CREATE TABLE " + table.getName() + " ( ");

        for (int i = 0; i < table.getColumns().size() - 1; i++) {
            MyTableColumn column = table.getColumns().get(i);
            createTableString.append(column.getName() + " " + column.getType());

            if (!column.getIsNullable())
                createTableString.append(" NOT NULL");

            if (!column.getDefaultValue().equals(""))
                createTableString.append(" DEFAULT '" + column.getDefaultValue() + "'");

            createTableString.append(", ");
        }

        MyTableColumn column = table.getColumns().get(table.getColumns().size() - 1);
        createTableString.append(column.getName() + " " + column.getType());

        if (!column.getIsNullable())
            createTableString.append(" NOT NULL");

        if (!column.getDefaultValue().equals(""))
            createTableString.append(" DEFAULT '" + column.getDefaultValue() + "'");

        if (table.getPrimaryKeys().size() != 0) {
            createTableString.append(", CONSTRAINT " + table.getPkConstraintName() + " PRIMARY KEY (");

            for (int i = 0; i < table.getPrimaryKeys().size() - 1; i++) {
                MyTableColumn pkColumn = table.getPrimaryKeys().get(i);
                createTableString.append(pkColumn.getName() + ", ");
            }

            MyTableColumn pkColumn = table.getPrimaryKeys().get(table.getPrimaryKeys().size() - 1);
            createTableString.append(pkColumn.getName() + ")");
        }

        for (int i = 0; i < table.getColumns().size(); i++) {
            column = table.getColumns().get(i);

            if (column.getFkName() != null && !column.getFkName().equals("")) {
                createTableString.append(", FOREIGN KEY (" + column.getName() + ") REFERENCES " + column.getFkTable() +
                        "(" + column.getFkColumn() + ")");
            }
        }

        createTableString.append(" )");

        return createTableString.toString();
    }
}
