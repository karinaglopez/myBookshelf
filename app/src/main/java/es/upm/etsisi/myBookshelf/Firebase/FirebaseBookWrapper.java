package es.upm.etsisi.myBookshelf.Firebase;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

import es.upm.etsisi.myBookshelf.REST.OpenBooks.BookResponse;
import es.upm.etsisi.myBookshelf.ui.bookshelf.shelfitem.EBookShelfItem;

public class FirebaseBookWrapper implements Serializable {
    private BookResponse bookResponse;

    private EBookShelfItem eBookShelfItem;

    private FirebaseBook2 firebaseBook2;

    public FirebaseBookWrapper(FirebaseBook2 firebaseBook2, EBookShelfItem type, BookResponse book) {
        this.eBookShelfItem = type;
        this.bookResponse = book;
        this.firebaseBook2 = firebaseBook2;
    }

    public BookResponse getBookResponse() {
        return bookResponse;
    }

    public EBookShelfItem geteBookShelfItem() {
        return eBookShelfItem;
    }

    public FirebaseBook2 getFirebaseBook2() {
        return firebaseBook2;
    }


    public boolean updateScore(int score) {
        if (score < 0 || score > 5)
            return false;


        DatabaseReference shelfRefence = Firebase_Utils.getRootFirebase().child(eBookShelfItem.name().toLowerCase());
        DatabaseReference globalBookRefence = FirebaseDatabase.getInstance().getReference("books");

        int previousScore = firebaseBook2.getScore();
        boolean scoreSubmited = firebaseBook2.isScoreSubmited();
        globalBookRefence.child(firebaseBook2.getBookIDFirebase()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseBookGlobal bookGlobal;
                if (snapshot.exists()) {
                    bookGlobal =snapshot.getValue(FirebaseBookGlobal.class);
                } else {
                    bookGlobal = new FirebaseBookGlobal(firebaseBook2.getBookID());
                }

                if (!scoreSubmited) {
                    bookGlobal.addNewScore(score);
                }
                else {
                    bookGlobal.editScore(previousScore, score);
                }

                globalBookRefence.child(bookGlobal.getBookIDFirebase()).setValue(bookGlobal);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        firebaseBook2.setSubmitedScore(true);
        firebaseBook2.setScore(score);
        shelfRefence.child(firebaseBook2.getBookIDFirebase()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    shelfRefence.child(firebaseBook2.getBookIDFirebase()).setValue(firebaseBook2);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        return true;
    }
}
