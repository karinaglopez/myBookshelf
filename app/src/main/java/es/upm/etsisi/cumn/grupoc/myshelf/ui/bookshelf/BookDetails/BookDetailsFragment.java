package es.upm.etsisi.cumn.grupoc.myshelf.ui.bookshelf.BookDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import es.upm.etsisi.cumn.grupoc.myshelf.Firebase.FirebaseBookGlobal;
import es.upm.etsisi.cumn.grupoc.myshelf.Firebase.FirebaseBookWrapper;
import es.upm.etsisi.cumn.grupoc.myshelf.REST.AuthorResponse;
import es.upm.etsisi.cumn.grupoc.myshelf.REST.OpenBooksAdapter;
import es.upm.etsisi.cumn.grupoc.myshelf.databinding.FragmentBookDetailsBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentBookDetailsBinding binding;

    public BookDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookDetailsBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();
        FirebaseBookWrapper firebaseBookWrapper = BookDetailsFragmentArgs.fromBundle(bundle).getMyArg();

        Picasso.get().load("https://covers.openlibrary.org/b/id/" + firebaseBookWrapper.getBookResponse().getCover() + "-L.jpg")
                .into(binding.bookCover);

        binding.bookTitle.setText(firebaseBookWrapper.getBookResponse().getTitle());

        DatabaseReference globalBookRefence = FirebaseDatabase.getInstance().getReference("books");
        globalBookRefence.child(firebaseBookWrapper.getFirebaseBook2().getBookIDFirebase()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.bookAvgRating.setRating(snapshot.getValue(FirebaseBookGlobal.class).avgScore);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        Call<AuthorResponse> call = OpenBooksAdapter.getApiService().getAuthorById(firebaseBookWrapper.getBookResponse().getAuthorKey());
        call.enqueue(new Callback<AuthorResponse>() {
            @Override
            public void onResponse(Call<AuthorResponse> call, Response<AuthorResponse> response) {
                binding.bookAuthor.setText(response.body().getName());
            }

            @Override
            public void onFailure(Call<AuthorResponse> call, Throwable t) {

            }
        });

        binding.bookRating.setRating(firebaseBookWrapper.getFirebaseBook2().getScore());

        binding.bookRating.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            firebaseBookWrapper.updateScore((int) v);
        });

        return binding.getRoot();
    }
}