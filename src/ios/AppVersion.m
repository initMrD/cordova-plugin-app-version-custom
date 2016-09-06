#import "AppVersion.h"
#import <Cordova/CDVPluginResult.h>


@implementation AppVersion

- (void)getAppName : (CDVInvokedUrlCommand *)command
{
    NSString * callbackId = command.callbackId;
    NSString * version =[[[NSBundle mainBundle]infoDictionary]objectForKey :@"CFBundleDisplayName"];
    CDVPluginResult * pluginResult =[CDVPluginResult resultWithStatus : CDVCommandStatus_OK messageAsString : version];
    [self.commandDelegate sendPluginResult : pluginResult callbackId : callbackId];
}

- (void)getPackageName:(CDVInvokedUrlCommand*)command
{
    NSString* callbackId = command.callbackId;
    NSString* packageName = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleIdentifier"];
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:packageName];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void)getVersionNumber:(CDVInvokedUrlCommand*)command
{
    NSString* callbackId = command.callbackId;
    NSString* version = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleShortVersionString"];
    if (version == nil) {
      NSLog(@"CFBundleShortVersionString was nil, attempting CFBundleVersion");
      version = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"];
      if (version == nil) {
        NSLog(@"CFBundleVersion was also nil, giving up");
        // not calling error callback here to maintain backward compatibility
      }
    }

    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:version];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void)getVersionCode:(CDVInvokedUrlCommand*)command
{
    NSString* callbackId = command.callbackId;
    NSString* version = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"];
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:version];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void)checkUpdate:(CDVInvokedUrlCommand*)command{
    self.plistUrl = command.arguments[0];
    NSLog(@"%@=================================",self.plistUrl);
    NSString* callbackId = command.callbackId;
    NSDictionary *infoDic = [[NSBundle mainBundle] infoDictionary];
    
    NSString *appVersion = [infoDic objectForKey:@"CFBundleVersion"];
    NSLog(@"appVersion ====%@",appVersion);
    
    [self checkUpdateWithURLString];
    
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:appVersion];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
    
}


#pragma mark - check version
- (void)checkUpdateWithURLString{
    
    BOOL forceUpdate = NO;

    NSString *returnString = @"";
    NSString *plist_url = self.plistUrl;
    NSString *updateMsg;
    //	forceUpdate = flag;
    NSString *currentVer = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"];
    NSDictionary *plistInfo = [NSDictionary dictionaryWithContentsOfURL:[NSURL URLWithString:plist_url]];
    
    if (plistInfo != nil) {
        NSArray *array = [plistInfo objectForKey:@"items"];
        NSDictionary *dict = [[array objectAtIndex:0] objectForKey:@"metadata"];
        NSString *newVer = [dict objectForKey:@"bundle-version"];
        NSString *mandatoryUpdate = [dict objectForKey:@"forceUpdate"];
        NSString *updateInfo = [dict objectForKey:@"updateInfo"];
        
        if ([mandatoryUpdate compare:@"YES"] == NSOrderedSame){
            forceUpdate = YES;
        }else if([mandatoryUpdate compare:@"NO"] == NSOrderedSame){
            forceUpdate = NO;
        }else {
            forceUpdate = YES;
        }
        
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        NSString *skipversion = [defaults objectForKey:@"skipversion"];
        NSLog(@"========skipversion=%@",skipversion);
        NSLog(@"========newVer=%@",newVer);
       
        if([newVer compare:currentVer] == NSOrderedDescending){
            updateMsg = @"检测到新版本：\n";
            updateMsg = [updateMsg stringByAppendingString:updateInfo];
            if(updateMsg) {
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"版本更新"
                                                                message:updateMsg
                                                               delegate:self
                                                      cancelButtonTitle:@"以后再说"
                                                      otherButtonTitles:@"进行更新",nil];
                [alert show];
                returnString = @"1";
            }
        }
        
        //        }
    }else {
        
        NSString *errMsg = [NSString stringWithFormat:@"无法获取最新版本信息，请检查网络！"];
        NSLog(@"%@", errMsg);
    }
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    NSLog(@"%ld",(long)buttonIndex);
    if (buttonIndex == 1){  //下载更新
        NSLog(@"buttonIndex==========openURL");
        NSLog(@"%@",self.plistUrl);
        NSURL *url  = [NSURL URLWithString:[NSString stringWithFormat:@"itms-services://?action=download-manifest&url=%@",self.plistUrl]];
        [[UIApplication sharedApplication] openURL:url];
        exit(0);
        
    } if (buttonIndex == 2){//  以后再说
        NSLog(@"buttonIndex==========skip version");
    } if (buttonIndex == 0){    //忽略此版本，记录版本号
        //        NSLog(@"buttonIndex==========0");
        //        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        //        [defaults setObject:self.newVersion forKey:@"skipversion"];
        //        NSLog(@"========newVersion=%@",self.newVersion);
        
    }
    
}


@end
