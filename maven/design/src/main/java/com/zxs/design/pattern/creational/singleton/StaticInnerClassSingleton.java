package com.zxs.design.pattern.creational.singleton;

public class StaticInnerClassSingleton {
    // 静态内部类 类初始化的延时加载 解决方案

    private StaticInnerClassSingleton() {

    }

    private static class StaticInnerClass{
        private static StaticInnerClassSingleton singleton = new StaticInnerClassSingleton();
    }

    public StaticInnerClassSingleton getInstance() {
        return StaticInnerClass.singleton;
    }

}
