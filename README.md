# cordova-plugin-app-version-custom

## 使用方法：

### 将原有的cordova-plugin-app-version删除：

#### cordova plugin rm cordova-plugin-app-version

### 加入新的cordova-plugin-app-version-custom：

#### cordova plugin add https://github.com/initMrD/cordova-plugin-app-version-custom.git

cordova.getAppVersion.checkUpdate("这里传入xml或者plist的url");

xml参考：
http://7xv1nq.com1.z0.glb.clouddn.com/update-android.xml

plist参考：
http://7xv1nq.com1.z0.glb.clouddn.com/update-IOS.plist
