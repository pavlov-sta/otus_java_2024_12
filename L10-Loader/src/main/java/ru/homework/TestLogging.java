package ru.homework;

public class TestLogging implements TestLoggingInterface {

    @Log
    @Override
    public void calculation(int param) {
    }

    @Log
    @Override
    public void calculation(int param, int param2) {
    }
}
