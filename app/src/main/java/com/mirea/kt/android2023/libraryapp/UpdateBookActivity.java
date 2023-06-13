package com.mirea.kt.android2023.libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mirea.kt.android2023.libraryapp.model.Book;
import com.mirea.kt.android2023.libraryapp.realm.DBManager;

import io.realm.Realm;

public class UpdateBookActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextAuthor;
    private EditText editTextRackNumber;
    private EditText editTextShelfNumber;
    private EditText editTextArticle;
    private TextView textViewError;
    private Button buttonUpdate;
    private DBManager dbManager;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        editTextTitle = findViewById(R.id.editTextUpdateBookTitle);
        editTextAuthor = findViewById(R.id.editTextUpdateBookAuthor);
        editTextRackNumber = findViewById(R.id.editTextUpdateBookRackNumber);
        editTextShelfNumber = findViewById(R.id.editTextUpdateBookShelfNumber);
        editTextArticle = findViewById(R.id.editTextUpdateBookArticle);
        textViewError = findViewById(R.id.textViewUpdateBookError);
        buttonUpdate = findViewById(R.id.buttonUpdateBook);
        dbManager = new DBManager();

        Toolbar toolbar = findViewById(R.id.toolbarUpdateBook);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Редактирование книги");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        preferences = getSharedPreferences(
                PreferenceManager.getDefaultSharedPreferencesName(getApplicationContext()), MODE_PRIVATE
        );

        int articleFromPreferences = -1;
        if (preferences.contains("current_article")) {
            articleFromPreferences = preferences.getInt("current_article", -1);
        }

        Book book = dbManager.getBook(articleFromPreferences);

        if (book != null) {
            editTextTitle.setText(book.getTitle());
            editTextAuthor.setText(book.getAuthor());
            editTextRackNumber.setText(book.getRackNumber() + "");
            editTextShelfNumber.setText(book.getShelfNumber() + "");
            editTextArticle.setText(book.getArticle() + "");
        }


        buttonUpdate.setOnClickListener(x -> {
            String title = editTextTitle.getText().toString();
            String author = editTextAuthor.getText().toString();
            String rackNumberString = editTextRackNumber.getText().toString();
            String shelfNumberString = editTextShelfNumber.getText().toString();
            String articleString = editTextArticle.getText().toString();

            if (title.isEmpty() || author.isEmpty() || rackNumberString.isEmpty() || shelfNumberString.isEmpty() || articleString.isEmpty()) {
                textViewError.setText("Все поля должны быть заполнены!");
                return;
            }

            try {
                int rackNumber = Integer.parseInt(rackNumberString);
                int shelfNumber = Integer.parseInt(shelfNumberString);
                int article = Integer.parseInt(articleString);

                if (rackNumber < 1 || shelfNumber < 1 || article < 0) {
                    textViewError.setText("Неверный формат данных!");
                    return;
                }

                Book newBook = new Book(article, title, author, rackNumber, shelfNumber);

                try {
                    dbManager.updateBook(book.getArticle(), newBook);
                } catch (IllegalArgumentException e) {
                    textViewError.setText("Книга с таким артиклем уже существует!");
                    return;
                }

                Toast.makeText(this, "Книга обновлена", Toast.LENGTH_LONG).show();

                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("current_article", article);
                editor.apply();

                onBackPressed();

            } catch (NumberFormatException e) {
                textViewError.setText("Неверный формат данных!");
            } catch (IllegalArgumentException e) {
                textViewError.setText("Книга с таким артиклем уже существует!");
            }
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
}