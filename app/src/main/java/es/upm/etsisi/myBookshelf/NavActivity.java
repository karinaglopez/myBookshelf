package es.upm.etsisi.myBookshelf;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import es.upm.etsisi.myBookshelf.Room.BookshelfDatabase;
import es.upm.etsisi.myBookshelf.databinding.ActivityNavBinding;
import es.upm.etsisi.myBookshelf.ui.bookshelf.shelfitem.EBookShelfItem;

public class NavActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavBinding binding;


    public static BookshelfDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = Room.databaseBuilder(getApplicationContext(),
                BookshelfDatabase.class, "database-name").build();

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
                R.id.bookShelfListFragment, R.id.bookAddFragment, R.id.bookLisitingFragment, R.id.mappingLibraryFragment, R.id.brightnessSensorFragment, R.id.brightnessSensorFragment)
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
        Intent intent = new Intent();
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
            case R.id.mappingLibraryFragment:
                intent = new Intent(this, NearbyLibrariesFragment.class);
                startActivity(intent);
                break;
            case R.id.brightnessSensorFragment:
                navController.navigate(R.id.brightnessSensorFragment);
                break;
        }
        return true;
    }


}