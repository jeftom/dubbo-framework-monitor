package com.framework.utils;

import com.framework.utils.model.TestUserModel;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李溪林 on 17-2-16.
 */
public class TypeUtilsTest {
    @Test
    public void getFields(){
        Field[] fields = TypeUtils.getFields(TestUserModel.class, true);
        for(Field item : fields){
            System.out.println(item.getName());
        }
    }

    @Test
    public void getField() {
        PropertyDescriptor prop = TypeUtils.getPropertyDescriptor(TestUserModel.class, "name");
        Field field = TypeUtils.getField(TestUserModel.class, prop);
    }

    @Test
    public void getField2(){
        Field field = TypeUtils.getField(TestUserModel.class, "name");
        System.out.println(field.getName());
    }

    @Test
    public void getPropertyDescriptor() {
        PropertyDescriptor prop = TypeUtils.getPropertyDescriptor(TestUserModel.class, "name");
    }

    @Test
    public void getAllowGetPropertyDescriptors() {
        PropertyDescriptor[] props = TypeUtils.getAllowGetPropertyDescriptors(TestUserModel.class, false);
        for(PropertyDescriptor item : props){
            System.out.println(item.getName());
        }
    }

    @Test
    public void isAssignable() {
        Class c1 = String.class;
        Class c2 = Double.class;

        boolean r1 = TypeUtils.isAssignable(c1, c2);
        System.out.println("结果1:" + r1);

        Class c3 = List.class;
        Class c4 = ArrayList.class;

        boolean r2 = c3.isAssignableFrom(c4);
        System.out.println("结果2:" + r2);
        System.out.println("结果22:" + TypeUtils.isAssignable(c3, c4));
        System.out.println("结果222:" + TypeUtils.isAssignable(c4, c3));

        Class c5 = Boolean.class;
        Class c6 = boolean.class;

        boolean r3 = c5.isPrimitive();
        boolean r4 = c6.isPrimitive();
        System.out.println("结果3:" + r3);
        System.out.println("结果4:" + r4);

        boolean r5 = TypeUtils.isAssignable(c5, c6);
        System.out.println("结果5:" + r5);

        boolean r6 = TypeUtils.isAssignable(c6, c5);
        System.out.println("结果6:" + r6);
    }

    @Test
    public void getConstructorWithoutParams(){

        Class<Integer> c1 = Integer.class;

        Class<Integer> c2 = int.class;

        Class<TestUserModel> c3 = TestUserModel.class;
        System.out.println(TypeUtils.getConstructorWithoutParams(c1));//取 Integer 的公共无参构造
        System.out.println(TypeUtils.getConstructorWithoutParams(c2));//取 int 的公共无参构造
        System.out.println(TypeUtils.getConstructorWithoutParams(c3));//取 TestUserModel 的公共无参构造
    }

    @Test
    public void getNewInstance(){

        Class<Integer> c1 = Integer.class;
        Class<Integer> c2 = int.class;
        Class<TestUserModel> c3 = TestUserModel.class;

        Integer r1 = TypeUtils.getNewInstance(c1);//取 Integer 的新对象
        System.out.println(r1);

        int r2 = TypeUtils.getNewInstance(c2);//取 int 的新对象(其实是取Integer的新对象)
        System.out.println(r2);

        TestUserModel r3 = TypeUtils.getNewInstance(c3);//取 TestUserModel 的新对象
        System.out.println(r3);
    }
}
