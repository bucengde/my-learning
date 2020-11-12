package com.learning.commons.bean.nil;

/**
 * 空对象，无实际意义
 * @author Wang Xu
 * @date 2020/10/24
 */
public class NilObject {
    private static NilObject NIL = new NilObject();

    private NilObject() {
    }

    public static NilObject getInstance() {
        return NIL;
    }
}