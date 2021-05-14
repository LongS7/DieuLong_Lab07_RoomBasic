package com.longs7.android_lab07;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.longs7.android_lab07.dao.UserDao;
import com.longs7.android_lab07.entity.User;

public class MainActivity extends AppCompatActivity {
    private EditText edtName;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lvUser = findViewById(R.id.listView);

        MyDatabase db =
                Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "my-database")
                        .allowMainThreadQueries()
                        .build();

        userDao = db.userDao();

        ArrayAdapter adapter = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1);
        lvUser.setAdapter(adapter);

        adapter.addAll(userDao.getAllUser());

        edtName = findViewById(R.id.edtName);

        lvUser.setOnItemClickListener((parent, view, position, id) -> {
            User u = (User) adapter.getItem(position);

            edtName.setText(u.getName());
        });

        findViewById(R.id.btnAdd).setOnClickListener(v -> {
            if(edtName.getText().toString().trim().isEmpty())
                return;

            User user = new User(edtName.getText().toString().trim());

            userDao.insert(user);

            adapter.add(userDao.getLastUser());
            adapter.notifyDataSetChanged();
        });

        findViewById(R.id.btnRemove).setOnClickListener(v -> {
            if(edtName.getText().toString().trim().isEmpty())
                return;

            User user = userDao.findUserByName(edtName.getText().toString().trim());
            if(user == null)
                return;

            userDao.delete(user);
            adapter.clear();

            adapter.addAll(userDao.getAllUser());

        });

    }
}