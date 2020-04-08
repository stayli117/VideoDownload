package com.gyh.download.douyin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gyh.download.R;
import com.gyh.download.bilibili.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeFilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DouyinActivity extends AppCompatActivity {

    private static final String TAG = "JsoupActivity";
    private ImageView mCover;
    private ProgressBar mPbDouyin;
    private TextView mpbDouyinText;
    private EditText mInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsoup);
        ActionBar sActionBar = getSupportActionBar();
        if (sActionBar != null) {
            sActionBar.setTitle("抖音视频下载");
        }
        mInput = findViewById(R.id.input);

        findViewById(R.id.btn_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String text = mInput.getText().toString().trim();
                parse(text);
            }
        });
        mCover = findViewById(R.id.iv_cover);
        mPbDouyin = findViewById(R.id.pb_douyin);
        mPbDouyin.setMax(100);
        mpbDouyinText = findViewById(R.id.tv_progress_text);
    }


    private void parse(String text) {
        Observable.just(text)
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String text) throws Exception {
                        updateUI(mpbDouyinText, "parseSrcUrl");
                        return parseSrcUrl(text);
                    }
                })
                .map(new Function<String, Document>() {
                    @Override
                    public Document apply(String url) throws Exception {
                        updateUI(mpbDouyinText, "parseDocument");
                        return getDocument(url);
                    }
                })
                .map(new Function<Document, Node>() {
                    @Override
                    public Node apply(Document document) throws Exception {
                        updateUI(mpbDouyinText, "filterMainContentBlock");
                        return filterMainContentBlock(document);
                    }
                })
                .map(new Function<Node, ArrayList<Element>>() {
                    @Override
                    public ArrayList<Element> apply(Node node) throws Exception {
                        updateUI(mpbDouyinText, "findScriptNode");
                        return findScript(node);
                    }
                })
                .flatMap(new Function<ArrayList<Element>, ObservableSource<Element>>() {
                    @Override
                    public ObservableSource<Element> apply(ArrayList<Element> elements) throws Exception {
                        updateUI(mpbDouyinText, "IterableElements");
                        return Observable.fromIterable(elements);
                    }
                })
                .flatMap(new Function<Element, ObservableSource<Element>>() {
                    @Override
                    public ObservableSource<Element> apply(Element element) throws Exception {
                        updateUI(mpbDouyinText, "ParseElement");
                        return Observable.fromArray(element);
                    }
                })
                .map(new Function<Element, JSONObject>() {
                    @Override
                    public JSONObject apply(Element element) throws Exception {
                        JSONObject jsonObject = new JSONObject();
                        String data = element.data();
                        if (data.contains("playAddr")) {
                            String[] split = data.split("create");
                            String play = split[1];
                            int s = play.indexOf("(") + 1;
                            int end = play.indexOf(")");
                            String content = play.substring(s, end).replaceAll("\\s*", "");
                            jsonObject = new JSONObject(content);
                            String playAddr = jsonObject.getString("playAddr");
                            String wm = playAddr.replace("wm", "");
                            jsonObject.put("wm", wm);

                        }
                        updateUI(mpbDouyinText, "ParseComplete");
                        return jsonObject;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        Log.d(TAG, "onNext: jsonObject " + jsonObject);
                        try {
                            if (jsonObject.has("wm")) {
                                String cover = jsonObject.optString("cover");
                                setCover(cover);
                                String wm = jsonObject.getString("wm");
                                downVideo(wm);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setCover(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .skipMemoryCache(true)
                .fitCenter()
                .thumbnail(0.1F)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new CustomViewTarget<ImageView, Bitmap>(mCover) {
                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {

                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ImageView view = getView();
                        view.setImageBitmap(resource);
                    }

                });
    }

    private void downVideo(String downloadUrl) {
        String parent = Integer.toHexString(downloadUrl.hashCode());
        String downFilePath = Util.createDouyinPath(parent, parent + "_file");
        new AndroidDownloadManager(this, downloadUrl, downFilePath)
                .setListener(new AndroidDownloadManagerListener() {
                    @Override
                    public void onPrepare() {
                        updateUI(mpbDouyinText, "开始下载");
                        Log.d(TAG, "onPrepare");
                    }

                    @Override
                    public void onProgress(int current, String cuFileSize, String toFileSize) {
                        mPbDouyin.setProgress(current);
                        Log.d(TAG, "onProgress " +current +" cuFileSize " +cuFileSize +" toFileSize " +toFileSize);
                        updateUI(mpbDouyinText, "文件大小 " + cuFileSize + " 总大小 " + toFileSize);
                    }

                    @Override
                    public void onSuccess(File path) {
                        Log.d(TAG, "onSuccess >>>>" + path.getAbsolutePath());
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(path)));
                        updateUI(mpbDouyinText, "下载完成 " + path.getAbsolutePath());
                        mPbDouyin.setProgress(100);
                        Toast.makeText(DouyinActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        Log.e(TAG, "onFailed", throwable);
                        updateUI(mpbDouyinText, "下载失败");
                    }


                })
                .download();
    }


    private ArrayList<Element> findScript(Node node) {
        ArrayList<Element> elements = new ArrayList<>();
        List<Node> nodes = node.childNodes();
        for (Node childNode : nodes) {
            if (childNode instanceof Element) {
                String tagName = ((Element) childNode).tagName();
                if ("script".equals(tagName)) {
                    elements.add((Element) childNode);
                }
            }
        }
        return elements;
    }


    private Node filterMainContentBlock(Document document) {

        final TreeSet<Node> nodes = new TreeSet<>(new Comparator<Node>() {
            @Override
            public int compare(Node node, Node t1) {
                return node.hashCode() - t1.hashCode();
            }
        });
        document.filter(new NodeFilter() {
            @Override
            public FilterResult head(Node node, int depth) {
                String aClass = node.attr("class");
                if ("main-content-block".equals(aClass)) {
                    nodes.add(node);
                    return FilterResult.STOP;
                }
                return FilterResult.CONTINUE;
            }

            @Override
            public FilterResult tail(Node node, int depth) {
                String aClass = node.attr("class");
                if ("main-content-block".equals(aClass)) {
                    nodes.add(node);
                    return FilterResult.STOP;
                }
                return FilterResult.CONTINUE;
            }
        });

        return nodes.first();
    }


    private Document getDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .data("query", "Java")
                .userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(3000)
                .get();
    }

    private String parseSrcUrl(String url) {
        if (url == null) {
            throw new IllegalArgumentException("url is null");
        }
        if (url.contains("http")) {
            String[] split = url.split("http");
            String s = split[1];
            Log.d(TAG, "start: " + s);
            int index = s.lastIndexOf("/");
            url = "http" + s.substring(0, index + 1);
            Log.d(TAG, "start: " + url);
        }
        if (!URLUtil.isHttpsUrl(url) && !URLUtil.isHttpUrl(url)) {
            throw new IllegalArgumentException(" no http or https");
        }
        if (mInput != null) {
            updateUI(mInput, url);
        }
        return url;
    }


    private void updateUI(final TextView textView, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(text);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
