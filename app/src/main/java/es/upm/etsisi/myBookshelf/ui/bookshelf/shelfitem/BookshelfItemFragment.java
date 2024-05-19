package es.upm.etsisi.myBookshelf.ui.bookshelf.shelfitem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import es.upm.etsisi.myBookshelf.BookShelfListFragmentDirections;
import es.upm.etsisi.myBookshelf.Firebase.FirebaseBookWrapper;
import es.upm.etsisi.myBookshelf.R;
import es.upm.etsisi.myBookshelf.databinding.FragmentBookshelfItemBinding;

public class BookshelfItemFragment extends Fragment {

    private FragmentBookshelfItemBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_TYPE_OF_BOOKSHELF = "TYPE_OF_BOOKSHELF";

    // TODO: Rename and change types of parameters
    private EBookShelfItem eBookShelfItem;
    public BookshelfItemFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    BookShelfItemModel bookShelfItemModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookshelfItemBinding.inflate(inflater, container, false);
        apply();

        return binding.getRoot();
    }

    public void apply() {
        if (getArguments() != null) {
            eBookShelfItem = (EBookShelfItem) getArguments().getSerializable(ARG_TYPE_OF_BOOKSHELF);
        }
        if (eBookShelfItem == null)
            return;

        binding.nameShelf.setText(eBookShelfItem.getDisplayName());

        bookShelfItemModel = new BookShelfItemModel(eBookShelfItem);

        bookShelfItemModel.getBookResponseList().observe(getViewLifecycleOwner(), (o) -> {
            binding.linearLayout.removeAllViews();
            for (MutableLiveData<FirebaseBookWrapper> bookResponse : o) {
                ImageView imageView = new ImageView(getContext());
                LinearLayout.LayoutParams viewParamsCenter = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, 300);
                viewParamsCenter.setMargins(20, 0, 20, 0);
                imageView.setLayoutParams(viewParamsCenter);
                imageView.setImageResource(R.mipmap.book_shelf_display);


                binding.linearLayout.addView(imageView);

                bookResponse.observe(getViewLifecycleOwner(), (o2) -> {
                    String cover = o2.getBookResponse().getCover();
                    if (cover != null) {
                        Picasso.get().load("https://covers.openlibrary.org/b/id/" + cover + "-L.jpg")
                                .resize(0, 300)
                                .centerCrop().into(imageView);
                    }
                    imageView.setOnClickListener((l) -> {
                        NavHostFragment.findNavController(this).navigate(BookShelfListFragmentDirections.actionBookShelfListFragmentToBookDetailsFragment(o2));
                    });
                });

            }
        });

        binding.addButton.setOnClickListener(this::onClickAddBook);

        binding.getRoot().setOnClickListener(this::onClickShowBooks);
    }

    private void onClickAddBook(View view) {
        NavHostFragment.findNavController(this).navigate(BookShelfListFragmentDirections.actionBookShelfListFragmentToBookAddFragment(bookShelfItemModel.getType()));
    }

    public void onClickShowBooks(View view) {
        NavHostFragment.findNavController(this).navigate(BookShelfListFragmentDirections.actionBookShelfListFragmentToBookLisitingFragment(bookShelfItemModel.getType()));
    }
}