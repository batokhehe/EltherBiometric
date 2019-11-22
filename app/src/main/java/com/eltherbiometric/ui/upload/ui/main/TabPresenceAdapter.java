package com.eltherbiometric.ui.upload.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eltherbiometric.R;
import com.eltherbiometric.data.model.Presence;

import java.util.List;

public class TabPresenceAdapter extends RecyclerView.Adapter<TabPresenceAdapter.ViewHolder>  {
    private List<Presence> dataList;
    private Context mContext;

    public TabPresenceAdapter(List<Presence> data, Context context)
    {
        this.dataList = data;
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView vNik, vName, vDate, vTime, vMethod;

        public ViewHolder(View itemView)
        {
            super(itemView);
            this.vNik = itemView.findViewById(R.id.nik);
            this.vDate = itemView.findViewById(R.id.date);
            this.vTime = itemView.findViewById(R.id.time);
            this.vMethod = itemView.findViewById(R.id.method);
        }
    }

    @NonNull
    @Override
    public TabPresenceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_item_presence, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TabPresenceAdapter.ViewHolder viewHolder, final int i) {
        final Presence data = dataList.get(i);
        viewHolder.vNik.setText(data.getNik());
        viewHolder.vDate.setText(data.getDate());
        viewHolder.vTime.setText(data.getTime());
        viewHolder.vMethod.setText(data.getMethod());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
