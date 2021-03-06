package com.belajar.newportofolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AboutFriends extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "MESSAGE";
    private ListView flist;
    DbHandler mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_friends);

        mydb = new DbHandler(this);

        flist = findViewById(R.id.flist);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final ArrayList<String> list = mydb.getAllFriend();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

        flist.setAdapter(adapter);
        flist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                int searchId = i + 1;

                String dataitm = list.get(i);
                int id = Integer.parseInt(dataitm.split(" - ")[0]);

                Bundle data = new Bundle();
                data.putInt("id", id);

                Intent detail = new Intent(getApplicationContext(), DetailFriend.class);
                detail.putExtras(data);
                startActivity(detail);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dd_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.item1) {
            Bundle data = new Bundle();
            data.putInt("id", 0);

            Intent detail = new Intent(getApplicationContext(), DetailFriend.class);
            detail.putExtras(data);
            startActivity(detail);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }
}