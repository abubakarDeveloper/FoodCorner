package ab_developer.com.foodcorner;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {

    ArrayList<History> dataset;
    AdapterView.OnItemClickListener onItemClickListener;

    public HistoryAdapter(ArrayList<History> dataset,AdapterView.OnItemClickListener onItemClickListener) {
        this.dataset = dataset;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.history_item_layout, parent, false);
        return new HistoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryHolder holder, final int position) {
//        Picasso.with(holder.itemView.getContext()).load(dataset.get(position).imageLink);
  //      holder.tvUserName.setText("UserName: " +dataset.get(position).userName);
        holder.tvProductName.setText("Product Name: " +dataset.get(position).productName);
        holder.tvProductPrice.setText("Rs. " + dataset.get(position).price);
        holder.tvQuantity.setText( "Quantity: "+dataset.get(position).quantity);
        holder.tvTotalBill.setText("Total Rs. " + dataset.get(position).total_Bill);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(null, holder.itemView, position, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {

    //    ImageView ivProductImage;
      //  TextView tvUserName;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvQuantity;
        TextView tvTotalBill;

        public HistoryHolder(View itemView) {
            super(itemView);

        //    ivProductImage = itemView.findViewById(R.id.iv_product_image);
          //  tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvTotalBill = itemView.findViewById(R.id.tv_total_bill);
        }
    }
}