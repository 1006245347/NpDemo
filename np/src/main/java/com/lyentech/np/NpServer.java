package com.lyentech.np;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import com.lyentech.np.MMKVUtil.Builder;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author by jason-何伟杰，2022/5/17
 * des:sdk管理类
 */
public class NpServer {
    private static Context mContext;
    private static Class mainCls;
    private static String uniqueId;

    public static void initSdk(Context appContext, String uniqueDevId, Class mainActivityClass) {
        mContext = appContext;
        uniqueId = uniqueDevId;
        mainCls = mainActivityClass;
        (new Builder()).setSavePath(appContext.getExternalFilesDir("np_dir").getPath()).build();
        MMKVUtil.remove(NpConfig.PUBLIC_FRONT_RUN); //app启动重置
        if (mContext == null) {
            Log.e("TAG", "np——sdk arg 'appContext' should not be null!");
        }
        postEvent("", mainCls.toString(), "pv", null, null); //要统计停留需要先传pv
    }

    public static void stayOnResume(Activity activity) { //首页前台显示
        if (mainCls != null && activity.getClass() == mainCls) {
            if (MMKVUtil.getLong(NpConfig.PUBLIC_FRONT_RUN, 0L) == 0L)
                MMKVUtil.addLong(NpConfig.PUBLIC_FRONT_RUN, System.currentTimeMillis());
        }
    }

    public static void stayOnPause(Activity activity) { //当前所有界面都不显示
        boolean isTop = isTopActivity(getPackageName(activity));
        long curTime = System.currentTimeMillis();
        long period = curTime - MMKVUtil.getLong(NpConfig.PUBLIC_FRONT_RUN, 0L);
        if (!isTop && (period > 4000) && (period != curTime)) {//短于5秒无效
            postStayLiving(period);
        }
    }

    //子线程进行网络请求-统计app前台显示
    private static void postStayLiving(long period) {
        MMKVUtil.remove(NpConfig.PUBLIC_FRONT_RUN);
        JSONObject js = new JSONObject();
        try {
            js.put("_default", period / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String urlArg = NpConfig.PUBLIC_URL + URIEncoder.encodeURI(
                    getNpArg("/launch", mainCls.toString(), "ev", "_stay", js.toString()));
            Log.v("TAG", "url_stay=" + urlArg);
            NpHttpUtil.getInstance().getAsync(urlArg, null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void postEvent(String eventKey, String value) {
        postEvent("/1", "/launch", "ev", eventKey, value);
    }

    //子线程请求-单个事件上报
    public static void postEvent(String dsc, String src, String tp, String ev, String evv) {
        JSONObject js = new JSONObject();
        try {
            js.put(ev, evv);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String urlArg = NpConfig.PUBLIC_URL + URIEncoder.encodeURI(getNpArg(dsc, src, tp, ev, js.toString()));
            Log.v("TAG", "urlArg=" + urlArg);
            NpHttpUtil.getInstance().getAsync(urlArg, new NpHttpUtil.NetCall() {
                @Override
                public void success(Call call, Response response) throws IOException {
                    try {
                        Log.v("TAG", "post_" + response.body().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failed(Call call, IOException e) {
                    Log.v("TAG", "post_err_" + e);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //将公共参数重构成接口的链接
    public static String getNpArg(String dsc, String src, String tp, String ev, String evv) {
        JSONObject js = setNpBody(dsc, src, tp, ev, evv);
        StringBuilder sb = new StringBuilder();
        Iterator iterator = js.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            try {
                sb.append(key + "=" + js.getString(key) + "&");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.v("TAG", "NP>>>" + sb);
        return sb.toString();
    }

    //设置公共参数
    public static JSONObject setNpBody(String dsc, String src, String tp, String ev, String evv) {
        JSONObject js = new JSONObject();
        try {
            js.put("ak", NpConfig.PUBLIC_APP);
            js.put("u", dsc);
            js.put("rf", "");
            js.put("sys", "Android " + Build.VERSION.SDK_INT);
            js.put("br", "");
            js.put("brv", "");
            int w = 1080, h = 1920;
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
//            float density = dm.density;
            w = dm.widthPixels;
            h = dm.heightPixels;
            js.put("sr", w + "x" + h);
            if (TextUtils.isEmpty(uniqueId)) {
                uniqueId = Settings.Secure.getString(mContext.getContentResolver(), "android_id");
            }
            js.put("uuid", uniqueId);
            js.put("rnd", System.currentTimeMillis());
            js.put("tp", tp);
            js.put("ev", ev);
            js.put("evv", evv);
            js.put("xy", null);
            js.put("v", "0.0.1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return js;
    }

    //当前进程在前台
    public static boolean isTopActivity(String packageName) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        if (list == null || list.size() == 0) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo info : list) {
            if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                    info.processName == packageName) {
                return true;
            }
        }
        return false;
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static Context getNpContext() {
        return mContext;
    }
}
