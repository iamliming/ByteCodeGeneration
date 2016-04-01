package com.cglib;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 四月 01, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class SampleClass{
    private int i ;
    public SampleClass(int i)
    {
        this.i = i;
    }
    public SampleClass(){}
    public String test(String input)
    {
        System.out.println(i);
        return "Hello world!";
    }
    public static String staticMethod(){
        return "static";
    }
    public final String finalMethod(){
        return "final";
    }
}
