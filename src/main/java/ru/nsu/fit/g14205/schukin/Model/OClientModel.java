package ru.nsu.fit.g14205.schukin.Model;

import ru.nsu.fit.g14205.schukin.DatabaseWorker.DatabaseWorker;
import ru.nsu.fit.g14205.schukin.Presenter.OClientPresenterInterface;
import ru.nsu.fit.g14205.schukin.View.OClientViewInterface;

/**
 * Created by kannabi on 10.06.2017.
 */
public class OClientModel implements OClientModelInterface {
    OClientPresenterInterface presenter;
    OClientViewInterface view;
    DatabaseWorker worker;

    public OClientModel() {
    }

    public void setPresenter(OClientPresenterInterface presenter){
        this.presenter = presenter;

    }

    public void setView(OClientViewInterface view){
        this.view = view;
    }

    public boolean loginDatabase(String ip,
                                 String port,
                                 String login,
                                 String password,
                                 String schema){
        return worker.login(ip, port, login, password, schema);
    }
}
