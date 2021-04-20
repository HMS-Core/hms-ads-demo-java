# 华为广告服务Java示例代码
中文 | [English](https://github.com/HMS-Core/hms-ads-demo-java/blob/master/README.md)
## 目录

 * [简介](#简介)
 * [安装](#安装)
 * [配置](#配置)
 * [环境要求](#环境要求)
 * [示例代码](#示例代码)
 * [运行结果](#运行结果)
 * [授权许可](#授权许可)
 
 
## 简介
华为广告服务（HUAWEI Ads Kit）Java示例代码向您介绍如何将HUAWEI Ads SDK集成到安卓应用中，实现广告展示。

## 安装
在华为手机上安装该示例代码项目。
<br>该示例也可以通过HMS Toolkit快速启动运行，且支持各Kit一站式集成，并提供远程真机免费调测等功能。了解更多信息，请参见[HMS Toolkit官方链接](https://developer.huawei.com/consumer/cn/doc/development/Tools-Guides/getting-started-0000001077381096)。</br>

## 配置
无需配置。

## 环境要求
在华为手机上安装HMS Core (APK) 4.0.0及以上版本。

## 示例代码
华为广告服务Java示例代码集成了HMS Core Ads SDK，为您提供横幅广告、原生广告、激励广告、插屏广告、开屏广告和贴片广告的展示页面。本示例代码包括以下六个文件，便于您进行广告加载、展示和用户意见征集：

1). BannerActivity.java
用于加载、展示横幅广告。
<br>代码位置：app\src\main\java\com\huawei\hms\ads\sdk\BannerActivity.java</br>
    
2). NativeActivity.java
用于加载、展示原生广告。
<br>代码位置： app\src\main\java\com\huawei\hms\ads\sdk\NativeActivity.java</br>
    
3). RewardActivity.java
用于加载、展示激励广告。
<br>代码位置：app\src\main\java\com\huawei\hms\ads\sdk\RewardActivity.java</br>
	
4). InterstitialActivity.java
用于加载、展示插屏广告。
<br>代码位置： app\src\main\java\com\huawei\hms\ads\sdk\InterstitialActivity.java</br>
	
5). SplashActivity.java
用于加载、展示开屏广告。
<br>代码位置： app\src\main\java\com\huawei\hms\ads\sdk\SplashActivity.java</br>
	
6). ConsentActivity.java
用于征求用户意见。
<br>代码位置：app\src\main\java\com\huawei\hms\ads\sdk\ConsentActivity.java</br>
    
7). ProtocolActivity.java
用于收集用户对隐私条款的意见。
<br>代码位置：app\src\main\java\com\huawei\hms\ads\sdk\ProtocolActivity.java</br>

8). InstreamActivity.java
用于加载、展示贴片广告。
<br>代码位置：app\src\main\java\com\huawei\hms\ads\sdk\InstreamActivity.java</br>

## 运行结果
Banner Ads&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp; Interstitial Ads&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp; Native Ads

 <img src="https://github.com/HMS-Core/hms-ads-demo-java/blob/master/result/Banner.gif" width=200>  <img src="https://github.com/HMS-Core/hms-ads-demo-java/blob/master/result/Interstitial.gif" width=200>  <img src="https://github.com/HMS-Core/hms-ads-demo-java/blob/master/result/Native.gif" width=200>

Reward Ads&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp; Instream Ads&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp; Splash Ads

<img src="https://github.com/HMS-Core/hms-ads-demo-java/blob/master/result/Reward.gif" width=200>  <img src="https://github.com/HMS-Core/hms-ads-demo-java/blob/master/result/Roll.gif" width=200>  <img src="https://github.com/HMS-Core/hms-ads-demo-java/blob/master/result/Splash.gif" width=200>

## 技术支持
如果您对HMS Core还处于评估阶段，可在[Reddit社区](https://www.reddit.com/r/HuaweiDevelopers/)获取关于HMS Core的最新讯息，并与其他开发者交流见解。

如果您对使用HMS示例代码有疑问，请尝试：
- 开发过程遇到问题上[Stack Overflow](https://stackoverflow.com/questions/tagged/huawei-mobile-services)，在`huawei-mobile-services`标签下提问，有华为研发专家在线一对一解决您的问题。
- 到[华为开发者论坛](https://forums.developer.huawei.com/forumPortal/en/home?fid=0101187876626530001) HMS Core板块与其他开发者进行交流。

如果您在尝试示例代码中遇到问题，请向仓库提交[issue](https://github.com/HMS-Core/hms-ads-demo-java/issues)，也欢迎您提交[Pull Request](https://github.com/HMS-Core/hms-ads-demo-java/pulls)。

##  授权许可
华为广告服务Java示例代码经过 [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0)授权许可。

