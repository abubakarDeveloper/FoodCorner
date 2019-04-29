package ab_developer.com.foodcorner;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TabCategoryAdapter extends  RecyclerView.Adapter<TabCategoryAdapter.CategoryHolder> {

    ArrayList<Category> dataset;
    //    static ArrayList<Category> exampleListFull;
    AdapterView.OnItemClickListener onItemClickListener;


    public TabCategoryAdapter(ArrayList<Category> dataset, AdapterView.OnItemClickListener onItemClickListener) {
        this.dataset = dataset;
        //      exampleListFull = new ArrayList<>(dataset);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.tab_category_item_layout, parent, false);
        return new TabCategoryAdapter.CategoryHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryHolder holder, final int position) {
        holder.tvCatName.setText(dataset.get(position).catName);
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

    //searchviewend
    public class CategoryHolder extends RecyclerView.ViewHolder{


        TextView tvCatName;
        public CategoryHolder(View itemView) {
            super(itemView);

            tvCatName = itemView.findViewById(R.id.tv_cat_name);

        }
    }
}
