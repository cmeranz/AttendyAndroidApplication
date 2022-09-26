package com.simerankaur.attendyapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.simerankaur.attendyapplication.R;
import com.simerankaur.attendyapplication.model.Clock;

import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.Views> {
    public DateAdapter(List<Clock> stringList) {
        this.stringList = stringList;
    }
    public onClickDate onClickDate;

    List<Clock> stringList;
    Context context;
    @NonNull
    @Override
    public Views onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dates,parent,false);
        context=parent.getContext();
        return new DateAdapter.Views(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Views holder, int position) {
        Clock clock=stringList.get(position);
        holder.date.setText(clock.getDate());
        holder.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Log.d("qwerty",clock.getClockIn().toString() + clock.getClockOut().toString());

                 onClickDate.onClickDateListener(clock.getClockIn(),clock.getClockOut());
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class Views extends RecyclerView.ViewHolder {
        Button date;
        public Views(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);

        }
    }

    public void setUpButton(onClickDate onClickDate){
        this.onClickDate=onClickDate;
    }

    public interface onClickDate{
        void  onClickDateListener(String clockIn,String clockOut);
    }
}

