package com.wl.dudian.app.model;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by yisheng on 16/6/20.
 */
public class LatestNews extends DataSupport {
    /**
     * date : 20160620
     * stories : [{"images":["http://pic1.zhimg.com/13f2808a86d85a04bc35ed75fea9fb20.jpg"],"type":0,"id":8459818,"ga_prefix":"062022","title":"小事 · 一夜之间变成穷人"},{"title":"没有好莱坞式的大制作，这些冷门电影一样可以很好看","ga_prefix":"062021","images":["http://pic4.zhimg.com/295b339f7667204ff5026a56b76c537b.jpg"],"multipic":true,"type":0,"id":8427314},{"images":["http://pic3.zhimg.com/ddfea36ed0371dd4c6f898873c56da4a.jpg"],"type":0,"id":8467740,"ga_prefix":"062020","title":"骑士大逆转首夺 NBA 总冠军，一场后来居上的马拉松"},{"images":["http://pic4.zhimg.com/e2851014da915d9cbcd7991d2ad6e0e3.jpg"],"type":0,"id":8467704,"ga_prefix":"062019","title":"《职人介绍所》第 21 期：中国特别有名的两位程序员 winter 和赵劼来了"},{"title":"一个想法：万磁王克钢铁侠么？","ga_prefix":"062019","images":["http://pic1.zhimg.com/c157b59ce0b915a2bf39ad4afdc9aeac.jpg"],"multipic":true,"type":0,"id":8458990},{"images":["http://pic3.zhimg.com/0123b095f16aeeb13c2109340a581be6.jpg"],"type":0,"id":8459326,"ga_prefix":"062018","title":"防晒衣选不对，小心没能防晒还捂痱"},{"images":["http://pic2.zhimg.com/f5dfdcd981f30b481f2b3d9a98bb9835.jpg"],"type":0,"id":8447027,"ga_prefix":"062017","title":"知乎好问题 · 有点贵但很值得买的商品有哪些？"},{"images":["http://pic3.zhimg.com/d7fa253ee76858e4386645a03a3b8fea.jpg"],"type":0,"id":8453174,"ga_prefix":"062016","title":"满脸爆痘、失眠焦虑，我是不是内分泌失调了？"},{"images":["http://pic2.zhimg.com/aa999fd744baad77f7726bbef71e8121.jpg"],"type":0,"id":8464631,"ga_prefix":"062015","title":"人人都想有场梦幻婚礼，首先你得有个\u2026\u2026好司仪"},{"images":["http://pic2.zhimg.com/2ba82bf6c7735854f0b4af45283f6bed.jpg"],"type":0,"id":8466518,"ga_prefix":"062014","title":"切实提升工作效率的三个技巧，实践下来对我很有帮助"},{"images":["http://pic3.zhimg.com/e243c13a560612911287cb39994dd42a.jpg"],"type":0,"id":8457955,"ga_prefix":"062013","title":"这种闷热潮湿，还动不动下场雨的天气，果然就是梅雨"},{"images":["http://pic3.zhimg.com/fc7fa785ebee70a21ab92cad30451b9a.jpg"],"type":0,"id":8455704,"ga_prefix":"062012","title":"大误 · 搓澡师江湖"},{"images":["http://pic4.zhimg.com/c39bb15d9fa8c0287da17d31c5b91767.jpg"],"type":0,"id":8437953,"ga_prefix":"062011","title":"写字为生的我，也无法描述这让人泪目的天空和大地"},{"images":["http://pic1.zhimg.com/0992cae8eceaaffe272a1c265b0a433c.jpg"],"type":0,"id":8463837,"ga_prefix":"062010","title":"一波还未平息，一波又来侵袭，这里就变得越来越「浪」"},{"images":["http://pic4.zhimg.com/b3c64a77001948d71d2156fd327c693f.jpg"],"type":0,"id":8455609,"ga_prefix":"062009","title":"有个段子说，日本人姓「田中」、「松下」，是因为\u2026\u2026"},{"images":["http://pic3.zhimg.com/88af88010cb8e6b479169d510178754e.jpg"],"type":0,"id":8462276,"ga_prefix":"062008","title":"金星一直在绕地球跳五瓣花的舞，我们却浑然不觉"},{"images":["http://pic1.zhimg.com/6ea877a53034404d4889972d55b5cc7c.jpg"],"type":0,"id":8464414,"ga_prefix":"062007","title":"这家全世界最会卖报纸的报社，也开始打起了 VR 的主意"},{"images":["http://pic1.zhimg.com/ca86d856f9640661ff267f45134b53e8.jpg"],"type":0,"id":8464455,"ga_prefix":"062007","title":"有些工作，就是需要一直加班，常常加班，总是加班"},{"images":["http://pic4.zhimg.com/18a3f0835af5e176dd7788c59f0eda6b.jpg"],"type":0,"id":8464520,"ga_prefix":"062007","title":"上个好大学 · 见过洛杉矶凌晨 4 点的天空么？我见过北京一天 24 小时的样子"},{"images":["http://pic4.zhimg.com/5379affd15c15cd22675a076d84855b7.jpg"],"type":0,"id":8464926,"ga_prefix":"062007","title":"读读日报 24 小时热门 TOP 5 · 只有中国人才爱敲西瓜吗？"},{"images":["http://pic4.zhimg.com/c9e36ea268607c621efa2675f4390a17.jpg"],"type":0,"id":8453591,"ga_prefix":"062006","title":"瞎扯 · 奇葩客户"}]
     * top_stories : [{"image":"http://pic4.zhimg.com/336cf3c4e468a67917c6f21b8fa63feb.jpg","type":0,"id":8467704,"ga_prefix":"062019","title":"《职人介绍所》第 21 期：中国特别有名的两位程序员 winter 和赵劼来了"},{"image":"http://pic2.zhimg.com/2c9caaec12fa6ce8ecdde3bf7fd66f41.jpg","type":0,"id":8447027,"ga_prefix":"062017","title":"知乎好问题 · 有点贵但很值得买的商品有哪些？"},{"image":"http://pic3.zhimg.com/3e6fcb06c57627952492f5ec2a333f36.jpg","type":0,"id":8466518,"ga_prefix":"062014","title":"切实提升工作效率的三个技巧，实践下来对我很有帮助"},{"image":"http://pic1.zhimg.com/699d8d944bf6544e64700988f4ed8784.jpg","type":0,"id":8464926,"ga_prefix":"062007","title":"读读日报 24 小时热门 TOP 5 · 只有中国人才爱敲西瓜吗？"},{"image":"http://pic3.zhimg.com/f2d87091f87951b55761ac1591d58c5e.jpg","type":0,"id":8464455,"ga_prefix":"062007","title":"有些工作，就是需要一直加班，常常加班，总是加班"}]
     */

    private String date;
    /**
     * images : ["http://pic1.zhimg.com/13f2808a86d85a04bc35ed75fea9fb20.jpg"]
     * type : 0
     * id : 8459818
     * ga_prefix : 062022
     * title : 小事 · 一夜之间变成穷人
     */

    private List<StoriesBean> stories;
    /**
     * image : http://pic4.zhimg.com/336cf3c4e468a67917c6f21b8fa63feb.jpg
     * type : 0
     * id : 8467704
     * ga_prefix : 062019
     * title : 《职人介绍所》第 21 期：中国特别有名的两位程序员 winter 和赵劼来了
     */

    private List<TopStoriesBean> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }
}
