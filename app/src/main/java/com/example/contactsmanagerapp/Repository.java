package com.example.contactsmanagerapp;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    // The Available Data Sources:
    // Room Database
    ExecutorService executor;
    Handler handler;



    private final ContactDAO contactDAO;


    public Repository(Application application) {
        ContactsDatabase contactsDatabase = ContactsDatabase.getInstance(application);
        this.contactDAO = contactsDatabase.getContactDAO();
        //Used for Background Database Operations
        executor = Executors.newSingleThreadExecutor();
        //Used for updating the UI
        handler = new Handler(Looper.getMainLooper());
    }

    //Methods in DAO being executed from repository
    public void addContact(Contacts contacts){

        //Runnable : Executing tasks on Separate thread
        executor.execute((new Runnable() {
            @Override
            public void run() {
                contactDAO.insert(contacts);
            }
        }));
    }
    public void deleteContact(Contacts contacts){
        //Runnable : Executing tasks on Separate thread
        executor.execute((new Runnable() {
            @Override
            public void run() {
                contactDAO.delete(contacts);
            }
        }));
    }

    public LiveData<List<Contacts>>  getAllContacts(){
        return contactDAO.getAllContacts();
    }

}
