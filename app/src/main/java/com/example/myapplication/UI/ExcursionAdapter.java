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

    public ExcursionAdapter(Context context) {
        this.context = context;
        this.xInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = xInflater.inflate(R.layout.excursion_list_item, parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        if (xExcursions != null && position < xExcursions.size()) {
            Excursion current = xExcursions.get(position);
            // Combine title and date into one string:
            String combined = current.getTitle() + " (" + current.getDate() + ")";
            holder.excursionInfoTextView.setText(combined);
        } else {
            holder.excursionInfoTextView.setText("No excursion info");
        }
    }

    @Override
    public int getItemCount() {
        return (xExcursions != null) ? xExcursions.size() : 0;
    }

    public void setmExcursions(List<Excursion> excursions) {
        this.xExcursions = excursions;
        notifyDataSetChanged();
    }

    class ExcursionViewHolder extends RecyclerView.ViewHolder {
        private final TextView excursionInfoTextView;

        private ExcursionViewHolder(View itemView) {
            super(itemView);
            excursionInfoTextView = itemView.findViewById(R.id.excursionInfoTextView);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && xExcursions != null && position < xExcursions.size()) {
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
}
