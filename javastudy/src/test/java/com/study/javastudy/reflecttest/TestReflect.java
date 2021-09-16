package com.study.javastudy.reflecttest;

import io.lettuce.core.ScriptOutputType;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

public class TestReflect {

    /**
     * 实例化Class类对象的三种方法
     * 1. 类名.class
     * 2. 实例.getClass()
     * 3. Class.forName("类的路径")  一般采用这种形式
     * @throws Exception
     */
    @Test
    void test1() throws Exception {
        System.out.println("类名称： " + Person.class.getName());
        System.out.println("类名称： " + new Person().getClass().getName());
        System.out.println("类名称： " + Class.forName("com.study.javastudy.reflecttest.Person").getName());
    }

    /**
     * 获取一个对象的父类与实现的接口
     * @throws Exception
     */
    @Test
    void test2() throws Exception {
        Class<?> clazz = Class.forName("com.study.javastudy.reflecttest.Person");
        // 取得父类
        System.out.println("Person的父类为：" +  clazz.getSuperclass().getName());
        // 获取所有的接口
        Class<?>[] interfaces = clazz.getInterfaces();
        System.out.println("Person实现的接口有：");
        for(Class ins : interfaces) {
            System.out.println(ins.getName());
        }
    }

    /**
     * 通过反射机制实例化一个类的对象
     * @throws Exception
     */
    @Test
    void test3() throws Exception {
        Class<?> clazz = Class.forName("com.study.javastudy.reflecttest.Person");
        // 第一种方法，实例化默认构造方法
        Person person = (Person) clazz.newInstance(); //自9已废弃
        person.setAge(18);
        System.out.println(person);
        System.out.println("-------------------");
        // 第二种方法 取得全部的构造函数 使用构造函数赋值
        Constructor<?> cons[] = clazz.getConstructors();
        // 查看每个构造方法需要的参数
        for (int i = 0; i < cons.length; i++) {
            Class<?> clazzs[] = cons[i].getParameterTypes();
            System.out.print("cons[" + i + "] (");
            for (int j = 0; j < clazzs.length; j++) {
                if (j == clazzs.length - 1)
                    System.out.print(clazzs[j].getName());
                else
                    System.out.print(clazzs[j].getName() + ",");
            }
            System.out.println(")");
        }
        System.out.println("---------------------------");
        person = (Person) cons[0].newInstance();
        System.out.println(person);
        person = (Person) cons[1].newInstance( "Rollen", 20, (byte)1, true);
        System.out.println(person);
    }

    /**
     * 获取某个类的全部属性
     * @throws Exception
     */
     @Test
    void test4() throws Exception {
         Class<?> clazz = Class.forName("com.study.javastudy.reflecttest.Person");
         System.out.println("===============本类属性===============");
         // 取得本类的全部属性
         Field[] field = clazz.getDeclaredFields();
         for (int i = 0; i < field.length; i++) {
             // 权限修饰符
             int mo = field[i].getModifiers();
             String priv = Modifier.toString(mo);
             // 属性类型
             Class<?> type = field[i].getType();
             System.out.println(priv + " " + type.getName() + " " + field[i].getName() + ";");
         }

         System.out.println("==========实现的接口或者父类的属性==========");
         // 取得实现的接口或者父类的属性
         Field[] filed1 = clazz.getFields();
         for (int j = 0; j < filed1.length; j++) {
             // 权限修饰符
             int mo = filed1[j].getModifiers();
             String priv = Modifier.toString(mo);
             // 属性类型
             Class<?> type = filed1[j].getType();
             System.out.println(priv + " " + type.getName() + " " + filed1[j].getName() + ";");
         }
     }

    /**
     * 获取某个类的全部方法
     * @throws Exception
     */
    @Test
    void test5() throws Exception {
        Class<?> clazz = Class.forName("com.study.javastudy.reflecttest.Person");
        Method method[] = clazz.getMethods();
        for (int i = 0; i < method.length; ++i) {
            Class<?> returnType = method[i].getReturnType();
            Class<?> para[] = method[i].getParameterTypes();
            int temp = method[i].getModifiers();
            System.out.print(Modifier.toString(temp) + " ");
            System.out.print(returnType.getName() + "  ");
            System.out.print(method[i].getName() + " ");
            System.out.print("(");
            for (int j = 0; j < para.length; ++j) {
                System.out.print(para[j].getName() + " " + "arg" + j);
                if (j < para.length - 1) {
                    System.out.print(",");
                }
            }
            Class<?> exce[] = method[i].getExceptionTypes();
            if (exce.length > 0) {
                System.out.print(") throws ");
                for (int k = 0; k < exce.length; ++k) {
                    System.out.print(exce[k].getName() + " ");
                    if (k < exce.length - 1) {
                        System.out.print(",");
                    }
                }
            } else {
                System.out.print(")");
            }
            System.out.println();
        }
    }

    /**
     * 通过反射机制调用某个类的方法
     * @throws Exception
     */
    @Test
    void test6() throws Exception {
        Class<?> clazz = Class.forName("com.study.javastudy.reflecttest.Person");

        Method method = clazz.getMethod("reflectMethod1");
        method.invoke(clazz.getConstructor().newInstance());

        method = clazz.getMethod("reflectMethod2", String.class, int.class);
        method.invoke(clazz.getConstructor().newInstance(), "Damon", 18);
    }

    /**
     * 通过反射机制操作某个类的属性
     * @throws Exception
     */
    @Test
    void test7() throws Exception {
        Class<?> clazz = Class.forName("com.study.javastudy.reflecttest.Person");
        Object obj = clazz.getConstructor().newInstance();
        // 可以直接对 private 的属性赋值
        Field field = clazz.getDeclaredField("name");
        field.setAccessible(true);
        field.set(obj, "Java反射机制");
        System.out.println(field.get(obj));
    }

    /**
     * 在泛型为Integer的ArrayList中存放一个String类型的对象
     *  @throws Exception
     */
    @Test
    void test8() throws Exception {
        ArrayList<Integer> list = new ArrayList<>();
        Method method = list.getClass().getMethod("add", Object.class);
        method.invoke(list, "Java反射机制实例。");
        System.out.println(list.get(0));
    }

    /**
     * 通过反射取得并修改数组的信息
     *  @throws Exception
     */
    @Test
    void test9() {
        int[] temp = { 1, 2, 3, 4, 5 };
        Class<?> demo = temp.getClass().getComponentType();
        System.out.println("数组类型： " + demo.getName());
        System.out.println("数组长度  " + Array.getLength(temp));
        System.out.println("数组的第一个元素: " + Array.get(temp, 0));
        Array.set(temp, 0, 100);
        System.out.println("修改之后数组第一个元素为： " + Array.get(temp, 0));
    }

    /**
     *  反射机制的动态代理
     *
     * 在java中有三种类类加载器。
     *
     * 1）Bootstrap ClassLoader 此加载器采用c++编写，一般开发中很少见。
     *
     * 2）Extension ClassLoader 用来进行扩展类的加载，一般对应的是jrelibext目录中的类
     *
     * 3）AppClassLoader 加载classpath指定的类，是最常用的加载器。同时也是java中默认的加载器。
     *
     * 如果想要完成动态代理，首先需要定义一个InvocationHandler接口的子类，以完成代理的具体操作。
     *
     *  @throws Exception
     */
    @Test
    void test10() {
        MyInvocationHandler demo = new MyInvocationHandler();
        Subject sub = (Subject) demo.bind(new RealSubject());
        String info = sub.say("Rollen", 20);
        System.out.println(info);
    }

    /**
     *  将反射机制应用于工厂模式
     *
     * 对于普通的工厂模式当我们在添加一个子类的时候，就需要对应的修改工厂类。 当我们添加很多的子类的时候，会很麻烦。
     *
     * 现在我们利用反射机制实现工厂模式，可以在不修改工厂类的情况下添加任意多个子类。
     *
     * 但是有一点仍然很麻烦，就是需要知道完整的包名和类名，这里可以使用properties配置文件来完成。
     *
     *  @throws Exception
     */
    @Test
    void test11() {
        fruit f = Factory.getInstance("com.study.javastudy.reflecttest.Apple");
        if (f != null) {
            f.eat();
        }
    }
}

interface fruit {
    void eat();
}
class Apple implements fruit {
    public void eat() {
        System.out.println("Apple");
    }
}
class Orange implements fruit {
    public void eat() {
        System.out.println("Orange");
    }
}
class Factory {
    public static fruit getInstance(String ClassName) {
        fruit f = null;
        try {
            f = (fruit) Class.forName(ClassName).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }
}

//定义项目接口
interface Subject {
    String say(String name, int age);
}
// 定义真实项目
class RealSubject implements Subject {
    public String say(String name, int age) {
        return name + "  " + age;
    }
}
class MyInvocationHandler implements InvocationHandler {
    private Object obj = null;
    public Object bind(Object obj) {
        this.obj = obj;
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), this);
    }
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object temp = method.invoke(this.obj, args);
        return temp;
    }
}

class Person implements Serializable, Comparable {

    private static final long serialVersionUID = -2862585049955236662L;

    private String name = "Tom";

    protected Integer age = 1;

    private Byte sex = (byte) 1;

    Boolean isMarriage = true;

    public Person() {
    }

    public Person(String name, Integer age, Byte sex, Boolean isMarriage) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.isMarriage = isMarriage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public Boolean getMarriage() {
        return isMarriage;
    }

    public void setMarriage(Boolean marriage) {
        isMarriage = marriage;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", isMarriage=" + isMarriage +
                '}';
    }

    public void reflectMethod1() {
        System.out.println("Java 反射调用方法1");
    }

    public void reflectMethod2(String name, int age) {
        System.out.println("Java 反射调用方法2".concat(" name -> ").concat(name).concat(" age -> ").concat(age+ ""));
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
