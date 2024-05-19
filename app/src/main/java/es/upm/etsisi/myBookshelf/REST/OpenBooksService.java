package es.upm.etsisi.myBookshelf.REST;

import es.upm.etsisi.myBookshelf.REST.OpenBooks.AuthorResponse;
import es.upm.etsisi.myBookshelf.REST.OpenBooks.BookResponse;
import es.upm.etsisi.myBookshelf.REST.OpenBooks.BookResponseSearch;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OpenBooksService {

    //https://openlibrary.org/search.json?q=the+lord+of+the+rings
    @GET("search.json")
    Call<BookResponseSearch> getBook(
            @Query("q") String name,
            @Query("fields") String fields,
            @Query("sort") String sort
    );

    @GET("{fullUrl}.json")
    Call<BookResponse> getBookById(@Path(value = "fullUrl", encoded = true) String fullUrl);

    @GET("{fullUrl}.json")
    Call<AuthorResponse> getAuthorById(@Path(value = "fullUrl", encoded = true) String fullUrl);

}
