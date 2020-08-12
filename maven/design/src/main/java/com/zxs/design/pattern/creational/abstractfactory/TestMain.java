package com.zxs.design.pattern.creational.abstractfactory;

public class TestMain {
    public static void main(String[] args) {
        ElectricalInterface electricalInterface = new XiaoMiElectrical();
        Television television = electricalInterface.getTelevision();
        Fridges fridge = electricalInterface.getFridge();
        fridge.produceFridge();
        television.produceTelevision();
    }
}
