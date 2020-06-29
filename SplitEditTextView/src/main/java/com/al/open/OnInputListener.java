package com.al.open;

/**
 * 输入的监听抽象类
 * 没定义接口的原因是可以在抽象类里面定义空实现的方法,可以让用户根据需求选择性的复写某些方法
 */
public abstract class OnInputListener {

    /**
     * 输入完成的抽象方法
     * @param content 输入内容
     */
   public abstract void onInputFinished(String content);

    /**
     * 输入的内容
     * 定义一个空实现方法,让用户选择性的复写该方法,需要就复写,不需要就不用重写
     */
   public void onInputChanged(String text){

    }
}
