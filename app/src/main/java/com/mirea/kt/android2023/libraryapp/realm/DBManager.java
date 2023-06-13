package com.mirea.kt.android2023.libraryapp.realm;

import com.mirea.kt.android2023.libraryapp.model.Book;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class DBManager {

    private Realm realm;

    public DBManager() {
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("book_db")
                .schemaVersion(1)
                .build();

        this.realm = Realm.getInstance(configuration);
    }

    public void saveBook(Book book) throws IllegalArgumentException {
        ArrayList<Book> books = getAllBooks();

        if (books.size() != 0) {
            if (books.stream().anyMatch(x -> x.getArticle() == book.getArticle())) {
                throw new IllegalArgumentException("Книга с таким артиклем уже существует");
            }
        }

        realm.beginTransaction();
        realm.insert(book);
        realm.commitTransaction();
    }

    public Book getBook(int article) {

        return realm.where(Book.class).equalTo("article", article).findFirst();
    }

    public ArrayList<Book> getAllBooks() {
        RealmResults<Book> resultRealmList = realm.where(Book.class).findAll();

        return new ArrayList<Book>(realm.copyFromRealm(resultRealmList));
    }

    public void updateBook(int article, Book book) throws IllegalArgumentException {

        if (article == book.getArticle()) {
            realm.beginTransaction();
            realm.insertOrUpdate(book);
            realm.commitTransaction();
            return;
        }

        try {
            saveBook(book);

            deleteBook(article);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }

    public void deleteBook(int article) {
        Book book = getBook(article);

        if (book != null) {
            realm.beginTransaction();
            book.deleteFromRealm();
            realm.commitTransaction();
        }
    }
}
