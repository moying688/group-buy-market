package com.moying.config;

import com.moying.types.annotations.DCCValue;
import com.moying.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: moying
 * @CreateTime: 2025-05-06
 * @Description:
 */


@Slf4j
@Configuration
public class DCCValueBeanFactory implements BeanPostProcessor {

    private static final String BASE_CONFIG_PATH = "group_buy_market_dcc_";

    private final RedissonClient redissonClient;


    private final Map<String, Object> dccObjGroup = new HashMap<>();
    public DCCValueBeanFactory(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Bean("dccTopic")
    public RTopic testRedisTopicListener(RedissonClient redissonClient){
        RTopic topic = redissonClient.getTopic("group_buy_market_dcc");
        topic.addListener(String.class,((charSequence, s) -> {
            String[] split = s.split(Constants.SPLIT);
            // 获取值
            String attribute = split[0];
            String key = BASE_CONFIG_PATH.concat(attribute);
            String value = split[1];
            // 设置值
            RBucket<String> bucket = redissonClient.getBucket(key);
            boolean exists = bucket.isExists();
            if(!exists) return ;
            bucket.set(value);
            Object objBean = dccObjGroup.get(key);
            if(null == objBean) return;

            // 获取对象的类
            Class<?> objBeanClass  = objBean.getClass();
            // 检查 objBean 是否是代理对象
            if (AopUtils.isAopProxy(objBean)) {
                // 获取代理对象的目标对象
                objBeanClass = AopUtils.getTargetClass(objBean);
            }

            try{
                // 1. getDeclaredField 方法用于获取指定类中声明的所有字段，包括私有字段、受保护字段和公共字段。
                // 2. getField 方法用于获取指定类中的公共字段，即只能获取到公共访问修饰符（public）的字段。
                Field field = objBeanClass.getDeclaredField(attribute);
                field.setAccessible(true);
                field.set(objBean, value);
                field.setAccessible(false);

                log.info("DCC 节点监听，动态设置值 {} {}", key, value);

            }catch (Exception e) {
                throw new RuntimeException(e);
            }


        }));
        return topic;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName){

        Class<?> targetBeanClass = bean.getClass();
        Object targetBeanObject = bean;
        // 如果是代理对象，获取目标类和目标对象
        if(AopUtils.isAopProxy(bean)){
            targetBeanClass = AopUtils.getTargetClass(bean);
            targetBeanObject = AopProxyUtils.getSingletonTarget(bean);
        }

        // 获取所有字段
        Field[] fields = targetBeanClass.getDeclaredFields();
        for(Field field: fields){
            if(!field.isAnnotationPresent(DCCValue.class)){
                continue;
            }

            // 获取字段的值
            DCCValue dccValue = field.getAnnotation(DCCValue.class);
            String value = dccValue.value();

            if (StringUtils.isBlank(value)) {
                throw new RuntimeException(field.getName() + " @DCCValue is not config value config case 「isSwitch/isSwitch:1」");
            }

            String[] split = value.split(":");
            String key = BASE_CONFIG_PATH.concat(split[0]);
            String defaultValue = split.length == 2 ? split[1] : null;

            // 设置值
            String setValue = defaultValue;
            try{

                // 如果为空则抛出异常
                if (StringUtils.isBlank(defaultValue)) {
                    throw new RuntimeException("dcc config error " + key + " is not null - 请配置默认值！");
                }

                // Redis操作,
                RBucket<String> bucket = redissonClient.getBucket(key);
                boolean exists = bucket.isExists();
                if(!exists){
                    bucket.set(defaultValue);
                }else{
                    setValue = bucket.get();
                }

                field.setAccessible(true);
                field.set(targetBeanObject, setValue);
                field.setAccessible(false);

            }catch (Exception e) {
                throw new RuntimeException(e);
            }
            dccObjGroup.put(key, targetBeanObject);
        }
        return bean;
    }
}
