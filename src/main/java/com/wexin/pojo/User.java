package com.wexin.pojo;

import java.util.Date;

/**
 * Created by Administrator on 2018/2/7.
 */
public class User {

        private int id;
        private String name;
        private int age;
        private Date ctm;

        public User() {
        }

        public User(String name, int age) {
            this.name = name;
            this.age = age;
            this.ctm = new Date();
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Date getCtm() {
            return ctm;
        }

        public void setCtm(Date ctm) {
            this.ctm = ctm;
        }
}
