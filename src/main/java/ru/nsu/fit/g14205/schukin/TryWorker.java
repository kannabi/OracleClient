package ru.nsu.fit.g14205.schukin;

import ru.nsu.fit.g14205.schukin.DatabaseWorker.DatabaseWorker;
import ru.nsu.fit.g14205.schukin.DatabaseWorker.DatabaseWorkerInterface;
import ru.nsu.fit.g14205.schukin.DatabaseWorker.Table.MyTableColumn;
import ru.nsu.fit.g14205.schukin.DatabaseWorker.Table.MyTableRow;
import ru.nsu.fit.g14205.schukin.DatabaseWorker.Table.Table;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kannabi on 11.06.2017.
 */
public class TryWorker {
    public static void main(String[] args){
//        DatabaseWorkerInterface worker = new DatabaseWorker();
//        worker.login("localhost", "1251", "kannabii", "kan", "kannabii");
//        try {
//            System.out.println(worker.getTablesNames());
//            Table table = worker.getTable("marshrut");
//            System.out.println("======================================");
//            System.out.println(table.getName());
//            System.out.println("======================================");
//            for (MyTableColumn column : table.getColumns())
//                System.out.println(column.getName());
//            System.out.println("======================================");
//            for (MyTableRow row : table.getRows()){
//                for (int i = 0; i < row.getDataSize(); ++i)
//                    System.out.println(row.getValue(i));
//                System.out.println("======================================");
//            }
//        } catch (SQLException e){
//            e.printStackTrace();
//        }

        Boolean b = true;
        Object o = b;
        System.out.println(o);
    }
}
