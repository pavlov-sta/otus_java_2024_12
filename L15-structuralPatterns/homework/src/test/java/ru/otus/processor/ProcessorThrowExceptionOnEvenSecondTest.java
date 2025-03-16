package ru.otus.processor;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProcessorThrowExceptionOnEvenSecondTest {

    @Test
    void shouldThrowExceptionOnEvenSecond() {

        DateTimeProvider dateTimeProvider = () -> LocalDateTime.of(2024, 3, 16, 12, 0, 2);
        Processor processor = new ProcessorThrowExceptionOnEvenSecond(dateTimeProvider);

        assertThrows(RuntimeException.class, () -> processor.process(initMessage()));
    }

    @Test
    void shouldNotThrowExceptionOnOddSecond() {
        DateTimeProvider dateTimeProvider = () -> LocalDateTime.of(2024, 3, 16, 12, 0, 3);
        Processor processor = new ProcessorThrowExceptionOnEvenSecond(dateTimeProvider);

        assertDoesNotThrow(() -> processor.process(initMessage()));
    }

    private Message initMessage() {
        return new Message.Builder(1L).build();
    }
}
