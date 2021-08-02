package com.biijay.taskplanner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.biijay.taskplanner.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private FloatingActionButton fabBtn;

    //Firebase

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    //Recycler

    private RecyclerView recyclerView;

    //update input field

    private EditText titleUpd;
    private EditText noteUpd;

    private Button btnDeletUpd;
    private Button btnUpdateUpd;

    //variable

    private String title;
    private String note;
    private String post_key;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Task Planner");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uId = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Tasks").child(uId);

        mDatabase.keepSynced(true);

        //Recycler

        recyclerView = findViewById(R.id.recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        fabBtn = findViewById(R.id.fab_btn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder myDialog = new AlertDialog.Builder(HomeActivity.this);

                LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);

                View myview = inflater.inflate(R.layout.custominputfield,null);

                myDialog.setView(myview);

                final AlertDialog dialog = myDialog.create();

                final EditText title = myview.findViewById(R.id.edit_title);
                final EditText note = myview.findViewById(R.id.edit_note);

                Button btnSave = myview.findViewById(R.id.btn_save);

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String mTitle = title.getText().toString().trim();
                        String mNote = note.getText().toString().trim();

                        if(TextUtils.isEmpty(mTitle)) {
                            title.setError("Required Field..");
                            return;
                        }

                        if(TextUtils.isEmpty(mNote)) {
                            note.setError("Required Field..");
                            return;

                        }

                        String id = mDatabase.push().getKey();

                        String datee = DateFormat.getDateInstance().format(new Date());

                        Data data = new Data(mTitle,mNote,datee,id);

                        mDatabase.child(id).setValue(data);

                        Toast.makeText(getApplicationContext(),"Data Insert",Toast.LENGTH_SHORT).show();

                        dialog.dismiss();

                    }
                });

                dialog.show();


            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mDatabase, Data.class)
                        .build();


        FirebaseRecyclerAdapter<Data,MyViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull final Data model) {

                holder.setTitle(model.getTitle());
                holder.setNote(model.getNote());
                holder.setDate(model.getDate());

                holder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        post_key = getRef(position).getKey();
                        title = model.getTitle();
                        note = model.getNote();


                        updateData();
                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_data, viewGroup, false);

                return new MyViewHolder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        View myview;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myview = itemView;
        }

        public void setTitle(String title) {
            TextView mTitle = myview.findViewById(R.id.title);
            mTitle.setText(title);
        }

        public void setNote(String note) {
            TextView mNote = myview.findViewById(R.id.note);
            mNote.setText(note);
        }

        public void setDate(String date) {
            TextView mDate = myview.findViewById(R.id.date);
            mDate.setText(date);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                mAuth.signOut();
               // startActivity(new Intent(getApplicationContext(),MainActivity.class));

                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void  updateData() {

        AlertDialog.Builder mydialog = new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);

        View myview = inflater.inflate(R.layout.updateinputfield,null);
        mydialog.setView(myview);

        final AlertDialog dialog = mydialog.create();

        titleUpd = myview.findViewById(R.id.edit_title_update);
        noteUpd = myview.findViewById(R.id.edit_note_update);

        titleUpd.setText(title);
        titleUpd.setSelection(title.length());

        noteUpd.setText(note);
        noteUpd.setSelection(note.length());

        btnDeletUpd = myview.findViewById(R.id.btn_delete);
        btnUpdateUpd = myview.findViewById(R.id.btn_update);

        btnUpdateUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title = titleUpd.getText().toString().trim();
                note = noteUpd.getText().toString().trim();

                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(title,note,mDate,post_key);

                mDatabase.child(post_key).setValue(data);

                dialog.dismiss();
            }
        });

        btnDeletUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child(post_key).removeValue();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
