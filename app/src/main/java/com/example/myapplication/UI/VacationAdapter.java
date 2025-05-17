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
    public VacationAdapter(Context context){
        xInflater= LayoutInflater.from(context);
        this.context=context;
    }
    public class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationItemView;
        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            vacationItemView=itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    final Vacation current=xVacations.get(position);
                    Intent intent=new Intent(context, VacationDetails.class);
                    intent.putExtra("vacationID", current.getVacationId());
                    intent.putExtra("vacationTitle", current.getVacationTitle());
                    intent.putExtra("vacationHotel", current.getVacationHotel());
                    intent.putExtra("startDate", current.getStartDate());
                    intent.putExtra("endDate", current.getEndDate());
                    context.startActivity(intent);

                }
            });
        }

    }
    @NonNull
    @Override
    public VacationAdapter.VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=xInflater.inflate(R.layout.vacation_list_item,parent,false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationAdapter.VacationViewHolder holder, int position) {
        if(xVacations!=null){
            Vacation current = xVacations.get(position);
            String title = current.getVacationTitle();
            holder.vacationItemView.setText(title);
        }
        else{
            holder.vacationItemView.setText("No vacation title");
        }

    }

    public int getItemCount() {
        if(xVacations!=null){
            return xVacations.size();

        }
        else return 0;
    }

    public void setVacations(List<Vacation> vacations){
        xVacations=vacations;
        notifyDataSetChanged();
    }


}

