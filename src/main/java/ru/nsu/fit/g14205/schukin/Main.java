package ru.nsu.fit.g14205.schukin;

import ru.nsu.fit.g14205.schukin.Model.OClientModel;
import ru.nsu.fit.g14205.schukin.Model.OClientModelInterface;
import ru.nsu.fit.g14205.schukin.Presenter.OClientPresenter;
import ru.nsu.fit.g14205.schukin.Presenter.OClientPresenterInterface;
import ru.nsu.fit.g14205.schukin.Utils.EDTInvocationHandler;
import ru.nsu.fit.g14205.schukin.View.OClientView;
import ru.nsu.fit.g14205.schukin.View.OClientViewInterface;

/**
 * Created by kannabi on 10.06.2017.
 */
public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                OClientPresenterInterface presenter = new OClientPresenter();
                OClientModelInterface model = new OClientModel();
                OClientViewInterface view = (OClientViewInterface) java.lang.reflect.Proxy.newProxyInstance(getClass().getClassLoader(),
                                        new Class[]{OClientViewInterface.class}, new EDTInvocationHandler<>(new OClientView()));

                presenter.setModel(model);
                presenter.setView(view);
                presenter.run();
            }
        });
        
    }
}
