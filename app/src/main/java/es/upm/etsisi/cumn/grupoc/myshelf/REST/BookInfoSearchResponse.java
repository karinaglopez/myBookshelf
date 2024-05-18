package es.upm.etsisi.cumn.grupoc.myshelf.REST;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BookInfoSearchResponse implements Serializable {

    @SerializedName("key")
    String key;

    @SerializedName("title")
    String title;

    public String getCover_i() {
        return cover_i;
    }

    @SerializedName("cover_i")
    String cover_i;

    public String getTitle() {
        return title;
    }

    public String getKey() {
        return key;
    }
}
