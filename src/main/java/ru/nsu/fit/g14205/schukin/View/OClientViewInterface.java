package ru.nsu.fit.g14205.schukin.View;

import ru.nsu.fit.g14205.schukin.Model.OClientModelInterface;
import ru.nsu.fit.g14205.schukin.Presenter.OClientPresenterInterface;

/**
 * Created by kannabi on 10.06.2017.
 */
public interface OClientViewInterface {
    void setModel(OClientModelInterface model);

    void setPresenter(OClientPresenterInterface presenter);

    void initUI();
}
