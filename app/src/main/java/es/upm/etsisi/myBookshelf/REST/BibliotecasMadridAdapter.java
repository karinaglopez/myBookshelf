package es.upm.etsisi.myBookshelf.REST;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BibliotecasMadridAdapter {
    private static BibliotecasMadridService API_SERVICE;

    private static final String BASE_URL = "https://datos.madrid.es/egob/catalogo/";

    public static BibliotecasMadridService getApiService() {
        // Create an interceptor and set the log level
        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Associate the interceptor with the requests
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);

        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build()) // <-- set log level
                    .build();

            API_SERVICE = retrofit.create(BibliotecasMadridService.class);
        }

        return API_SERVICE;
    }
}