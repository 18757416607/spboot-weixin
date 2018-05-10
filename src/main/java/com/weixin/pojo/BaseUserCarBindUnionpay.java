package com.weixin.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/10.
 */
public class BaseUserCarBindUnionpay implements Serializable{

    private Integer id;
    private String user_name;       //用户名
    private String plate_num;       //车牌号
    private String card_bin;        //卡号前6位
    private String card_num_last;   //卡号后4位
    private String card_bank;       //银行卡所属银行
    private String card_type;       //银行卡类型
    private String is_open;         //代扣开关，0关闭，1开启
    private String is_bind;         //绑定状态(1为绑定状态，0为已解绑)
    private String source;          //绑卡来源
    private Date create_time;       //创建时间
    private Date edit_time;         //修改时间
    private String mer_area_id;     //对应base_merchant_area中的主键


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPlate_num() {
        return plate_num;
    }

    public void setPlate_num(String plate_num) {
        this.plate_num = plate_num;
    }

    public String getCard_bin() {
        return card_bin;
    }

    public void setCard_bin(String card_bin) {
        this.card_bin = card_bin;
    }

    public String getCard_num_last() {
        return card_num_last;
    }

    public void setCard_num_last(String card_num_last) {
        this.card_num_last = card_num_last;
    }

    public String getCard_bank() {
        return card_bank;
    }

    public void setCard_bank(String card_bank) {
        this.card_bank = card_bank;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getIs_open() {
        return is_open;
    }

    public void setIs_open(String is_open) {
        this.is_open = is_open;
    }

    public String getIs_bind() {
        return is_bind;
    }

    public void setIs_bind(String is_bind) {
        this.is_bind = is_bind;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getEdit_time() {
        return edit_time;
    }

    public void setEdit_time(Date edit_time) {
        this.edit_time = edit_time;
    }

    public String getMer_area_id() {
        return mer_area_id;
    }

    public void setMer_area_id(String mer_area_id) {
        this.mer_area_id = mer_area_id;
    }

    @Override
    public String toString() {
        return "BaseUserCarBindUnionpay{" +
                "id=" + id +
                ", user_name='" + user_name + '\'' +
                ", plate_num='" + plate_num + '\'' +
                ", card_bin='" + card_bin + '\'' +
                ", card_num_last='" + card_num_last + '\'' +
                ", card_bank='" + card_bank + '\'' +
                ", card_type='" + card_type + '\'' +
                ", is_open='" + is_open + '\'' +
                ", is_bind='" + is_bind + '\'' +
                ", source='" + source + '\'' +
                ", create_time=" + create_time +
                ", edit_time=" + edit_time +
                ", mer_area_id='" + mer_area_id + '\'' +
                '}';
    }
}
