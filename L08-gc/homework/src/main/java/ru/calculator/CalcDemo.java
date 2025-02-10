package ru.calculator;

/*
-Xms256m
-Xmx256m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/heapdump.hprof
-XX:+UseG1GC
-Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
*/

/*              |   до                       |   после
   обьм памяти  | время выполнения           | время выполнения
   256          | spend msec:173876, sec:173 | spend msec:145605, sec:145
   512          | spend msec:172149, sec:172 | spend msec:150157, sec:150
   768          | spend msec:166318, sec:166 | spend msec:144647, sec:144
   1024         | spend msec:167678, sec:167 | spend msec:136247, sec:136
   1280         | spend msec:161860, sec:161 | spend msec:145733, sec:145
   1536         | spend msec:166881, sec:166 | spend msec:136871, sec:136
   2048         | spend msec:163508, sec:163 | spend msec:141373, sec:141
*/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class CalcDemo {
    private static final Logger log = LoggerFactory.getLogger(CalcDemo.class);

    public static void main(String[] args) {
        long counter = 500_000_000;
        var summator = new Summator();
        long startTime = System.currentTimeMillis();

        for (var idx = 0; idx < counter; idx++) {
            var data = new Data(idx);
            summator.calc(data);

            if (idx % 10_000_000 == 0) {
                log.info("{} current idx:{}", LocalDateTime.now(), idx);
            }
        }

        long delta = System.currentTimeMillis() - startTime;
        log.info("PrevValue:{}", summator.getPrevValue());
        log.info("PrevPrevValue:{}", summator.getPrevPrevValue());
        log.info("SumLastThreeValues:{}", summator.getSumLastThreeValues());
        log.info("SomeValue:{}", summator.getSomeValue());
        log.info("Sum:{}", summator.getSum());
        log.info("spend msec:{}, sec:{}", delta, (delta / 1000));
    }
}
