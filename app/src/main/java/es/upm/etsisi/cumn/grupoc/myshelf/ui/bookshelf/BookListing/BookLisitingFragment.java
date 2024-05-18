package es.upm.etsisi.cumn.grupoc.myshelf.ui.bookshelf.BookListing;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import es.upm.etsisi.cumn.grupoc.myshelf.BookShelfListFragmentDirections;
import es.upm.etsisi.cumn.grupoc.myshelf.Firebase.FirebaseBookWrapper;
import es.upm.etsisi.cumn.grupoc.myshelf.R;
import es.upm.etsisi.cumn.grupoc.myshelf.databinding.FragmentBookLisitingBinding;
import es.upm.etsisi.cumn.grupoc.myshelf.ui.bookshelf.shelfitem.BookShelfItemModel;
import es.upm.etsisi.cumn.grupoc.myshelf.ui.bookshelf.shelfitem.EBookShelfItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookLisitingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookLisitingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentBookLisitingBinding binding;

    public BookLisitingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookLisitingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookLisitingFragment newInstance(String param1, String param2) {
        BookLisitingFragment fragment = new BookLisitingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        binding = FragmentBookLisitingBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();
        EBookShelfItem eBookShelfItem = BookLisitingFragmentArgs.fromBundle(bundle).getMyArg();

        binding.shelfName.setText(eBookShelfItem.getDisplayName());
        EBookShelfItem bookShelfItemEAux;

        switch (eBookShelfItem) {
            case READ:
                bookShelfItemEAux = EBookShelfItem.TO_READ;
                break;
            case TO_READ:
                bookShelfItemEAux = EBookShelfItem.READ;
                break;
            default:
                bookShelfItemEAux = null;
        }

        BookListingModel bookListingModel = new BookListingModel(eBookShelfItem);

        bookListingModel.getBookResponseList().observe(getViewLifecycleOwner(), (o) -> {
            ArrayList<FirebaseBookWrapper> firebaseBookWrappers = new ArrayList<>();
            for (MutableLiveData<FirebaseBookWrapper> data: o) {
                data.observe(getViewLifecycleOwner(), (o2) -> {
                    firebaseBookWrappers.add(o2);
                    binding.listBook.setAdapter(new BookInfoAdapter(firebaseBookWrappers, eBookShelfItem, bookShelfItemEAux, this));
                });

            }

        });


        return binding.getRoot();
    }
}