package com.example.user.quizproject;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SetActivity extends AppCompatActivity {

    private GridView mGrSet;
    private Dialog mLoadingDialog;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        android.support.v7.widget.Toolbar tb = findViewById(R.id.tbSet);
        setSupportActionBar(tb);
        getSupportActionBar().setTitle("Level");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLoadingDialog = new Dialog(SetActivity.this);
        mLoadingDialog.setContentView(R.layout.loading_progressbar);
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.getWindow()
                .setBackgroundDrawableResource(R.drawable.loading_progressbackground);
        mLoadingDialog.getWindow()
                .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLoadingDialog.show();

        mGrSet = (GridView) findViewById(R.id.grSet);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("sets");

        loadSets();
    }

    public void loadSets(){
        // Read from the database
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long totalSets = dataSnapshot.getChildrenCount();
                SetAdapter sa = new SetAdapter((int)totalSets);
                mGrSet.setAdapter(sa);
                mLoadingDialog.cancel();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SetActivity.this, error.toString(), Toast.LENGTH_SHORT);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            SetActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}