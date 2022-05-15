package com.myinnovation.mbrowser.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myinnovation.mbrowser.Activities.MainActivity;
import com.myinnovation.mbrowser.Models.LinkChildModel;
import com.myinnovation.mbrowser.R;
import com.myinnovation.mbrowser.databinding.SingleLinkLayoutBinding;

import java.util.ArrayList;

class LinkChildAdapter extends RecyclerView.Adapter<LinkChildAdapter.LinkChildViewHolder> {

    ArrayList<LinkChildModel> list;
    Context context;

    public LinkChildAdapter(ArrayList<LinkChildModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public LinkChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinkChildViewHolder(LayoutInflater.from(context).inflate(R.layout.single_link_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinkChildViewHolder holder, int position) {
        LinkChildModel model = list.get(position);
        holder.binding.urlName.setText(model.getUrl_name());
        holder.binding.urlImage.setImageResource(model.getUrl_img());
        Bundle data = new Bundle();
        data.putString("URL", model.getUrl());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtras(data);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class LinkChildViewHolder extends RecyclerView.ViewHolder{
        SingleLinkLayoutBinding binding;

        public LinkChildViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SingleLinkLayoutBinding.bind(itemView);
        }
    }
}
