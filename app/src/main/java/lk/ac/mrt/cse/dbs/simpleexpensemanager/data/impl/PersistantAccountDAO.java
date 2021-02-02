package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class PersistantAccountDAO  implements AccountDAO {

    public static final String ACCOUNT_TABLE = "account_table";
    public static final String ACCOUNT_NO = "account_no";
    public static final String BANK_NAME = "bank_name";
    public static final String ACCOUNT_HOLDER_NAME = "account_holder_name";
    public static final String BALANCE = "balance";
    private DatabaseHelper dh;
    public PersistantAccountDAO(DatabaseHelper dh){
        this.dh=dh;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> resultList = new ArrayList<>();

        String query = "SELECT "+ ACCOUNT_NO +" FROM " + ACCOUNT_TABLE ;
        //SQLiteDatabase db = this.getReadableDatabase();
        SQLiteDatabase db = dh.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()){
            do{
                String acnNo = cursor.getString(0);
                resultList.add(acnNo);

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return resultList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> resultList = new ArrayList<>();

        String query = "SELECT * FROM " + ACCOUNT_TABLE;
        //SQLiteDatabase db = this.getReadableDatabase();
        SQLiteDatabase db = dh.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()){
            do{
                String acnNo = cursor.getString(0);
                String bankName = cursor.getString(1);
                String userName = cursor.getString(2);
                double balance = cursor.getDouble(3);

                Account acc = new Account(acnNo,bankName,userName,balance);
                resultList.add(acc);

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return resultList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String query = "SELECT * FROM " + ACCOUNT_TABLE + " WHERE "+ACCOUNT_NO+" = "+accountNo;
        //SQLiteDatabase db = this.getReadableDatabase();
        SQLiteDatabase db = dh.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.isNull(0) == false){
            String acnNo = cursor.getString(0);
            String bankName = cursor.getString(1);
            String userName = cursor.getString(2);
            double balance = cursor.getDouble(3);

            Account acc = new Account(acnNo,bankName,userName,balance);
            return acc;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
   }

    @Override
    public void addAccount(Account account) {
        //SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = dh.getWritableDatabase();
        ContentValues values  = new ContentValues();

        values.put(ACCOUNT_NO,account.getAccountNo());
        values.put(BANK_NAME,account.getBankName());
        values.put(ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        values.put(BALANCE,account.getBalance());

        long ret = db.insert("ACCOUNT_TABLE", null, values);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        //SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = dh.getReadableDatabase();
        String query = "DELETE FROM " + ACCOUNT_TABLE + " WHERE " +ACCOUNT_NO+ " = " + accountNo;
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        String query1 = "SELECT "+BALANCE+" FROM " + ACCOUNT_TABLE + " WHERE "+ACCOUNT_NO+" = "+accountNo;
        //SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db = dh.getReadableDatabase();
        Cursor cursor = db.rawQuery(query1,null);
        double balance;
        if (cursor.moveToFirst()){
            balance = cursor.getFloat(0);
            switch (expenseType) {
                case EXPENSE:
                    balance -= amount;
                case INCOME:
                    balance+= amount;
            }
        } else {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        cursor.close();
        String query2 = "UPDATE "+ACCOUNT_TABLE+ " SET " +BALANCE +" = "+ Double.toString(balance) +" WHERE "+ ACCOUNT_NO+ " = "+ accountNo;
        Cursor cursor1 =db.rawQuery(query2,null);
        cursor1.close();
        db.close();
    }

}
