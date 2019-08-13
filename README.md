# Redission 分布式锁实践

* 在测试的过程中，如果使用了Thread.sleep(),来模拟程序中断的话，在具体的业务中，这个时候线程的锁会被释放，导致在业务最后去释放锁的时候找不到key
* 在写自定义锁的注解的时候，如果不做任何配置，会造成在使用Aspect的地方不能注入service或者dao，分析院系是因为CGlib代理和jdk代理冲突造成的，具体报错是所注入的service和dao不是CGlib的代理类，这个时候我们可以修改配置，改变aop代理方式，下面是具体的配置：
~~~yaml
spring:
    aop:
      proxy-target-class: false
      auto: false
~~~
 * 在不能注入服务层和dao的情况下还有可能是切面的加载顺序，这个时候可以指定加载顺序order(0)
 * 另外在本示例中，在主内中添加 **@EnableAspectJAutoProxy(proxyTargetClass = true)**没有解决代理冲突问题