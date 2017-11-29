package com.konka.speech.global;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.affy.zlogger.ZLogger;
import com.konka.speech.di.ForApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static android.content.Context.CONTEXT_IGNORE_SECURITY;
import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * 扫描系统中assets目录下的全局语音配置文件
 *
 * @author ZhangFei
 * @date 2017-11-28
 */

public class Scanner {
    private static final String SCANNER_THREAD_NAME = "Scanner";
    private static final String CONFIG_FILE_SUFFIX = ".kkspeech";
    private static final int SCAN_INIT = 0;
    private static final int SCAN_UPDATE = 1;
    private static final int SCAN_UPDATE_PACKAGE = 2;

    private Context mContext;
    private HandlerThread mScanHandlerThread;
    private Handler mHandler;
    private Map<String, String> mGlobalSpeechMap;

    @Inject
    public Scanner(@ForApp Context context) {
        mContext = context;
        mScanHandlerThread = new HandlerThread(SCANNER_THREAD_NAME, THREAD_PRIORITY_BACKGROUND);
        mScanHandlerThread.start();
        mHandler = new ScannerHandler(mScanHandlerThread.getLooper(), this);
        mGlobalSpeechMap = new HashMap<>(4);
    }

    public void init() {
        mHandler.sendMessage(mHandler.obtainMessage(SCAN_INIT));
    }

    public void shutdown() {
        mScanHandlerThread.quit();
    }

    public Map<String, String> getGlobalSpeechMap() {
        return mGlobalSpeechMap;
    }

    private void scanGlobalAssets() {
        ZLogger.t("scanner").d("scanner start to search...");
        mGlobalSpeechMap.clear();
        //get from own assets
        try {
            String[] fileList = mContext.getAssets().list("");
            for (String fileName : fileList) {
                if (fileName.endsWith(CONFIG_FILE_SUFFIX)) {
                    InputStream inputStream = mContext.getAssets().open(fileName);
                    if (inputStream != null) {
                        StringBuilder assetsContent = new StringBuilder();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            assetsContent.append(line);
                        }
                        inputStream.close();
                        mGlobalSpeechMap.put(mContext.getPackageName(), assetsContent.toString());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get from third party app
        PackageManager pkgManager = mContext.getPackageManager();
        List<ApplicationInfo> appList = pkgManager.getInstalledApplications(0);
        for (ApplicationInfo app : appList) {
            if (pkgManager.getApplicationEnabledSetting(app.packageName) < COMPONENT_ENABLED_STATE_DISABLED
                    && !app.packageName.startsWith("com.android.")
                    && !app.packageName.startsWith("com.google.")) {
//                String global = getAssetsInfo(mContext, app.packageName, "global" + CONFIG_FILE_SUFFIX);
                String global = getAssetsInfo(mContext, app.packageName, "global_iflytek.xiri");
                if (!TextUtils.isEmpty(global)) {
                    mGlobalSpeechMap.put(app.packageName, global);
                }
            }
        }
        Iterator<String> keyIterator = mGlobalSpeechMap.keySet().iterator();
        while (keyIterator.hasNext()) {
            String pkgName = keyIterator.next();
            ZLogger.t("global").d("global speech:" + pkgName + mGlobalSpeechMap.get(pkgName));
        }
    }

    private String getAssetsInfo(@Nonnull Context context, @Nonnull String pkgName, String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = context.createPackageContext(pkgName, CONTEXT_IGNORE_SECURITY).getAssets().open(fileName);
            if (inputStream != null) {
                StringBuilder assetsContent = new StringBuilder();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    assetsContent.append(line);
                }
                inputStream.close();
                return assetsContent.toString();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    private static class ScannerHandler extends Handler {
        private Scanner mScanner;

        ScannerHandler(Looper looper, Scanner scanner) {
            super(looper);
            mScanner = scanner;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCAN_INIT:
                    mScanner.scanGlobalAssets();
                    break;
                case SCAN_UPDATE:
                    break;
                case SCAN_UPDATE_PACKAGE:
                    break;
                default:
                    break;
            }
        }
    }
}
