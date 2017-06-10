package ru.nsu.fit.g14205.schukin.Model;

import ru.nsu.fit.g14205.schukin.Presenter.OClientPresenterInterface;
import ru.nsu.fit.g14205.schukin.View.OClientViewInterface;

/**
 * Created by kannabi on 10.06.2017.
 */
public interface OClientModelInterface {
    void setView(OClientViewInterface view);

    void setPresenter(OClientPresenterInterface presenter);

    boolean loginDatabase(String ip,
                          String port,
                          String login,
                          String password,
                          String schema);
}
