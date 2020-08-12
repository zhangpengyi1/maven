package com.zxs.design.pattern.creational.singleton;

public class LazySingleModel {


    private LazySingleModel() {

    }

    // 普通懒汉模式 线程不安全
    /*
    private static LazySingleModel singleModel = null;
    public static LazySingleModel getInstance(){
        if (singleModel == null){
            singleModel = new LazySingleModel();
        }
        return singleModel;
    }
    */

    // 懒汉模式加上同步锁 线程安全 锁整个类
    /*
    private static LazySingleModel singleModel = null;
    public synchronized LazySingleModel getInstance(){
        if (singleModel == null) {
            singleModel = new LazySingleModel();
        }
        return singleModel;
    }
    */

    // 双重校验 volatile 使对象初始化时顺序不变，
    private volatile static LazySingleModel singleModel = null;
    public LazySingleModel getInstance() {
        if(singleModel == null) {
            synchronized (LazySingleModel.class) {
                if (singleModel == null) {
                    // 对象初始化三部曲 1.分配对象的内存空间 2.初始化对象 3.指向内存空间
                    singleModel = new LazySingleModel();
                }
            }
        }
        return singleModel;
    }

}
