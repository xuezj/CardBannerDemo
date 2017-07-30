package com.xuezj.cardbanner;

/**
 * Created by xuezj on 2017/7/29.
 */

public class ImageData extends BannerData {
    private Object image;
    private String mainTitle;
    private String subtitleTitle;

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getSubtitleTitle() {
        return subtitleTitle;
    }

    public void setSubtitleTitle(String subtitleTitle) {
        this.subtitleTitle = subtitleTitle;
    }
}
