package com.example.takemethereapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BlogRecylerAdapter extends RecyclerView.Adapter<BlogRecylerAdapter.ViewHolder> {

    public List<BlogPost> blog_list;

    public BlogRecylerAdapter(List<BlogPost> blog_list){
        this.blog_list = blog_list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_blog_list_item , parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String desc_data = blog_list.get(position).getDescription();
        holder.setDescText(desc_data);
    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView descView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDescText(String DescText){
            descView = mView.findViewById(R.id.blog_desc);
            descView.setText(DescText);
        }
    }
}
