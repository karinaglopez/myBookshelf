package es.upm.etsisi.myBookshelf.REST.OpenBooks.CallBacks;

import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import es.upm.etsisi.myBookshelf.REST.OpenBooks.AuthorResponse;
import es.upm.etsisi.myBookshelf.REST.OpenBooks.BookInfoSearchResponse;
import es.upm.etsisi.myBookshelf.REST.OpenBooks.BookResponseSearch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorResponseCallback implements Callback<AuthorResponse>{
    TextView bookAuthor;
    private Call<AuthorResponse> call;

    public AuthorResponseCallback(TextView bookAuthor, Call<AuthorResponse> call) {
        this.bookAuthor = bookAuthor;
        this.call = call;
        this.call.enqueue(this);
    }

    public void onResponse(Call<AuthorResponse> call, Response<AuthorResponse> response) {
        if (response.isSuccessful()) {
            bookAuthor.setText(response.body().getName());
        } else {
            Log.i("ERROR",  response.message());
            //TODO Mostrar al usuario el error
            this.call.clone().enqueue(this);

        }
    }

    @Override
    public void onFailure(Call<AuthorResponse> call, Throwable t) {
        Log.i("TEST",  t.toString());
    }

}
