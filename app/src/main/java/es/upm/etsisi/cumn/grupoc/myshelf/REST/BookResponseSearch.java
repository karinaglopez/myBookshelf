package es.upm.etsisi.cumn.grupoc.myshelf.REST;

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
