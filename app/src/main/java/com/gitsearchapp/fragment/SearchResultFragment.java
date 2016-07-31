package com.gitsearchapp.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.gitsearchapp.activity.R;
import com.gitsearchapp.adapter.EndlessRecyclerViewScrollListener;
import com.gitsearchapp.adapter.RepoListAdapter;
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

    @InjectView(R.id.list)
    RecyclerView mRecyclerView;

    @InjectView(R.id.btnChangeSpan)
    ImageButton btnChangeSpan;

    private StaggeredGridLayoutManager mStaggeredLayoutManager;

    private ProgressDialog pdialog;

    public static final String EXTRA_URL = "url";
    public static final String USERNAME = "username";
    private int pageIndex = 0;
    private String username;
    private int spanCount = 1;

    private List<Repo> repos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_search_result, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String link = bundle.getString(EXTRA_URL);
            username = bundle.getString(USERNAME);
            searchRepo(link);
        }

        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mStaggeredLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                int curSize = totalItemsCount;
                pageIndex = page;
                Log.e("API",String.format(getResources().getString(R.string.post_link), username, pageIndex, getResources().getInteger(R.integer.per_page)));
                searchRepo(String.format(getResources().getString(R.string.post_link), username, pageIndex, getResources().getInteger(R.integer.per_page)));

            }
        });

    }

    @OnClick(R.id.btnChangeSpan) void onChangeSpan(){
        spanCount = (spanCount+1)%4;
        if (spanCount == 0)
            spanCount = 1;
        mStaggeredLayoutManager.setSpanCount(spanCount);
        if (spanCount == 1)
            btnChangeSpan.setBackgroundResource(R.mipmap.ic_reorder_black);
        else if (spanCount == 2)
            btnChangeSpan.setBackgroundResource(R.mipmap.ic_view_list_black);
        else if (spanCount == 3)
            btnChangeSpan.setBackgroundResource(R.mipmap.ic_view_module_black);
    }

    public void searchRepo(String link) {

        final Gson gson = new Gson();
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("User-Agent", getResources().getString(R.string.user_agent));
        client.get(link, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                pdialog = ProjectPreferences.dialogShow(getActivity(), "Loading repos!", "Please wait...");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String result = new String(response);
                Type listType = new TypeToken<ArrayList<Repo>>() {
                }.getType();
                final List<Repo> reposTemp = gson.fromJson(result, listType);
                //Log.e("RESULT", result);
                int currSize = repos.size();
                repos.addAll(reposTemp);
                final RepoListAdapter adapter = new RepoListAdapter(getActivity(), repos);
                mRecyclerView.setAdapter(adapter);
                adapter.notifyItemRangeInserted(currSize, repos.size() - 1);
                //mStaggeredLayoutManager.setSpanCount(2);

                pdialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e("RESULT", new String(errorResponse));
                pdialog.dismiss();
            }

            @Override
            public void onRetry(int retryNo) {
                Log.e("RESULT", "onRetry");
                pdialog.dismiss();
            }
        });
    }

}
