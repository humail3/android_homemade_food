package uo.nexton.homemadefood.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.HashMap;

import uo.nexton.homemadefood.R;

public class ProductDetailsActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    String productId;
    TextView productName_tv, productPrice_tv, productDescription_tv;
    Button addToCart_btn;
    ImageView back_iv, fav_iv, cart_iv;
    String productN, productD, productP, productIv, productQ, productU;
    boolean fav, cart;
    String userId;
    ImageView v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        productId = getIntent().getExtras().getString("productId");

        productName_tv = findViewById(R.id.productNameDA_tv);
        productPrice_tv = findViewById(R.id.productPriceDA_tv);
        productDescription_tv = findViewById(R.id.productDescriptionDA_tv);
        fav_iv = findViewById(R.id.favorite_iv);
        back_iv = findViewById(R.id.back_iv);
        cart_iv = findViewById(R.id.cart_iv);
        addToCart_btn = findViewById(R.id.addToCart_btn);
        v = findViewById(R.id.productView);

        getProductData();

        fav_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> favMap = new HashMap<>();

                favMap.put("productId", productId);
                favMap.put("productName", productN);
                favMap.put("productDescription", productD);
                favMap.put("productPrice", productP);
                favMap.put("productQuantity", productQ);
                favMap.put("productUnit", productU);
                favMap.put("productImage", productIv);
                favMap.put("fav", true);

                databaseReference.child("users").child(userId).child("favorite")
                        .child(productId).child("fav").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    DatabaseReference df;
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    df = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("favorite").child(productId);
                                    df.removeValue();
                                    fav_iv.setImageResource(R.drawable.ic_fav_outlined);
                                    Toast.makeText(getApplicationContext(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
                                } else {
                                    databaseReference.child("users").child(userId).child("favorite")
                                            .child(productId)
                                            .updateChildren(favMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        fav_iv.setImageResource(R.drawable.ic_fav_filled);
                                                        Toast.makeText(getApplicationContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

        addToCart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> cartMap = new HashMap<>();

                cartMap.put("productId", productId);
                cartMap.put("productName", productN);
                cartMap.put("productPrice", productP);
                cartMap.put("productQuantity", productQ);
                cartMap.put("productImage", productIv);
                cartMap.put("cart", true);

                databaseReference.child("users").child(userId).child("cart")
                        .child(productId).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    cart_iv.setImageResource(R.drawable.ic_cart_filled);
                                    Toast.makeText(getApplicationContext(), "Already added to Cart", Toast.LENGTH_SHORT).show();
                                } else {
                                    databaseReference.child("users").child(userId).child("cart")
                                            .child(productId)
                                            .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        databaseReference.child("users").child(userId).child("cartList").child("cartBill").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                long cartBill = (long) snapshot.getValue();
                                                                long productPrice = Long.parseLong(productP);
                                                                long sum = cartBill + productPrice;
                                                                databaseReference.child("users").child(userId).child("cartList").child("cartBill").setValue(sum);
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });

                                                        cart_iv.setImageResource(R.drawable.ic_cart_filled);
                                                        Toast.makeText(getApplicationContext(), "Added to Cart", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });
        cart_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> cartMap = new HashMap<>();

                cartMap.put("productId", productId);
                cartMap.put("productName", productN);
                cartMap.put("productPrice", productP);
                cartMap.put("productImage", productIv);
                cartMap.put("productQuantity", productQ);
                cartMap.put("cart", true);

                databaseReference.child("users").child(userId).child("cart")
                        .child(productId).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    cart_iv.setImageResource(R.drawable.ic_cart_filled);
                                    Toast.makeText(getApplicationContext(), "Already added to Cart", Toast.LENGTH_SHORT).show();
                                } else {
                                    databaseReference.child("users").child(userId).child("cart")
                                            .child(productId)
                                            .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        databaseReference.child("users").child(userId).child("cartList").child("cartBill").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                long cartBill = (long) snapshot.getValue();
                                                                long productPrice = Long.parseLong(productP);
                                                                long sum = cartBill + productPrice;
                                                                databaseReference.child("users").child(userId).child("cartList").child("cartBill").setValue(sum);
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });

                                                        cart_iv.setImageResource(R.drawable.ic_cart_filled);
                                                        Toast.makeText(getApplicationContext(), "Added to Cart", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

    private void getProductData() {
        databaseReference.child("products").child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    databaseReference.child("users").child(userId).child("favorite")
                            .child(productId).child("fav").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        fav = (boolean) snapshot.getValue();
                                        if (fav) {
                                            fav_iv.setImageResource(R.drawable.ic_fav_filled);
                                        } else {
                                            fav_iv.setImageResource(R.drawable.ic_fav_outlined);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                    databaseReference.child("users").child(userId).child("cart")
                            .child(productId).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        cart = (boolean) snapshot.getValue();
                                        if (cart) {
                                            cart_iv.setImageResource(R.drawable.ic_cart_filled);
                                        } else {
                                            cart_iv.setImageResource(R.drawable.ic_cart_white);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                    productN = snapshot.child("productName").getValue().toString();
                    productP = snapshot.child("productPrice").getValue().toString();
                    productD = snapshot.child("productDescription").getValue().toString();
                    productIv = snapshot.child("productImage").getValue().toString();
                    productQ = snapshot.child("productQuantity").getValue().toString();
                    productU = snapshot.child("productUnit").getValue().toString();

                    productName_tv.setText(productN);
                    productPrice_tv.setText(productP + "$");
                    productDescription_tv.setText(productD);
                    Glide
                            .with(getApplicationContext())
                            .load(productIv)
                            .into(v);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}