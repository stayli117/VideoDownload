package com.gyh.download.bilibili.bibili;


import androidx.annotation.MainThread;

import com.gyh.download.bilibili.utils.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by  yahuigao
 * Date: 2019-12-17
 * Time: 11:36
 * Description:
 */
public class VideoParser {

    private static final String TAG = "VideoParser";
    private static VideoParser parser = new VideoParser();
    private String cookies = "";
    private String userAgent = Util.USER_AGENT;
    private BilibiliUtil util = new BilibiliUtil();

    @MainThread
    public static VideoParser getInstance() {
        return parser;
    }

    public String getCookies() {
        return this.cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public Document getCookiesDocument() {
        try {
            return Jsoup.connect(util.getCookiesApi()).get();
        } catch (Exception localException) {
            return null;
        }
    }

    public String getDataDocument(InputData inputData) {
        if (inputData.isEmptyAid() && inputData.isEmptyBid()) {
            return null;
        }
        boolean empty = inputData.isEmptyBid();
        try {
            return Jsoup.connect(util.getDataApi())
                    .data(empty ? "aid" : "bvid", empty ? inputData.getAid() : inputData.getBid())
                    .ignoreContentType(true)
                    .userAgent(userAgent)
                    .timeout(5000)
                    .get()
                    .text();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUrlDocument(InputData inputData) {
        try {
            return Jsoup.connect(util.getDownloadApi())
                    .data("avid", inputData.getAid())
                    .data("cid", inputData.getCid())
                    .data("qn", inputData.getQuality() + "")
                    .header("Host", "api.bilibili.com")
                    .cookie("SESSDATA", cookies)
                    .ignoreContentType(true)
                    .userAgent(this.userAgent)
                    .timeout(5000)
                    .get()
                    .text();
        } catch (Exception localException) {
            return null;
        }
    }

//    public Data parseData(@NonNull IParser parser) {
//        try {
//            parser.onStart();
//            JSONObject jSONObject3 = new JSONObject(getDataDocument().text());
//
//
//            if ((jSONObject3.getInt("code") != 0 ? null : 1) != null) {
//                jSONObject3 = jSONObject3.getJSONObject("data");
//                data.setTitle(jSONObject3.getString("title"));
//                data.setCid(jSONObject3.getInt("cid") + "");
//                data.setAid(jSONObject3.getString("aid"));
//                data.setQuality(16);
//                JSONArray jSONArray = jSONObject3.getJSONArray("pages");
//                List<String> pages_text = data.getPages_text();
//                pages_text.clear();
//                List<Integer> pages_cids = data.getPages_cids();
//                pages_cids.clear();
//                int length = jSONArray.length();
//                for (int i = 0; i < length; i++) {
//                    JSONObject jSONObject4 = jSONArray.getJSONObject(i);
//                    pages_cids.add(jSONObject4.getInt("cid"));
//                    pages_text.add(jSONObject4.getString("part"));
//                }
//                data.setCoverLink(jSONObject3.getString("pic"));
//                if (cookies.equals("")) {
//                    String text = "";
//                    Document cookiesDocument = getCookiesDocument();
//                    if (cookiesDocument != null) {
//                        Elements elementsByClass = cookiesDocument.getElementsByClass("wo-entry-section wo-text-markdown");
//                        if (elementsByClass != null) {
//                            text = elementsByClass.text();
//                            this.cookies = text;
//                        }
//                    }
//                    Log.d(TAG, "parseData: text " + text);
//                }
//                JSONObject jSONObject2 = new JSONObject(getUrlDocument(data).text());
//                JSONObject jSONObject4 = jSONObject2.getJSONObject("data");
//                JSONArray jSONArray2 = jSONObject4.getJSONArray("accept_description");
//                JSONArray jSONArray3 = jSONObject4.getJSONArray("accept_quality");
//                List<Integer> qualitys = data.getQualitys();
//                qualitys.clear();
//                List<String> qualitys_text = data.getQualitys_text();
//                qualitys_text.clear();
//                for (int length2 = jSONArray2.length() - 1; length2 >= 0; length2--) {
//                    qualitys.add(jSONArray3.getInt(length2));
//                    qualitys_text.add(jSONArray2.getString(length2));
//                }
//                JSONArray jSONArray4 = jSONObject4.getJSONArray("durl");
//                this.data.setSize(jSONArray4.getJSONObject(0).getLong("size"));
//                this.data.setDownloadUrl(jSONArray4.getJSONObject(0).getString("url"));
//            } else {
//                StringBuilder stringBuffer = new StringBuilder();
//                data.setTitle(stringBuffer.append("Av").append(this.data.getAid()).append(" 视频获取错误.").toString());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return this.data;
//    }

//    public Data update() {
//        try {
//            if (!this.data.getAid().equals("")) {
//                Document localDocument = getUrlDocument(data);
//                JSONObject jSONObject = new JSONObject(localDocument.text());
//                JSONArray jSONArray = jSONObject.getJSONObject("data").getJSONArray("durl");
//                this.data.setSize(jSONArray.getJSONObject(0).getLong("size"));
//                this.data.setDownloadUrl(jSONArray.getJSONObject(0).getString("url"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return this.data;
//    }
}
