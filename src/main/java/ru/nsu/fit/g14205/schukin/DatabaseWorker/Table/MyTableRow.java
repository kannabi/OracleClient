package ru.nsu.fit.g14205.schukin.DatabaseWorker.Table;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class MyTableRow {
    @Getter
    private List<String> data;
    private List<String> newData;

    public MyTableRow() {
        data = new ArrayList<>();
        newData = new ArrayList<>();
    }

    public void addValue(String value) {
        data.add(value);
        newData.add(value);
    }

    public String getValue(int index) {
        return data.get(index);
    }

    public String getNewValue(int index) {
        return newData.get(index);
    }

    public void setValue(int index, String value) {
        data.set(index, value);
    }

    public void setNewValue(int index, String value) {
        newData.set(index, value);
    }

    public int getDataSize() {
        return data.size();
    }

    public void commitNewData() {
        data.clear();
        data.addAll(newData);
    }

    public void deleteData(int index) {
        data.remove(index);
        newData.remove(index);
    }
}