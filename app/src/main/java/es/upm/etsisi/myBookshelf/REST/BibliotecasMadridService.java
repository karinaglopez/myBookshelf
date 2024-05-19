package es.upm.etsisi.myBookshelf.REST;
import es.upm.etsisi.myBookshelf.REST.BibliotecasMadrid.BibliotecasMadridResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BibliotecasMadridService {

    @GET("201747-0-bibliobuses-bibliotecas.json")
    Call<BibliotecasMadridResponse> getBibliotecasData();

    @GET("201747-0-bibliobuses-bibliotecas.json")
    Call<BibliotecasMadridResponse> getBibliotecasDataWithLocation (@Query("latitud") double latitude, @Query("longitud") double longitude, @Query("distancia") int distance);
}