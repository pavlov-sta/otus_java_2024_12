package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorThrowExceptionOnEvenSecond implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public ProcessorThrowExceptionOnEvenSecond(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        int second = dateTimeProvider.getDate().getSecond();
        if (second % 2 == 0) {
            throw new RuntimeException("Even second: " + second);
        }
        return message;
    }
}
