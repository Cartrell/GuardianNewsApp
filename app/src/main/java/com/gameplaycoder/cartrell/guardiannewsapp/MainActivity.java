package com.gameplaycoder.cartrell.guardiannewsapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsArticle>> {

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // static / const
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////
  private static final String GUARDIAN_BASE_URL = "https://content.guardianapis.com/search";
  private static final String GUARDIAN_API_KEY_PARAM = "api-key";
  private static final String GUARDIAN_SECTION_PARAM = "section";
  private static final String GUARDIAN_QUERY_PARAM = "q";
  private static final String GUARDIAN_SHOW_TAGS_PARAM = "show-tags";
  private static final String GUARDIAN_SHOW_TAGS_VALUE = "contributor";

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // members
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////
  private NewsArticleAdapter mAdapter;

  @BindView(R.id.txt_empty)
  TextView mEmptyStateTextView;

  @BindView(R.id.news_article_list)
  ListView mListView;

  @BindView(R.id.control_layout)
  View mControlLayout;

  @BindView(R.id.progress_bar)
  ProgressBar mProgressBar;

  @BindView(R.id.img_btn_section)
  ImageButton mImgBtnSection;

  @BindView(R.id.edit_text_search)
  EditText mSearchEditText;

  private String mQuery;
  private String mSection;
  private int mLoaderId;

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // public
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////

  //=======================================================================================
  // onCreateLoader
  //=======================================================================================
  @NonNull
  @Override
  public Loader<List<NewsArticle>> onCreateLoader(int id, @Nullable Bundle args) {
    mEmptyStateTextView.setText("");
    String url = buildUrlString();
    return(new NewsArticleLoader(this, url));
  }

  //=======================================================================================
  // onLoadFinished
  //=======================================================================================
  @Override
  public void onLoadFinished(Loader<List<NewsArticle>> loader, List<NewsArticle> newsArticles) {
    mEmptyStateTextView.setText(R.string.no_articles_found);
    mProgressBar.setVisibility(View.INVISIBLE);

    if (newsArticles != null && !newsArticles.isEmpty()) {
      mAdapter.addAll(newsArticles);
      return;
    }

    mListView.setEmptyView(mEmptyStateTextView);
  }

  //=======================================================================================
  // onLoaderReset
  //=======================================================================================
  @Override
  public void onLoaderReset(Loader<List<NewsArticle>> loader) {
    mAdapter.clear();
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // protected
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////

  //=======================================================================================
  // onCreate
  //=======================================================================================
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // bind view using butter knife
    ButterKnife.bind(this);

    setupListView();

    mQuery = mSection = "";
    mLoaderId = 0;

    initSearchEditText();
    initSectionsButton();

    if (!isConnected()) {
      mProgressBar.setVisibility(View.INVISIBLE);
      mControlLayout.setVisibility(View.GONE);
      mEmptyStateTextView.setText(R.string.no_internet_connection);
      return;
    }

    beginSearch();
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // protected
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////

  //=======================================================================================
  // beginSearch
  //=======================================================================================
  private void beginSearch() {
    getSection();

    mAdapter.clear();
    mProgressBar.setVisibility(View.VISIBLE);

    mLoaderId++; //each loader needs a unique id
    getLoaderManager().initLoader(mLoaderId, null, this);
  }

  //=======================================================================================
  // buildUrlString
  //=======================================================================================
  private String buildUrlString() {
    UrlStringBuilder urlStringBuilder = new UrlStringBuilder(GUARDIAN_BASE_URL);
    urlStringBuilder.addParam(GUARDIAN_API_KEY_PARAM, BuildConfig.GuardianApiKey);
    urlStringBuilder.addParam(GUARDIAN_SHOW_TAGS_PARAM, GUARDIAN_SHOW_TAGS_VALUE);

    if (!mSection.isEmpty()) {
      urlStringBuilder.addParam(GUARDIAN_SECTION_PARAM, mSection);
    }

    if (!mQuery.isEmpty()) {
      urlStringBuilder.addParam(GUARDIAN_QUERY_PARAM, mQuery);
    }

    return(urlStringBuilder.buildString());
  }

  //=======================================================================================
  // getSection
  //=======================================================================================
  private void getSection() {
    Intent intent = getIntent();
    mSection = intent.getStringExtra(SectionActivity.INTENT_PARAM_SECTION);
    if (mSection == null) {
      mSection = "";
    }
  }

  //=======================================================================================
  // initSearchEditText
  //=======================================================================================
  private void initSearchEditText() {
    mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      //===================================================================================
      // onEditorAction
      //===================================================================================
      @Override
      public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          mQuery = textView.getText().toString();
          beginSearch();
        }
        return(false);
      }
    });
  }

  //=======================================================================================
  // initSectionsButton
  //=======================================================================================
  private void initSectionsButton() {
    final Activity activity = this;
    final Context context = this;

    mImgBtnSection.setOnClickListener(new View.OnClickListener() {

      //===================================================================================
      // onClick
      //===================================================================================
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(context, SectionActivity.class);
        activity.startActivity(intent);
      }
    });
  }

  //=======================================================================================
  // isConnected
  //=======================================================================================
  private boolean isConnected() {
    ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivityManager == null) {
      return(false);
    }

    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
    return(
      activeNetwork != null &&
      activeNetwork.isConnectedOrConnecting());
  }

  //=======================================================================================
  // openWebPage
  //=======================================================================================
  private void openWebPage(String url) {
    Uri webPage = Uri.parse(url);
    Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
    if (intent.resolveActivity(getPackageManager()) != null) {
      startActivity(intent);
    }
  }

  //=======================================================================================
  // setupListView
  //=======================================================================================
  private void setupListView() {
    mAdapter = new NewsArticleAdapter(this, new ArrayList<NewsArticle>());

    mListView.setAdapter(mAdapter);

    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      //===================================================================================
      // onItemClick
      //===================================================================================
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        NewsArticle newsArticle = mAdapter.getItem(position);
        if (newsArticle != null) {
          openWebPage(newsArticle.getUrl());
        }
      }
    });

    mListView.setEmptyView(mEmptyStateTextView);
  }
}
