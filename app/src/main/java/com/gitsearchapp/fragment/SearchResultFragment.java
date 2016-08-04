package com.gitsearchapp.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.gitsearchapp.activity.R;
import com.gitsearchapp.adapter.RepoListAdapter;
import com.gitsearchapp.decorator.DividerItemDecoration;
import com.gitsearchapp.listener.OnLoadMoreListener;
import com.gitsearchapp.model.Repo;
import com.gitsearchapp.util.ProjectPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class SearchResultFragment extends Fragment {

    public static final String EXTRA_URL = "url";
    public static final String USERNAME = "username";

    @InjectView(R.id.list)
    RecyclerView mRecyclerView;

    @InjectView(R.id.btnChangeSpan)
    ImageButton btnChangeSpan;

    private GridLayoutManager mGridLayoutManager;
    private ProgressDialog progressDialog;
    private int pageIndex = 0;
    private String username;
    private int spanCount = 1;

    private List<Repo> repos;
    private RepoListAdapter mRepoListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_search_result, container, false);
        ButterKnife.inject(this, view);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
        mGridLayoutManager.setSpanCount(1);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        repos = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String link = bundle.getString(EXTRA_URL);
            username = bundle.getString(USERNAME);
            searchRepo(link);
        }

        mRepoListAdapter = new RepoListAdapter(mRecyclerView, repos, getActivity());
        mRecyclerView.setAdapter(mRepoListAdapter);

        mRepoListAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                repos.add(null);
                mRepoListAdapter.notifyItemInserted(repos.size() - 1);
                searchRepo(String.format(getResources().getString(R.string.post_link), username, pageIndex, getResources().getInteger(R.integer.per_page)));
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @OnClick(R.id.btnChangeSpan)
    void onChangeSpan() {
        spanCount = (spanCount + 1) % 4;
        if (spanCount == 0)
            spanCount = 1;
        mGridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        if (spanCount == 1)
            btnChangeSpan.setBackgroundResource(R.mipmap.ic_reorder_black);
        else if (spanCount == 2)
            btnChangeSpan.setBackgroundResource(R.mipmap.ic_view_list_black);
        else if (spanCount == 3)
            btnChangeSpan.setBackgroundResource(R.mipmap.ic_view_module_black);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
    }


    public void searchRepo(String link) {

        final Gson gson = new Gson();
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("User-Agent", getResources().getString(R.string.user_agent));
        client.get(link, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                if (isFirstPage())
                    progressDialog = ProjectPreferences.dialogShow(getActivity(), "Loading repos!", "Please wait...");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                if (hasItem()) {
                    repos.remove(repos.size() - 1);
                    mRepoListAdapter.notifyItemRemoved(repos.size());
                }

                String result = new String(response);
                Type listType = new TypeToken<ArrayList<Repo>>() {
                }.getType();
                List<Repo> reposTemp = gson.fromJson(result, listType);
                repos.addAll(reposTemp);

                mRepoListAdapter.notifyDataSetChanged();
                mRepoListAdapter.setLoaded();

                if (isFirstPage())
                    progressDialog.dismiss();

                pageIndex++;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                progressDialog.dismiss();
            }

            @Override
            public void onRetry(int retryNo) {
                progressDialog.dismiss();
            }
        });
    }

    private boolean hasItem() {
        return repos != null && repos.size() > 0;
    }

    private boolean isFirstPage() {
        return pageIndex == 0;
    }

}
