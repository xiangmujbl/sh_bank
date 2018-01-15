package com.mmec.util;

public class CustomImages {
	private int x,y;
    private String src;
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public String getSrc() {
        return src;
    }
    public void setSrc(String src) {
        this.src = src;
    }
    public CustomImages(int x, int y, String src) {
        this.x = x;
        this.y = y;
        this.src = src;
    }
}
