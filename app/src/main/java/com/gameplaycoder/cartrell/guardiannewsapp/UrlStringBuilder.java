package com.gameplaycoder.cartrell.guardiannewsapp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class UrlStringBuilder {
  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // members
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////
  private HashMap<String, String>mParams;
  private String mBaseUrl;

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // package-private
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////

  //=======================================================================================
  // ctor
  //=======================================================================================
  UrlStringBuilder(String baseUrl) {
    mParams = new HashMap<>();
    mBaseUrl = baseUrl;
  }

  //=======================================================================================
  // addParam
  //=======================================================================================
  void addParam(String name, String value) {
    mParams.put(name, value);
  }

  //=======================================================================================
  // buildString
  //=======================================================================================
  String buildString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(mBaseUrl);

    Set set = mParams.entrySet();
    Iterator iterator = set.iterator();
    int numParamsAdded = 0;
    while (iterator.hasNext()) {
      if (numParamsAdded == 0) {
        stringBuilder.append("?");
      } else {
        stringBuilder.append("&");
      }

      Map.Entry entry = (Map.Entry)iterator.next();
      String key = (String)entry.getKey();
      String value = (String)entry.getValue();

      stringBuilder.append(key)
        .append("=")
        .append(value);

      numParamsAdded++;
    }

    return(stringBuilder.toString());
  }
}
