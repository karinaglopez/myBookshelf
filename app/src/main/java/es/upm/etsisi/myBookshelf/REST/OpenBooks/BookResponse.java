package es.upm.etsisi.myBookshelf.REST.OpenBooks;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BookResponse implements Serializable {
    @SerializedName("key")
    String key;
    @SerializedName("title")
    String title;
    @SerializedName("covers")
    List<String> covers;

    @SerializedName("authors")
    List<AuthorBookResponse> authors;


    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }


    public List<String> getCovers() {
        return covers;
    }

    public String getCover() {
        return getCovers() != null ? getCovers().get(getCovers().size() - 1) : null;
    }

    private static class AuthorBookResponse {

        @SerializedName("author")
        public AuthorKeyBookResponse author;

        private static class AuthorKeyBookResponse {
            @SerializedName("key")
            public String key;
        }
    }

    public String getAuthorKey() {
        return authors.get(0).author.key;
    }
}
