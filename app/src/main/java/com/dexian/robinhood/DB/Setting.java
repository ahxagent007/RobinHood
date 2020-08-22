package com.dexian.robinhood.DB;

public class Setting {
    String title;
    String iv_01, iv_02, iv_03, iv_04, iv_05;

    public Setting(String title, String iv_01, String iv_02, String iv_03, String iv_04, String iv_05) {
        this.title = title;
        this.iv_01 = iv_01;
        this.iv_02 = iv_02;
        this.iv_03 = iv_03;
        this.iv_04 = iv_04;
        this.iv_05 = iv_05;
    }

    public Setting() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIv_01() {
        return iv_01;
    }

    public void setIv_01(String iv_01) {
        this.iv_01 = iv_01;
    }

    public String getIv_02() {
        return iv_02;
    }

    public void setIv_02(String iv_02) {
        this.iv_02 = iv_02;
    }

    public String getIv_03() {
        return iv_03;
    }

    public void setIv_03(String iv_03) {
        this.iv_03 = iv_03;
    }

    public String getIv_04() {
        return iv_04;
    }

    public void setIv_04(String iv_04) {
        this.iv_04 = iv_04;
    }

    public String getIv_05() {
        return iv_05;
    }

    public void setIv_05(String iv_05) {
        this.iv_05 = iv_05;
    }
}
