package es.upm.etsisi.myBookshelf.Firebase;


import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class FirebaseBook2 implements Serializable {
    public String bookID;

    public int score;

    public boolean submitedScore;

    public FirebaseBook2() {
    }

    public FirebaseBook2(String bookID) {
        this.bookID = bookID;
    }

    public String getBookID() {
        return bookID;
    }

    @Exclude
    public String getBookIDFirebase() {
        return bookID.replace('/', '-');
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setSubmitedScore(boolean submitedScore) {
        this.submitedScore = submitedScore;
    }

    public boolean isScoreSubmited() {
        return submitedScore;
    }
}
