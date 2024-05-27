package es.upm.etsisi.myBookshelf.Room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Book {
    @PrimaryKey
    @NonNull
    public String key;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "cover")
    public String cover;

    @ColumnInfo(name = "author")
    public String author;



}
