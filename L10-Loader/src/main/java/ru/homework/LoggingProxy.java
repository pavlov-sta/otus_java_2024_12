package ru.homework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.logging.Logger;

public class LoggingProxy {
    private static final Logger logger = Logger.getLogger(LoggingProxy.class.getName());

    @SuppressWarnings("unchecked")
    public static <T> T create(T target) {
        Class<?> targetClass = target.getClass();
        InvocationHandler handler = new LogInvocationHandler(target);
        return (T) Proxy.newProxyInstance(targetClass.getClassLoader(), targetClass.getInterfaces(), handler);
    }

    private record LogInvocationHandler(Object target) implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
            if (targetMethod.isAnnotationPresent(Log.class)) {
                logger.info("executed method: " + method.getName() + ", params: " + Arrays.toString(args));
            }
            return method.invoke(target, args);
        }
    }
}
