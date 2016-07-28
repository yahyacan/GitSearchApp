package com.gitsearchapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gitsearchapp.activity.R;
import com.gitsearchapp.model.Repo;
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

/**
 * Created by yahya on 28/07/16.
 */
public class SearchFragment extends Fragment {

    @InjectView(R.id.btnSearch) Button btnSearch;
    @InjectView(R.id.edSearch) EditText edSearch;

    //Variables
    private String URL = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_search, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick(R.id.btnSearch)
    public void onClickSearch(){
        URL = "https://api.github.com/users/"+ edSearch.getText().toString().trim() +"/repos?type=owner/page=0&per_page=10";
        final Gson gson = new Gson();
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("User-Agent","Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_5_8; en-US) AppleWebKit/532.5 (KHTML, like Gecko) Chrome/4.0.249.0 Safari/532.5");
        client.get(URL, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                String result = new String(response);
                Type listType = new TypeToken<ArrayList<Repo>>(){}.getType();
                List<Repo> repos = gson.fromJson(result,listType);
                Log.e("RESULT",result);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e("RESULT",new String(errorResponse));
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.e("RESULT","onRetry");
            }
        });
    }
}
