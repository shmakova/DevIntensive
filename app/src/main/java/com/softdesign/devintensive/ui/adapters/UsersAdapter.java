package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shmakova on 14.07.16.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    Context mContext;

    List<UserListRes.UserData> mUsers;

    public UsersAdapter(List<UserListRes.UserData> users) {
        mUsers = users;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);
        return new UserViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserListRes.UserData user = mUsers.get(position);
        Context mContext = holder.mUserPhoto.getContext();

        Picasso.with(mContext)
                .load(user.getPublicInfo().getPhoto())
                .placeholder(mContext.getResources().getDrawable(R.mipmap.user_bg))
                .error(mContext.getResources().getDrawable(R.mipmap.user_bg))
                .into(holder.mUserPhoto);

        holder.mFullName.setText(user.getFullName());
        holder.mRating.setText(String.valueOf(user.getProfileValues().getRating()));
        holder.mCodeLines.setText(String.valueOf(user.getProfileValues().getLinesCode()));
        holder.mProjects.setText(String.valueOf(user.getProfileValues().getProjects()));

        if (user.getPublicInfo().getBio() == null || user.getPublicInfo().getBio().isEmpty()) {
            holder.mBio.setVisibility(View.GONE);
        } else {
            holder.mBio.setVisibility(View.VISIBLE);
            holder.mBio.setText(user.getPublicInfo().getBio());
        }

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        protected AspectRatioImageView mUserPhoto;
        protected TextView mFullName, mRating, mCodeLines, mProjects, mBio;

        public UserViewHolder(View itemView) {
            super(itemView);

            mUserPhoto = (AspectRatioImageView) itemView.findViewById(R.id.user_photo);
            mFullName = (TextView) itemView.findViewById(R.id.user_full_name_txt);
            mRating = (TextView) itemView.findViewById(R.id.rating_txt);
            mCodeLines = (TextView) itemView.findViewById(R.id.code_lines_txt);
            mProjects = (TextView) itemView.findViewById(R.id.projects_txt);
            mBio = (TextView) itemView.findViewById(R.id.bio_txt);
        }
    }
}
