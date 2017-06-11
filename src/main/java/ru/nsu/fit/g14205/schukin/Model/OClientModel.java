package ru.nsu.fit.g14205.schukin.Model;

import ru.nsu.fit.g14205.schukin.DatabaseWorker.DatabaseWorkerInterface;
import ru.nsu.fit.g14205.schukin.Presenter.OClientPresenterInterface;
import ru.nsu.fit.g14205.schukin.View.OClientViewInterface;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kannabi on 10.06.2017.
 */
public class OClientModel implements OClientModelInterface {
    OClientPresenterInterface presenter;
    OClientViewInterface view;
    DatabaseWorkerInterface worker;

    public OClientModel() {}

    public void setPresenter(OClientPresenterInterface presenter){
        this.presenter = presenter;
    }

    public void setDatabaseWorker(DatabaseWorkerInterface worker){
        this.worker = worker;
        worker.login("localhost", "1251", "kannabii", "kan", "kannabii");
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

    public List<String> getTablesName(){
        try {
            return worker.getTablesNames();
        } catch (SQLException e){
            e.printStackTrace();
            return new LinkedList<>();
        }
    }
}
