package com.cui.beancopy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanCopy {


    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        Manager manager = new Manager("tom","NEC",20);

        Manager managerCopy = new Manager();

        BeanCopy.copyBean(manager,managerCopy);

        System.out.println(managerCopy);
        System.err.println(manager);

    }


    public static void copyBean(Object obj1,Object obj2) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        if (obj1.getClass() != obj2.getClass())
            throw new RuntimeException("传入的两个参数类型不一致");

        //获得Class运行时类
        Class objClass = obj1.getClass();

        while (objClass != Object.class) {

            //返回所有属性
            Field[] fields = objClass.getDeclaredFields();

            for (Field f : fields) {
                String fieldName = f.getName();

                //动态拼接出get方法名称
                String getMethod = (f.getType() == boolean.class ? "is" : "get")
                        + String.valueOf(fieldName.charAt(0)).toUpperCase()
                        + fieldName.substring(1);

                //创建get方法对象
                Method get = objClass.getMethod(getMethod);

                //动态调用get方法得到返回值
                Object result = get.invoke(obj1);

                //动态拼接出set方法名称
                String setMethod = "set"
                        + String.valueOf(fieldName.charAt(0)).toUpperCase()
                        + fieldName.substring(1);

                //创建set方法对象
                Method set = objClass.getMethod(setMethod, get.getReturnType());

                //动态调用set方法赋值
                set.invoke(obj2, result);

            }

            //获得父类Class类
            objClass = objClass.getSuperclass();
        }


    }

}
