package ru.nsu.fit.g14205.schukin.View;

import oracle.jdbc.internal.OracleTypeMetaData;
import ru.nsu.fit.g14205.schukin.Model.OClientModelInterface;
import ru.nsu.fit.g14205.schukin.Presenter.OClientPresenterInterface;
import ru.nsu.fit.g14205.schukin.View.Utils.RequiresEDT;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static javax.swing.SwingUtilities.updateComponentTreeUI;

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
    private JPanel configurationPanel;
    private JTable configurationTable;
    private JTable foreignKeysTable;
    private JButton addForeignKeyButton;
    private JButton addFieldButton;

    private DefaultListModel<String> tableListModel;
    private TableModel tableDataTableModel;
//    private List<String> configurationColumnNames = new LinkedList<>(Arrays.asList("Column name",
//                                                                "Column type",
//                                                                "Default value",
//                                                                "Primary key",
//                                                                "Not null",
//                                                                "Foreign key(to table)",
//                                                                "Foreign key (to column)",
//                                                                " "));
    private List<String> configurationColumnNames = new LinkedList<>(Arrays.asList(
        "Column name",
        "Column type",
        "Default value",
        "Primary key",
        "Not null",
        " "));

    private List<String> foreignKeyColumnsName = new LinkedList<>(Arrays.asList(
            "Column name",
            "Key name",
            "Foreign key(to table)",
            "Foreign key (to column)",
            " "));


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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            updateComponentTreeUI(this);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }


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
                model.updateRow(dataTable.getSelectedRow(), oldData, newData);
                refreshDataTable();
            }
        };

        configurationTable = new JTable(){
            private static final long serialVersionUID = 1L;

            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 3:
                        return Boolean.class;
                    case 4:
                        return Boolean.class;
//                    case 7:
//                        return JButton.class;
                    default:
                        return String.class;
                }
            }

            @Override
            public void setValueAt(Object aValue, int row, int column){
                super.setValueAt(aValue, row, column);
                switch (column){
                    case 4:
                        model.setNotNull((String) this.getValueAt(row, 0), (Boolean) aValue);
                }
            }
        };

        foreignKeysTable = new JTable(){
            private static final long serialVersionUID = 1L;

            @Override
            public void setValueAt(Object aValue, int row, int column){
                super.setValueAt(aValue, row, column);
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

        addFieldButton.addActionListener((e) -> new AddingField(model, this));
    }

    private void loadTableOnDataPane(String tableName){
        new Thread(() ->{
            List<Vector> res = castToList(model.getTableColumnsName(tableName), model.getTableRows(tableName));
            repaintTable(res.get(0), res.get(1), dataTable);

            res = castToList(configurationColumnNames, new LinkedList<>());

            Vector<Vector<Object>> v = new Vector<>(model.getTableMetaData().stream().
                    map(o -> {
                        ArrayList<Object> r = new ArrayList<>(o);
                        r.add("Delete");
                        return new Vector<>(r);
                    })
                    .collect(Collectors.toList()));

            repaintTable(res.get(0), v, configurationTable);

            Action deleteField = new AbstractAction()
            {
                public void actionPerformed(ActionEvent e)
                {
                    new Thread(() ->{
                        JTable table = (JTable)e.getSource();
                        int modelRow = Integer.valueOf(e.getActionCommand());
                        model.deleteField((String) table.getModel().getValueAt(modelRow, 0));
//                        ((DefaultTableModel)table.getModel()).removeRow(modelRow);
                        refreshDataTable();
                    }).start();
                }
            };

            ButtonColumn buttonColumnConfig = new ButtonColumn(configurationTable, deleteField, 5);

            res = castToList(foreignKeyColumnsName, new LinkedList<>());
            v = new Vector<>(model.getTableForeignKeys().stream().
                    map(o -> {
                        ArrayList<Object> r = new ArrayList<>(o);
                        r.add("Delete");
                        return new Vector<>(r);
                    })
                    .collect(Collectors.toList()));
            repaintTable(res.get(0), v, foreignKeysTable);

            Action deleteForeignKey = new AbstractAction()
            {
                public void actionPerformed(ActionEvent e)
                {
                    JTable table = (JTable)e.getSource();
                    int modelRow = Integer.valueOf(e.getActionCommand());
                    ((DefaultTableModel)table.getModel()).removeRow(modelRow);
                }
            };

            ButtonColumn buttonForeignKey = new ButtonColumn(foreignKeysTable, deleteForeignKey, 4);
        }).start();
    }

    private void deleteSelectedRow(int index){
        System.out.println(index);
        if (index != NO_ONE_ROW_SELECTED){
            new Thread(()->{
                model.deleteRow(index);
                loadTableOnDataPane(tableNameList.getSelectedValue());
            }).start();
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
        table.setModel(new DefaultTableModel(data, columns));
        table.repaint();
    }

    void refreshDataTable(){
        loadTableOnDataPane(tableNameList.getSelectedValue());
    }
}
