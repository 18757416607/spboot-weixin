package com.weixin.pojo;

/**
 * Created by Administrator on 2018/3/5.
 */
public class Result {

    private String code;
    private String msg;   //-1:异常    00:成功  01以上是其他原因
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
