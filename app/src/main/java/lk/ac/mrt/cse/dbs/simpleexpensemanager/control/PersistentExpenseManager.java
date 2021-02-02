package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.app.Application;
import android.provider.ContactsContract;
import android.support.annotation.MainThread;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistantTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class PersistentExpenseManager extends ExpenseManager {
    MainActivity context;
    public PersistentExpenseManager(MainActivity context) {
        this.context=context;
        setup();
    }
    @Override
    public void setup() {
        DatabaseHelper dh = new DatabaseHelper(this.context);
        TransactionDAO persistantTransactionDAO = new PersistantTransactionDAO(dh);
        setTransactionsDAO(persistantTransactionDAO);

        AccountDAO persistantAccountDAO = new PersistantAccountDAO(dh);
        setAccountsDAO(persistantAccountDAO);
    }
}
