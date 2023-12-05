package com.example.contactsmanagerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.contactsmanagerapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //data Source
    private ContactsDatabase contactsDatabase;
    private ArrayList<Contacts> contactsArrayList = new ArrayList<>();
    //Adapter
    private MyAdapter myAdapter;

    //Binding
    private ActivityMainBinding mainBinding;
    private MainActivityClickHandlers handlers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainBinding = DataBindingUtil.setContentView(
                this,R.layout.activity_main
        );
        handlers = new MainActivityClickHandlers(this);

        mainBinding.setClickHandler(handlers);

        //RecyclerView
        RecyclerView recyclerView = mainBinding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        //Database
        contactsDatabase = ContactsDatabase.getInstance(this);


        //view model
        MyViewModel viewModel = new ViewModelProvider(this).get(MyViewModel.class);

//        //Inserting a new Contact (just for testing)
//
//        Contacts c1 = new Contacts("jack","jack@gmial.com");
//        viewModel.addNewContact(c1);

        //loading the data from room db
        viewModel.getAllContacts().observe(this,
                new Observer<List<Contacts>>() {
                    @Override
                    public void onChanged(List<Contacts> contacts) {
                        contactsArrayList.clear();
                        for(Contacts c : contacts){
                            Log.v("TAG", c.getName());
                            contactsArrayList.add(c);

                        }
                        myAdapter.notifyDataSetChanged();
                    }

                });
        //adapter
        myAdapter = new MyAdapter(contactsArrayList);

        //Linking the recyclerView with the adapter
        recyclerView.setAdapter(myAdapter);

        //swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Contacts c = contactsArrayList.get(viewHolder.getAdapterPosition());


                viewModel.deleteContact(c);
            }
        }).attachToRecyclerView(recyclerView);



    }
}