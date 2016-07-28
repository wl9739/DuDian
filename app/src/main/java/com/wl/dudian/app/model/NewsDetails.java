package com.wl.dudian.app.model;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by yisheng on 16/6/22.
 */
public class NewsDetails extends DataSupport {
    /**
     * body : dfsad image_source : Yestone.com 版权图片库 title : 深夜惊奇 · 朋友圈错觉 image : http://pic3.zhimg.com/2d41a1d1ebf37fb699795e78db76b5c2.jpg
     * share_url : http://daily.zhihu.com/story/4772126 js : [] recommenders : [{"avatar":"http:
     * //pic2.zhimg.com/fcb7039c1_m.jpg"},{"avatar":"http: //pic1.zhimg.com/29191527c_m.jpg"},{"avatar":"http:
     * //pic4.zhimg.com/e6637a38d22475432c76e6c9e46336fb_m.jpg"},{"avatar":"http: //pic1.zhimg.com/bd751e76463e94aa10c7ed2529738314_m.jpg"},{"avatar":"http:
     * //pic1.zhimg.com/4766e0648_m.jpg"}] ga_prefix : 050615 section : {"thumbnail":"http://pic4.zhimg.com/6a1ddebda9e8899811c4c169b92c35b3.jpg","id":1,"name":"深夜惊奇"}
     * type : 0 id : 4772126 css : ["http://news.at.zhihu.com/css/news_qa.auto.css?v=1edab"]
     */

    private String body;
    private String image_source;
    private String title;
    private String image;
    private String share_url;
    private String ga_prefix;
    private List<String> images;

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public SectionBean getSection() {
        return section;
    }

    public void setSection(SectionBean section) {
        this.section = section;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<RecommendersBean> getRecommenders() {
        return recommenders;
    }

    public void setRecommenders(List<RecommendersBean> recommenders) {
        this.recommenders = recommenders;
    }

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }

    /**
     * thumbnail : http://pic4.zhimg.com/6a1ddebda9e8899811c4c169b92c35b3.jpg id : 1 name : 深夜惊奇
     */

    private SectionBean section;
    private int type;
    private int id;
    /**
     * avatar : http: //pic2.zhimg.com/fcb7039c1_m.jpg
     */

    private java.util.List<RecommendersBean> recommenders;
    private java.util.List<String> css;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static class SectionBean extends DataSupport {
        private String thumbnail;
        private int id;
        private String name;

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
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
    }

    public static class RecommendersBean extends DataSupport {
        private String avatar;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
