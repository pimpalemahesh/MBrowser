package com.myinnovation.mbrowser.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myinnovation.mbrowser.Activities.MainActivity;
import com.myinnovation.mbrowser.Models.LinkModel;
import com.myinnovation.mbrowser.R;
import com.myinnovation.mbrowser.databinding.SingleLinkLayoutBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.LinkViewHolder> {
    ArrayList<LinkModel> list;
    Context context;

    public LinkAdapter(ArrayList<LinkModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public LinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinkViewHolder(LayoutInflater.from(context).inflate(R.layout.single_link_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinkViewHolder holder, int position) {
        LinkModel model = list.get(position);
        holder.binding.urlName.setText(model.getUrl_name());
        holder.binding.urlImage.setImageResource(model.getUrl_img());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("URL", model.getUrl());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        });
//        Picasso.get()
//                .load(model.getUrl_image())
//                .placeholder(R.drawable.ic_computer)
//                .into(holder.binding.urlImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LinkViewHolder extends RecyclerView.ViewHolder{
        SingleLinkLayoutBinding binding;
        public LinkViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SingleLinkLayoutBinding.bind(itemView);
        }
    }
}
