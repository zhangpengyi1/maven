package com.zxs.design.pattern.creational.abstractfactory;

public class XiaoMiElectrical implements ElectricalInterface {

    @Override
    public Television getTelevision() {
        return new XiaoMiTelevision();
    }

    @Override
    public Fridges getFridge() {
        return new XiaoMiFridges();
    }
}
