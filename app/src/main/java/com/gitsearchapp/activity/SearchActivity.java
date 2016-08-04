package com.gitsearchapp.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.gitsearchapp.fragment.SearchFragment;
import com.gitsearchapp.fragment.SearchResultFragment;

import io.fabric.sdk.android.Fabric;

public class SearchActivity extends FragmentActivity implements SearchFragment.OnClickListener {

    private static final int CURRENT_PAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.search);

        if (savedInstanceState != null) {
            getFragmentManager().executePendingTransactions();
            Fragment fragmentById = getFragmentManager().
                    findFragmentById(R.id.fragment_container);
            if (fragmentById != null) {
                getFragmentManager().beginTransaction()
                        .remove(fragmentById).commit();
            }
        }
        SearchFragment searchFragment = new SearchFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, searchFragment).commit();
    }

    @Override
    public void onSearch(String username) {
        String URL = String.format(getResources().getString(R.string.post_link), username,
                CURRENT_PAGE, getResources().getInteger(R.integer.per_page));

        SearchResultFragment newFragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(SearchResultFragment.EXTRA_URL, URL);
        args.putString(SearchResultFragment.USERNAME, username);
        newFragment.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
