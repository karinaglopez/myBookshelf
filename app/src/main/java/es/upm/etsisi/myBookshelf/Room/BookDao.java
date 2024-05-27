package es.upm.etsisi.myBookshelf.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface BookDao {
    @Query("SELECT * FROM book WHERE key LIKE :key LIMIT 1")
    Book findByKey(String key);

    @Insert
    void insertAll(Book... books);


}
