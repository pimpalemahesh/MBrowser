package com.myinnovation.mbrowser.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myinnovation.mbrowser.Activities.MainActivity;
import com.myinnovation.mbrowser.Models.LinkParentModel;
import com.myinnovation.mbrowser.R;
import com.myinnovation.mbrowser.databinding.SingleChildrclvLayoutBinding;
import com.myinnovation.mbrowser.databinding.SingleLinkLayoutBinding;

import java.util.ArrayList;

public class LinkParentAdapter extends RecyclerView.Adapter<LinkParentAdapter.LinkViewHolder> {
    ArrayList<LinkParentModel> list;
    Context context;

    public LinkParentAdapter(ArrayList<LinkParentModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public LinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinkViewHolder(LayoutInflater.from(context).inflate(R.layout.single_childrclv_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinkViewHolder holder, int position) {
        LinkParentModel model = list.get(position);
        holder.binding.rclvName.setText(model.getTitle());

        LinkChildAdapter adapter = new LinkChildAdapter(model.getList(), context);
        holder.binding.childRclv.setLayoutManager(new GridLayoutManager(context, 4));
        holder.binding.childRclv.setAdapter(adapter);
        holder.binding.childRclv.setNestedScrollingEnabled(false);
        holder.binding.childRclv.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class LinkViewHolder extends RecyclerView.ViewHolder{
        SingleChildrclvLayoutBinding binding;

        public LinkViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SingleChildrclvLayoutBinding.bind(itemView);
        }
    }
}
