package ru.otus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        runTests();
    }

    private static void runTests() {
        int passed = 0;
        int failed = 0;
        int total = 0;

        try {
            Method[] methods = TestClass.class.getDeclaredMethods();

            List<Method> beforeMethods = new ArrayList<>();
            List<Method> testMethods = new ArrayList<>();
            List<Method> afterMethods = new ArrayList<>();

            for (Method method : methods) {
                if (method.isAnnotationPresent(Before.class)) {
                    beforeMethods.add(method);
                } else if (method.isAnnotationPresent(Test.class)) {
                    testMethods.add(method);
                } else if (method.isAnnotationPresent(After.class)) {
                    afterMethods.add(method);
                }
            }

            for (Method testMethod : testMethods) {
                Object testInstance = TestClass.class.getDeclaredConstructor().newInstance();
                try {
                    for (Method beforeMethod : beforeMethods) {
                        beforeMethod.invoke(testInstance);
                    }

                    total++;
                    testMethod.invoke(testInstance);
                    passed++;

                } catch (Exception e) {
                    failed++;
                    System.out.println("Test " + testMethod.getName() + " failed: " + e.getCause());
                } finally {
                    for (Method afterMethod : afterMethods) {
                        afterMethod.invoke(testInstance);
                    }
                }
            }

            System.out.println("Total tests run: " + total);
            System.out.println("Passed: " + passed);
            System.out.println("Failed: " + failed);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
