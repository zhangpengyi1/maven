package com.zxs.design.pattern.creational.factorymodel;

public class TestMain {
    public static void main(String[] args) {
        CarFactory carFactory = new BmwFactory();
        Car car = carFactory.produceCar();
        car.run();

        CarFactory carFactory2 = new BenzFactory();
        Car car2 = carFactory2.produceCar();
        car2.run();
    }
}
