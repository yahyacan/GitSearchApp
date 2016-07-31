package com.gitsearchapp.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gitsearchapp.activity.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SearchFragment extends Fragment {

    @InjectView(R.id.btnSearch) Button btnSearch;
    @InjectView(R.id.edSearch) EditText edSearch;

    OnClickListener listener;

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnClickListener) {
            listener = (OnClickListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement SearchFragment.OnClickListener");
        }
    }

    @OnClick(R.id.btnSearch)
    public void onClickSearch(){
        search(edSearch.getText().toString());
    }

    public void search(String username){
        listener.onSearch(username);

    }

    public interface OnClickListener{
        void onSearch(String username);
    }
}
