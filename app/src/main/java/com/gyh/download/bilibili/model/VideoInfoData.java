package com.gyh.download.bilibili.model;

import java.util.List;

/**
 * Created by  yahuigao
 * Date: 2020/4/8
 * Time: 9:32 AM
 * Description:
 */
public class VideoInfoData {


    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : {"bvid":"BV134411e79Y","aid":52151527,"videos":2,"tid":199,"tname":"明星舞蹈","copyright":1,"pic":"http://i1.hdslb.com/bfs/archive/c1ef9ba4bd4f88ad0d1a5d2388e33a8ae2569b6c.jpg","title":"【阿呆】Violeta-izone 超仙翻跳❀2p竖屏","pubdate":1557575224,"ctime":1557575224,"desc":"wb：@-阿呆每天都不想动\n群：436587270\n准备了好久的翻跳\n本来计划去一个仙的很景但是因为各种原因没拍成 具体原因大家可以看我下周的vlog\n这个景是临是去的朋友开的店里录的 所以看起来有点不合适\n-感谢\n摄影：符号\n后期：衣杏","state":0,"attribute":16768,"duration":174,"rights":{"bp":0,"elec":0,"download":1,"movie":0,"pay":0,"hd5":1,"no_reprint":1,"autoplay":1,"ugc_pay":0,"is_cooperation":0,"ugc_pay_preview":0,"no_background":0},"owner":{"mid":49676,"name":"阿呆每天都不想动","face":"http://i0.hdslb.com/bfs/face/330a6fcbad0c8383645efd6d18c5ed6522508a06.jpg"},"stat":{"aid":52151527,"view":1884495,"danmaku":678,"reply":711,"favorite":35182,"coin":13976,"share":1225,"now_rank":0,"his_rank":47,"like":19225,"dislike":0,"evaluation":""},"dynamic":"#翻跳##韩舞翻跳##izone#\n妹妹们早就回归了我却现在才翻跳呜呜呜\n本来计划是超仙的景但是没去成\n这次准备了竖屏放在2p想看竖屏的朋友可以看啦","cid":91285683,"dimension":{"width":1920,"height":1080,"rotate":0},"no_cache":false,"pages":[{"cid":91285683,"page":1,"from":"vupload","part":"IMG_5910","duration":87,"vid":"","weblink":"","dimension":{"width":1920,"height":1080,"rotate":0}},{"cid":91285722,"page":2,"from":"vupload","part":"IMG_5909","duration":87,"vid":"","weblink":"","dimension":{"width":900,"height":1600,"rotate":0}}],"subtitle":{"allow_submit":false,"list":[]}}
     */

    private int code;
    private String message;
    private int ttl;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * bvid : BV134411e79Y
         * aid : 52151527
         * videos : 2
         * tid : 199
         * tname : 明星舞蹈
         * copyright : 1
         * pic : http://i1.hdslb.com/bfs/archive/c1ef9ba4bd4f88ad0d1a5d2388e33a8ae2569b6c.jpg
         * title : 【阿呆】Violeta-izone 超仙翻跳❀2p竖屏
         * pubdate : 1557575224
         * ctime : 1557575224
         * desc : wb：@-阿呆每天都不想动
         群：436587270
         准备了好久的翻跳
         本来计划去一个仙的很景但是因为各种原因没拍成 具体原因大家可以看我下周的vlog
         这个景是临是去的朋友开的店里录的 所以看起来有点不合适
         -感谢
         摄影：符号
         后期：衣杏
         * state : 0
         * attribute : 16768
         * duration : 174
         * rights : {"bp":0,"elec":0,"download":1,"movie":0,"pay":0,"hd5":1,"no_reprint":1,"autoplay":1,"ugc_pay":0,"is_cooperation":0,"ugc_pay_preview":0,"no_background":0}
         * owner : {"mid":49676,"name":"阿呆每天都不想动","face":"http://i0.hdslb.com/bfs/face/330a6fcbad0c8383645efd6d18c5ed6522508a06.jpg"}
         * stat : {"aid":52151527,"view":1884495,"danmaku":678,"reply":711,"favorite":35182,"coin":13976,"share":1225,"now_rank":0,"his_rank":47,"like":19225,"dislike":0,"evaluation":""}
         * dynamic : #翻跳##韩舞翻跳##izone#
         妹妹们早就回归了我却现在才翻跳呜呜呜
         本来计划是超仙的景但是没去成
         这次准备了竖屏放在2p想看竖屏的朋友可以看啦
         * cid : 91285683
         * dimension : {"width":1920,"height":1080,"rotate":0}
         * no_cache : false
         * pages : [{"cid":91285683,"page":1,"from":"vupload","part":"IMG_5910","duration":87,"vid":"","weblink":"","dimension":{"width":1920,"height":1080,"rotate":0}},{"cid":91285722,"page":2,"from":"vupload","part":"IMG_5909","duration":87,"vid":"","weblink":"","dimension":{"width":900,"height":1600,"rotate":0}}]
         * subtitle : {"allow_submit":false,"list":[]}
         */

        private String bvid;
        private int aid;
        private int videos;
        private int tid;
        private String tname;
        private int copyright;
        private String pic;
        private String title;
        private int pubdate;
        private int ctime;
        private String desc;
        private int state;
        private int attribute;
        private int duration;
        private RightsBean rights;
        private OwnerBean owner;
        private StatBean stat;
        private String dynamic;
        private int cid;
        private DimensionData dimension;
        private boolean no_cache;
        private SubtitleBean subtitle;
        private List<PageData> pages;

        public String getBvid() {
            return bvid;
        }

        public void setBvid(String bvid) {
            this.bvid = bvid;
        }

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public int getVideos() {
            return videos;
        }

        public void setVideos(int videos) {
            this.videos = videos;
        }

        public int getTid() {
            return tid;
        }

        public void setTid(int tid) {
            this.tid = tid;
        }

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public int getCopyright() {
            return copyright;
        }

        public void setCopyright(int copyright) {
            this.copyright = copyright;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getPubdate() {
            return pubdate;
        }

        public void setPubdate(int pubdate) {
            this.pubdate = pubdate;
        }

        public int getCtime() {
            return ctime;
        }

        public void setCtime(int ctime) {
            this.ctime = ctime;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getAttribute() {
            return attribute;
        }

        public void setAttribute(int attribute) {
            this.attribute = attribute;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public RightsBean getRights() {
            return rights;
        }

        public void setRights(RightsBean rights) {
            this.rights = rights;
        }

        public OwnerBean getOwner() {
            return owner;
        }

        public void setOwner(OwnerBean owner) {
            this.owner = owner;
        }

        public StatBean getStat() {
            return stat;
        }

        public void setStat(StatBean stat) {
            this.stat = stat;
        }

        public String getDynamic() {
            return dynamic;
        }

        public void setDynamic(String dynamic) {
            this.dynamic = dynamic;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public DimensionData getDimension() {
            return dimension;
        }

        public void setDimension(DimensionData dimension) {
            this.dimension = dimension;
        }

        public boolean isNo_cache() {
            return no_cache;
        }

        public void setNo_cache(boolean no_cache) {
            this.no_cache = no_cache;
        }

        public SubtitleBean getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(SubtitleBean subtitle) {
            this.subtitle = subtitle;
        }

        public List<PageData> getPages() {
            return pages;
        }

        public void setPages(List<PageData> pages) {
            this.pages = pages;
        }

        public static class RightsBean {
            /**
             * bp : 0
             * elec : 0
             * download : 1
             * movie : 0
             * pay : 0
             * hd5 : 1
             * no_reprint : 1
             * autoplay : 1
             * ugc_pay : 0
             * is_cooperation : 0
             * ugc_pay_preview : 0
             * no_background : 0
             */

            private int bp;
            private int elec;
            private int download;
            private int movie;
            private int pay;
            private int hd5;
            private int no_reprint;
            private int autoplay;
            private int ugc_pay;
            private int is_cooperation;
            private int ugc_pay_preview;
            private int no_background;

            public int getBp() {
                return bp;
            }

            public void setBp(int bp) {
                this.bp = bp;
            }

            public int getElec() {
                return elec;
            }

            public void setElec(int elec) {
                this.elec = elec;
            }

            public int getDownload() {
                return download;
            }

            public void setDownload(int download) {
                this.download = download;
            }

            public int getMovie() {
                return movie;
            }

            public void setMovie(int movie) {
                this.movie = movie;
            }

            public int getPay() {
                return pay;
            }

            public void setPay(int pay) {
                this.pay = pay;
            }

            public int getHd5() {
                return hd5;
            }

            public void setHd5(int hd5) {
                this.hd5 = hd5;
            }

            public int getNo_reprint() {
                return no_reprint;
            }

            public void setNo_reprint(int no_reprint) {
                this.no_reprint = no_reprint;
            }

            public int getAutoplay() {
                return autoplay;
            }

            public void setAutoplay(int autoplay) {
                this.autoplay = autoplay;
            }

            public int getUgc_pay() {
                return ugc_pay;
            }

            public void setUgc_pay(int ugc_pay) {
                this.ugc_pay = ugc_pay;
            }

            public int getIs_cooperation() {
                return is_cooperation;
            }

            public void setIs_cooperation(int is_cooperation) {
                this.is_cooperation = is_cooperation;
            }

            public int getUgc_pay_preview() {
                return ugc_pay_preview;
            }

            public void setUgc_pay_preview(int ugc_pay_preview) {
                this.ugc_pay_preview = ugc_pay_preview;
            }

            public int getNo_background() {
                return no_background;
            }

            public void setNo_background(int no_background) {
                this.no_background = no_background;
            }
        }

        public static class OwnerBean {
            /**
             * mid : 49676
             * name : 阿呆每天都不想动
             * face : http://i0.hdslb.com/bfs/face/330a6fcbad0c8383645efd6d18c5ed6522508a06.jpg
             */

            private int mid;
            private String name;
            private String face;

            public int getMid() {
                return mid;
            }

            public void setMid(int mid) {
                this.mid = mid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getFace() {
                return face;
            }

            public void setFace(String face) {
                this.face = face;
            }
        }

        public static class StatBean {
            /**
             * aid : 52151527
             * view : 1884495
             * danmaku : 678
             * reply : 711
             * favorite : 35182
             * coin : 13976
             * share : 1225
             * now_rank : 0
             * his_rank : 47
             * like : 19225
             * dislike : 0
             * evaluation :
             */

            private int aid;
            private int view;
            private int danmaku;
            private int reply;
            private int favorite;
            private int coin;
            private int share;
            private int now_rank;
            private int his_rank;
            private int like;
            private int dislike;
            private String evaluation;

            public int getAid() {
                return aid;
            }

            public void setAid(int aid) {
                this.aid = aid;
            }

            public int getView() {
                return view;
            }

            public void setView(int view) {
                this.view = view;
            }

            public int getDanmaku() {
                return danmaku;
            }

            public void setDanmaku(int danmaku) {
                this.danmaku = danmaku;
            }

            public int getReply() {
                return reply;
            }

            public void setReply(int reply) {
                this.reply = reply;
            }

            public int getFavorite() {
                return favorite;
            }

            public void setFavorite(int favorite) {
                this.favorite = favorite;
            }

            public int getCoin() {
                return coin;
            }

            public void setCoin(int coin) {
                this.coin = coin;
            }

            public int getShare() {
                return share;
            }

            public void setShare(int share) {
                this.share = share;
            }

            public int getNow_rank() {
                return now_rank;
            }

            public void setNow_rank(int now_rank) {
                this.now_rank = now_rank;
            }

            public int getHis_rank() {
                return his_rank;
            }

            public void setHis_rank(int his_rank) {
                this.his_rank = his_rank;
            }

            public int getLike() {
                return like;
            }

            public void setLike(int like) {
                this.like = like;
            }

            public int getDislike() {
                return dislike;
            }

            public void setDislike(int dislike) {
                this.dislike = dislike;
            }

            public String getEvaluation() {
                return evaluation;
            }

            public void setEvaluation(String evaluation) {
                this.evaluation = evaluation;
            }
        }


        public static class SubtitleBean {
            /**
             * allow_submit : false
             * list : []
             */

            private boolean allow_submit;
            private List<?> list;

            public boolean isAllow_submit() {
                return allow_submit;
            }

            public void setAllow_submit(boolean allow_submit) {
                this.allow_submit = allow_submit;
            }

            public List<?> getList() {
                return list;
            }

            public void setList(List<?> list) {
                this.list = list;
            }
        }


    }
}
