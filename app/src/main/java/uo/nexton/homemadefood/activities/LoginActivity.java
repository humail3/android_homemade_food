package uo.nexton.homemadefood.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import uo.nexton.homemadefood.R;
import uo.nexton.homemadefood.models.Users;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout userEmailAddressLogin_ed, userPasswordLogin_ed;
    AppCompatButton login_Btn;
    TextView dontHaveAnAccount_Tv;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        checkInternetConnection();

        firebaseDatabase = FirebaseDatabase.getInstance("https://homemadefood-fee74-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference().child("users");
        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this, options);

        userEmailAddressLogin_ed = findViewById(R.id.userEmailAddressLogin_ed);
        userPasswordLogin_ed = findViewById(R.id.userPasswordLogin_ed);
        login_Btn = findViewById(R.id.addToCart_btn);
        dontHaveAnAccount_Tv = findViewById(R.id.dontHaveAnAccount_Tv);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        login_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String
                        userEmailAddress = userEmailAddressLogin_ed.getEditText().getText().toString().trim(),
                        userPassword = userPasswordLogin_ed.getEditText().getText().toString().trim(),
                        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(!userEmailAddress.isEmpty()){
                    userEmailAddressLogin_ed.setError(null);
                    userEmailAddressLogin_ed.setErrorEnabled(false);
                    if(!userPassword.isEmpty()){
                        userPasswordLogin_ed.setError(null);
                        userPasswordLogin_ed.setErrorEnabled(false);
                        if(userEmailAddress.matches(emailPattern)){
                            userEmailAddressLogin_ed.setError(null);
                            userEmailAddressLogin_ed.setErrorEnabled(false);

                            progressDialog.show();
                            firebaseAuth.signInWithEmailAndPassword(userEmailAddress,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                                        startActivity(i);
                                        progressDialog.dismiss();
                                    }else{
                                        if(isConnected()){
                                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }else{
                                            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                        }
                                    }
                                }
                            });

                        }else{
                            userEmailAddressLogin_ed.setError("Invalid Email");
                        }
                    }else{
                        userPasswordLogin_ed.setError("Password Missing");
                        if(userEmailAddress.isEmpty()){
                            userPasswordLogin_ed.setError("Email Missing");
                        }
                    }
                }else{
                    userEmailAddressLogin_ed.setError("Email Missing");
                    if(userPassword.isEmpty()){
                        userPasswordLogin_ed.setError("Password Missing");
                    }
                }
            }
        });

        dontHaveAnAccount_Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
            }
        });


        userEmailAddressLogin_ed.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    userEmailAddressLogin_ed.setError(null);
                    userEmailAddressLogin_ed.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        userPasswordLogin_ed.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    userPasswordLogin_ed.setError(null);
                    userPasswordLogin_ed.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isConnected()) {
            if (requestCode == 1234) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        String userId = user.getUid();
                                        Query query = databaseReference.orderByChild("userId").equalTo(userId);
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                    startActivity(intent);
                                                    progressDialog.dismiss();
                                                } else {
                                                    String fullName = account.getDisplayName();
                                                    String emailAddress = account.getEmail();
                                                    Users userData = new Users(fullName, emailAddress, userId);
                                                    databaseReference.child(userId).setValue(userData);
                                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    progressDialog.dismiss();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            isConnectionAvailable = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return isConnectionAvailable;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return isConnectionAvailable;
    }
}