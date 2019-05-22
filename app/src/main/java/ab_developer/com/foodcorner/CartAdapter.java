package ab_developer.com.foodcorner;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class CartAdapter extends  RecyclerView.Adapter<CartAdapter.cartHolder>{

    ArrayList<CartItem> dataset;
    AdapterView.OnItemClickListener addClickListener;
    AdapterView.OnItemClickListener subtractClickListener;
    AdapterView.OnItemClickListener removeClickListener;
    AdapterView.OnItemClickListener onItemClickListener;


    public CartAdapter(ArrayList<CartItem> dataset, AdapterView.OnItemClickListener addClickListener, AdapterView.OnItemClickListener subtractClickListener, AdapterView.OnItemClickListener removeClickListener, AdapterView.OnItemClickListener onItemClickListener) {
        this.dataset = dataset;
        this.addClickListener = addClickListener;
        this.subtractClickListener = subtractClickListener;
        this.removeClickListener = removeClickListener;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public cartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.cart_item_layout, parent, false);
        return new cartHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final cartHolder holder, int position) {
        holder.tvName.setText(dataset.get(position).p.name);
        holder.tvPrice.setText("Rs. " + dataset.get(position).p.price);

       // int amount = (dataset.get(position).p.price * dataset.get(position).quantity) + (dataset.get(position).meat_quantity * dataset.get(position).meat_price);
        int amount = dataset.get(position).p.price * dataset.get(position).quantity;
        int extra_price = dataset.get(position).meat_price;
        int size_price = dataset.get(position).size_price;
        int bottle_price = dataset.get(position).bottle_price;
        int petty_price = dataset.get(position).petty_price;
        int combo_price = dataset.get(position).combo_price;
        holder.tvAmount.setText("Rs. " + amount);
        holder.tvQuantity.setText(String.valueOf(dataset.get(position).quantity));
        holder.tvSize.setText(String.valueOf(dataset.get(position).size));
        holder.tvBottle.setText(dataset.get(position).bottle);
        holder.tvPetty.setText(dataset.get(position).petty);
        holder.tvCombo.setText(dataset.get(position).combo);
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtractClickListener.onItemClick(null, holder.btnRemove, holder.getAdapterPosition(),0);
            }
        });

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClickListener.onItemClick(null, holder.btnAdd, holder.getAdapterPosition(),0);
            }
        });
        holder.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeClickListener.onItemClick(null, holder.btnRemove, holder.getAdapterPosition(),0);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class cartHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvPrice;
        TextView tvRemove;

        TextView tvAmount;

        Button btnAdd;
        TextView tvQuantity;
        Button btnRemove;

        TextView tvSize;
        TextView tvCombo;
        TextView tvPetty;

        TextView tvBottle;
        public cartHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_product_name);
            tvPrice = itemView.findViewById(R.id.tv_product_price);
            tvRemove = itemView.findViewById(R.id.tv_remove);

            tvAmount = itemView.findViewById(R.id.tv_product_amount);
            btnAdd = itemView.findViewById(R.id.btn_add);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            btnRemove = itemView.findViewById(R.id.btn_remove);
            tvSize = itemView.findViewById(R.id.rd_size);
            tvBottle = itemView.findViewById(R.id.tv_bottle);
            tvCombo = itemView.findViewById(R.id.tv_combo);
            tvPetty = itemView.findViewById(R.id.tv_petty);
        }
    }
}