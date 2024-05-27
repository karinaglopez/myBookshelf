package es.upm.etsisi.myBookshelf.ui.bookshelf.AddBook;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.upm.etsisi.myBookshelf.REST.OpenBooks.BookInfoSearchResponse;
import es.upm.etsisi.myBookshelf.REST.OpenBooks.BookResponseSearch;
import es.upm.etsisi.myBookshelf.REST.OpenBooks.CallBacks.BookSearchCallback;
import es.upm.etsisi.myBookshelf.REST.OpenBooksAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBookViewModel extends ViewModel {

    MutableLiveData<List<BookInfoSearchResponse>> bookResponse;
    public LiveData<List<BookInfoSearchResponse>> getBookResponseList() {
        return  bookResponse;
    }

    public AddBookViewModel() {
        this.bookResponse = new MutableLiveData<>();
    }

    public void search(String text) {
        Call<BookResponseSearch> call = OpenBooksAdapter.getApiService().getBook(text, "", "");
        new BookSearchCallback(bookResponse, call);
    }
}