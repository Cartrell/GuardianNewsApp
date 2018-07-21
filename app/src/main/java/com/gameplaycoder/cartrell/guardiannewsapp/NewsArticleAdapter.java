package com.gameplaycoder.cartrell.guardiannewsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class NewsArticleAdapter extends ArrayAdapter<NewsArticle> {
  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // members
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////
  @BindView(R.id.txt_date_time)
  TextView mTxtDateTime;

  @BindView(R.id.txt_header)
  TextView mTxtHeader;

  @BindView(R.id.txt_section)
  TextView mTxtSection;

  @BindView(R.id.txt_author)
  TextView mTxtAuthor;

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // package-private
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////

  //=======================================================================================
  // ctor
  //=======================================================================================
  NewsArticleAdapter(Context context, List<NewsArticle> newsArticles) {
    super(context, 0, newsArticles);
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // public
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////

  //=======================================================================================
  // getView
  //=======================================================================================
  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    View listItemView = convertView;
    if (listItemView == null) {
      listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_article_list_item,
        parent, false);
    }

    // bind view using butter knife
    ButterKnife.bind(this, listItemView);

    NewsArticle newsArticle = getItem(position);
    if (newsArticle == null) {
      return (listItemView);
    }

    parseSection(newsArticle);
    parseHeader(newsArticle);
    parseDateTime(newsArticle);
    parseAuthor(newsArticle);
    return(listItemView);
  }


  //=======================================================================================
  // parseAuthor
  //=======================================================================================
  private void parseAuthor(NewsArticle newsArticle) {
    String author = newsArticle.getAuthor();
    if (author == null || author.isEmpty()) {
      mTxtAuthor.setText("");
    } else {
      String format = getContext().getString(R.string.by_line);
      mTxtDateTime.setText(String.format(format, author));
    }
  }

  //=======================================================================================
  // parseDateTime
  //=======================================================================================
  private void parseDateTime(NewsArticle newsArticle) {
    String format = "%1$s\n%2$s";
    mTxtDateTime.setText(String.format(format, newsArticle.getDate(), newsArticle.getTime()));
  }

  //=======================================================================================
  // parseHeader
  //=======================================================================================
  private void parseHeader(NewsArticle newsArticle) {
    mTxtHeader.setText(newsArticle.getHeader());
  }

  //=======================================================================================
  // parseSection
  //=======================================================================================
  private void parseSection(NewsArticle newsArticle) {
    mTxtSection.setText(newsArticle.getSection());
  }
}