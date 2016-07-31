package com.gitsearchapp.activity;

/**
 * Created by yahya on 29/07/16.
 */
/*@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)*/
public class SearchFragmentTest {

   /* @InjectView(R.id.btnSearch)
    Button btnSearch;

    @InjectView(R.id.edSearch)
    EditText edSearch;
    private SearchActivity searchActivity;

    @Before
    public void setUp(){
        searchActivity = Robolectric.setupActivity(SearchActivity.class);
        ButterKnife.inject(searchActivity);
    }

    @Test
    public void shouldMainActivityNotBeNull() throws Exception {

        assertTrue(Robolectric.buildActivity(SearchActivity.class).create().get() != null);

        String hello = new SearchActivity().getResources().getString(R.string.btnSearch);

        Assert.assertEquals(hello, "Search");
    }

    @Test
    public void shouldChangeFragment(){
        assertTrue(Robolectric.buildActivity(SearchActivity.class).create().get() != null);
        btnSearch.performClick();
        Fragment fragment = searchActivity.getFragmentManager().findFragmentById(R.id.fragment_container);
        Assert.assertEquals(fragment, SearchFragment.class);



    }*/
}
