package ru.nsu.fit.g14205.schukin.DatabaseWorker.Table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MyTableRow {
    private ObservableList<StringProperty> data;
    private ObservableList<StringProperty> newData;

    public MyTableRow() {
        data = FXCollections.observableArrayList();
        newData = FXCollections.observableArrayList();
    }

    public void addValue(String value) {
        data.add(new SimpleStringProperty(value));
        newData.add(new SimpleStringProperty(value));
    }

    public StringProperty getValue(int index) {
        return data.get(index);
    }

    public StringProperty getNewValue(int index) {
        return newData.get(index);
    }

    public void setValue(int index, String value) {
        data.get(index).setValue(value);
    }

    public void setNewValue(int index, String value) {
        newData.get(index).setValue(value);
    }

    public int getDataSize() {
        return data.size();
    }

    public void commitNewData() {
        data.clear();

        for (StringProperty value : newData) {
            data.add(new SimpleStringProperty(value.get()));
        }

        return;
    }

    public void deleteData(int index) {
        data.remove(index);
        newData.remove(index);

        return;
    }
}