package es.upm.etsisi.myBookshelf.REST.OpenBooks.CallBacks;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import es.upm.etsisi.myBookshelf.Firebase.FirebaseBook2;
import es.upm.etsisi.myBookshelf.Firebase.FirebaseBookWrapper;
import es.upm.etsisi.myBookshelf.REST.OpenBooks.BookInfoSearchResponse;
import es.upm.etsisi.myBookshelf.REST.OpenBooks.BookResponse;
import es.upm.etsisi.myBookshelf.REST.OpenBooks.BookResponseSearch;
import es.upm.etsisi.myBookshelf.ui.bookshelf.shelfitem.EBookShelfItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookSearchCallback implements Callback<BookResponseSearch>{
    MutableLiveData<List<BookInfoSearchResponse>> bookResponse;
    private Call<BookResponseSearch> call;

    public BookSearchCallback(MutableLiveData<List<BookInfoSearchResponse>> bookResponse, Call<BookResponseSearch> call) {
        this.bookResponse = bookResponse;
        this.call = call;
        this.call.enqueue(this);
    }

    public void onResponse(Call<BookResponseSearch> call, Response<BookResponseSearch> response) {
        if (response.isSuccessful()) {
            BookResponseSearch bookSearch = response.body();
            bookResponse.postValue(bookSearch.getDocs().subList(0, Math.min(10, bookSearch.getNumFound())));
        } else {
            Log.i("ERROR",  response.message());
            //TODO Mostrar al usuario el error
            this.call.clone().enqueue(this);

        }
    }

    @Override
    public void onFailure(Call<BookResponseSearch> call, Throwable t) {
        Log.i("TEST",  t.toString());
    }

}
