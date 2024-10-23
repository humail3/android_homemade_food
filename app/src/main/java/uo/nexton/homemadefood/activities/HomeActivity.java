package uo.nexton.homemadefood.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import uo.nexton.homemadefood.R;
import uo.nexton.homemadefood.fragments.CartFragment;
import uo.nexton.homemadefood.fragments.FavoriteFragment;
import uo.nexton.homemadefood.fragments.HomeFragment;
import uo.nexton.homemadefood.fragments.ProfileFragment;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        loadFragment();
        checkInternetConnection();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    private void loadFragment() {
        FragmentTransaction fragmentHome = getSupportFragmentManager().beginTransaction();
        fragmentHome.replace(R.id.home_fragment_layout, new HomeFragment());
        fragmentHome.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_nav_home:
                checkInternetConnection();
                FragmentTransaction fragmentHome = getSupportFragmentManager().beginTransaction();
                fragmentHome.replace(R.id.home_fragment_layout, new HomeFragment());
                fragmentHome.commit();
                break;
            case R.id.bottom_nav_cart:
                checkInternetConnection();
                FragmentTransaction fragmentMenu = getSupportFragmentManager().beginTransaction();
                fragmentMenu.replace(R.id.home_fragment_layout, new CartFragment());
                fragmentMenu.commit();
                break;
            case R.id.bottom_nav_fav:
                checkInternetConnection();
                FragmentTransaction fragmentCart = getSupportFragmentManager().beginTransaction();
                fragmentCart.replace(R.id.home_fragment_layout, new FavoriteFragment());
                fragmentCart.commit();
                break;
            case R.id.bottom_nav_profile:
                checkInternetConnection();
                FragmentTransaction fragmentProfile = getSupportFragmentManager().beginTransaction();
                fragmentProfile.replace(R.id.home_fragment_layout, new ProfileFragment());
                fragmentProfile.commit();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Foodator");
        builder.setMessage("Do you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void checkInternetConnection() {
        if (isConnected()) {
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isConnected() {
        boolean isConnectionAvailable = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            isConnectionAvailable = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return isConnectionAvailable;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return isConnectionAvailable;
    }

}
