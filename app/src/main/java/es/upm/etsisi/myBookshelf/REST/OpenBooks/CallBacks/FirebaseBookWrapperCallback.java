package es.upm.etsisi.myBookshelf.REST.OpenBooks.CallBacks;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import es.upm.etsisi.myBookshelf.Firebase.FirebaseBook2;
import es.upm.etsisi.myBookshelf.Firebase.FirebaseBookWrapper;
import es.upm.etsisi.myBookshelf.NavActivity;
import es.upm.etsisi.myBookshelf.REST.OpenBooks.BookResponse;
import es.upm.etsisi.myBookshelf.Room.Book;
import es.upm.etsisi.myBookshelf.ui.bookshelf.shelfitem.EBookShelfItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseBookWrapperCallback implements Callback<BookResponse> {
    private MutableLiveData<FirebaseBookWrapper> bookResponseMutableLiveData;
    private FirebaseBook2 firebaseBook;
    private Call<BookResponse> call;
    private EBookShelfItem type;


    public FirebaseBookWrapperCallback(MutableLiveData<FirebaseBookWrapper> bookResponseMutableLiveData, FirebaseBook2 firebaseBook, Call<BookResponse> call, EBookShelfItem type) {
        this.bookResponseMutableLiveData = bookResponseMutableLiveData;
        this.firebaseBook = firebaseBook;
        this.type = type;
        this.call = call;
        this.call.enqueue(this);
    }

    public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
        if (response.isSuccessful()) {
            BookResponse book = response.body();
            this.bookResponseMutableLiveData.setValue(new FirebaseBookWrapper(this.firebaseBook, type, book));
            //Book bookDB = new Book();
            //NavActivity.db.bookDao().insertAll();

        } else {
            Log.i("ERROR",  response.message());
            //TODO Mostrar al usuario el error
            this.call.clone().enqueue(this);

        }
    }

    @Override
    public void onFailure(Call<BookResponse> call, Throwable t) {
        Log.i("TEST",  t.toString());
    }

}