package com.example.myapplication.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.entities.Excursion;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
    private List<Excursion> xExcursions;
    private final Context context;
    private final LayoutInflater xInflater;
    class ExcursionViewHolder extends RecyclerView.ViewHolder {

        private final TextView excursionItemView;

        private ExcursionViewHolder(View itemView) {
            super(itemView);
            excursionItemView = itemView.findViewById(R.id.textView3);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Excursion current = xExcursions.get(position);
                    Intent intent = new Intent(context, ExcursionDetails.class);
                    intent.putExtra("excursionID", current.getId());
                    intent.putExtra("excursionTitle", current.getTitle());
                    intent.putExtra("vacationID", current.getVacationID());
                    intent.putExtra("excursionDate", current.getDate());
                    context.startActivity(intent);
                }
            });
        }
    }

    public ExcursionAdapter(Context context) {
        xInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = xInflater.inflate(R.layout.excursion_list_item, parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        if (xExcursions != null) {
            Excursion current = xExcursions.get(position);
            String title = current.getTitle();
            int vacationID = current.getVacationID();
            holder.excursionItemView.setText(title);
        } else {
            holder.excursionItemView.setText("No excursion title");
        }
    }

    public void setmExcursions(List<Excursion> excursions) {
        xExcursions = excursions;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        if (xExcursions != null) return xExcursions.size();
        else return 0;
    }

}