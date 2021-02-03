package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(@Nullable Context context) {
        super(context, "180053M.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

            String accountTablequery = "CREATE TABLE account_table (account_no TEXT PRIMARY KEY, bank_name TEXT NOT NULL, account_holder_name TEXT NOT NULL, balance REAL NOT NULL)";
            String transactionTablequery = "CREATE TABLE transaction_table (id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT NOT NULL, account_no TEXT NOT NULL, expense_type TEXT NOT NULL, amount REAL NOT NULL, FOREIGN KEY(account_no) REFERENCES account_table(account_no))";
            sqLiteDatabase.execSQL(accountTablequery);
            sqLiteDatabase.execSQL(transactionTablequery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
