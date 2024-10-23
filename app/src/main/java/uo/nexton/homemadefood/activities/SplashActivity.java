package uo.nexton.homemadefood.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uo.nexton.homemadefood.R;

public class SplashActivity extends AppCompatActivity {

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.secondary));
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.primary));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if(firebaseUser!=null){
                    i = new Intent(getApplicationContext(),HomeActivity.class);
                }else{
                    i = new Intent(getApplicationContext(),LoginActivity.class);
                }
                startActivity(i);
            }
        },3000);
    }
}