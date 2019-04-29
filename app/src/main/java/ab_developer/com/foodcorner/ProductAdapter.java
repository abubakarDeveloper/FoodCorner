package ab_developer.com.foodcorner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> implements Filterable {
//public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder>{

    ArrayList<Product> dataset;
    ArrayList<Product> exampleListFull;


    public ProductAdapter(ArrayList<Product> dataset, AdapterView.OnItemClickListener onItemClickListener) {
        this.dataset = dataset;
        exampleListFull = new ArrayList<>(dataset);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.product_item_layout, parent, false);

        return new ProductHolder(v);
    }    AdapterView.OnItemClickListener onItemClickListener;

    @Override
    public void onBindViewHolder(@NonNull final ProductHolder holder, final int position) {
        Picasso.with(holder.itemView.getContext())
                .load(dataset.get(position).imageLink)
                .into(holder.ivProductImage);

        holder.tvName.setText(dataset.get(position).name);
         holder.tvPrice.setText("Rs. " + dataset.get(position).price);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   onItemClickListener.onItemClick(null, holder.itemView, position(0), 0);
                onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), holder.getItemId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFiltre;
    }

    private Filter exampleFiltre = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(exampleListFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Product item: exampleListFull){
                    if(item.name.toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataset.clear();
            dataset.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class ProductHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage;
        TextView tvName;
        TextView tvPrice;

        public ProductHolder(View itemView) {
            super(itemView);

            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvPrice = itemView.findViewById(R.id.tv_product_price);
        }
    }
}
