package ru.nsu.fit.g14205.schukin.View;

import org.omg.CORBA.VersionSpecHelper;
import ru.nsu.fit.g14205.schukin.Model.OClientModelInterface;
import ru.nsu.fit.g14205.schukin.Presenter.OClientPresenterInterface;
import ru.nsu.fit.g14205.schukin.Utils.RequiresEDT;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kannabi on 10.06.2017.
 */
public class OClientView extends JFrame implements OClientViewInterface {
    private OClientPresenterInterface presenter;
    private OClientModelInterface model;

    private JTabbedPane tabbedPane1;
    private JList<String> tableNameList;
    private JTabbedPane tabbedPane2;
    private JTextArea textArea1;
    private JButton button1;
    private JPanel contentPane;
    private JPanel tableViewTab;
    private JTable dataTable;
    private JPanel tableDataPanel;

    DefaultListModel<String> tableListModel;
    TableModel tableDataTableModel;

    public OClientView() {
    }


    public void setModel(OClientModelInterface model){
        this.model = model;
    }

    public void setPresenter(OClientPresenterInterface presenter){
        this.presenter = presenter;
    }

    public void initUI(){
        setTitle("Oracle Client");

        setContentPane(contentPane);
        setUpTableNameList();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);
//        pack();
        setSize(new Dimension(800, 600));
        setVisible(true);
    }

    public void setUpTableNameList(){
        List<String> tableNames = model.getTablesName();
        for (String n : tableNames) {
            tableListModel.addElement(n);
            System.out.println(n);
        }

        tableNameList.getSelectionModel().
                        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableNameList.getSelectionModel().addListSelectionListener((e) ->{
//            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
//            System.out.println(e.getFirstIndex());
            if(e.getValueIsAdjusting()) {
//                System.out.println(tableNameList.getSelectedValue());
                loadTableOnDataPane(tableNameList.getSelectedValue());
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        tableListModel = new DefaultListModel<>();
        tableNameList = new JList<>(tableListModel);

        dataTable = new JTable();
        
    }

    private void loadTableOnDataPane(String tableName){
        Vector<Object> columns = new Vector<>(model.getTableColumnsName(tableName).stream().
                map(o -> (Object)o).collect(Collectors.toList()));
        Vector<Vector<Object>> data = new Vector<>(
                model.getTableRows(tableName).
                stream().map(o -> new Vector<>((o.stream().map(i -> (Object) i)).collect(Collectors.toList())))
                            .collect(Collectors.toList()));

//        for (List<String> row : model.getTableRows(tableName)){
//            data.add(row.stream().map(o -> (Object)o).collect(Collectors.toList()).toArray());
//        }

        System.out.println(data);
        repaintDataTable(columns, data);
    }

    @RequiresEDT
    private void repaintDataTable(Vector<Object> columns, Vector<Vector<Object>> data){
        dataTable.setModel(new DefaultTableModel(data, columns));
        dataTable.repaint();
//        dataTable = new JTable(data, columns);
    }
}
