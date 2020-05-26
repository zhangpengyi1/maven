package com.zxs.design.pattern.creational.factorymodel;

public class BmwFactory extends CarFactory {

    @Override
    public Car produceCar() {
        return new BmwCar();
    }

}
