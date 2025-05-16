package com.nhom11.qlnhasach.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom11.qlnhasach.R;
import com.nhom11.qlnhasach.model.NhaSach;

import java.util.List;

public class NhaSachAdapter extends RecyclerView.Adapter<NhaSachAdapter.NhaSachViewHolder> {

    private Context context;
    private List<NhaSach> nhaSachList;
    private OnItemClickListener listener;

    public NhaSachAdapter(Context context, List<NhaSach> nhaSachList) {
        this.context = context;
        this.nhaSachList = nhaSachList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NhaSachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nha_sach, parent, false);
        return new NhaSachViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NhaSachViewHolder holder, int position) {
        NhaSach currentNhaSach = nhaSachList.get(position);
        holder.textViewTenNhaSach.setText(currentNhaSach.getTenNhaSach());
        holder.textViewDiaChi.setText(currentNhaSach.getDiaChi());
        holder.textViewMaNhaSach.setText(currentNhaSach.getMaNhaSach());
        // Load icon nếu có URI (bạn cần thư viện Glide hoặc Picasso)
        // Ví dụ (cần implement loadImage):
        // if (currentNhaSach.getIconUri() != null) {
        //     loadImage(context, currentNhaSach.getIconUri(), holder.imageViewIcon);
        // } else {
        //     holder.imageViewIcon.setImageResource(R.drawable.ic_launcher_background);
        // }
    }

    @Override
    public int getItemCount() {
        return nhaSachList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(NhaSach nhaSach);
    }

    public class NhaSachViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewIcon;
        public TextView textViewTenNhaSach;
        public TextView textViewDiaChi;
        public TextView textViewMaNhaSach;

        public NhaSachViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
            textViewTenNhaSach = itemView.findViewById(R.id.textViewTenNhaSach);
            textViewDiaChi = itemView.findViewById(R.id.textViewDiaChi);
            textViewMaNhaSach = itemView.findViewById(R.id.textViewMaNhaSach);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onItemClick(nhaSachList.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}