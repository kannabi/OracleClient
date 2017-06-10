package ru.nsu.fit.g14205.schukin.Presenter;

import ru.nsu.fit.g14205.schukin.Model.OClientModelInterface;
import ru.nsu.fit.g14205.schukin.View.OClientViewInterface;

/**
 * Created by kannabi on 10.06.2017.
 */
public class OClientPresenter implements OClientPresenterInterface {
    OClientViewInterface view;
    OClientModelInterface model;

    public void setModel(OClientModelInterface model){
        this.model = model;
    }

    public void setView(OClientViewInterface view){
        this.view = view;
    }

    public void run(){
        model.setPresenter(this);
        view.setPresenter(this);
        view.setModel(model);
        view.initUI();
    }
}
