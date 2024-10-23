package uo.nexton.homemadefood.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uo.nexton.homemadefood.R;
import uo.nexton.homemadefood.models.Users;

public class ConfirmOrderActivity extends AppCompatActivity {

    TextView totalCost, personName, personPhoneNumber, personAddress;
    Button confirmOrder_btn;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        totalCost = findViewById(R.id.totalCost);
        personName = findViewById(R.id.personName);
        personPhoneNumber = findViewById(R.id.personPhoneNumber);
        personAddress = findViewById(R.id.personAddress);
        confirmOrder_btn = findViewById(R.id.confirmOrder_btn);

        databaseReference.child(firebaseUser.getUid()).child("cartList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    long cartBill = (long) snapshot.child("cartBill").getValue();
                    totalCost.setText(String.valueOf(cartBill)+"$");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child(firebaseUser.getUid()).child("shippingAddress").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    final Users users = snapshot.getValue(Users.class);
                    personName.setText(users.getUserName().toString());
                    personPhoneNumber.setText(users.getPhoneNumber().toString());
                    personAddress.setText(users.getUserAddress().toString()+", "+users.getUserCity());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        confirmOrder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Order Placed Successfully",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}