# cordova-plugin-app-version-custom

## 使用方法：

## 将原有的cordova-plugin-app-version删除：

## cordova plugin rm cordova-plugin-app-version

## 加入新的cordova-plugin-app-version-custom：

## cordova plugin add https://github.com/initMrD/cordova-plugin-app-version-custom.git





注意事项：如需修改检查更新文件地址

android:
在cordova-plugin-app-version\src\android\AppVersion.java第46行处修改

ios:
在cordova-plugin-app-version\src\ios\AppVersion.m第五行处修改

