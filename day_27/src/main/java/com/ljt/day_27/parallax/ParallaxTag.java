package com.ljt.day_27.parallax;

/**
 * Created by JoeLjt on 2019/4/11.
 * Email: lijiateng1219@gmail.com
 * Description:
 */

public class ParallaxTag {

    private float xIn;
    private float xOut;
    private float yIn;
    private float yOut;

    public float getxIn() {
        return xIn;
    }

    public void setxIn(float xIn) {
        this.xIn = xIn;
    }

    public float getxOut() {
        return xOut;
    }

    public void setxOut(float xOut) {
        this.xOut = xOut;
    }

    public float getyIn() {
        return yIn;
    }

    public void setyIn(float yIn) {
        this.yIn = yIn;
    }

    public float getyOut() {
        return yOut;
    }

    public void setyOut(float yOut) {
        this.yOut = yOut;
    }

    @Override
    public String toString() {
        return "ParallaxTag{" +
                "xIn=" + xIn +
                ", xOut=" + xOut +
                ", yIn=" + yIn +
                ", yOut=" + yOut +
                '}';
    }
}
