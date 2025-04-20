package ru.otus.appcontainer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        Method[] methods = configClass.getMethods();
        List<Method> appComponentMethods = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparing(
                        method -> method.getAnnotation(AppComponent.class).order()))
                .toList();

        try {
            Object appComponentInstance = configClass.getDeclaredConstructor().newInstance();

            for (Method method : appComponentMethods) {
                createComponent(appComponentInstance, method);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to process config class", e);
        }
    }

    private void createComponent(Object configInstance, Method method) throws Exception {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] parameters =
                Arrays.stream(parameterTypes).map(this::getAppComponent).toArray();

        Object componentInstance = method.invoke(configInstance, parameters);
        String componentName = method.getAnnotation(AppComponent.class).name();

        if (appComponentsByName.containsKey(componentName)) {
            throw new IllegalArgumentException("Duplicate component name: " + componentName);
        }

        appComponents.add(componentInstance);
        appComponentsByName.put(componentName, componentInstance);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<C> components = appComponents.stream()
                .filter(componentClass::isInstance)
                .map(componentClass::cast)
                .toList();

        if (components.size() != 1) {
            throw new NoSuchElementException("Expected exactly one component " + componentClass.getName());
        }

        return components.get(0);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return Optional.ofNullable((C) appComponentsByName.get(componentName))
                .orElseThrow(() -> new NoSuchElementException("Component not found: " + componentName));
    }
}
