package com.smartdot.wenbo.controlcenter.bean;

import java.util.List;

import com.google.gson.annotations.Expose;

public class ScheduleAllData {


    /**
     * code : 200
     * info : [{"meetting":[{"startTime":1471658400000,"meettingtime":"2016-08-20 10:00-11:00","address":"华夏大酒店1楼多功能会议厅","responsibilityUnit":"省政府办公厅","endTiime":1471662000000,"undertakingContact":"许褚","undertakingUnit":"市委宣传部","meettingtitle":"中医药论坛开幕式"},{"startTime":1471667400000,"meettingtime":"2016-08-20 12:30-14:30","address":"国际会展中心2号楼2楼2号多功能会议厅","responsibilityUnit":"省委办公厅","endTiime":1471674600000,"undertakingContact":"张学德","undertakingUnit":"华夏酒店","meettingtitle":"文博会开幕式"}],"date":"08月20日 ","week":"星期六"}]
     * message : 大会日程列表!
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
     * meetting : [{"startTime":1471658400000,"meettingtime":"2016-08-20 10:00-11:00","address":"华夏大酒店1楼多功能会议厅","responsibilityUnit":"省政府办公厅","endTiime":1471662000000,"undertakingContact":"许褚","undertakingUnit":"市委宣传部","meettingtitle":"中医药论坛开幕式"},{"startTime":1471667400000,"meettingtime":"2016-08-20 12:30-14:30","address":"国际会展中心2号楼2楼2号多功能会议厅","responsibilityUnit":"省委办公厅","endTiime":1471674600000,"undertakingContact":"张学德","undertakingUnit":"华夏酒店","meettingtitle":"文博会开幕式"}]
     * date : 08月20日 
     * week : 星期六
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
        private String date;
    	@Expose
        private String week;
        /**
         * startTime : 1471658400000
         * meettingtime : 2016-08-20 10:00-11:00
         * address : 华夏大酒店1楼多功能会议厅
         * responsibilityUnit : 省政府办公厅
         * endTiime : 1471662000000
         * undertakingContact : 许褚
         * undertakingUnit : 市委宣传部
         * meettingtitle : 中医药论坛开幕式
         */
    	@Expose
        private List<MeettingBean> meetting;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public List<MeettingBean> getMeetting() {
            return meetting;
        }

        public void setMeetting(List<MeettingBean> meetting) {
            this.meetting = meetting;
        }

        public static class MeettingBean {
        	@Expose
            private long startTime;
        	@Expose
            private String meettingtime;
        	@Expose
            private String address;
        	@Expose
            private String responsibilityUnit;
        	@Expose
            private long endTiime;
        	@Expose
            private String undertakingContact;
        	@Expose
            private String undertakingUnit;
        	@Expose
            private String meettingtitle;

            public long getStartTime() {
                return startTime;
            }

            public void setStartTime(long startTime) {
                this.startTime = startTime;
            }

            public String getMeettingtime() {
                return meettingtime;
            }

            public void setMeettingtime(String meettingtime) {
                this.meettingtime = meettingtime;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getResponsibilityUnit() {
                return responsibilityUnit;
            }

            public void setResponsibilityUnit(String responsibilityUnit) {
                this.responsibilityUnit = responsibilityUnit;
            }

            public long getEndTiime() {
                return endTiime;
            }

            public void setEndTiime(long endTiime) {
                this.endTiime = endTiime;
            }

            public String getUndertakingContact() {
                return undertakingContact;
            }

            public void setUndertakingContact(String undertakingContact) {
                this.undertakingContact = undertakingContact;
            }

            public String getUndertakingUnit() {
                return undertakingUnit;
            }

            public void setUndertakingUnit(String undertakingUnit) {
                this.undertakingUnit = undertakingUnit;
            }

            public String getMeettingtitle() {
                return meettingtitle;
            }

            public void setMeettingtitle(String meettingtitle) {
                this.meettingtitle = meettingtitle;
            }
        }
    }

}

