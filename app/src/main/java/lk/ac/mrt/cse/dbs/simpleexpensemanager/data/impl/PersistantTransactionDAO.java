package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistantTransactionDAO implements TransactionDAO  {

    public static final String TRANSACTION_TABLE = "transaction_table";
    public static final String TRANACTION_ID = "id";
    public static final String DATE = "date";
    public static final String EXPENSE_TYPE = "expense_type";
    public static final String AMOUNT = "amount";
    public static final String ACCCOUNT_NO = "account_no";
    private DatabaseHelper dh;
    public PersistantTransactionDAO(DatabaseHelper dh) {
        this.dh=dh;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String pattern = "MM/dd/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        String String_date = df.format(date);
        String type = expenseType.toString();



        SQLiteDatabase db = dh.getWritableDatabase();
        ContentValues values  = new ContentValues();

        values.put(DATE,String_date);
        values.put(ACCCOUNT_NO,accountNo);
        values.put(EXPENSE_TYPE,type);
        values.put(AMOUNT,amount);

        long ret = db.insert(TRANSACTION_TABLE, null, values);

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> resultList = new ArrayList<>();

        String query = "SELECT * FROM " + TRANSACTION_TABLE;
        SQLiteDatabase db = dh.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()){
            do{
                String id = cursor.getString(0);
                Date date = null;
                try {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String acnNo = cursor.getString(2);
                ExpenseType expenseType = ExpenseType.valueOf(cursor.getString(3));
                double amount = cursor.getDouble(4);

                Transaction tx = new Transaction(date,acnNo,expenseType,amount);
                resultList.add(tx);

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return resultList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        List<Transaction> transactions = getAllTransactionLogs();

        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }

}
