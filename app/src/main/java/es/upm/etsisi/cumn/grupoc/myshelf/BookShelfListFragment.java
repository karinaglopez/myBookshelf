package es.upm.etsisi.cumn.grupoc.myshelf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.upm.etsisi.cumn.grupoc.myshelf.databinding.FragmentBookShelfListBinding;
import es.upm.etsisi.cumn.grupoc.myshelf.ui.bookshelf.shelfitem.BookshelfItemFragment;
import es.upm.etsisi.cumn.grupoc.myshelf.ui.bookshelf.shelfitem.EBookShelfItem;

public class BookShelfListFragment extends Fragment {

    public BookShelfListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentBookShelfListBinding binding = FragmentBookShelfListBinding.inflate(inflater, container, false);


        Bundle args = new Bundle();
        args.putSerializable(BookshelfItemFragment.ARG_TYPE_OF_BOOKSHELF, EBookShelfItem.TO_READ);
        BookshelfItemFragment fragment0 = (BookshelfItemFragment) getChildFragmentManager().getFragments().get(0);
        fragment0.setArguments(args);

        Bundle args1 = new Bundle();
        args1.putSerializable(BookshelfItemFragment.ARG_TYPE_OF_BOOKSHELF, EBookShelfItem.READ);
        getChildFragmentManager().getFragments().get(1).setArguments(args1);

        return binding.getRoot();
    }

}