package uo.nexton.homemadefood.activities;

import android.app.ProgressDialog;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import uo.nexton.homemadefood.R;
import uo.nexton.homemadefood.models.Users;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout userNameRegister_ed, userEmailAddressRegister_ed, userPasswordRegister_ed;
    AppCompatButton register_Btn;
    TextView alreadyHaveAnAccount_Tv;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        checkInternetConnection();

        firebaseDatabase = FirebaseDatabase.getInstance("https://homemadefood-fee74-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference().child("users");
        firebaseAuth = FirebaseAuth.getInstance();

        userNameRegister_ed = findViewById(R.id.userNameRegister_ed);
        userEmailAddressRegister_ed = findViewById(R.id.userEmailAddressRegister_ed);
        userPasswordRegister_ed = findViewById(R.id.userPasswordRegister_ed);
        register_Btn = findViewById(R.id.register_Btn);
        alreadyHaveAnAccount_Tv = findViewById(R.id.alreadyHaveAnAccount_Tv);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating new account");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        register_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String
                        userName = userNameRegister_ed.getEditText().getText().toString().trim(),
                        userEmailAddress = userEmailAddressRegister_ed.getEditText().getText().toString().trim(),
                        userPassword = userPasswordRegister_ed.getEditText().getText().toString().trim(),
                        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.com";

                if (!userName.isEmpty()) {
                    userNameRegister_ed.setError(null);
                    userNameRegister_ed.setErrorEnabled(false);
                    if (!userEmailAddress.isEmpty()) {
                        userEmailAddressRegister_ed.setError(null);
                        userEmailAddressRegister_ed.setErrorEnabled(false);
                        if (!userPassword.isEmpty()) {
                            userPasswordRegister_ed.setError(null);
                            userPasswordRegister_ed.setErrorEnabled(false);
                            if (userEmailAddress.matches(emailPattern)) {
                                userEmailAddressRegister_ed.setError(null);
                                userEmailAddressRegister_ed.setErrorEnabled(false);
                                if (userPassword.length() >= 8) {
                                    userPasswordRegister_ed.setError(null);
                                    userPasswordRegister_ed.setErrorEnabled(false);

                                    progressDialog.show();
                                    firebaseAuth.createUserWithEmailAndPassword(userEmailAddress,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                firebaseUser = firebaseAuth.getCurrentUser();
                                                if(firebaseUser!=null){
                                                    userId = firebaseUser.getUid();
                                                }
                                                Users userData = new Users(userName,userEmailAddress,userPassword,userId);
                                                databaseReference.child(userId).setValue(userData);
                                                databaseReference.child(userId).child("cartList").child("cartBill").setValue(0);
                                                Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                                                Toast.makeText(getApplicationContext(), "User Successfully Registered", Toast.LENGTH_LONG).show();
                                                startActivity(i);
                                                progressDialog.dismiss();
                                            }else{
                                                if(isConnected()){
                                                    Toast.makeText(RegisterActivity.this, "Email Already Registered",Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }else{
                                                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                                                    progressDialog.dismiss();
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    userPasswordRegister_ed.setError("Password should be 8 characters or more");
                                }
                            } else {
                                userEmailAddressRegister_ed.setError("Invalid Email");
                            }
                        } else {
                            if (userEmailAddress.isEmpty()) {
                                userEmailAddressRegister_ed.setError("Email Missing");
                            }
                            if (userPassword.isEmpty()) {
                                userPasswordRegister_ed.setError("Password Missing");
                            }
                            if (userName.isEmpty()) {
                                userNameRegister_ed.setError("Username Missing");
                            }
                        }
                    } else {
                        if (userEmailAddress.isEmpty()) {
                            userEmailAddressRegister_ed.setError("Email Missing");
                        }
                        if (userPassword.isEmpty()) {
                            userPasswordRegister_ed.setError("Password Missing");
                        }
                        if (userName.isEmpty()) {
                            userNameRegister_ed.setError("Username Missing");
                        }
                    }
                } else {
                    if (userEmailAddress.isEmpty()) {
                        userEmailAddressRegister_ed.setError("Email Missing");
                    }
                    if (userPassword.isEmpty()) {
                        userPasswordRegister_ed.setError("Password Missing");
                    }
                    if (userName.isEmpty()) {
                        userNameRegister_ed.setError("Username Missing");
                    }
                }

            }
        });

        alreadyHaveAnAccount_Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });

        userEmailAddressRegister_ed.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    userEmailAddressRegister_ed.setError(null);
                    userEmailAddressRegister_ed.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        userPasswordRegister_ed.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    userPasswordRegister_ed.setError(null);
                    userPasswordRegister_ed.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        userNameRegister_ed.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    userNameRegister_ed.setError(null);
                    userNameRegister_ed.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


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