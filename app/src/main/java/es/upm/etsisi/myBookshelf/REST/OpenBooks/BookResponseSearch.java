package es.upm.etsisi.myBookshelf.REST.OpenBooks;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookResponseSearch {
    @SerializedName("numFound")
    int numFound;
    @SerializedName("docs")
    List<BookInfoSearchResponse> docs;

    public int getNumFound() {
        return numFound;
    }

    public List<BookInfoSearchResponse> getDocs() {
        return docs;
    }
}
