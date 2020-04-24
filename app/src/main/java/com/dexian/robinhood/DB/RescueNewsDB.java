package com.dexian.robinhood.DB;

public class RescueNewsDB {

    long ID;
    String PictureLink;
    String NewsLink;

    public String getNewsLink() {
        return NewsLink;
    }

    public void setNewsLink(String newsLink) {
        NewsLink = newsLink;
    }

    public RescueNewsDB() {
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getPictureLink() {
        return PictureLink;
    }

    public void setPictureLink(String pictureLink) {
        PictureLink = pictureLink;
    }
}
