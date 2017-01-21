package com.smartdot.wenbo.controlcenter.bean;

import java.util.List;

/**
 * Created by lixiaoming on 16/8/14.
 */
public class HomeBean {

    /**
     * code : 200
     * info : [{"meetting":[{"startTime":1471658400000,"dunhuangService":"","meettingtime":"2016-08-20 10:00-11:00","venueContact":"","speakPerson":"维顿汉","endTiime":1471662000000,"undertakingContact":"许褚","undertakingUnit":"市委宣传部","type":2,"meettingtitle":"中医药论坛开幕式"},{"startTime":1471667400000,"dunhuangService":"赵一明","meettingtime":"2016-08-20 12:30-14:30","venueContact":"张强","speakPerson":"赵刚,赵强,战三,孙进","endTiime":1471674600000,"undertakingContact":"张学德","undertakingUnit":"华夏酒店","type":1,"meettingtitle":"文博会开幕式"},{"startTime":1471676408000,"dunhuangService":"","meettingtime":"2016-08-20 15:00-16:30","venueContact":"","speakPerson":"","endTiime":1471681820000,"undertakingContact":"","undertakingUnit":"","type":2,"meettingtitle":""}],"date":"2016-08-20","week":"星期六"}]
     * message : 大会日程列表!
     * pagecount : 0
     * pagenum : 0
     */

    private String code;
    private String message;
    private int pagecount;
    private int pagenum;
    /**
     * meetting : [{"startTime":1471658400000,"dunhuangService":"","meettingtime":"2016-08-20 10:00-11:00","venueContact":"","speakPerson":"维顿汉","endTiime":1471662000000,"undertakingContact":"许褚","undertakingUnit":"市委宣传部","type":2,"meettingtitle":"中医药论坛开幕式"},{"startTime":1471667400000,"dunhuangService":"赵一明","meettingtime":"2016-08-20 12:30-14:30","venueContact":"张强","speakPerson":"赵刚,赵强,战三,孙进","endTiime":1471674600000,"undertakingContact":"张学德","undertakingUnit":"华夏酒店","type":1,"meettingtitle":"文博会开幕式"},{"startTime":1471676408000,"dunhuangService":"","meettingtime":"2016-08-20 15:00-16:30","venueContact":"","speakPerson":"","endTiime":1471681820000,"undertakingContact":"","undertakingUnit":"","type":2,"meettingtitle":""}]
     * date : 2016-08-20
     * week : 星期六
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
        private String date;
        private String week;
        private String flag;
        /**
         * startTime : 1471658400000
         * dunhuangService :
         * meettingtime : 2016-08-20 10:00-11:00
         * venueContact :
         * speakPerson : 维顿汉
         * endTiime : 1471662000000
         * undertakingContact : 许褚
         * undertakingUnit : 市委宣传部
         * type : 2
         * meettingtitle : 中医药论坛开幕式
         */

        private List<MeettingBean> meetting;
        

        public String getFlag() {
			return flag;
		}

		public void setFlag(String flag) {
			this.flag = flag;
		}

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
            private String dunhuangService;
            private String meettingtime;
            private String venueContact;
            private String speakPerson;
            private long endTiime;
            private String undertakingContact;
            private String undertakingUnit;
            private int type;
            private String meettingtitle;
            private String responsibilityProvincePerson;
            private String responsibilityUnit;
            
            

            public String getResponsibilityProvincePerson() {
				return responsibilityProvincePerson;
			}

			public void setResponsibilityProvincePerson(String responsibilityProvincePerson) {
				this.responsibilityProvincePerson = responsibilityProvincePerson;
			}

			public String getResponsibilityUnit() {
				return responsibilityUnit;
			}

			public void setResponsibilityUnit(String responsibilityUnit) {
				this.responsibilityUnit = responsibilityUnit;
			}

			public long getStartTime() {
                return startTime;
            }

            public void setStartTime(long startTime) {
                this.startTime = startTime;
            }

            public String getDunhuangService() {
                return dunhuangService;
            }

            public void setDunhuangService(String dunhuangService) {
                this.dunhuangService = dunhuangService;
            }

            public String getMeettingtime() {
                return meettingtime;
            }

            public void setMeettingtime(String meettingtime) {
                this.meettingtime = meettingtime;
            }

            public String getVenueContact() {
                return venueContact;
            }

            public void setVenueContact(String venueContact) {
                this.venueContact = venueContact;
            }

            public String getSpeakPerson() {
                return speakPerson;
            }

            public void setSpeakPerson(String speakPerson) {
                this.speakPerson = speakPerson;
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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
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
