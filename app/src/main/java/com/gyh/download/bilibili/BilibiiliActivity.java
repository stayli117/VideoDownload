package com.gyh.download.bilibili;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.arialyy.aria.core.common.HttpOption;
import com.arialyy.aria.core.common.RequestEnum;
import com.arialyy.aria.core.download.DownloadEntity;
import com.arialyy.aria.core.task.DownloadTask;
import com.arialyy.aria.util.CommonUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gyh.download.R;
import com.gyh.download.aria.AriaManager;
import com.gyh.download.bilibili.bibili.BilibiliUtil;
import com.gyh.download.bilibili.bibili.Data;
import com.gyh.download.bilibili.bibili.InputData;
import com.gyh.download.bilibili.bibili.QualityData;
import com.gyh.download.bilibili.model.PageData;
import com.gyh.download.bilibili.utils.FileNameUtil;
import com.gyh.download.bilibili.utils.Util;
import com.gyh.download.bilibili.viewmodel.DemoViewModel;
import com.gyh.download.ffmpeg.FFmpegManager;
import com.gyh.download.view.HorizontalRvManager;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.microshow.rxffmpeg.RxFFmpegSubscriber;

public class BilibiiliActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "DemoActivity";
    private BilibiliUtil util = new BilibiliUtil();
    private ProgressBar progressBar_progress;
    private Button mBtnDownVideo;
    private EditText mInput;
    private TextView textView_progress;
    private TextView textView_title;
    private RecyclerView Spinner_page;
    private RecyclerView spinner_quality;
    private ImageView imageView_cover;
    private VideoView mVideoView;
    private DemoViewModel mViewModel;
    private Button mBtnGetVideo;
    private DownloadTask mTask;

    private boolean isToMp4ing = false;


    private boolean isDelVideo = true;
    private boolean isAutoToMp4 = true;
    private boolean isSaveCover = true;
    private boolean isAutoPlay = true;
    private HorizontalRvManager<QualityData> mHorQualityRvManager;
    private HorizontalRvManager<PageData> mHorPageRvManager;

    private InputData inputData = new InputData();
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ActionBar sActionBar = getSupportActionBar();
        if (sActionBar != null) {
            sActionBar.setTitle("b站视频下载");
            initActionBar(sActionBar);
        }


        mBtnGetVideo = findViewById(R.id.btn_get_video);
        mBtnDownVideo = findViewById(R.id.btn_download_video);
        mInput = findViewById(R.id.editText_input);

        progressBar_progress = ((ProgressBar) findViewById(R.id.ProgressBar_progress));
        textView_progress = ((TextView) findViewById(R.id.TextView_progress));
        textView_title = ((TextView) findViewById(R.id.TextView_title));
        spinner_quality = findViewById(R.id.Spinner_quality);
        Spinner_page = ((RecyclerView) findViewById(R.id.Spinner_page));
        imageView_cover = ((ImageView) findViewById(R.id.ImageView_cover));
        mVideoView = findViewById(R.id.video_view);

        mBtnGetVideo.setOnClickListener(this);
        mBtnDownVideo.setOnClickListener(this);
        initDown();
        initView();
        initViewModel();
    }

    private void initActionBar(ActionBar actionBar) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //通过getMenuInflater()方法获得MenuInflate对象，通过MenuInflate对象的inflate方法创建活动菜单
        getMenuInflater().inflate(R.menu.bili, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        item.setChecked(!item.isChecked());

        switch (itemId) {
            case R.id.auto_del_video:
                isDelVideo = item.isChecked();
                break;
            case R.id.auto_mp4:
                isAutoToMp4 = item.isChecked();
                break;
            case R.id.auto_save_cover:
                isSaveCover = item.isChecked();
                break;
            case R.id.auto_play:
                isAutoPlay = item.isChecked();
                break;
        }
        return false;
    }

    private void initDown() {
        AriaManager.get().register(new AriaManager.TaskListener() {
            @Override
            public void onWait(DownloadTask task) {
                Log.d(TAG, "onWait: task " + task.getTaskName());
            }

            @Override
            public void onPre(DownloadTask task) {
                mBtnDownVideo.setText("开始下载");
            }

            @Override
            public void taskStart(DownloadTask task) {
                mBtnDownVideo.setText("暂停");
            }

            @Override
            public void running(DownloadTask task) {

                DownloadEntity entity = task.getDownloadEntity();
                String convertSpeed = entity.getConvertSpeed();
                int percent = entity.getPercent();
                String convertFileSize = entity.getConvertFileSize();
                long currentProgress = entity.getCurrentProgress();
                String currentFileSize = CommonUtil.formatFileSize(currentProgress);
                progressBar_progress.setProgress(percent);
                textView_progress.setText(String.format("%s |文件大小: %s / %s", convertSpeed, currentFileSize, convertFileSize));
            }

            @Override
            public void taskResume(DownloadTask task) {
                mBtnDownVideo.setText("暂停");
            }

            @Override
            public void taskStop(DownloadTask task) {
                mBtnDownVideo.setText("恢复");
            }

            @Override
            public void taskCancel(DownloadTask task) {
                mBtnDownVideo.setText("开始下载");
            }

            @Override
            public void taskFail(DownloadTask task) {

            }

            @Override
            public void taskComplete(DownloadTask task) {
                mBtnDownVideo.setText("开始下载");
                progressBar_progress.setProgress(100);
                mBtnDownVideo.setEnabled(false);
                mTask = task;

                Toast.makeText(BilibiiliActivity.this, "下载完成", Toast.LENGTH_SHORT).show();

                textView_progress.setText(String.format("文件地址 %s", task.getFilePath()));
                toMp4();
            }
        });
    }


    private void initViewModel() {
        mBtnDownVideo.setEnabled(false);
        mViewModel = ViewModelProviders.of(this).get(DemoViewModel.class);
        LiveData<Data> videoData = mViewModel.getVideoData();
        videoData.observe(this, new Observer<Data>() {
            @Override
            public void onChanged(final Data data) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                setVideoInfo(data);
                imageView_cover.setVisibility(View.VISIBLE);
                Glide.with(BilibiiliActivity.this)
                        .asBitmap()
                        .load(data.getCoverLink())
                        .skipMemoryCache(true)
                        .fitCenter()
                        .thumbnail(0.1F)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new CustomViewTarget<ImageView, Bitmap>(imageView_cover) {
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
                                saveCover(resource, data);
                            }

                        });

            }
        });
    }

    private void setVideoInfo(Data data) {
        textView_title.setText(data.getTitle());
        String formatFileSize = CommonUtil.formatFileSize(data.getSize());
        textView_progress.setText(String.format("视频大小%s", formatFileSize));


        List<QualityData> qualitys = data.getQualitys();
        mHorQualityRvManager.updateList(qualitys);

        String cid = data.getCid();
        inputData.setCid(cid);
        inputData.setQualitys(qualitys);

        List<PageData> pages = data.getPageDataList();
        inputData.setPages(pages);

        mHorPageRvManager.updateList(pages);


        mBtnDownVideo.setEnabled(true);
        mBtnGetVideo.setEnabled(false);
    }

    private void initView() {
        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isToMp4ing) {
                    return;
                }
                mBtnGetVideo.setEnabled(true);
            }
        });
        initQualityAdapter();
        initPageAdapter();

    }

    private void initPageAdapter() {
        mHorPageRvManager = new HorizontalRvManager<>(Spinner_page, new HorizontalRvManager.HolderProvider<PageData>() {
            @Override
            public void bindData(HorizontalRvManager.HorHolder<PageData> holder, PageData data) {
                holder.textView.setText(data.getPart());
                holder.textView.setTextColor(data.isChecked() ? Color.RED : Color.GRAY);
                if (data.isChecked()) {
                    holder.textView.setBackgroundResource(R.drawable.text_shape);
                } else {
                    holder.textView.setBackgroundColor(Color.WHITE);
                }
            }
        }, new HorizontalRvManager.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View childView) {
                if (!TextUtils.isEmpty(inputData.getCid())) {
                    PageData pageData = inputData.getPages().get(position);
                    inputData.setCid(String.valueOf(pageData.getCid()));
                    update();
                }
            }
        });
    }

    private void initQualityAdapter() {

        mHorQualityRvManager = new HorizontalRvManager<>(spinner_quality, new HorizontalRvManager.HolderProvider<QualityData>() {
            @Override
            public void bindData(HorizontalRvManager.HorHolder<QualityData> holder, QualityData data) {
                holder.textView.setText(data.getDescription());
                holder.textView.setTextColor(data.isChecked() ? Color.RED : Color.GRAY);
                if (data.isChecked()) {
                    holder.textView.setBackgroundResource(R.drawable.text_shape);
                } else {
                    holder.textView.setBackgroundColor(Color.WHITE);
                }
            }
        }, new HorizontalRvManager.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View childView) {
                if (!TextUtils.isEmpty(inputData.getCid())) {
                    int quality = inputData.getQualitys().get(position).getQuality();
                    inputData.setQuality(quality);
                    update();
                }
            }
        });

    }

    public void loadData() {
        showWaiting();
        String str = getAvOrBvNum();
        if (isBvNum(str)) {
            inputData.setBid(str);
            getVideoData();
            return;
        }
        if (isAvNum(str)) {
            inputData.setAid(str);
            getVideoData();
        }

    }

    public void getVideoData() {
        if (!util.isNetworkAvalible(this)) {
            Toast.makeText(this, "当前无网络.", Toast.LENGTH_LONG).show();
            return;
        }
        if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.suspend();
            mVideoView.stopPlayback();
        }
        mViewModel.loadVideoData(inputData);
    }

    public void update() {
        if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.suspend();
            mVideoView.stopPlayback();
        }
        mViewModel.updateVideoData(inputData);
    }

    public void saveCover(Bitmap resource, Data data) {
        if (resource == null || !isSaveCover) return;
        String coverFilePath = Util.createCoverFilePath(data.getAid() + ".png");
        Log.d(TAG, "saveCover: coverFilePath " + coverFilePath);
        String saveBitmap = util.saveBitmap(coverFilePath, resource);
        Log.d(TAG, "saveCover: saveBitmap " + saveBitmap);
    }

    public void downloadVideo(Data data) {
        if (!util.isNetworkAvalible(this)) {
            Toast.makeText(this, "当前无网络.", Toast.LENGTH_LONG).show();
            return;
        }
        isToMp4ing = true;
        String title = data.getTitle();
        if (TextUtils.isEmpty(title)) {
            title = data.getCid();
        }
        String fileName = FileNameUtil.correctFileName(title) + ".flv";
        Uri uri = new Uri.Builder()
                .encodedPath(fileName).build();
        String path = uri.getPath();

        String downFilePath = Util.createDownFilePath(data.getAid(), path);
        Map<String, String> header = Util.getHeader();
        HttpOption httpOption = new HttpOption()
                .addHeaders(header)
                .setRequestType(RequestEnum.GET);
        AriaManager.get().start(data.getDownloadUrl(), downFilePath, httpOption);
        Log.d(TAG, "downloadVideo: " + downFilePath + " path " + path);
    }

    private void clickDown(Data data) {
        if (!AriaManager.get().isExists()) {
            downloadVideo(data);
            return;
        }

        if (AriaManager.get().isRunning()) { // 任务在执行
            AriaManager.get().stop();
            return;
        }
        AriaManager.get().resume();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (isToMp4ing) {
            Toast.makeText(this, "请稍等", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (id) {
            case R.id.btn_get_video:
                loadData();
                break;
            case R.id.btn_download_video:
                Data data = mViewModel.getVideoData().getValue();
                clickDown(data);
                break;

        }
    }


    private void toMp4() {
        if (mTask == null || !isAutoToMp4) return;
        String filePath = mTask.getFilePath();
        File file = new File(filePath);
        String name = FileNameUtil.removeFileExt(file.getName());
        Log.d(TAG, "toMp4: name " + name);
        final File outFile = new File(file.getParent(), name + ".mp4");
        final String outFilePath = outFile.getAbsolutePath();
        final String srcPath = file.getAbsolutePath();
        Log.d(TAG, "taskComplete: srcPath " + srcPath + " outFilePath " + outFilePath);
        String[] boxblur = FFmpegManager.getTranscoding(srcPath, outFilePath);
        textView_progress.setText("转码中 ");
        final long start = System.currentTimeMillis();
        FFmpegManager
                .invokeRxJava(boxblur)
                .subscribe(new RxFFmpegSubscriber() {
                    @Override
                    public void onFinish() {
                        Log.d(TAG, "onFinish: -->");
                        textView_progress.setText(String.format("文件地址： %s", outFilePath));
                        progressBar_progress.setProgress(100);
                        isToMp4ing = false;
                        scanFile(outFile);
                        Toast.makeText(BilibiiliActivity.this, "转码完成", Toast.LENGTH_SHORT).show();
                        deleteSrcVideo(srcPath);
                        showDialogTip(outFilePath);
                    }

                    @Override
                    public void onProgress(int progress, long progressTime) {
                        long second = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start);
                        long seconds = TimeUnit.MICROSECONDS.toSeconds(progressTime);
                        String formatSeconds = Util.formatSeconds(second);
                        String videoTime = Util.formatSeconds(seconds);
                        Log.d(TAG, "onProgress: progress " + progress + "second " + second + " 视频时长 videoTime " + videoTime);
                        progressBar_progress.setProgress(progress);
                        textView_progress.setText(String.format(Locale.getDefault(), "转码进度: %d |转码耗时: %s |视频转码时长: %s", progress, formatSeconds, videoTime));
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "onCancel: --->");
                        textView_progress.setText("转码取消");
                        isToMp4ing = false;
                    }

                    @Override
                    public void onError(String message) {
                        Log.d(TAG, "onError: message " + message);
                        textView_progress.setText(String.format("转码失败 %s", message));
                        isToMp4ing = false;
                    }
                });
    }

    private void showDialogTip(final String outFilePath) {
        if (isAutoPlay) {
            playVideo(outFilePath);
            return;
        }
        AlertDialog dialog = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher)
                .setTitle("视频播放")
                .setMessage("播放当前视频").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        playVideo(outFilePath);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        dialog.show();

    }

    private void playVideo(String outFilePath) {
        if (mVideoView == null) return;
        imageView_cover.setVisibility(View.GONE);

        mVideoView.setVideoPath(outFilePath);
        mVideoView.start();

    }

    /**
     * 圆圈加载进度的 dialog
     */
    private void showWaiting() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setTitle("视频信息获取");
        progressDialog.setMessage("加载中...");
        progressDialog.setIndeterminate(true);// 是否形成一个加载动画  true表示不明确加载进度形成转圈动画  false 表示明确加载进度
        progressDialog.setCancelable(false);//点击返回键或者dialog四周是否关闭dialog  true表示可以关闭 false表示不可关闭
        progressDialog.show();

    }

    private void deleteSrcVideo(String srcPath) {
        if (!isDelVideo) return;
        File file = new File(srcPath);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    private void scanFile(final File file) {
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        MediaScannerConnection.scanFile(BilibiiliActivity.this, new String[]{file.getAbsolutePath()}, null, null);
    }


    private String getAvOrBvNum() {
        String str = mInput.getText().toString();
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        // 获取分享数据
        str = util.getShareData(this);
        if (!TextUtils.isEmpty(str)) {
            return str;
        }

        str = util.getClipData(this);
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        return "";
    }

    private boolean isBvNum(String str) {
        if (util.isBvNumber(str)) {
            if (!"".equals(str)) {
                mInput.setText(str);
                mInput.setSelection(str.length());
            }
            return true;
        }
        return false;
    }

    private boolean isAvNum(String str) {
        str = str.toLowerCase();
        if (util.isAvNumber(str)) {
            str = util.DealInput(str);
            if (!"".equals(str) && util.isNumber(str)) {
                mInput.setText(str);
                mInput.setSelection(str.length());
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null && !mVideoView.isPlaying()) {
            mVideoView.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null && mVideoView.canPause()) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.suspend();
            mVideoView.stopPlayback();
        }
        AriaManager.get().unRegister();
    }

}
