package ru.nsu.fit.g14205.schukin.View;

import ru.nsu.fit.g14205.schukin.Presenter.OClientPresenterInterface;

import javax.swing.*;
import java.awt.event.*;

import static javax.swing.SwingUtilities.updateComponentTreeUI;

public class LoginWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField portField;
    private JTextField loginField;
    private JTextField ipField;
    private JPasswordField passwordField;

    private OClientPresenterInterface presenter;

    public LoginWindow(OClientPresenterInterface presenter) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.presenter = presenter;

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

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        ipField.setText("localhost");
        portField.setText("1251");
        loginField.setText("kannabii");
        passwordField.setText("kan");

        pack();
        setResizable(false);
        setVisible(true);
    }

    private void onOK() {
        presenter.setLoginParams(ipField.getText(), portField.getText(), loginField.getText(), passwordField.getText());

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
