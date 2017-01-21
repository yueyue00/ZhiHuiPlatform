package com.smartdot.wenbo.controlcenter.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

public class HomeImageBean {

	 /**
     * code : 200
     * info : [{"imgUrl":"https://mobile.silkroaddunhuang.com:8088/wenbo2/upload/zhptImage/20160815112810654307.png","imgname":"aaa"}]
     * message : 返回图片信息成功!
     * pagecount : 0
     * pagenum : 0
     */
    @Expose
    private String code;
    @Expose
    private String message;
    @Expose
    private int pagecount;
    @Expose
    private int pagenum;
    /**
     * imgUrl : https://mobile.silkroaddunhuang.com:8088/wenbo2/upload/zhptImage/20160815112810654307.png
     * imgname : aaa
     */
    @Expose
    private List<InfoBean> info;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPagecount() {
        return pagecount;
    }

    public void setPagecount(int pagecount) {
        this.pagecount = pagecount;
    }

    public int getPagenum() {
        return pagenum;
    }

    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
    	@Expose
        private String imgUrl;
    	@Expose
        private String imgname;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getImgname() {
            return imgname;
        }

        public void setImgname(String imgname) {
            this.imgname = imgname;
        }
    }
}
