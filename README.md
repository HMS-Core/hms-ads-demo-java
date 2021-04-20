# HMS Ads Demo for Java
English | [中文](https://github.com/HMS-Core/hms-ads-demo-java/blob/master/README_ZH.md)
## Table of Contents

 * [Introduction](#introduction)
 * [Installation](#installation)
 * [Configuration ](#configuration)
 * [Supported Environments](#supported-environments)
 * [Sample Code](#sample-code)
 * [Result](#result)
 * [License](#license)
 
 
## Introduction
The hms-ads-demo-java program demonstrates how to integrate the HMS Core Ads SDK into an Android app and display ads.

## Installation
Install the program on Huawei Android mobile phones.
<br>You also can use HMS Toolkit to quickly integrate the kit and run the demo project, as well as debug the app using a remote device for free. For details, please visit[HMS Toolkit](https://developer.huawei.com/consumer/en/doc/development/Tools-Guides/getting-started-0000001077381096).</br>

## Configuration 
No additional configuration is required.

## Supported Environments
HMS Core (APK) 4.0.0 or later has been installed on Huawei Android phones.

## Sample Code
The hms-ads-demo-java program integrates the HMS Core Ads SDK and provides the pages for displaying banner, native, rewarded, interstitial, splash ads, and roll ads respectively.
The following classes in the demo are used for loading and displaying ads and collecting user comments:

1). BannerActivity.java
Loads and displays banner ads.
<br>Code location: app\src\main\java\com\huawei\hms\ads\sdk\BannerActivity.java</br>
    
2). NativeActivity.java
Loads and displays native ads.
<br>Code location: app\src\main\java\com\huawei\hms\ads\sdk\NativeActivity.java</br>
    
3). RewardActivity.java
Loads and displays rewarded ads.
<br>Code location: app\src\main\java\com\huawei\hms\ads\sdk\RewardActivity.java</br>
	
4). InterstitialActivity.java
Loads and displays interstitial ads.
<br>Code location: app\src\main\java\com\huawei\hms\ads\sdk\InterstitialActivity.java</br>
	
5). SplashActivity.java
Loads and displays splash ads.
<br>Code location: app\src\main\java\com\huawei\hms\ads\sdk\SplashActivity.java</br>
	
6). ConsentActivity.java
Collects user consent.
<br>Code location: app\src\main\java\com\huawei\hms\ads\sdk\ConsentActivity.java</br>
    
7). ProtocolActivity.java
Collects user consent on privacy terms.
<br>Code location: app\src\main\java\com\huawei\hms\ads\sdk\ProtocolActivity.java</br>

8). InstreamActivity.java
Loads and displays roll ads.
<br>Code location: app\src\main\java\com\huawei\hms\ads\sdk\InstreamActivity.java</br>

## Result
Banner Ads&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp; Interstitial Ads&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp; Native Ads

 <img src="https://github.com/HMS-Core/hms-ads-demo-java/blob/master/result/Banner.gif" width=200>  <img src="https://github.com/HMS-Core/hms-ads-demo-java/blob/master/result/Interstitial.gif" width=200>  <img src="https://github.com/HMS-Core/hms-ads-demo-java/blob/master/result/Native.gif" width=200>

Reward Ads&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp; Instream Ads&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp; Splash Ads

<img src="https://github.com/HMS-Core/hms-ads-demo-java/blob/master/result/Reward.gif" width=200>  <img src="https://github.com/HMS-Core/hms-ads-demo-java/blob/master/result/Roll.gif" width=200>  <img src="https://github.com/HMS-Core/hms-ads-demo-java/blob/master/result/Splash.gif" width=200>

## Question or issues
If you want to evaluate more about HMS Core,
[HMSCore on Reddit](https://www.reddit.com/r/HuaweiDevelopers/) is for you to keep up with latest news about HMS Core, and to exchange insights with other developers.

If you have questions about how to use HMS samples, try the following options:
- [Stack Overflow](https://stackoverflow.com/questions/tagged/huawei-mobile-services) is the best place for any programming questions. Be sure to tag your question with 
`huawei-mobile-services`.
- [Huawei Developer Forum](https://forums.developer.huawei.com/forumPortal/en/home?fid=0101187876626530001) HMS Core Module is great for general questions, or seeking recommendations and opinions.

If you run into a bug in our samples, please submit an [issue](https://github.com/HMS-Core/hms-ads-demo-java/issues) to the Repository. Even better you can submit a [Pull Request](https://github.com/HMS-Core/hms-ads-demo-java/pulls) with a fix.

##  License
hms-ads-demo-java is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
