package es.upm.etsisi.cumn.grupoc.myshelf.Firebase;


import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class FirebaseBookGlobal implements Serializable {
    public String bookID;

    public int avgScore = 0;
    public long numTotalScore = 0;
    public int numScores = 0;

    public FirebaseBookGlobal() {
    }

    public FirebaseBookGlobal(String bookID) {
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

    public int getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(int avgScore) {
        this.avgScore = avgScore;
    }

    public long getNumTotalScore() {
        return numTotalScore;
    }

    public void setNumTotalScore(long numTotalScore) {
        this.numTotalScore = numTotalScore;
    }

    public int getNumScores() {
        return numScores;
    }

    public void setNumScores(int numScores) {
        this.numScores = numScores;
    }

    public void addNewScore(int score) {
        numScores++;
        numTotalScore = score;

        avgScore = (int) (numTotalScore / numScores);
    }

    public void editScore(int previousScore, int newScore) {
        numTotalScore += newScore - previousScore;
        avgScore = (int) (numTotalScore / numScores);
    }
}
