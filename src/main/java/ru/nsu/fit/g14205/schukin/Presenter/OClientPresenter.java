package ru.nsu.fit.g14205.schukin.Presenter;

import ru.nsu.fit.g14205.schukin.DatabaseWorker.DatabaseWorker;
import ru.nsu.fit.g14205.schukin.Model.OClientModelInterface;
import ru.nsu.fit.g14205.schukin.View.LoginWindow;
import ru.nsu.fit.g14205.schukin.View.OClientViewInterface;

import javax.swing.*;
import java.util.SplittableRandom;

/**
 * Created by kannabi on 10.06.2017.
 */
public class OClientPresenter implements OClientPresenterInterface {
    OClientViewInterface view;
    OClientModelInterface model;
    String ip, port, login, password;

    public void setModel(OClientModelInterface model){
        this.model = model;
    }

    public void setView(OClientViewInterface view){
        this.view = view;
    }

    public void run(){
        model.setPresenter(this);
        model.setDatabaseWorker(new DatabaseWorker());
        new LoginWindow(this);
        if (model.loginDatabase(ip, port, login, password, "test_schema")){
            view.setPresenter(this);
            view.setModel(model);
            view.initUI();
        } else {
            Thread dialog = new Thread(() -> JOptionPane.
                    showMessageDialog(null, "Sorry, u r try to login from Windows. Try to install OS."));
            dialog.start();
        }

//        view.setPresenter(this);
//        view.setModel(model);
//        view.initUI();
    }

    public void setLoginParams(String ip, String port, String login, String password){
        this.ip = ip;
        this.port = port;
        this.login = login;
        this.password = password;
    }
}
