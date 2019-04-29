package ab_developer.com.foodcorner;

import android.content.Context;
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

//public class CategoryAdapter extends  RecyclerView.Adapter<CategoryAdapter.CategoryHolder> implements Filterable{
public class CategoryAdapter extends  RecyclerView.Adapter<CategoryAdapter.CategoryHolder>{

    ArrayList<Category> dataset;
//    static ArrayList<Category> exampleListFull;
    AdapterView.OnItemClickListener onItemClickListener;

    public CategoryAdapter(ArrayList<Category> dataset, AdapterView.OnItemClickListener onItemClickListener) {
        this.dataset = dataset;
  //      exampleListFull = new ArrayList<>(dataset);
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.category_item_layout, parent, false);
        return new CategoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryHolder holder, final int position) {
        //error show by picasso.get(holder.itemView.getContext())
        //getContext() method cannot be applied with get()
        Picasso.with(holder.itemView.getContext())
                .load(dataset.get(position).catImage)
                .into(holder.ivCatImage);
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


/*
    public static ArrayList<Category> getExampleListFull() {
        return exampleListFull;
    }

    @Override
    public Filter getFilter() {
        getExampleListFull();

        return null;
    }
    //searchview start

    private final Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Category> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(exampleListFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Category item : exampleListFull){
                    if(item.catName.toLowerCase().contains(filterPattern)){
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
            dataset.addAll((ArrayList)results.values);
            notifyDataSetChanged();

        }
    };*/

    //searchviewend
    public class CategoryHolder extends RecyclerView.ViewHolder{


        ImageView ivCatImage;
        TextView tvCatName;
        public CategoryHolder(View itemView) {
            super(itemView);

            ivCatImage = itemView.findViewById(R.id.iv_cat_image);
            tvCatName = itemView.findViewById(R.id.tv_cat_name);

        }
    }
}
