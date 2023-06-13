package com.mirea.kt.android2023.libraryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mirea.kt.android2023.libraryapp.model.Book;
import com.mirea.kt.android2023.libraryapp.realm.DBManager;

import io.realm.Realm;

public class BookActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private TextView textViewAuthor;
    private TextView textViewRackNumber;
    private TextView textViewShelfNumber;
    private TextView textViewArticle;
    private Button buttonEdit;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        textViewTitle = findViewById(R.id.textViewBookTitle);
        textViewAuthor = findViewById(R.id.textViewBookAuthor);
        textViewRackNumber = findViewById(R.id.textViewBookRackNumber);
        textViewShelfNumber = findViewById(R.id.textViewBookShelfNumber);
        textViewArticle = findViewById(R.id.textViewBookArticle);
        buttonEdit = findViewById(R.id.buttonEditBook);
        dbManager = new DBManager();

        SharedPreferences preferences = getSharedPreferences(
                PreferenceManager.getDefaultSharedPreferencesName(getApplicationContext()), MODE_PRIVATE
        );

        int article = -1;
        if (preferences.contains("current_article")) {
            article = preferences.getInt("current_article", -1);
        }

        Book book = dbManager.getBook(article);
        if (book != null) {

            textViewTitle.setText(book.getTitle());
            textViewAuthor.setText("Автор: " + book.getAuthor());
            textViewRackNumber.setText("Номер стеллажа: " + book.getRackNumber());
            textViewShelfNumber.setText("Номер полки: " + book.getShelfNumber());
            textViewArticle.setText("Артикул: " + book.getArticle());
        }

        Toolbar toolbar = findViewById(R.id.toolbarBook);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (book != null) {
                actionBar.setTitle(book.getTitle());
            } else {
                actionBar.setTitle("Книга");
            }
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        buttonEdit.setOnClickListener(x -> {
            Intent editBookIntent = new Intent(BookActivity.this, UpdateBookActivity.class);
            startActivity(editBookIntent);
        });
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences(
                PreferenceManager.getDefaultSharedPreferencesName(getApplicationContext()), MODE_PRIVATE
        );

        if (preferences.contains("current_article")) {
            int currentArticle = preferences.getInt("current_article", -1);

            Book book = dbManager.getBook(currentArticle);

            textViewTitle.setText(book.getTitle());
            textViewAuthor.setText("Автор: " + book.getAuthor());
            textViewRackNumber.setText("Номер стеллажа: " + book.getRackNumber());
            textViewShelfNumber.setText("Номер полки: " + book.getShelfNumber());
            textViewArticle.setText("Артикул: " + book.getArticle());
        }
    }
}