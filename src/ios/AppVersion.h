#import <Cordova/CDVPlugin.h>

@interface AppVersion : CDVPlugin

@property(nonatomic,copy) NSString* plistUrl;

- (void)setPlistUrl:(NSString *)plistUrl;

- (NSString*) plistUrl;

- (void)getAppName:(CDVInvokedUrlCommand*)command;

- (void)getPackageName:(CDVInvokedUrlCommand*)command;

- (void)getVersionNumber:(CDVInvokedUrlCommand*)command;

- (void)getVersionCode:(CDVInvokedUrlCommand*)command;

- (void)checkUpdate:(CDVInvokedUrlCommand*)command;
@end
