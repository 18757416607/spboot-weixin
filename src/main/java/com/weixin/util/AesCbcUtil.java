package com.weixin.util;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;

/**
* 创建时间：2018年2月2日 下午11:59:38
* 项目名称：FreshGoods
* @author zhang.qifeng
* @version 1.0
* 文件名称：AesCbcUtil
* 类说明：
*/
public class AesCbcUtil {
	
    static {
        //BouncyCastle是一个开源的加解密解决方案，主页在http://www.bouncycastle.org/
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String decrypt(String data, String key, String iv, String encodingFormat) throws Exception {
//      initialize();

      //被加密的数据
      byte[] dataByte = Base64.decodeBase64(data);
      //加密秘钥
      byte[] keyByte = Base64.decodeBase64(key);
      //偏移量
      byte[] ivByte = Base64.decodeBase64(iv);


      try {
          Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

          SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");

          AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
          parameters.init(new IvParameterSpec(ivByte));

          cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化

          byte[] resultByte = cipher.doFinal(dataByte);
          if (null != resultByte && resultByte.length > 0) {
              String result = new String(resultByte, encodingFormat);
              return result;
          }
          return null;
      } catch (NoSuchAlgorithmException e) {
          e.printStackTrace();
      } catch (NoSuchPaddingException e) {
          e.printStackTrace();
      } catch (InvalidParameterSpecException e) {
          e.printStackTrace();
      } catch (InvalidKeyException e) {
          e.printStackTrace();
      } catch (InvalidAlgorithmParameterException e) {
          e.printStackTrace();
      } catch (IllegalBlockSizeException e) {
          e.printStackTrace();
      } catch (BadPaddingException e) {
          e.printStackTrace();
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      return null;
  }
}
