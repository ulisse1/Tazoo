package com.example.myapplication;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

        private ArrayList<Alarm> Alarms;
        private OnItemClickListener mListener;
        public interface OnItemClickListener {
                void onItemClick(int position);
                void onDeleteClick(int position);
        }
        public void setOnItemClickListener(OnItemClickListener listener) {
                mListener = listener;
        }
        public static class CardViewHolder extends RecyclerView.ViewHolder {
                public TextView phoneNo;
                public TextView phoneMsg;
                public TextView date;
                public TextView time;
                public CardViewHolder(View itemView, final OnItemClickListener listener) {
                        super(itemView);
                        phoneNo = itemView.findViewById(R.id.phoneNo);
                        phoneMsg = itemView.findViewById(R.id.shortenMsg);
                        date = itemView.findViewById(R.id.dateField);
                        time = itemView.findViewById(R.id.timeField);

                        itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        if (listener != null) {
                                                int position = getAdapterPosition();
                                                if (position != RecyclerView.NO_POSITION) {
                                                        listener.onItemClick(position);
                                                }
                                        }
                                }
                        });

                        itemView.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                        if (listener != null) {
                                                int position = getAdapterPosition();
                                                if(position != RecyclerView.NO_POSITION) {
                                                        listener.onDeleteClick(position);
                                                }
                                        }
                                        return false;
                                }
                        });



                }
        }
        public CardAdapter(ArrayList<Alarm> exampleList) {
                Alarms = exampleList;
        }
        @NonNull
        @Override
        public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item, parent, false);
                return new CardViewHolder(v, mListener);
        }
        @Override
        public void onBindViewHolder(CardViewHolder holder, int position) {
                Alarm currentItem = Alarms.get(position);
                holder.phoneNo.setText(currentItem.getPhoneNo());
                holder.phoneMsg.setText(currentItem.getShortenMsg());
                holder.date.setText(currentItem.getDate());
                holder.time.setText(currentItem.getTime());
        }

        @Override
        public int getItemCount() {
                return Alarms.size();
        }
}