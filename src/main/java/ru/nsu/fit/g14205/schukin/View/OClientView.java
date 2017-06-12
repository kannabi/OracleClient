package ru.nsu.fit.g14205.schukin.View;

import ru.nsu.fit.g14205.schukin.Model.OClientModelInterface;
import ru.nsu.fit.g14205.schukin.Presenter.OClientPresenterInterface;
import ru.nsu.fit.g14205.schukin.Utils.RequiresEDT;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kannabi on 10.06.2017.
 */
public class OClientView extends JFrame implements OClientViewInterface {
    private OClientPresenterInterface presenter;
    private volatile OClientModelInterface model;

    private final int NO_ONE_ROW_SELECTED = -1;

    private JTabbedPane tabbedPane1;
    private JList<String> tableNameList;
    private JTabbedPane tabbedPane2;
    private JTextArea queryTextArea;
    private JButton executeQeuryButton;
    private JPanel contentPane;
    private JPanel tableViewTab;
    private JTable dataTable;
    private JPanel tableDataPanel;
    private JTextArea queryResultTextArea;
    private JTabbedPane tabbedPane3;
    private JTable queryResultTable;
    private JButton dropTableButton;
    private JButton deleteRowButton;
    private JButton addRowButton;
    private JButton refreshButton;
    private JButton applyChangesButton;

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

        setUpTableViewTab();
        setUpQueryTab();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);

        queryTextArea.setFont(queryTextArea.getFont().deriveFont(16f));
        queryResultTextArea.setFont(queryResultTextArea.getFont().deriveFont(16f));

//        pack();
        setSize(new Dimension(800, 600));
        setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        tableListModel = new DefaultListModel<>();
        tableNameList = new JList<>(tableListModel);

        dataTable = new JTable()
        {
            private static final long serialVersionUID = 1L;

//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }

            @Override
            public void setValueAt(Object aValue, int row, int column){
                List<String> oldData = new ArrayList<>();
                for (int i = 0; i < dataTable.getColumnCount(); ++i){
                    oldData.add((String) dataTable.getValueAt(row, i));
                }
                super.setValueAt(aValue, row, column);
                List<String> newData = new ArrayList<>();
                for (int i = 0; i < dataTable.getColumnCount(); ++i){
                    newData.add((String) dataTable.getValueAt(row, i));
                }
                System.out.println(newData);
                model.updateRow(dataTable.getSelectedRow(),oldData, newData);
                refreshDataTable();
            }
        };
    }

    /*
    * Секция с функция для tableview таба
    * */
    private void setUpTableViewTab(){
        List<String> tableNames = model.getTablesName();
        for (String n : tableNames) {
            tableListModel.addElement(n);
        }

        tableNameList.getSelectionModel().
                setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableNameList.getSelectionModel().addListSelectionListener((e) ->{
            if(e.getValueIsAdjusting()) {
                loadTableOnDataPane(tableNameList.getSelectedValue());
            }
        });

        deleteRowButton.addActionListener((e) -> deleteSelectedRow(dataTable.getSelectedRow()));

        refreshButton.addActionListener((e) -> refreshDataTable());

        addRowButton.addActionListener((e) ->
                new AddingRowWindow(model.getTableColumnsName(tableNameList.getSelectedValue()), model, this));
    }

    private void loadTableOnDataPane(String tableName){
        new Thread(() ->{
            List<Vector> res = castToList(model.getTableColumnsName(tableName), model.getTableRows(tableName));
            repaintTable(res.get(0), res.get(1), dataTable);
        }).start();
    }

    private void deleteSelectedRow(int index){
        System.out.println(index);
        if (index != NO_ONE_ROW_SELECTED){
//            new Thread(()->{
                model.deleteRow(index);
                loadTableOnDataPane(tableNameList.getSelectedValue());
//            });
        }
    }


    /*
    * Конец секции для tableview таба
    * */

    /*
    * Секция для query таба
    * */

    private void setUpQueryTab(){
        executeQeuryButton.addActionListener((e)->{
            new Thread(() ->{
                List<Object> res = model.executeQeury(queryTextArea.getText());

                setQueryResultText((String)res.get(0));
                if (res.size() > 1){
                    List<Vector> data = castToList((List<String>)res.get(1), (List<List<String>>) res.get(2));
                    repaintTable(data.get(0),
                                    data.get(1),
                                    queryResultTable);
                }
            }).start();
        });
    }

    @RequiresEDT
    private void setQueryResultText(String text){
        queryResultTextArea.setText(text);
        queryResultTextArea.repaint();
    }

    /*
    * Конец секции для query таба
    * */


    private List<Vector> castToList(List<String> columns, List<List<String>> data){
        List<Vector> res = new LinkedList<>();

        res.add(new Vector<>(columns.stream().
                map(o -> (Object)o).collect(Collectors.toList())));

        res.add(new Vector<>(data.stream().
                map(o -> new Vector<>((o.stream().map(i -> (Object) i)).collect(Collectors.toList())))
                        .collect(Collectors.toList())));

        return res;
    }

    @RequiresEDT
    private void repaintTable(Vector<Object> columns, Vector<Vector<Object>> data, JTable table){
        System.out.println(columns);
        table.setModel(new DefaultTableModel(data, columns));
        table.repaint();
    }

    void refreshDataTable(){
        loadTableOnDataPane(tableNameList.getSelectedValue());
    }
}
