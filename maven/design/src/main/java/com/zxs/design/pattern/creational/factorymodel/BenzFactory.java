package com.zxs.design.pattern.creational.factorymodel;

public class BenzFactory extends CarFactory {

    @Override
    public Car produceCar() {
        return new BenzCar();
    }
}
