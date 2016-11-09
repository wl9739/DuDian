package com.wl.dudian.app.db.model;

import java.util.List;

/**
 * @author Qiushui
 * @since 0.0.2
 */
public class DiscussDataModel {
    /**
     * author : 每一天都在混水摸鱼 content : 钱会让它变的好吃 avatar : http://pic3.zhimg.com/0ecf2216c2612b04592126adc16affa2_im.jpg time
     * : 1413987020 id : 556780 likes : 0
     */

    private List<CommentsBean> comments;

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }
}
