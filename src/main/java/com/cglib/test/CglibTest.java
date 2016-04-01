package com.cglib.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.lang.reflect.Method;

import org.junit.Test;

import com.cglib.SampleClass;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * <一句话功能简述> <功能详细描述>
 *
 * @author liming
 * @version [版本号, 四月 01, 2016]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CglibTest
{
    /**
     * FixedValue委派了所有的非static和final方法
     * 不论输入参数是什么都返回FixedValue中定义的类型
     */
    @Test
    public void testFixedValue(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SampleClass.class);
        enhancer.setCallback(new FixedValue()
        {
            @Override
            public Object loadObject()
                throws Exception
            {
                return "Hello Cglib";
            }
        });
        Class[] classes = new Class[]{int.class};
        Object[] objs = new Object[]{1};
        //the Enhancer class cannot instrument constructors. Neither can it instrument static or final classes
        //enhancer 不能增强构造函数，也不能增强static和final 类
        SampleClass sampleClass = (SampleClass)enhancer.create(classes, objs);
        String s = sampleClass.test("test");
        System.out.println(s);
        System.out.println(sampleClass.getClass());
        //java.lang.ClassCastException: java.lang.String cannot be cast to java.lang.Number
        //所有的方法都会被Callback中的方法委派，包括父类及Object中的方法，所以toString方法也返回hello cglib
        //而hashcode也会返回字符串类型，而抛出类型转换异常
        //sampleClass.hashCode();
        System.out.println(sampleClass.toString());
        System.out.println(sampleClass.finalMethod());
        System.out.println(sampleClass.staticMethod());
    }

    @Test
    public void testInvokeHandler()
    {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SampleClass.class);
        enhancer.setCallback(new InvocationHandler()
        {
            @Override
            public Object invoke(Object o, Method method, Object[] objects)
                throws Throwable
            {
                if (method.getDeclaringClass() != Object.class && method.getReturnType() == String.class)
                {
                    return "Hello Cglib!";
                }
                else{
                    throw new RuntimeException("Do not now what todo");
                }
            }
        });
        SampleClass sampleClass = (SampleClass)enhancer.create();
        assertEquals(sampleClass.test("1"), "Hello Cglib!");
        assertNotEquals(sampleClass.toString(), "Hello Cglib!");
    }

    @Test
    public void testMethordInterceptor()
    {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SampleClass.class);
        enhancer.setCallback(new MethodInterceptor()
        {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy)
                throws Throwable
            {
                if (method.getDeclaringClass() != Object.class && method.getReturnType() == String.class)
                {
                    return "Hello Cglib!";
                }
                else{
                    return methodProxy.invokeSuper(o, objects);
                }
            }
        });
        SampleClass sampleClass = (SampleClass)enhancer.create();
        assertEquals(sampleClass.test("1"), "Hello Cglib!");
        assertNotEquals(sampleClass.toString(), "Hello Cglib!");
        sampleClass.hashCode();
    }
}
