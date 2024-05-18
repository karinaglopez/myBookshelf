package es.upm.etsisi.cumn.grupoc.myshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {

    Intent signInIntent;
    int counter = 0;
    private int MAX_RETRIES = 5;
    private int RC_SIGN_IN = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (FirebaseAuth.getInstance().getCurrentUser() == null){

            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build()
            );

            signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setIsSmartLockEnabled(false)
                    .setLogo(R.drawable.ic_launcher_foreground)
                    .build();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        else {
            Intent intent = new Intent(this, NavActivity.class);
            startActivity(intent);
            finish();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 30)
            onSignInResult(resultCode);
    }


    private void onSignInResult(int result) {
        if (result == RESULT_OK) {
            Toast.makeText(getApplicationContext(), "Logged Successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, NavActivity.class);
            this.startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Logged Failed", Toast.LENGTH_LONG).show();
            if (counter >= MAX_RETRIES) {
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("LOGIN ERROR")
                        .setMessage("Has excedido el numero maximo de intentos, la aplicación se va a cerrar")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                counter++;
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // we are calling our auth
        // listener method on app resume.
        //mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // here we are calling remove auth
        // listener method on stop.
        //mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }
}