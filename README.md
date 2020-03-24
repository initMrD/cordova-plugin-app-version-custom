# cordova-plugin-app-version-custom

## 使用方法：

### 将原有的cordova-plugin-app-version删除：
<pre>
  <code>cordova plugin rm cordova-plugin-app-version</code>
</pre>
### 加入新的cordova-plugin-app-version-custom：
<pre>
  <code>cordova plugin add https://github.com/initMrD/cordova-plugin-app-version-custom.git</code>
</pre>
### 在你的js代码中调用：
<pre>
  <code>cordova.getAppVersion.checkUpdate("这里传入xml或者plist的url");</code>
</pre>

xml参考：
<?xml version="1.0" encoding="UTF-8"?>
<info>
    <version>2.0</version>
    <description>有新的版本了，赶快来下载吧！</description>
    <url>http://xxx.xxxx.xx</url>
</info>

plist参考：
http://7xv1nq.com1.z0.glb.clouddn.com/update-IOS.plist



## Usage：

### delete old plugin：
<pre>
  <code>cordova plugin rm cordova-plugin-app-version</code>
</pre>
### use new plugin：
<pre>
  <code>cordova plugin add https://github.com/initMrD/cordova-plugin-app-version-custom.git</code>
</pre>
### your js code:
<pre>
  <code>cordova.getAppVersion.checkUpdate("your xml file or plist file url");</code>
</pre>
