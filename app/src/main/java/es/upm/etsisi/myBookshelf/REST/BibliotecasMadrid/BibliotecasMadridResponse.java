package es.upm.etsisi.myBookshelf.REST.BibliotecasMadrid;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BibliotecasMadridResponse {
    @SerializedName("@context")
    private Object context;

    @SerializedName("@graph")
    private List<Library> nearbyLibraries;

    // Getters and setters
    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }

    public List<Library> getNearbyLibraries() {
        return nearbyLibraries;
    }

    public void setNearbyLibraries(List<Library> nearbyLibraries) {
        this.nearbyLibraries = nearbyLibraries;
    }
}
