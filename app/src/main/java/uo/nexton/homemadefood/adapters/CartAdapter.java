package uo.nexton.homemadefood.adapters;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uo.nexton.homemadefood.R;
import uo.nexton.homemadefood.models.CartProducts;

public class CartAdapter extends FirebaseRecyclerAdapter<CartProducts, CartAdapter.myviewholder> {
    public CartAdapter(@NonNull FirebaseRecyclerOptions<CartProducts> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CartAdapter.myviewholder holder, int position,
                                    @NonNull CartProducts products) {
        holder.productName.setText(products.getProductName());
        holder.productPrice.setText(products.getProductPrice());
        Glide.with(holder.productImage.getContext()).load(products.getProductImage()).into(holder.productImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUpmenu(view);
            }

            private void showPopUpmenu(View view) {

                PopupMenu popupMenu = new PopupMenu(holder.productImage.getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.delete_item_menu,
                        popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.delete_item:

                                DatabaseReference df;
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                df = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("cart").child(products.getProductId());
                                df.removeValue();

                                databaseReference.child("users").child(user.getUid()).child("cartList").child("cartBill").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        long cartBill = (long) snapshot.getValue();
                                        long productPrice = Long.parseLong(products.getProductPrice());
                                        long sum = cartBill-productPrice;
                                        databaseReference.child("users").child(user.getUid()).child("cartList").child("cartBill").setValue(sum);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                Toast.makeText(holder.productName.getContext(), "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                                notifyItemChanged(holder.getAdapterPosition());
                                return true;
                        }

                        return false;
                    }

                });
                popupMenu.show();

            }
        });
    }

    @NonNull
    @Override
    public CartAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_layout, parent, false);
        return new CartAdapter.myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage_iv);
            productName = itemView.findViewById(R.id.productName_tv);
            productPrice = itemView.findViewById(R.id.productPrice_tv);
        }
    }
}
