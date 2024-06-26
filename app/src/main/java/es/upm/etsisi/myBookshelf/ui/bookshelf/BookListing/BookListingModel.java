package es.upm.etsisi.myBookshelf.ui.bookshelf.BookListing;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.upm.etsisi.myBookshelf.Firebase.FirebaseBook2;
import es.upm.etsisi.myBookshelf.Firebase.FirebaseBookWrapper;
import es.upm.etsisi.myBookshelf.Firebase.Firebase_Utils;
import es.upm.etsisi.myBookshelf.REST.OpenBooks.BookResponse;
import es.upm.etsisi.myBookshelf.REST.OpenBooks.CallBacks.FirebaseBookWrapperCallback;
import es.upm.etsisi.myBookshelf.REST.OpenBooksAdapter;
import es.upm.etsisi.myBookshelf.ui.bookshelf.shelfitem.EBookShelfItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookListingModel implements Serializable {
    private EBookShelfItem type;
    private MutableLiveData<List<MutableLiveData<FirebaseBookWrapper>>> bookResponseList;

    public BookListingModel() {

    }

    public BookListingModel(EBookShelfItem type) {

        this.type = type;
        bookResponseList = new MutableLiveData<List<MutableLiveData<FirebaseBookWrapper>>>();


        Firebase_Utils.getRootFirebase().child(type.name().toLowerCase()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MutableLiveData<FirebaseBookWrapper>> bookResponses = new ArrayList<>();

                Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();

                while (iterator.hasNext()) {
                    DataSnapshot dataSnapshot = iterator.next();
                    FirebaseBook2 firebaseBook2 = dataSnapshot.getValue(FirebaseBook2.class);

                    MutableLiveData<FirebaseBookWrapper> bookResponseMutableLiveData = new MutableLiveData<>();
                    bookResponses.add(bookResponseMutableLiveData);

                    Call<BookResponse> call = OpenBooksAdapter.getApiService().getBookById(firebaseBook2.getBookID());
                    new FirebaseBookWrapperCallback(bookResponseMutableLiveData, firebaseBook2, call, type);


                }


                bookResponseList.postValue(bookResponses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", "Error getting data", error.toException());
            }
        });

    }

    public MutableLiveData<List<MutableLiveData<FirebaseBookWrapper>>> getBookResponseList() {
        return bookResponseList;
    }

    public String getName() {
        return type.getDisplayName();
    }

    public EBookShelfItem getType() {
        return type;
    }

}
