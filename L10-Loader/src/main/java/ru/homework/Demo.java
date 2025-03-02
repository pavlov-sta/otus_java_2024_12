package ru.homework;

public class Demo {
    public void action() {
        TestLoggingInterface testLoggingInterface = LoggingProxy.create(new TestLogging());
        testLoggingInterface.calculation(6);
        testLoggingInterface.calculation(6, 7);

        TestLoggingSecondInterface testLoggingSecondInterface = LoggingProxy.create(new TestLoggingSecond());
        testLoggingSecondInterface.calculation(6);
    }
}
