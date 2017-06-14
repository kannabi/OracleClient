package ru.nsu.fit.g14205.schukin.View;

import ru.nsu.fit.g14205.schukin.Model.OClientModelInterface;
import ru.nsu.fit.g14205.schukin.View.Utils.RequiresEDT;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

public class AddingNewTable extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTabbedPane tabbedPane1;
    private JButton addKeyButton;
    private JTable foreignKeysTable;
    private JTable fieldsTable;
    private JButton addFieldButton;
    private JTextField tableNameField;

    OClientModelInterface model;
    OClientView parent;

    private Vector<Object> fieldsColumnNames = new Vector<>(Arrays.asList(
            "Field name",
            "Field type",
            "Default value",
            "Primary key",
            "Not null",
            " "));

    private Vector<Object> emptyFieldRow = new Vector<>(Arrays.asList("", "", "", false, false, "Delete"));
//    private Vector<Object> emptyFieldRow = new Vector<>(Arrays.asList(null, null, null, false, false, "Delete"));

    private Vector<Object> keysColumnNames = new Vector<>(Arrays.asList("Field name", "To table", "To column", "FK name", " "));

    private Vector<Object> emptyKeysRow = new Vector<>(Arrays.asList("", "", "", "", "Delete"));

    public AddingNewTable(OClientModelInterface model, OClientView parent) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.model = model;
        this.parent = parent;

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setUpTables();
        setUpButtons();

        setSize(new Dimension(600, 400));
        setResizable(false);
        setVisible(true);
    }

    private void setUpTables(){
        Vector<Vector<Object>> v = new Vector<>();
        v.add(emptyFieldRow);
        fieldsTable.setModel(new DefaultTableModel(v, fieldsColumnNames));

        System.out.println(fieldsColumnNames);
        System.out.println(v);

        Action deleteField = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                new Thread(() ->{
                    JTable table = (JTable)e.getSource();
                    int modelRow = Integer.valueOf(e.getActionCommand());
                    ((DefaultTableModel)table.getModel()).removeRow(modelRow);
                }).start();
            }
        };

        ButtonColumn buttonColumnConfig = new ButtonColumn(fieldsTable, deleteField, 5);

        v = new Vector<>();
        v.add(emptyKeysRow);

        foreignKeysTable.setModel(new DefaultTableModel(v, keysColumnNames));

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

        repaintFieldTable();
        repaintForeignTable();
    }

    private void setUpButtons(){
        addFieldButton.addActionListener((e) ->{
            ((DefaultTableModel)fieldsTable.getModel()).addRow(new Vector<>(Arrays.asList("", "", "", false, false, "Delete")));
            repaintFieldTable();
        });

        addKeyButton.addActionListener((e) ->{
            ((DefaultTableModel)foreignKeysTable.getModel()).addRow(new Vector<>(Arrays.asList("", "", "", "", "Delete")));
            repaintForeignTable();
        });
    }

    private void onOK() {
        List<List<Object>> columns = new LinkedList<>();
        List<List<String>> foreignKeys = new LinkedList<>();
        List<Object> buf;
        for(int i = 0; i < fieldsTable.getModel().getRowCount(); ++i){
            buf = new LinkedList<>();
            for(int j = 0; j < fieldsTable.getModel().getColumnCount() - 1; ++j){
                buf.add(fieldsTable.getValueAt(i, j));
            }
            columns.add(buf);
        }

        List<String> fkBuf;
        for (int i = 0; i < foreignKeysTable.getModel().getRowCount(); ++i){
            fkBuf = new LinkedList<>();
            for (int j = 0; j < foreignKeysTable.getModel().getColumnCount() - 1; ++j)
                fkBuf.add((String) foreignKeysTable.getValueAt(i, j));
            foreignKeys.add(fkBuf);
        }

        model.createTable(tableNameField.getText(), columns, foreignKeys);
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        foreignKeysTable = new JTable();

        fieldsTable = new JTable(){
            private static final long serialVersionUID = 1L;

            @Override
            public Class getColumnClass(int column) {
                return column == 3 || column == 4 ? Boolean.class : String.class;
            }
        };
    }

    @RequiresEDT
    private void repaintForeignTable(){
        foreignKeysTable.repaint();
    }

    @RequiresEDT
    private void repaintFieldTable(){
        fieldsTable.repaint();
    }
}
