package es.upm.etsisi.myBookshelf.ui.bookshelf.BookListing;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.upm.etsisi.myBookshelf.Firebase.FirebaseBook2;
import es.upm.etsisi.myBookshelf.Firebase.FirebaseBookWrapper;
import es.upm.etsisi.myBookshelf.Firebase.Firebase_Utils;
import es.upm.etsisi.myBookshelf.R;
import es.upm.etsisi.myBookshelf.REST.OpenBooks.BookResponse;
import es.upm.etsisi.myBookshelf.databinding.BookInfoBinding;
import es.upm.etsisi.myBookshelf.ui.bookshelf.shelfitem.EBookShelfItem;

public class BookInfoAdapter extends RecyclerView.Adapter<BookInfoAdapter.ViewHolder>{


    private final Fragment fragment;
    private List<FirebaseBookWrapper> bookList;
    private EBookShelfItem eBookShelfItem;
    private EBookShelfItem eBookShelfItem2;

    public BookInfoAdapter(List<FirebaseBookWrapper> bookList, EBookShelfItem eBookShelfItem, EBookShelfItem eBookShelfItem2, Fragment fragment) {
        this.bookList = bookList;
        this.eBookShelfItem = eBookShelfItem;
        this.fragment = fragment;
        this.eBookShelfItem2 = eBookShelfItem2;

    }

    @NonNull
    @Override
    public BookInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(BookInfoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseBookWrapper firebaseBookWrapper = bookList.get(position);
        BookResponse bookResponse = firebaseBookWrapper.getBookResponse();
        BookInfoBinding binding = holder.getBinding();

        if (bookResponse != null) {
            String cover = bookResponse.getCover();

            binding.imageView12.setImageResource(R.mipmap.book_shelf_display);

            if (cover != null)
                Picasso.get().load("https://covers.openlibrary.org/b/id/" + cover + "-L.jpg")
                        .resize(150, 225)
                        .centerCrop().into(binding.imageView12);

            binding.bookTitle.setText(bookResponse.getTitle());

            binding.button5.setOnClickListener((l) -> {
                // Comprobar si el libro ya existe en Firebase
                DatabaseReference shelfRefence = Firebase_Utils.getRootFirebase().child(eBookShelfItem.name().toLowerCase());
                FirebaseBook2 firebaseBook2 = new FirebaseBook2(bookResponse.getKey());
                shelfRefence.child(firebaseBook2.getBookIDFirebase()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // El libro está en la biblioteca y se elimina
                            shelfRefence.child(firebaseBook2.getBookIDFirebase()).removeValue();
                            //Toast.makeText(l.getContext(), "El libro " + bookResponse.getTitle() + " ha sido eliminado.", Toast.LENGTH_LONG).show();

                        } else {
                            // El libro no está en la biblioteca
                            //Toast.makeText(l.getContext(), "El libro " + bookResponse.getTitle() + " no esta en la biblioteca", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Error en la consulta a Firebase
                        Toast.makeText(l.getContext(), "Error al comprobar la biblioteca.", Toast.LENGTH_LONG).show();
                    }
                });
                DatabaseReference shelfRefence2 = Firebase_Utils.getRootFirebase().child(eBookShelfItem2.name().toLowerCase());
                shelfRefence2.child(firebaseBook2.getBookIDFirebase()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // El libro ya está en la biblioteca
                            Toast.makeText(l.getContext(), "El libro " + bookResponse.getTitle() + " ya está en tu biblioteca.", Toast.LENGTH_LONG).show();
                        } else {
                            // El libro no está en la biblioteca, añadirlo
                            shelfRefence2.child(firebaseBook2.getBookIDFirebase()).setValue(firebaseBook2);
                           Toast.makeText(l.getContext(), "Se ha movido el libro " + bookResponse.getTitle() + " con éxito.", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Error en la consulta a Firebase
                        Toast.makeText(l.getContext(), "Error al comprobar la biblioteca.", Toast.LENGTH_LONG).show();
                    }
                });
            });



            binding.button4.setOnClickListener((l) -> {
                NavHostFragment.findNavController(fragment).navigate(BookLisitingFragmentDirections.actionBookLisitingFragmentToBookDetailsFragment(firebaseBookWrapper));
            });

            binding.button3.setOnClickListener((l) -> {
                // Comprobar si el libro ya existe en Firebase
                DatabaseReference shelfRefence = Firebase_Utils.getRootFirebase().child(eBookShelfItem.name().toLowerCase());
                FirebaseBook2 firebaseBook2 = new FirebaseBook2(bookResponse.getKey());
                shelfRefence.child(firebaseBook2.getBookIDFirebase()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // El libro está en la biblioteca y se elimina
                            shelfRefence.child(firebaseBook2.getBookIDFirebase()).removeValue();
                            Toast.makeText(l.getContext(), "El libro " + bookResponse.getTitle() + " ha sido eliminado.", Toast.LENGTH_LONG).show();

                        } else {
                            // El libro no está en la biblioteca
                            Toast.makeText(l.getContext(), "El libro " + bookResponse.getTitle() + " no esta en la biblioteca", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Error en la consulta a Firebase
                        Toast.makeText(l.getContext(), "Error al comprobar la biblioteca.", Toast.LENGTH_LONG).show();
                    }
                });
                NavHostFragment.findNavController(fragment).navigate(BookLisitingFragmentDirections.actionBookLisitingFragmentToBookDetailsFragment(firebaseBookWrapper));
            });
        }
    }
    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private BookInfoBinding binding;
        public ViewHolder(BookInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public BookInfoBinding getBinding() {
            return binding;
        }
    }
}
