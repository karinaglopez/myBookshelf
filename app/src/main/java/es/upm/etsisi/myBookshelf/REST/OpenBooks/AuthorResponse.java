package es.upm.etsisi.myBookshelf.REST.OpenBooks;

import com.google.gson.annotations.SerializedName;

public class AuthorResponse {
    @SerializedName("name")
    String name;

    public String getName() {
        return name;
    }
}
