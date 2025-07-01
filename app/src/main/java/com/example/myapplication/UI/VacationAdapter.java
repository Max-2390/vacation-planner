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
import com.example.myapplication.entities.Vacation;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {
    private List<Vacation> xVacations;
    private final Context context;
    private final LayoutInflater xInflater;

    public VacationAdapter(Context context) {
        xInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationCombinedView;

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            vacationCombinedView = itemView.findViewById(R.id.vacationCombinedTextView);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Vacation current = xVacations.get(position);
                Intent intent = new Intent(context, VacationDetails.class);
                intent.putExtra("vacationID", current.getVacationID());
                intent.putExtra("vacationTitle", current.getVacationTitle());
                intent.putExtra("vacationHotel", current.getVacationHotel());
                intent.putExtra("startDate", current.getStartDate());
                intent.putExtra("endDate", current.getEndDate());
                context.startActivity(intent);
            });
        }
    }


    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = xInflater.inflate(R.layout.vacation_list_item, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        if (xVacations != null) {
            Vacation current = xVacations.get(position);
            String combined = current.getVacationTitle() + "\n"
                    + "Dates: " + current.getStartDate() + " - " + current.getEndDate() + "\n"
                    + "Hotel: " + current.getVacationHotel();
            holder.vacationCombinedView.setText(combined);
        } else {
            holder.vacationCombinedView.setText("No vacation data");
        }
    }


    @Override
    public int getItemCount() {
        return (xVacations != null) ? xVacations.size() : 0;
    }

    public void setVacations(List<Vacation> vacations) {
        xVacations = vacations;
        notifyDataSetChanged();
    }
}
