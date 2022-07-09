## 前言

[Java并发编程之美笔记](https://www.yuque.com/docs/share/bbe8cc28-c98e-42d8-87df-8ea2b4f873de)

这几天看《Java并发编程之美》的时候又遇到了`ThradLocal`这个类，不得不说，这个类在平时很多场景都遇得到，所以对其进行一个系统性的学习，然后再输出成这篇博客。
那么，什么是ThreadLocal呢？
我们都知道，多线程访问同一个共享变量很容易出现并发问题，特别是当多个线程对同一个共享变量进行写入操作时。一般为了避免这种情况，我们会使用`synchronized`这个关键字对代码块加锁。但是这种方式一是会让没获取到锁的线程进行阻塞等待，二是需要使用者对锁有一定的了解，无疑提高了编程的难度。其实ThreadLocal 就可以做这件事情，虽然ThreadLocal 并不是为了解决这个问题而出现的。
ThreadLocal 是JDK 包提供的，它提供了线程本地变量，也就是如果你创建了一个ThreadLocal 变量，那么访问这个变量的每个线程都会有这个变量的一个本地副本。当多个线程操作这个变量时，实际操作的是自己本地内存里面的变量，从而避免了线程安全问题。如图所示：
![](https://pymjl.oss-cn-shanghai.aliyuncs.com/picgo/1656646273878-41083ca3-3c20-43f6-bb90-f63a07cb6d7c.jpeg)

## 快速开始

接下来我们就先用一个简单的样例给大家展示一下ThreadLocal的基本用法

```java
package cuit.pymjl.thradlocal;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/1 10:56
 **/
public class MainTest {
    static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    static void print(String str) {
        //打印当前线程中本地内存中本地变量的值
        System.out.println(str + " :" + threadLocal.get());
        //清除本地内存中的本地变量
        threadLocal.remove();
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                //设置线程1中本地变量的值
                threadLocal.set("thread1 local variable");
                //调用打印方法
                print("thread1");
                //打印本地变量
                System.out.println("after remove : " + threadLocal.get());
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                //设置线程1中本地变量的值
                threadLocal.set("thread2 local variable");
                //调用打印方法
                print("thread2");
                //打印本地变量
                System.out.println("after remove : " + threadLocal.get());
            }
        });

        t1.start();
        t2.start();
    }
}

```

运行结果如图所示：

![image.png](https://pymjl.oss-cn-shanghai.aliyuncs.com/picgo/1656646666529-2ef55aba-704d-4dc5-8c38-4a8522e76b67.png)

## ThreadLocal的原理

### ThreadLocal相关类图

我们先来看一下ThreadLocal 相关类的类图结构，如图所示：
![](https://pymjl.oss-cn-shanghai.aliyuncs.com/picgo/1656647235312-1b6afdef-0ef3-4cdd-9bf2-c7d8c2999a83.jpeg)
由该图可知， Thread 类中有一个threadLocals 和一个inheritableThreadLocals ， 它们都是ThreadLocalMap 类型的变量， 而ThreadLocalMap 是一个定制化的Hashmap 。在默认情况下， 每个线程中的这两个变量都为null ，只有当前线程第一次调用ThreadLocal 的set 或者get 方法时才会创建它们。其实每个线程的本地变量不是存放在ThreadLocal 实例里面，而是存放在调用线程的threadLocals 变量里面。也就是说， ThreadLocal 类型的本地变量存放在具体的线程内存空间中。ThreadLocal 就是一个工具壳，它通过set 方法把value 值放入调用线程的threadLocals 里面并存放起来， 当调用线程调用它的get 方法时，再从当前线程的threadLocals 变量里面将其拿出来使用。
如果调用线程一直不终止， 那么这个本地变量会一直存放在调用线程的threadLocals 变量里面，所以当不需要使用本地变量时可以通过调用ThreadLocal 变量的remove 方法，从当前线程的threadLocals 里面删除该本地变量。另外， Thread 里面的threadLocals 为何被设计为map 结构？很明显是因为每个线程可以关联多个ThreadLocal 变量。
接下来我们来看看ThreadLocal的set、get、以及remove的源码

### set

```java
    public void set(T value) {
        // 1.获取当前线程（调用者线程）
        Thread t = Thread.currentThread();
        // 2.以当前线程作为key值，去查找对应的线程变量，找到对应的map
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            // 3.如果map不为null，则直接添加元素
            map.set(this, value);
        } else {
            // 4.否则就先创建map，再添加元素
            createMap(t, value);
        }
    }
```

```java
    void createMap(Thread t, T firstValue) {
        /**
         * 这里是创建一个ThreadLocalMap，以当前调用线程的实例对象为key，初始值为value
         * 然后放入当前线程的Therad.threadLocals属性里面
         */
        t.threadLocals = new ThreadLocalMap(this, firstValue);
    }
```

```java
    ThreadLocalMap getMap(Thread t) {
        //这里就是直接获取调用线程的成员属性threadlocals
        return t.threadLocals;
    }
```

### get

```java
    public T get() {
        // 1.获取当前线程
        Thread t = Thread.currentThread();
        // 2.获取当前线程的threadlocals，即ThreadLocalMap
        ThreadLocalMap map = getMap(t);
        // 3.如果map不为null，则直接返回对应的值
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T)e.value;
                return result;
            }
        }
        // 4.否则，则进行初始化
        return setInitialValue();
    }
```

下面是`setInitialValue`的代码

```java
private T setInitialValue() {
    //初始化属性，其实就是null
    T value = initialValue();
    //获取当前线程
    Thread t = Thread.currentThread();
    //通过当前线程获取ThreadLocalMap
    ThreadLocalMap map = getMap(t);
    //如果map不为null，则直接添加元素
    if (map != null) {
        map.set(this, value);
    } else {
        //否则就创建，然后将创建好的map放入当前线程的属性threadlocals
        createMap(t, value);
    }
        //将当前ThreadLocal实例注册进TerminatingThreadLocal类里面
    if (this instanceof TerminatingThreadLocal) {
        TerminatingThreadLocal.register((TerminatingThreadLocal<?>) this);
    }
    return value;
}
```

这里我需要补充说明一下`TerminatingThreadLocal`。这个类是jdk11新出的，jdk8中并没有这个类，所以在网上很多源码分析中并未看见这个类的相关说明。
这个类我看了一下源码，其作用应该是避免ThreadLocal内存泄露的问题(感兴趣的可以去看看源码，若有错误，还请指正)。
这是官方对其的解释：

```java
/**
 * A thread-local variable that is notified when a thread terminates and
 * it has been initialized in the terminating thread (even if it was
 * initialized with a null value).
 * 一个线程局部变量，
 * 当一个线程终止并且它已经在终止线程中被初始化时被通知（即使它被初始化为一个空值）。
 */
```

### remove

```java
     public void remove() {
         //如果当前线程的threadLocals 变量不为空， 则删除当前线程中指定ThreadLocal 实例的本地变量。
         ThreadLocalMap m = getMap(Thread.currentThread());
         if (m != null) {
             m.remove(this);
         }
     }
```

### 小结

在每个线程内部都有一个名为threadLocals 的成员变量， 该变量的类型为Hash Map ， 其中key 为我们定义的ThreadLocal 变量的this 引用， value 则为我们使用set 方法设置的值。每个线程的本地变量存放在线程自己的内存变量threadLocals 中，如果当前线程一直不消亡， 那么这些本地变量会一直存在， 所以可能会造成内存溢出， 因此使用完毕后要记得调用ThreadLocal 的remove 方法删除对应线程的threadLocals 中的本地变量。

## ThreadLocal内存泄露

### 为什么会出现内存泄漏？

ThreadLocalMap使用ThreadLocal的弱引用作为key，如果一个ThreadLocal没有外部强引用来引用它，那么系统 GC 的时候，这个ThreadLocal势必会被回收，**这样一来，ThreadLocalMap中就会出现key为null的Entry**，就没有办法访问这些key为null的Entry的value，**如果当前线程再迟迟不结束的话，这些key为null的Entry的value就会一直存在一条强引用链：Thread Ref -> Thread -> ThreaLocalMap -> Entry -> value永远无法回收，造成内存泄漏。**
其实，ThreadLocalMap的设计中已经考虑到这种情况，也加上了一些防护措施：在ThreadLocal的get(),set(),remove()的时候都会清除线程ThreadLocalMap里所有key为null的value。
但是这些被动的预防措施并不能保证不会内存泄漏：

- 使用static的ThreadLocal，延长了ThreadLocal的生命周期，可能导致的内存泄漏
- 分配使用了ThreadLocal又不再调用get(),set(),remove()方法，那么就会导致内存泄漏

### 为什么使用弱引用？

既然我们都知道，使用了弱引用会造成ThreadLocalMap内存泄漏，那么官方为什么依然使用弱引用而不是强引用呢？这就要从使用弱引用和强引用的区别来说起了：

1. 如果使用强引用：我们知道，ThreadLocalMap的生命周期基本和Thread的生命周期一样，当前线程如果没有终止，那么ThreadLocalMap始终不会被GC回收，而ThreadLocalMap持有对ThreadLocal的强引用，那么ThreadLocal也不会被回收，当线程生命周期长，如果没有手动删除，则会造成kv累积，从而导致OOM
1. 如果使用弱引用：弱引用中的对象具有很短的声明周期，因为在系统GC时，只要发现弱引用，不管堆空间是否足够，都会将对象进行回收。而当ThreadLocal的强引用被回收时，ThreadLocalMap所持有的弱引用也会被回收，如果没有手动删除kv，那么会造成value累积，也会导致OOM

对比可知，使用弱引用至少可以保证不会因为map的key累积从而导致OOM，而对应的value可以通过remove，get，set方法在下一次调用时被清除。可见，内存泄露的根源不是弱引用，而是ThreadLocalMap的生命周期和Thread一样长，造成累积导致的

### 解决方法

既然问题的根源是value的累积造成OOM，那么我们对症下药，每次使用完ThreadLocal调用`remove()`方法清理掉就行了。

