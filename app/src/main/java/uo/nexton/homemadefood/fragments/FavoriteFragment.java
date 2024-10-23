package uo.nexton.homemadefood.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import uo.nexton.homemadefood.R;
import uo.nexton.homemadefood.adapters.FavoriteAdapter;
import uo.nexton.homemadefood.models.FavoriteProducts;

public class FavoriteFragment extends Fragment {

    View root;
    RecyclerView favProducts_rcv;
    FavoriteAdapter favProductsAdapter;
    String userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_favorite, container, false);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();

        favProducts_rcv = root.findViewById(R.id.favProducts_rcv);
        favProducts_rcv.setLayoutManager(new GridLayoutManager(getContext().getApplicationContext(), 2));

        FirebaseRecyclerOptions<FavoriteProducts> options =
                new FirebaseRecyclerOptions.Builder<FavoriteProducts>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("favorite"), FavoriteProducts.class)
                        .build();

        favProductsAdapter = new FavoriteAdapter(options);
        favProducts_rcv.setAdapter(favProductsAdapter);

        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        favProducts_rcv.getRecycledViewPool().clear();
        favProductsAdapter.notifyDataSetChanged();
        favProductsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        favProductsAdapter.stopListening();
    }
}