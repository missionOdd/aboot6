/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */
package com.wteam.utils;

import cn.hutool.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author mission
 * @since 2019/10/26 17:10
 */
public class TranslatorUtil {

  public static String translate(String word){
    try {
      String url = "https://translate.googleapis.com/translate_a/single?" +
          "client=gtx&" +
          "sl=en" +
          "&tl=zh-CN" +
          "&dt=t&q=" + URLEncoder.encode(word, "UTF-8");

      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();
      con.setRequestProperty("User-Agent", "Mozilla/5.0");

      BufferedReader in = new BufferedReader(
          new InputStreamReader(con.getInputStream()));
      String inputLine;
      StringBuilder response = new StringBuilder();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
      return parseResult(response.toString());
    }catch (Exception e){
      return  word;
    }
  }

  private static String parseResult(String inputJson){
    JSONArray jsonArray2 = (JSONArray) new JSONArray(inputJson).get(0);
    StringBuilder result = new StringBuilder();
    for (Object o : jsonArray2) {
      result.append(((JSONArray) o).get(0).toString());
    }
    return result.toString();
  }
}
