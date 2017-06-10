package ru.nsu.fit.g14205.schukin.Model;

import ru.nsu.fit.g14205.schukin.Presenter.OClientPresenterInterface;
import ru.nsu.fit.g14205.schukin.View.OClientViewInterface;

/**
 * Created by kannabi on 10.06.2017.
 */
public class OClientModel implements OClientModelInterface {
    OClientPresenterInterface presenter;
    OClientViewInterface view;

    public OClientModel() {
    }

    public void setPresenter(OClientPresenterInterface presenter){
        this.presenter = presenter;

    }

    public void setView(OClientViewInterface view){
        this.view = view;
    }

    public boolean loginDatabase(){

    }
}
