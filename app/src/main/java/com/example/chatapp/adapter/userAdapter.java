package com.example.chatapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.R;
import com.example.chatapp.activities.MessageActivity;
import com.example.chatapp.model.User;

import java.util.List;

public class userAdapter extends RecyclerView.Adapter<userAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUsers;

    public userAdapter(Context mContext, List<User> userList){
        this.mContext = mContext;
        this.mUsers = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = mUsers.get(position);
        holder.contactName.setText(user.getUsername());
        holder.contactStatus.setText(user.getStatus());

        if(user.getImageUrl().equals("default")){
            holder.profileImg.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(mContext).load(user.getImageUrl()).into(holder.profileImg);
        }

        // add Listener to the item:
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                // send the userId:
                intent.putExtra("userId", user.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{



        TextView contactName, contactStatus;
        ImageView profileImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contactName = itemView.findViewById(R.id.ContactName);
            contactStatus = itemView.findViewById(R.id.ContactStatus);
            profileImg = itemView.findViewById(R.id.imgProfile);
        }
    }

}
