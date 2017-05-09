package demo.wang.cn.librarysdemo.bean;

import cn.wang.adapter.bases.ItemBean;

/**
 * author : wangshuai Created on 2017/5/8
 * email : wangs1992321@gmail.com
 */
public class ImageBean implements ItemBean{
    private String url;

    public ImageBean(String url) {
        this.url = url;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
