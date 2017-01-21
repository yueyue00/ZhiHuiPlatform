package com.smartdot.wenbo.controlcenter.bean;

import java.util.List;

public class SchduleNewBean {
	private String date;
    private String week;
    /**
     * startTime : 1471658400000
     * meettingtime : 2016-08-20 10:00-11:00
     * address : 华夏大酒店1楼多功能会议厅
     * responsibilityUnit : 省政府办公厅
     * endTiime : 1471662000000
     * undertakingContact : 许褚
     * undertakingUnit : 市委宣传部
     * meettingtitle : 
     */

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
        private long startTime;
        private String meettingtime;
        private String address;
        private String responsibilityUnit;
        private long endTiime;
        private String undertakingContact;
        private String undertakingUnit;
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
