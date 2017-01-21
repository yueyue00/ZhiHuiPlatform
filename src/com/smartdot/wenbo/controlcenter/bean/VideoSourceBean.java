package com.smartdot.wenbo.controlcenter.bean;

import java.util.List;

public class VideoSourceBean {
	 /**
     * code : 200
     * info : [{"vedioPath":"http://127.0.0.1:8080/wenbo2/main.do","vedioSort":1},{"vedioPath":"http://127.0.0.1:8080/wenbo2/main.do","vedioSort":2},{"vedioPath":"http://127.0.0.1:8080/wenbo2/main.do","vedioSort":3},{"vedioPath":"http://127.0.0.1:8080/wenbo2/main.do","vedioSort":4}]
     * message : 视频链接数据
     * pagecount : 0
     * pagenum : 0
     */

    private String code;
    private String message;
    private int pagecount;
    private int pagenum;
    /**
     * vedioPath : http://127.0.0.1:8080/wenbo2/main.do
     * vedioSort : 1
     */

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
        private String vedioPath;
        private int vedioSort;

        public String getVedioPath() {
            return vedioPath;
        }

        public void setVedioPath(String vedioPath) {
            this.vedioPath = vedioPath;
        }

        public int getVedioSort() {
            return vedioSort;
        }

        public void setVedioSort(int vedioSort) {
            this.vedioSort = vedioSort;
        }
    }
}
