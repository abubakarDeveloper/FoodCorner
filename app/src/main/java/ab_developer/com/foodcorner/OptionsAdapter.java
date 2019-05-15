package ab_developer.com.foodcorner;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.OptionsHolder>{

    private ArrayList<Option> dataset;

    public OptionsAdapter(ArrayList<Option> dataset) {
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public OptionsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OptionsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.option_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OptionsHolder holder, int position) {
        holder.tvOptionName.setText(dataset.get(position).option_name);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false);
        OptionValueAdapter optionValueAdapter= new OptionValueAdapter(dataset.get(position).valuesList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        holder.rvOptionValues.setLayoutManager(linearLayoutManager);
        holder.rvOptionValues.setAdapter(optionValueAdapter);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    class OptionsHolder extends RecyclerView.ViewHolder{

        TextView tvOptionName;
        RecyclerView rvOptionValues;

        public OptionsHolder(View itemView) {
            super(itemView);
            tvOptionName = itemView.findViewById(R.id.tv_option_name);
            rvOptionValues = itemView.findViewById(R.id.rv_option_values);
        }
    }
}
