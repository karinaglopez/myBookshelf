package es.upm.etsisi.myBookshelf.ui.bookshelf.AddBook;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.upm.etsisi.myBookshelf.Firebase.FirebaseBook2;
import es.upm.etsisi.myBookshelf.Firebase.Firebase_Utils;
import es.upm.etsisi.myBookshelf.R;
import es.upm.etsisi.myBookshelf.REST.OpenBooks.BookInfoSearchResponse;
import es.upm.etsisi.myBookshelf.databinding.AddBookTileBinding;
import es.upm.etsisi.myBookshelf.ui.bookshelf.shelfitem.EBookShelfItem;

public class AddBookTileAdapter extends RecyclerView.Adapter<AddBookTileAdapter.ViewHolder>{


    private List<BookInfoSearchResponse> bookList;
    private EBookShelfItem eBookShelfItem;

    public AddBookTileAdapter(List<BookInfoSearchResponse> bookList, EBookShelfItem eBookShelfItem) {
        this.bookList = bookList;
        this.eBookShelfItem = eBookShelfItem;
    }

    @NonNull
    @Override
    public AddBookTileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AddBookTileBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookInfoSearchResponse bookResponse = bookList.get(position);
        AddBookTileBinding binding = holder.getBinding();

        if (bookResponse != null) {
            String cover = bookResponse.getCover_i();

            binding.imageView12.setImageResource(R.mipmap.book_shelf_display);

            if (cover != null)
                Picasso.get().load("https://covers.openlibrary.org/b/id/" + cover +"-L.jpg")
                        .placeholder(R.mipmap.book_shelf_display)
                        .resize(150, 300)
                        .centerCrop().into(binding.imageView12);

            binding.bookTitle.setText(bookResponse.getTitle());


            /*binding.button3.setOnClickListener((l) -> {
                Task task = Firebase_Utils.getRootFirebase().child(eBookShelfItem.name().toLowerCase()).push().setValue(bookResponse.getKey());
                Toast.makeText(l.getContext(), "Se ha añadido el libro " + bookResponse.getTitle() + " con exito.", Toast.LENGTH_LONG).show();
            });*/
            binding.button3.setOnClickListener((l) -> {
                // Comprobar si el libro ya existe en Firebase
                DatabaseReference shelfRefence = Firebase_Utils.getRootFirebase().child(eBookShelfItem.name().toLowerCase());
                FirebaseBook2 firebaseBook2 = new FirebaseBook2(bookResponse.getKey());
                shelfRefence.child(firebaseBook2.getBookIDFirebase()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // El libro ya está en la biblioteca
                            Toast.makeText(l.getContext(), "El libro " + bookResponse.getTitle() + " ya está en tu biblioteca.", Toast.LENGTH_LONG).show();
                        } else {
                            // El libro no está en la biblioteca, añadirlo
                            shelfRefence.child(firebaseBook2.getBookIDFirebase()).setValue(firebaseBook2);
                            Toast.makeText(l.getContext(), "Se ha añadido el libro " + bookResponse.getTitle() + " con éxito.", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Error en la consulta a Firebase
                        Toast.makeText(l.getContext(), "Error al comprobar la biblioteca.", Toast.LENGTH_LONG).show();
                    }
                });
            });

        }
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AddBookTileBinding binding;
        public ViewHolder(AddBookTileBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public AddBookTileBinding getBinding() {
            return binding;
        }
    }
}
