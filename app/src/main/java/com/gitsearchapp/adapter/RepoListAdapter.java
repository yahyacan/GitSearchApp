package com.gitsearchapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gitsearchapp.activity.R;
import com.gitsearchapp.model.Repo;

import java.util.List;

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.ViewHolder> {
 
  Context mContext;
  private List<Repo> repos;

  public RepoListAdapter(Context context,List<Repo> list) {
    this.mContext = context;
    this.repos = list;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView txtName;
 
    public ViewHolder(View itemView) {
      super(itemView);
      txtName = (TextView) itemView.findViewById(R.id.txtName);
    }
  }

  @Override
  public int getItemCount() {
    return this.repos.size();
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_repo, parent, false);
    return new ViewHolder(view);
  }

  // 3
  @Override
  public void onBindViewHolder(final ViewHolder holder, final int position) {
    final Repo repo = repos.get(position);
    holder.txtName.setText(repo.getName());
    //Picasso.with(mContext).load(place.getImageResourceId(mContext)).into(holder.placeImage);
  }
}