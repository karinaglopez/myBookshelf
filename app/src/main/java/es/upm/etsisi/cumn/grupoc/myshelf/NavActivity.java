package es.upm.etsisi.cumn.grupoc.myshelf;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.HashMap;

import es.upm.etsisi.cumn.grupoc.myshelf.REST.BookResponse;
import es.upm.etsisi.cumn.grupoc.myshelf.REST.BookResponseSearch;
import es.upm.etsisi.cumn.grupoc.myshelf.REST.OpenBooksAdapter;
import es.upm.etsisi.cumn.grupoc.myshelf.databinding.ActivityNavBinding;
import es.upm.etsisi.cumn.grupoc.myshelf.ui.bookshelf.ISBN.ISBNScannerActivity;
import es.upm.etsisi.cumn.grupoc.myshelf.ui.bookshelf.shelfitem.EBookShelfItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        View view = binding.navView.getHeaderView(0);
        TextView textView = view.findViewById(R.id.userEmail);
        textView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());


        TextView userTextView = view.findViewById(R.id.userName);
        userTextView.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        Button signout = view.findViewById(R.id.signoutButton);
        signout.setOnClickListener((l) -> {
            FirebaseAuth.getInstance().signOut();
            AuthUI.getInstance().signOut(getApplicationContext());
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        setSupportActionBar(binding.appBarNav.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.bookShelfListFragment, R.id.bookAddFragment, R.id.bookLisitingFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav);
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {
            case R.id.bookShelfListFragment:
                navController.navigate(R.id.bookShelfListFragment);
                break;
            case R.id.bookLisitingFragmentREAD:

                bundle.putSerializable("myArg", EBookShelfItem.READ);
                navController.navigate(R.id.bookLisitingFragment, bundle);
                break;
            case R.id.bookLisitingFragmentTOREAD:
                bundle.putSerializable("myArg", EBookShelfItem.TO_READ);
                navController.navigate(R.id.bookLisitingFragment, bundle);
                break;
        }
        return true;
    }


}