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
```
<?xml version="1.0" encoding="UTF-8"?>
<info>
    <version>2.0</version>
    <description>有新的版本了，赶快来下载吧！</description>
    <url>http://xxx.xxxx.xx</url>
</info>
```

plist参考：
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
	<key>items</key>
	<array>
		<dict>
			<key>assets</key>
			<array>
				<dict>
					<key>kind</key>
					<string>software-package</string>
					<key>url</key>
					<string>your_ipa_download_url</string>
				</dict>
			</array>
			<key>metadata</key>
			<dict>
				<key>forceUpdate</key>
				<string>NO</string>
				<key>updateInfo</key>
				<string>your_update_description</string>>
				<key>bundle-identifier</key>
				<string>your_APP_id</string>
				<key>bundle-version</key>
				<string>your_App_version</string>
				<key>kind</key>
				<string>software</string>
				<key>subtitle</key>
				<string>your_subtitle</string>
				<key>title</key>
				<string>your_app_name</string>
			</dict>
		</dict>
	</array>
</dict>
</plist>
```



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
