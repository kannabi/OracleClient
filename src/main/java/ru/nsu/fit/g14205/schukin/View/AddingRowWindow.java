package ru.nsu.fit.g14205.schukin.View;

import ru.nsu.fit.g14205.schukin.Model.OClientModelInterface;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kannabi on 12.06.2017.
 */
public class AddingRowWindow extends JDialog {
    private JButton okButton = new JButton("Ok");;
    private JButton cancelButton = new JButton("Cancel");
    LinkedList<JTextField> fieldsList = new LinkedList<>();
    OClientModelInterface model;
    OClientView parent;

    public AddingRowWindow(List<String> columns, OClientModelInterface model, OClientView parent){
        setTitle("Add row");
        setModal(true);
        getRootPane().setDefaultButton(okButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.model = model;
        this.parent = parent;

        JPanel main = new JPanel(new BorderLayout());

        GridLayout grid = new GridLayout(0, 2);
        JPanel fields = new JPanel(grid);

        JPanel buttonsPanel = new JPanel(new GridLayout(0, 2));
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);

        okButton.addActionListener((e -> onOk()));
        cancelButton.addActionListener((e -> onCancel()));

        JTextField textField;
        for (String f : columns){
            fields.add(new JLabel(f));
            textField = new JTextField();
            fields.add(textField);
            fieldsList.add(textField);
        }

        main.add(fields, BorderLayout.CENTER);
        main.add(buttonsPanel, BorderLayout.SOUTH);

        setContentPane(main);
        pack();
        setResizable(false);
        setVisible(true);
    }

    private void onOk(){
        model.addRow(fieldsList.stream().map(JTextComponent::getText).collect(Collectors.toList()));
        parent.refreshDataTable();
        dispose();
    }

    private void onCancel(){
        dispose();
    }
}
