package util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {
    public static Object get(Object object, Field field) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String getFieldName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        Method method = object.getClass().getMethod(getFieldName);
        return method.invoke(object);
    }
}
