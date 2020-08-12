package com.zxs.design.pattern.creational.singleton;


public class HungrySingleModel{

    private final static HungrySingleModel singleModel = new HungrySingleModel();

    private HungrySingleModel(){

    }

    public static HungrySingleModel getInstance(){
        return singleModel;
    }

}
