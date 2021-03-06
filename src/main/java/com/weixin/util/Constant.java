package com.weixin.util;

/**
 * Created by Administrator on 2018/3/27.
 */

import com.weixin.pojo.AccessToken;

/**
 * 常量类
 * @author Administrator
 *
 */
public class Constant {

    //获取微信的Token接口
    public final static String WX_GET_ACCESSTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    //记录token
    public static AccessToken accessToken = null;
    //获取用户详细信息
    public final static String WX_DETAIL_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";


    //微信接入常量
    public final static String WX_APPID = "wxea7b6fcad806d905"; //APPID
    public final static String WX_TOKEN = "cabin";//微信對接tonken
    public final static String WX_APPSECRET = "a5adc57251d1b23de3b214fc81044904";  //微信開發者密碼
    public final static String WX_ENCODINGAESKEY  = "quIMJ8MXbo3wGvnxbljnSPlSRjsGfXZTfqTWASHOHNQ"; //消息加解密密钥
    //新增临时素材接口
    public final static String WX_UPLOAD_RESOURCE = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
    //自定义创建菜单接口
    public final static String WX_INIT_MENU = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";



    //小程序接口常量
    public final static String XCX_APPID = "wx96fa49493e152caf";//"wxec11f0bc68ebe57d";  //小程序APPID
    public final static String XCX_APPSECRET = "c3b5170b303e823cde161ca3e75902c7";//"08e36aca836da7586e5cfc95004e8548"; //小程序开发者密钥
    //根据前端传的CODE获取openid和UnionID
    public final static String XCX_CODE_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
    //获取小程序用户详细信息
    public final static String XCX_DETAILINIFO_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=CODE&grant_type=authorization_code";



    //微信消息类型常量
    public final static String WX_MESSAGE_TEXT = "text";  //文本消息
    public final static String WX_MESSAGE_IMAGE = "image";  //图片消息
    public final static String WX_MESSAGE_VOICE = "voice";   //语音消息
    public final static String WX_MESSAGE_VIDEO = "video";   //视频消息
    public final static String WX_MESSAGE_MUSIC = "music";   //音乐消息
    public final static String WX_MESSAGE_LINK = "link";   //链接消息
    public final static String WX_MESSAGE_EVENT  = "event";  //事件推送
    public final static String WX_MESSAGE_SUBSCRIBE = "subscribe"; //事件推送  -> 关注
    public final static String WX_MESSAGE_UNSUBSCRIBE = "unsubscribe"; //事件推送   -> 取消关注
    public final static String WX_MESSAGE_NEWS = "news";  //图文消息

    //微信按钮类型常量
    public final static String WX_BUTTON_CLICK = "CLICK";  //事件推送  ->菜单点击
    public final static String WX_BUTTON_CLICK1 = "click";  //事件推送  ->菜单点击
    //扫码推事件用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后显示扫描结果（如果是URL，将进入URL），且会将扫码的结果传给开发者，开发者可以下发消息。
    public final static String WX_BUTTON_SCANCODE_PUSH = "scancode_push";
    //弹出地理位置选择器用户点击按钮后，微信客户端将调起地理位置选择工具，完成选择操作后，将选择的地理位置发送给开发者的服务器，同时收起位置选择工具，随后可能会收到开发者下发的消息
    public final static String WX_BUTTON_LOCATION_SELECT = "location_select";
    public final static String WX_BUTTON_SCANCODE = "scancode";   //事件推送  ->扫码
    public final static String WX_MESSAGE_LOCATION = "location";  //地址位置消息
    public final static String WX_MESSAGE_LOCATION1 = "location_select"; //地址位置消息

    public final static String WX_BUTTON_VIEW = "VIEW";   //事件推送  ->菜单点击
    public final static String WX_BUTTON_VIEW1 = "view";   //事件推送  ->菜单点击


}
