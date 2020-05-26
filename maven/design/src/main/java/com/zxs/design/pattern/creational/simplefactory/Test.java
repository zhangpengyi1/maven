package com.zxs.design.pattern.creational.simplefactory;

public class Test {
    public static void main(String[] args) {
        VideoFactory videoFactory = new VideoFactory();
        Video video = videoFactory.getVideo("java");
        if(video == null){
            return;
        }
        video.produce();

        VideoFactory videoFactory1 = new VideoFactory();
        Video video1 = videoFactory1.getVideo(JavaVideo.class);
        if(video1 == null){
            return;
        }
        video1.produce();
    }
}
