package com.gitsearchapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gitsearchapp.activity.R;
import com.gitsearchapp.listener.OnLoadMoreListener;
import com.gitsearchapp.model.Repo;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RepoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener mOnLoadMoreListener;

    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private int[] lastPositions;
    private RecyclerView mRecyclerView;
    private List<Repo> data;
    private Context cntx;

    public RepoListAdapter(RecyclerView recyclerView, List<Repo> list, Context context) {
        this.cntx = context;
        this.data = list;
        this.mRecyclerView = recyclerView;
        final GridLayoutManager mGridLayoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        lastPositions = new int[mGridLayoutManager.getSpanCount()];
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = mGridLayoutManager.getItemCount();
                lastVisibleItem = mGridLayoutManager.findLastCompletelyVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(cntx).inflate(R.layout.cell_repo, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(cntx).inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            Repo repo = data.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.txtName.setText(repo.getName());
            viewHolder.txtDescription.setText(repo.getDescription());
            viewHolder.txtStarCount.setText("" + repo.getStargazers_count());
            if (repo.isFork())
                viewHolder.imgConnectType.setBackgroundResource(R.mipmap.ic_call_split_black_48dp);
            else
                viewHolder.imgConnectType.setBackgroundResource(R.mipmap.ic_cloud_upload_black_48dp);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @InjectView(R.id.txtName) TextView txtName;
        @InjectView(R.id.txtDescription) TextView txtDescription;
        @InjectView(R.id.txtStarCount) TextView txtStarCount;
        @InjectView(R.id.imgConnectType) ImageView imgConnectType;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            openDetailDialog(data.get(getPosition()));
        }

        public void openDetailDialog(Repo repo){
            final Dialog dialog=new Dialog(cntx,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.setContentView(R.layout.repo_detail_dialog);
            dialog.setTitle(cntx.getString(R.string.repo_detail));

            TextView txtName = (TextView) dialog.findViewById(R.id.txtName);
            TextView txtGitUrl = (TextView) dialog.findViewById(R.id.txtGitUrl);
            TextView txtSshUrl = (TextView) dialog.findViewById(R.id.txtSshUrl);
            TextView txtCloneUrl = (TextView) dialog.findViewById(R.id.txtCloneUrl);

            txtName.setText(repo.getName());
            txtGitUrl.setText(repo.getGit_url());
            txtSshUrl.setText(repo.getSsh_url());
            txtCloneUrl.setText(repo.getClone_url());


            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }
}