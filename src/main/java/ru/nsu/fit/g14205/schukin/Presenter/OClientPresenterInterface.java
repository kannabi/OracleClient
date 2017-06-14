package ru.nsu.fit.g14205.schukin.Presenter;

import ru.nsu.fit.g14205.schukin.Model.OClientModelInterface;
import ru.nsu.fit.g14205.schukin.View.OClientViewInterface;

/**
 * Created by kannabi on 10.06.2017.
 */
public interface OClientPresenterInterface {

    void setModel(OClientModelInterface model);

    void setView(OClientViewInterface view);

    void run();

    void setLoginParams(String ip, String port, String login, String password);
}
