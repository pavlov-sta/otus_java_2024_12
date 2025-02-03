package ru.otus;

public class SecondTestClass {

    @Before
    public void init() {
        System.out.println("Before up");
    }

    @Test
    public void test_1() {
        System.out.println("Running test-1");
        System.out.println("Successful");
    }

    @Test
    public void test_2() {
        System.out.println("Running test-2");
        throw new RuntimeException("Test-2 failed");
    }

    @Test
    public void test_3() {
        System.out.println("Running test-3:");
        var g = 1 / 0;
        System.out.println("It should not be completed");
    }

    @Test
    public void test_4() {
        var e = 2 + 2;
        System.out.println("Running test-4: " + e);
        System.out.println("Successful");
    }

    @After
    public void deleteAll() {
        System.out.println("After down");
    }
}