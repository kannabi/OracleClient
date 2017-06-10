package ru.nsu.fit.g14205.schukin.DatabaseWorker;

import javafx.util.Pair;

public interface LoginInterface {
//    public boolean login(Pair<String, String> ipPort, Pair<String, String> logPass, String schema);
    boolean login(String ip,
                     String port,
                     String login,
                     String password,
                     String schema);

    boolean connectionEstablished();
}
