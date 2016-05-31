package uk.co.whiteoctober.cordova;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageManager;
import org.xmlpull.v1.XmlPullParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppVersion extends CordovaPlugin {
  private UpdataInfo mUpdataInfo;
  private final int UPDATA_NONEED = 0;
  private final int UPDATA_CLIENT = 1;
  private final int GET_UNDATAINFO_ERROR = 2;
  private final int SDCARD_NOMOUNTED = 3;
  private final int DOWN_ERROR = 4;
  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    InputStream is = null;
    try {
      Log.d("verion","enter"+action);
      if (action.equals("checkVersion")) {
        Log.d("verion","enter");
        PackageManager packageManager = this.cordova.getActivity().getPackageManager();
        String mVersion = packageManager.getPackageInfo(this.cordova.getActivity().getPackageName(), 0).versionName;
        try{
          String path = "https://o78xagd1m.qnssl.com/version-jt.xml";//更新检查文件地址
          URL url = new URL(path);
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
          conn.setConnectTimeout(5000);
          conn.setRequestMethod("GET");
          int responseCode = conn.getResponseCode();
          if (responseCode == 200) {
            // 从服务器获得一个输入流
            is = conn.getInputStream();
          }
          mUpdataInfo = getUpdataInfo(is);
          if (!mVersion.equals(mUpdataInfo.getVersion())) {
            Log.d("verion","版本号不一致");
            showUpdataDialog();
          }
        }catch (Exception e){
          e.printStackTrace();
        }
        callbackContext.success(packageManager.getPackageInfo(this.cordova.getActivity().getPackageName(), 0).versionName);
        return true;
      }
      if (action.equals("getAppName")) {
        PackageManager packageManager = this.cordova.getActivity().getPackageManager();
        ApplicationInfo app = packageManager.getApplicationInfo(this.cordova.getActivity().getPackageName(), 0);
        callbackContext.success((String)packageManager.getApplicationLabel(app));
        return true;
      }
      if (action.equals("getPackageName")) {
        callbackContext.success(this.cordova.getActivity().getPackageName());
        return true;
      }
      if (action.equals("getVersionNumber")) {
        PackageManager packageManager = this.cordova.getActivity().getPackageManager();
        callbackContext.success(packageManager.getPackageInfo(this.cordova.getActivity().getPackageName(), 0).versionName);
      return true;
      }
      if (action.equals("getVersionCode")) {
        PackageManager packageManager = this.cordova.getActivity().getPackageManager();
        callbackContext.success(packageManager.getPackageInfo(this.cordova.getActivity().getPackageName(), 0).versionCode);
      return true;
      }
      return false;
    } catch (NameNotFoundException e) {
      callbackContext.success("N/A");
      return true;
    }
  }
  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case UPDATA_NONEED:
//				Toast.makeText(getApplicationContext(), "不需要更新",Toast.LENGTH_SHORT).show();
          break;
        case UPDATA_CLIENT:
          //对话框通知用户升级程序
          showUpdataDialog();
          break;
        case GET_UNDATAINFO_ERROR:
          //服务器超时
          break;
        case DOWN_ERROR:
          //下载apk失败
          break;
      }
    }
  };
  //提示升级框
  protected void showUpdataDialog() {
    AlertDialog.Builder builer = new AlertDialog.Builder(this.cordova.getActivity(),AlertDialog.THEME_HOLO_LIGHT);
    builer.setTitle("版本升级");
    String mMessage = "";
    Log.d("u", mUpdataInfo.getDescription());
    try {
      mMessage = new String(mUpdataInfo.getDescription().getBytes("UTF-8"),"UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    builer.setMessage(mMessage);
    //当点确定按钮时从服务器上下载 新的apk 然后安装װ
    builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        Log.i("u", "下载apk,更新");
        downLoadApk();
      }
    });
    builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {

      }
    });
    AlertDialog dialog = builer.create();
    dialog.show();
  }
  //下载APK
  protected void downLoadApk() {
    final ProgressDialog pd;    //进度条对话框
    pd = new  ProgressDialog(this.cordova.getActivity(), AlertDialog.THEME_HOLO_LIGHT);
    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    pd.setMessage("正在下载更新");
    pd.show();
    new Thread(){
      @Override
      public void run() {
        try {
          DownLoadManager mManager = new DownLoadManager();
          File file = mManager.getFileFromServer(mUpdataInfo.getUrl(), pd);
          Log.d("u", mUpdataInfo.getUrl());
          //用户体验
          sleep(1500);
          installApk(file);
          pd.dismiss(); //结束掉进度条对话框
        } catch (Exception e) {
          Message msg = new Message();
          msg.what = DOWN_ERROR;
          handler.sendMessage(msg);
          e.printStackTrace();
        }
      }}.start();

  }
  //安装apk
  protected void installApk(File file) {
    Intent intent = new Intent();
    //执行动作
    intent.setAction(Intent.ACTION_VIEW);
    //执行的数据类型
    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
    this.cordova.getActivity().startActivity(intent);

  }
  public UpdataInfo getUpdataInfo(InputStream is) throws Exception{
    //读取XML文件
    XmlPullParser  parser = Xml.newPullParser();
    //设置读取编码
    parser.setInput(is, "utf-8");
    //获取事件类型
    int type = parser.getEventType();
    //新建更新信息对象
    UpdataInfo mUpdataInfo = new UpdataInfo();
    //如果类型不为接受
    while(type != XmlPullParser.END_DOCUMENT ){
      switch (type) {
        case XmlPullParser.START_TAG:
          //当开始时
          if("version".equals(parser.getName())){
            //读取version
            mUpdataInfo.setVersion(parser.nextText());
          }else if ("url".equals(parser.getName())){
            //读取url
            mUpdataInfo.setUrl(parser.nextText());
          }else if ("description".equals(parser.getName())){
            //读取description
            mUpdataInfo.setDescription(parser.nextText());
          }
          break;
      }
      type = parser.next();
    }
    return mUpdataInfo;
  }
  class DownLoadManager {
    File getFileFromServer(String path, ProgressDialog pd) throws Exception{
      //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
      if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
        URL url = new URL(path);
        HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        //获取到文件的大小
        pd.setMax(conn.getContentLength());
        InputStream is = conn.getInputStream();
        File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
        FileOutputStream fos = new FileOutputStream(file);
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buffer = new byte[1024];
        int len ;
        int total=0;
        while((len =bis.read(buffer))!=-1){
          fos.write(buffer, 0, len);
          total+= len;
          //获取当前下载量
          pd.setProgress(total);
        }
        fos.close();
        bis.close();
        is.close();
        return file;
      }
      else{
        return null;
      }
    }

  }
  class UpdataInfo {
    private String version;

    private String url;

    private String description;

    private String url_server;

    public String getVersion() {
      return version;
    }

    public void setVersion(String version) {
      this.version = version;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getUrl_server() {
      return url_server;
    }

    public void setUrl_server(String url_server) {
      this.url_server = url_server;
    }

  }
}

