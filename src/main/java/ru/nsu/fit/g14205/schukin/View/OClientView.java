package ru.nsu.fit.g14205.schukin.View;

import ru.nsu.fit.g14205.schukin.Model.OClientModelInterface;
import ru.nsu.fit.g14205.schukin.Presenter.OClientPresenterInterface;

import javax.swing.*;
import java.util.List;

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

    DefaultListModel<String> tableListModel;

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
        pack();
        setVisible(true);
    }

    public void setUpTableNameList(){
        List<String> tableNames = model.getTablesName();
        for (String n : tableNames) {
            tableListModel.addElement(n);
            System.out.println(n);
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        tableListModel = new DefaultListModel<>();
        tableNameList = new JList<>(tableListModel);

        String[] columnNames = {"First Name",
                "Last Name",
                "Sport",
                "# of Years",
                "Vegetarian"};

        Object[][] data = {
                {"Kathy", "Smith",
                        "Snowboarding", new Integer(5), new Boolean(false)},
                {"John", "Doe",
                        "Rowing", new Integer(3), new Boolean(true)},
                {"Sue", "Black",
                        "Knitting", new Integer(2), new Boolean(false)},
                {"Jane", "White",
                        "Speed reading", new Integer(20), new Boolean(true)},
                {"Joe", "Brown",
                        "Pool", new Integer(10), new Boolean(false)}
        };
        dataTable = new JTable(data, columnNames);
    }
}
