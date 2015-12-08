package com.yaowang.douniwan;

/**
 * @author : created by chuangWu
 * @version : 0.01
 * @email : chuangwu127@gmail.com
 * @created time : 2015-08-20 15:35
 * @description : none
 * @for your attention : none
 * @revise : none
 */
public class ResultEntity {

    private String id;
    private String type;
    private String nickname;
    private String sex;
    private String headpic;

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    @Override
    public String toString() {
        return "id-->" + id + ":type-->" + type + ":nickname-->" + nickname + ":sex-->" + sex + ":headpic-->" + headpic;
    }


}
