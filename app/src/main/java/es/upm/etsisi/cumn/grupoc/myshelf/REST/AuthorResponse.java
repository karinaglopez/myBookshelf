package es.upm.etsisi.cumn.grupoc.myshelf.REST;

import com.google.gson.annotations.SerializedName;

public class AuthorResponse {
    @SerializedName("name")
    String name;

    public String getName() {
        return name;
    }
}
