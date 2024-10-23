package uo.nexton.homemadefood.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uo.nexton.homemadefood.R;
import uo.nexton.homemadefood.activities.ConfirmOrderActivity;
import uo.nexton.homemadefood.adapters.CartAdapter;
import uo.nexton.homemadefood.models.CartProducts;
import uo.nexton.homemadefood.models.Users;

public class CartFragment extends Fragment {

    View root;
    RecyclerView cartProducts_rcv;
    CartAdapter cartProductsAdapter;
    String userId;
    ExtendedFloatingActionButton checkoutFloatingActionButton;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_cart, container, false);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        cartProducts_rcv = root.findViewById(R.id.cartProducts_rcv);
        checkoutFloatingActionButton = root.findViewById(R.id.checkoutFloatingActionButton);
        cartProducts_rcv.setLayoutManager(new GridLayoutManager(getContext().getApplicationContext(), 2));

        FirebaseRecyclerOptions<CartProducts> options =
                new FirebaseRecyclerOptions.Builder<CartProducts>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("cart"), CartProducts.class)
                        .build();

        cartProductsAdapter = new CartAdapter(options);
        cartProducts_rcv.setAdapter(cartProductsAdapter);

        checkoutFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(container.getContext());
                LayoutInflater inflater = LayoutInflater.from(container.getContext());
                View editShippingView = inflater.inflate(R.layout.shipping_detials_dialog, null);
                alertDialog.setView(editShippingView);
                AlertDialog dialog = alertDialog.create();

                dialog.show();

                TextInputLayout
                        fullName_shipping_ed = editShippingView.findViewById(R.id.fullName_shipping_ed),
                        phoneNumber_shipping_ed = editShippingView.findViewById(R.id.phoneNumber_shipping_ed),
                        address_shipping_ed = editShippingView.findViewById(R.id.address_shipping_ed),
                        city_shipping_ed = editShippingView.findViewById(R.id.city_shipping_ed);
                Button
                        updateShippingDialog = editShippingView.findViewById(R.id.update_shipping_btn),
                        cancelShippingDialog = editShippingView.findViewById(R.id.cancel_shipping_btn);

                databaseReference.child(userId).child("shippingAddress").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            final Users users = snapshot.getValue(Users.class);
                            fullName_shipping_ed.getEditText().setText(users.getUserName().toString());
                            phoneNumber_shipping_ed.getEditText().setText(users.getPhoneNumber().toString());
                            address_shipping_ed.getEditText().setText(users.getUserAddress().toString());
                            city_shipping_ed.getEditText().setText(users.getUserCity().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                updateShippingDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String
                                fullName = fullName_shipping_ed.getEditText().getText().toString().trim(),
                                phoneNumber = phoneNumber_shipping_ed.getEditText().getText().toString().trim(),
                                address = address_shipping_ed.getEditText().getText().toString().trim(),
                                city = city_shipping_ed.getEditText().getText().toString().trim();

                        if(!fullName.isEmpty()){
                            fullName_shipping_ed.setErrorEnabled(false);
                            fullName_shipping_ed.setError(null);
                            if(!phoneNumber.isEmpty()){
                                phoneNumber_shipping_ed.setErrorEnabled(false);
                                phoneNumber_shipping_ed.setError(null);
                                if(!address.isEmpty()){
                                    address_shipping_ed.setErrorEnabled(false);
                                    address_shipping_ed.setError(null);
                                    if(!city.isEmpty()){
                                        city_shipping_ed.setErrorEnabled(false);
                                        city_shipping_ed.setError(null);

                                        if (firebaseUser != null) {
                                            Users users = new Users(fullName, firebaseUser.getEmail(), phoneNumber, address, city);
                                            databaseReference.child(userId).child("shippingAddress").setValue(users);
                                            Toast.makeText(getContext().getApplicationContext(),"Information Updated Successfully",Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(getContext().getApplicationContext(), ConfirmOrderActivity.class);
                                            startActivity(i);
                                        }
                                        dialog.dismiss();



                                    }else{
                                        city_shipping_ed.setError("City Missing");
                                    }
                                }else{
                                    address_shipping_ed.setError("Address Missing");
                                }
                            }else{
                                phoneNumber_shipping_ed.setError("Phone Missing");
                            }

                        }else{
                            fullName_shipping_ed.setError("Full Name Missing");
                        }
                    }
                });

                cancelShippingDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        cartProducts_rcv.getRecycledViewPool().clear();
        cartProductsAdapter.notifyDataSetChanged();
        cartProductsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        cartProductsAdapter.stopListening();
    }
}