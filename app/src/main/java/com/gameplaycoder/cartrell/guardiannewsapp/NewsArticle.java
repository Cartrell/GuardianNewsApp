package com.gameplaycoder.cartrell.guardiannewsapp;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

class NewsArticle {

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // static / const
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////
  private static final String LOG_TAG = "BackendUtils";

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // members
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////
  private String mSection;
  private String mHeader;
  private String mDate;
  private String mTime;
  private String mUrl;
  private String mAuthor;

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // package-private
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////

  //=======================================================================================
  // ctor
  //=======================================================================================
  NewsArticle(String section, String header, String webPublicationDate, String url,
  String author) {
    mSection = section;
    mHeader = header;
    parsePublicationDate(webPublicationDate);
    mUrl = url;
    mAuthor = author;
  }

  //=======================================================================================
  // getAuthor
  //=======================================================================================
  public String getAuthor() {
    return(mAuthor);
  }

  //=======================================================================================
  // getDate
  //=======================================================================================
  public String getDate() {
    return(mDate);
  }

  //=======================================================================================
  // getHeader
  //=======================================================================================
  public String getHeader() {
    return(mHeader);
  }

  //=======================================================================================
  // getSection
  //=======================================================================================
  public String getSection() {
    return(mSection);
  }

  //=======================================================================================
  // getTime
  //=======================================================================================
  public String getTime() {
    return(mTime);
  }

  //=======================================================================================
  // getUrl
  //=======================================================================================
  public String getUrl() {
    return(mUrl);
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // private
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////

  //=======================================================================================
  // parsePublicationDate
  //=======================================================================================
  /**
   * Takes a publication date in the format 2018-07-18T10:51:12Z, and prses the date and time from
   * it.
   * @param webPublicationDate - The publication date to parse
   */
  private void parsePublicationDate(String webPublicationDate) {
    Locale defaultLocale = Locale.getDefault();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",
      defaultLocale);
    Date date;

    try {
      date = simpleDateFormat.parse(webPublicationDate);
    } catch (ParseException exception) {
      Log.e(LOG_TAG, "parsePublicationDate", exception);
      return;
    }

    SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d, yyyy", defaultLocale);
    mDate = outputFormat.format(date);

    outputFormat = new SimpleDateFormat("h:mm a", defaultLocale);
    mTime = outputFormat.format(date);
  }
}