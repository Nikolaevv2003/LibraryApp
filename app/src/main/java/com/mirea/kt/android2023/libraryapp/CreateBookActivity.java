package com.mirea.kt.android2023.libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mirea.kt.android2023.libraryapp.model.Book;
import com.mirea.kt.android2023.libraryapp.realm.DBManager;

public class CreateBookActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextAuthor;
    private EditText editTextRackNumber;
    private EditText editTextShelfNumber;
    private EditText editTextArticle;
    private TextView textViewError;
    private Button buttonCreate;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_book);

        editTextTitle = findViewById(R.id.editTextAddBookTitle);
        editTextAuthor = findViewById(R.id.editTextAddBookAuthor);
        editTextRackNumber = findViewById(R.id.editTextAddBookRackNumber);
        editTextShelfNumber = findViewById(R.id.editTextAddBookShelfNumber);
        editTextArticle = findViewById(R.id.editTextAddBookArticle);
        textViewError = findViewById(R.id.textViewCreateBookError);
        buttonCreate = findViewById(R.id.buttonAddBook);
        dbManager = new DBManager();

        Toolbar toolbar = findViewById(R.id.toolbarCreateBook);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Добавление книги");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        buttonCreate.setOnClickListener(x -> {
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

                Book book = new Book(article, title, author, rackNumber, shelfNumber);

                dbManager.saveBook(book);

                Toast.makeText(this, "Книга создана!", Toast.LENGTH_LONG).show();

                editTextTitle.setText("");
                editTextAuthor.setText("");
                editTextRackNumber.setText("");
                editTextShelfNumber.setText("");
                editTextArticle.setText("");
                textViewError.setText("");

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