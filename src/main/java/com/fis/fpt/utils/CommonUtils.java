package com.fis.fpt.utils;

import lombok.extern.log4j.Log4j2;
import org.quartz.JobDataMap;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Log4j2
public class CommonUtils {

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue != null && !pd.getName().equals("class")) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static Map<String, Class<?>> getPropertyTypes(Object source) {
        BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Map<String, Class<?>> propertyTypes = new HashMap<>();
        for (PropertyDescriptor pd : pds) {
            if (!pd.getName().equals("class")) {
                Class<?> propertyType = pd.getPropertyType();
                propertyTypes.put(pd.getName(), propertyType);
            }
        }
        return propertyTypes;
    }

    public static JobDataMap getJobDataMap(Object obj) {
        JobDataMap jobDataMap = new JobDataMap();

        String[] nonNull = CommonUtils.getNullPropertyNames(obj);
        BeanWrapper beanWrapperMap = new BeanWrapperImpl(obj);
        for (String att : nonNull) {
            jobDataMap.put(att, beanWrapperMap.getPropertyValue(att));
        }
        return jobDataMap;
    }

    public static <T> T convertJobDataMapObject(JobDataMap jobDataMap, Class<T> clazz) {
        T object;
        try {
            object = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
        BeanWrapper beanWrapper = new BeanWrapperImpl(object);
        for (Map.Entry<String, Object> entry : jobDataMap.entrySet()) {
            beanWrapper.setPropertyValue(entry.getKey(), entry.getValue());
        }
        return object;
    }

}
