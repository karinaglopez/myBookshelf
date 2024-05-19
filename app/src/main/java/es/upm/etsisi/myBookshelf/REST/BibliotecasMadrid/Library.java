package es.upm.etsisi.myBookshelf.REST.BibliotecasMadrid;

import com.google.gson.annotations.SerializedName;

public class Library {
    @SerializedName("@id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("location")
    private Location location;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
