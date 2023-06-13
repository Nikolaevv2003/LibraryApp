package com.mirea.kt.android2023.libraryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mirea.kt.android2023.libraryapp.model.Book;
import com.mirea.kt.android2023.libraryapp.realm.DBManager;
import com.mirea.kt.android2023.libraryapp.util.HTTPRunnable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private TextView textViewLoginInfo;
    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button buttonLogin;
    private String login, password;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);

        textViewLoginInfo = findViewById(R.id.textViewLoginInfo);
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        dbManager = new DBManager();

        buttonLogin.setOnClickListener(x -> {
            Log.i("app_tag", "login button was pressed");

            login = editTextLogin.getText().toString();
            password = editTextPassword.getText().toString();

            if (login.isEmpty() || password.isEmpty()) {
                textViewLoginInfo.setText("Поля не могут быть пустыми!");
                return;
            }

            String server = "https://android-for-students.ru";
            String serverPath = "/coursework/login.php";
            HashMap<String, String> map = new HashMap<>();
            map.put("lgn", login);
            map.put("pwd", password);
            map.put("g", "RIBO-01-21");
            HTTPRunnable httpRunnable = new HTTPRunnable(server + serverPath, map);
            Thread th = new Thread(httpRunnable);
            th.start();
            try {
                th.join();
            } catch (InterruptedException e) {
                Log.e("app_tag", e.getMessage());
            } finally {
                try {
                    JSONObject jsonObject = new JSONObject(httpRunnable.getResponseBody());
                    int result = jsonObject.getInt("result_code");

                    if (result == 1) {
                        JSONArray data = jsonObject.getJSONArray("data");

                        for (int i=0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);

                            Book book = new Book();
                            book.setArticle(object.getInt("vendor_code"));
                            book.setTitle(object.getString("title"));
                            book.setAuthor(object.getString("author"));
                            book.setRackNumber(object.getInt("rack_number"));
                            book.setShelfNumber(object.getInt("shelf_number"));

                            try {
                                dbManager.saveBook(book);
                            } catch (IllegalArgumentException ignored) {
                            }
                        }
                        Toast.makeText(this, "Успешно!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(MainActivity.this, LibraryActivity.class);
                        startActivity(intent);
                    } else {
                        textViewLoginInfo.setText("Неверный логин или пароль!");
                    }
                } catch (JSONException e) {
                    Log.e("app_tag", e.getMessage());
                }
            }
        });
    }
}