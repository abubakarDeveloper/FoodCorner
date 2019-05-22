package ab_developer.com.foodcorner;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;

import java.util.ArrayList;

public class OptionValueAdapter extends RecyclerView.Adapter<OptionValueAdapter.OptionValueHolder> {

    private ArrayList<Value> dataset;
    private AdapterView.OnItemClickListener onItemClickListener;
    private Value selectedValue;
    private int selectedPosition;


    public OptionValueAdapter(ArrayList<Value> dataset, AdapterView.OnItemClickListener onItemClickListener) {
        this.dataset = dataset;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public OptionValueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OptionValueHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.value_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final OptionValueHolder holder, int position) {
        Log.i("mytag", "value: " + dataset.get(position).value_name);
        holder.rdoOptionValue.setText(dataset.get(position).value_name);
        /*holder.rdoOptionValue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });*/
        holder.rdoOptionValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.rdoOptionValue.isChecked()) {
                    selectedPosition = holder.getAdapterPosition();
                    notifyDataSetChanged();
                }

            }
        });
        if (selectedPosition == position) {
            holder.rdoOptionValue.setChecked(true);
        } else {
            holder.rdoOptionValue.setChecked(false);
        }


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    class OptionValueHolder extends RecyclerView.ViewHolder {

        RadioButton rdoOptionValue;

        public OptionValueHolder(View itemView) {
            super(itemView);
            rdoOptionValue = itemView.findViewById(R.id.rdo_value);
        }
    }
}
