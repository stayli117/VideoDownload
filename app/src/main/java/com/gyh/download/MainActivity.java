package com.gyh.download;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gyh.download.bilibili.BilibiiliActivity;
import com.gyh.download.douyin.DouyinActivity;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity {

    private static final int WRITE_EXTERNAL_STORAGE = 1111;


    private String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Class clazz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_bilibili).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestProxy(BilibiiliActivity.class);

            }
        });
        findViewById(R.id.btn_douyin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestProxy(DouyinActivity.class);
            }
        });
    }

    private boolean hasStoragePermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void requestProxy(Class c) {
        clazz = c;
        if (hasStoragePermission()) {
            startActivity(new Intent(MainActivity.this, clazz));
            return;
        }
        requestPre();
    }


    @AfterPermissionGranted(WRITE_EXTERNAL_STORAGE)
    private void requestPre() {
        if (hasStoragePermission()) {
            requestProxy(clazz);
        } else {
            EasyPermissions.requestPermissions(this, "下载过程需要用到存储权限",
                    WRITE_EXTERNAL_STORAGE, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
