package com.mirea.kt.android2023.libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.mirea.kt.android2023.libraryapp.adapter.BookAdapter;
import com.mirea.kt.android2023.libraryapp.model.Book;
import com.mirea.kt.android2023.libraryapp.realm.DBManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.realm.Realm;

public class LibraryActivity extends AppCompatActivity implements BookAdapter.OnBookClickListener {

    private EditText editTextSearch;
    private RecyclerView recyclerView;
    private DBManager dbManager;
    private BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        Realm.init(this);

        editTextSearch = findViewById(R.id.editTextSearch);
        dbManager = new DBManager();

        Toolbar toolbar = findViewById(R.id.toolbarLibrary);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Библиотека");
        }

        ArrayList<Book> books = dbManager.getAllBooks();

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new BookAdapter(books, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Book> newBooks = adapter.getBooks().stream().filter(x -> x.getTitle().toLowerCase().contains(s.toString().toLowerCase())
                        || x.getAuthor().toLowerCase().contains(s.toString().toLowerCase())).collect(Collectors.toList());

                recyclerView.setAdapter(new BookAdapter(newBooks, LibraryActivity.this));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.library_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_add) {
            startActivity(new Intent(LibraryActivity.this, CreateBookActivity.class));
            return true;
        }

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBookClick(Book book, int position) {
        SharedPreferences preferences = getSharedPreferences(
                PreferenceManager.getDefaultSharedPreferencesName(getApplicationContext()), MODE_PRIVATE
        );

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("current_article", book.getArticle());
        editor.apply();

        Intent intent = new Intent(LibraryActivity.this, BookActivity.class);
        startActivity(intent);
    }

    @Override
    public void onMoreClick(Book book, ImageView imageView) {
        PopupMenu popupMenu = new PopupMenu(this, imageView);
        popupMenu.getMenu().add(0, 1, Menu.NONE, "Удалить");

        popupMenu.setOnMenuItemClickListener(x -> {
            switch (x.getItemId()) {
                case 1:

                    dbManager.deleteBook(book.getArticle());
                    adapter = new BookAdapter(dbManager.getAllBooks(), LibraryActivity.this);
                    recyclerView.setAdapter(adapter);

                    Toast.makeText(this, "Книга удалена", Toast.LENGTH_LONG).show();
                    break;
            }
            return true;
        });

        popupMenu.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter = new BookAdapter(dbManager.getAllBooks(), LibraryActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<Book> initBooks() {
        ArrayList<Book> books = new ArrayList<>();

        books.add(new Book(1, "War and Peace", "L.N.Tolstoy", 1, 1));
        books.add(new Book(2, "War and Peace1", "L.N.Tolstoy", 2, 1));
        books.add(new Book(3, "War and Peace2", "L.N.Tolstoy", 3, 1));
        books.add(new Book(4, "War and Peace3", "L.N.Tolstoy", 4, 1));
        books.add(new Book(5, "War and Peace4", "L.N.Tolstoy", 1, 2));
        books.add(new Book(6, "War and Peace5", "L.N.Tolstoy", 1, 3));
        books.add(new Book(7, "War and Peace6", "L.N.Tolstoy", 1, 4));
        books.add(new Book(8, "War and Peace7", "L.N.Tolstoy", 1, 5));
        books.add(new Book(9, "War and Peace8", "L.N.Tolstoy", 1, 6));
        books.add(new Book(10, "War and Peace9", "L.N.Tolstoy", 1, 7));

        return books;
    }


}