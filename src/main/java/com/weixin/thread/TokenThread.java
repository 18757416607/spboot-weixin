package com.weixin.thread;

/**
 * Created by Administrator on 2018/3/27.
 */
import java.io.IOException;

import com.weixin.util.Constant;
import org.apache.http.client.ClientProtocolException;

import com.weixin.pojo.AccessToken;
import com.weixin.util.RequestWx;

/**
 * 微信获取   Access_Token  线程
 * 能够保持Access_Token的不失效
 * @author Administrator
 *
 */
public class TokenThread implements Runnable {


    public void run() {
        while (true) {
            try {
                AccessToken accessToken = Constant.accessToken = RequestWx.getAccessToken();
                System.out.println("\n\n【获取微信token】:"+accessToken.getAccess_token()+"\n\n");
                if (null != accessToken) {
                    // 休眠7000秒
                    Thread.sleep((accessToken.getExpires_in() - 200) * 1000);
                } else {
                    // 如果access_token为null，60秒后再获取
                    Thread.sleep(60 * 1000);
                }
            } catch (InterruptedException e) {
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e1) {

                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}