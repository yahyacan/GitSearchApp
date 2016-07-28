package com.gitsearchapp.activity;

import android.widget.Button;
import android.widget.EditText;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertTrue;

/**
 * Created by yahya on 29/07/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class SearchFragmentTest {
    private Button btnSearch;
    private EditText edSearch;
    private SearchActivity searchActivity;

    @Before
    public void setUp(){
        searchActivity = Robolectric.setupActivity(SearchActivity.class);
        btnSearch = (Button) searchActivity.findViewById(R.id.btnSearch);
        edSearch = (EditText) searchActivity.findViewById(R.id.edSearch);
    }

    @Test
    public void shouldMainActivityNotBeNull() throws Exception {

        assertTrue(Robolectric.buildActivity(SearchActivity.class).create().get() != null);

        String hello = new SearchActivity().getResources().getString(R.string.btnSearch);

        Assert.assertEquals(hello, "Search");
    }
}
