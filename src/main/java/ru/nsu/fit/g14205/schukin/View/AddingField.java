package ru.nsu.fit.g14205.schukin.View;

import ru.nsu.fit.g14205.schukin.Model.OClientModelInterface;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kannabi on 13.06.2017.
 */
public class AddingField extends JDialog {
    private JButton okButton = new JButton("Ok");;
    private JButton cancelButton = new JButton("Cancel");
    LinkedList<JTextField> fieldsList = new LinkedList<>();
    OClientModelInterface model;
    OClientView parent;

    public AddingField(OClientModelInterface model, OClientView parent) {
        setTitle("Add field");
        setModal(true);
        getRootPane().setDefaultButton(okButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.model = model;
        this.parent = parent;

        JPanel main = new JPanel(new BorderLayout());

        JPanel buttonsPanel = new JPanel(new GridLayout(0, 2));
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);

        okButton.addActionListener((e -> onOk()));
        cancelButton.addActionListener((e -> onCancel()));

        GridLayout grid = new GridLayout(0, 2);
        JPanel fields = new JPanel(grid);

        JTextField textField;
        fields.add(new JLabel("Name"));
        textField = new JTextField();
        fields.add(textField);
        fieldsList.add(textField);

        fields.add(new JLabel("Type"));
        textField = new JTextField();
        fields.add(textField);
        fieldsList.add(textField);


        main.add(fields, BorderLayout.CENTER);
        main.add(buttonsPanel, BorderLayout.SOUTH);

        setContentPane(main);
        pack();
        setResizable(false);
        setVisible(true);
    }

    private void onOk(){
        model.addField(fieldsList.get(0).getText(), fieldsList.get(1).getText());
        parent.refreshDataTable();
        dispose();
    }

    private void onCancel(){
        dispose();
    }
}
