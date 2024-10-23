package uo.nexton.homemadefood.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import uo.nexton.homemadefood.R;
import uo.nexton.homemadefood.activities.ProductDetailsActivity;
import uo.nexton.homemadefood.models.FavoriteProducts;

public class FavoriteAdapter extends FirebaseRecyclerAdapter<FavoriteProducts, FavoriteAdapter.myviewholder> {
    public FavoriteAdapter(@NonNull FirebaseRecyclerOptions<FavoriteProducts> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FavoriteAdapter.myviewholder holder, int position,
                                    @NonNull FavoriteProducts products) {
        holder.productName.setText(products.getProductName());
        holder.productPrice.setText(products.getProductPrice());
        Glide.with(holder.productImage.getContext()).load(products.getProductImage()).into(holder.productImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(holder.productImage.getContext(), ProductDetailsActivity.class);
                i.putExtra("productId", products.getProductId());
                holder.productImage.getContext().startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public FavoriteAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_layout, parent, false);
        return new FavoriteAdapter.myviewholder(view);
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
