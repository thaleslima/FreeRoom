package com.ciandt.app.freeroom.adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ciandt.app.freeroom.R;
import com.ciandt.app.freeroom.model.Room;

import java.util.List;

/**
 * Created by Garage on 10/09/15.
 */
public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {
    private OnItemClickListener mListener;
    private List<Room> mDataSet;

    public RoomAdapter(List<Room> mDataSet) {
        this.mDataSet = mDataSet;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.populate(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView nameView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.room_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getLayoutPosition(), mDataSet.get(getLayoutPosition()));
            }
        }

        public void populate(Room data) {
            if(data.isSelected()) {
                nameView.setBackgroundResource(R.drawable.frame_accent);
            }
            else {
                nameView.setBackgroundResource(R.drawable.selectable_item_background);
            }

            nameView.setText(data.getName());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int index, Room room);
    }

    public void setItemSelected(int index)
    {
        for (Room room: mDataSet){
            room.setSelected(false);
        }

        mDataSet.get(index).setSelected(true);
        notifyDataSetChanged();
    }
}
