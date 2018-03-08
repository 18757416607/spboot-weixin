package com.weixin.pojo;

import java.util.Date;

/**
 * Created by Administrator on 2018/3/2.
 */
public class Parking {

    private int id;
    private String username;
    private String platenum;
    private String car_brand;
    private String car_model;
    private String car_vin;
    private String car_vin_evaluate;
    private String car_time;
    private String status;
    private String is_open_unionpay;
    private int unionpay_bind_id;
    private int isactive;
    private String card_type;
    private String bind_table;
    private String limit_amount;
    private Date addtime;
    private Date edittime;
    private int mer_area_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlatenum() {
        return platenum;
    }

    public void setPlatenum(String platenum) {
        this.platenum = platenum;
    }

    public String getCar_brand() {
        return car_brand;
    }

    public void setCar_brand(String car_brand) {
        this.car_brand = car_brand;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public String getCar_vin() {
        return car_vin;
    }

    public void setCar_vin(String car_vin) {
        this.car_vin = car_vin;
    }

    public String getCar_vin_evaluate() {
        return car_vin_evaluate;
    }

    public void setCar_vin_evaluate(String car_vin_evaluate) {
        this.car_vin_evaluate = car_vin_evaluate;
    }

    public String getCar_time() {
        return car_time;
    }

    public void setCar_time(String car_time) {
        this.car_time = car_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_open_unionpay() {
        return is_open_unionpay;
    }

    public void setIs_open_unionpay(String is_open_unionpay) {
        this.is_open_unionpay = is_open_unionpay;
    }

    public int getUnionpay_bind_id() {
        return unionpay_bind_id;
    }

    public void setUnionpay_bind_id(int unionpay_bind_id) {
        this.unionpay_bind_id = unionpay_bind_id;
    }

    public int getIsactive() {
        return isactive;
    }

    public void setIsactive(int isactive) {
        this.isactive = isactive;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getBind_table() {
        return bind_table;
    }

    public void setBind_table(String bind_table) {
        this.bind_table = bind_table;
    }

    public String getLimit_amount() {
        return limit_amount;
    }

    public void setLimit_amount(String limit_amount) {
        this.limit_amount = limit_amount;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Date getEdittime() {
        return edittime;
    }

    public void setEdittime(Date edittime) {
        this.edittime = edittime;
    }

    public int getMer_area_id() {
        return mer_area_id;
    }

    public void setMer_area_id(int mer_area_id) {
        this.mer_area_id = mer_area_id;
    }
}
