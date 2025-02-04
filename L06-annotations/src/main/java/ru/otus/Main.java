package ru.otus;

public class Main {
    public static void main(String[] args) {

        System.out.println("Running FirstTestClass");
        TestRunner.runTests(FirstTestClass.class);

        System.out.println();
        System.out.println("Running SecondTestClass");
        TestRunner.runTests(SecondTestClass.class);
    }
}
