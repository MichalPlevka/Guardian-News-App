package application.newsapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Article>>,
        RadioGroup.OnCheckedChangeListener {

    private final static int NETWORK_LOADER = 1;
    private final static String GUARDIAN_API_KEY = "3abc6b5f-e8a9-401d-8d59-356569730b63";
    private final static String urlTemplate = "http://content.guardianapis.com/search";

    private ArticleAdapter articleAdapter;
    private ConnectivityManager connectivityManager;
    private RecyclerView.LayoutManager layoutManager;

    private RecyclerView recyclerView;
    private TextView emptyView;
    private ProgressBar loadingIndicator;

    private boolean hasInternetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        emptyView = findViewById(R.id.errorMessageText);
        loadingIndicator = findViewById(R.id.loadingIndicator);

        RadioGroup orderByRadioGroup = findViewById(R.id.orderByRadioGroup);
        orderByRadioGroup.setOnCheckedChangeListener(this);

        //Check internet connection
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null) {
            //Execute async loader
            hasInternetConnection = true;
            getLoaderManager().initLoader(NETWORK_LOADER, null, this);
        } else {
            hasInternetConnection = false;
            loadingIndicator.setVisibility(View.GONE);
            emptyView.setText("No internet connection...");
        }

    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        loadingIndicator.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        StaticVariables.searchPreferenceValue = sharedPreferences.getString("search_edit_text_preference","");
        StaticVariables.sectionPreferenceValue = sharedPreferences.getString("sections_edit_text_preference", "");
        StaticVariables.tagPreferenceValue = sharedPreferences.getString("tags_edit_text_preference", "");

        Uri uri = Uri.parse(urlTemplate);
        Uri.Builder url = uri.buildUpon();
        if (!TextUtils.isEmpty(StaticVariables.searchPreferenceValue)) {
            url.appendQueryParameter("q", StaticVariables.searchPreferenceValue);
        }
        if (!TextUtils.isEmpty(StaticVariables.orderByRadioButtonValue)) {
            url.appendQueryParameter("order-by", StaticVariables.orderByRadioButtonValue);
        }
        if (!TextUtils.isEmpty(StaticVariables.tagPreferenceValue)) {
            url.appendQueryParameter("tag", StaticVariables.tagPreferenceValue);
        }
        if (!TextUtils.isEmpty(StaticVariables.sectionPreferenceValue)) {
            url.appendQueryParameter("section", StaticVariables.sectionPreferenceValue);
        }

        url.appendQueryParameter("api-key", GUARDIAN_API_KEY);

        return new NewsAsyncTaskLoader(MainActivity.this, url.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Article>> loader, ArrayList<Article> articles) {
        //PostExecute
        loadingIndicator.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        if (connectivityManager.getActiveNetworkInfo() == null) {
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText("No internet connection...");
        } else {
            if (articles != null && !articles.isEmpty()) {
                articleAdapter = new ArticleAdapter(this, articles);
                recyclerView.setAdapter(articleAdapter);
            } else {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText("No articles available...");
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.d("LOADER", "onLoaderReset()");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.searchMenu) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        String radioButtonName = radioButton.getText().toString();

        if (radioButtonName.equals(getResources().getString(R.string.order_by_newest))) {
            StaticVariables.orderByRadioButtonValue = "newest";
        } else if (radioButtonName.equals(getResources().getString(R.string.order_by_oldest))) {
            StaticVariables.orderByRadioButtonValue = "oldest";
        } else if (radioButtonName.equals(getResources().getString(R.string.order_by_relevance))) {
            StaticVariables.orderByRadioButtonValue = "relevance";
        } else {
            StaticVariables.orderByRadioButtonValue = "";
        }

        if (hasInternetConnection) {
            emptyView.setVisibility(View.GONE);
            getLoaderManager().restartLoader(NETWORK_LOADER, null, this);
        }

    }

    private static class NewsAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Article>> {

        private String url;

        public NewsAsyncTaskLoader(Context context, String url) {
            super(context);
            this.url = url;
        }

        @Override
        protected void onStartLoading() {
            //PreExecute
            super.onStartLoading();
            forceLoad();
        }

        @Override
        public ArrayList<Article> loadInBackground() {
            // Thread.sleep() for loading simulation...
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return QueryMethods.makeHTTPRequest(url);
        }
    }
}
