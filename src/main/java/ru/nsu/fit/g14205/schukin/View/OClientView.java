package ru.nsu.fit.g14205.schukin.View;

import ru.nsu.fit.g14205.schukin.Model.OClientModelInterface;
import ru.nsu.fit.g14205.schukin.Presenter.OClientPresenterInterface;

import javax.swing.*;

/**
 * Created by kannabi on 10.06.2017.
 */
public class OClientView extends JFrame implements OClientViewInterface {
    private OClientPresenterInterface presenter;
    private OClientModelInterface model;

    private JTabbedPane tabbedPane1;
    private JList list1;
    private JTabbedPane tabbedPane2;
    private JTextArea textArea1;
    private JButton button1;
    private JPanel contentPane;
    private JPanel tableViewTab;

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

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
}
