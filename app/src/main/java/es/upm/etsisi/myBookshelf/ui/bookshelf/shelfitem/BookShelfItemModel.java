package es.upm.etsisi.myBookshelf.ui.bookshelf.shelfitem;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.upm.etsisi.myBookshelf.Firebase.FirebaseBookWrapper;
import es.upm.etsisi.myBookshelf.Firebase.FirebaseBook2;
import es.upm.etsisi.myBookshelf.Firebase.Firebase_Utils;
import es.upm.etsisi.myBookshelf.REST.OpenBooks.BookResponse;
import es.upm.etsisi.myBookshelf.REST.OpenBooks.CallBacks.FirebaseBookWrapperCallback;
import es.upm.etsisi.myBookshelf.REST.OpenBooksAdapter;
import es.upm.etsisi.myBookshelf.ui.bookshelf.BookListing.BookListingModel;
import retrofit2.Call;

public class BookShelfItemModel implements Serializable {
        private EBookShelfItem type;
    private MutableLiveData<List<MutableLiveData<FirebaseBookWrapper>>> bookResponseList;

    public BookShelfItemModel() {

    }

    public BookShelfItemModel(EBookShelfItem type) {

        this.type = type;

        bookResponseList = new MutableLiveData<List<MutableLiveData<FirebaseBookWrapper>>>();

        List<MutableLiveData<FirebaseBookWrapper>> bookResponses = new ArrayList<>();

        try {
            Firebase_Utils.getRootFirebase().child(type.name().toLowerCase()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else if (task.getResult().exists()){
                        Iterator<DataSnapshot> iterator = task.getResult().getChildren().iterator();



                        while (iterator.hasNext()) {
                            DataSnapshot dataSnapshot = iterator.next();
                            FirebaseBook2 firebaseBook2 = dataSnapshot.getValue(FirebaseBook2.class);

                            MutableLiveData<FirebaseBookWrapper> bookResponseMutableLiveData = new MutableLiveData<>();
                            bookResponses.add(bookResponseMutableLiveData);

                            Call<BookResponse> call = OpenBooksAdapter.getApiService().getBookById(firebaseBook2.getBookID());
                            new FirebaseBookWrapperCallback(bookResponseMutableLiveData, firebaseBook2, call, type);
                        }

                    }

                    bookResponseList.postValue(bookResponses);
                }
            });
        } catch (Exception e) {
            Log.e("LOAD", e.toString());
        }

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
