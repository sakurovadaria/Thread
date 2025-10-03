package com.Thread.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class MathServiceImpl implements MathService {

    private static final Logger logger = LoggerFactory.getLogger(MathServiceImpl.class);

    @Override
    public long calculateSum() {
        logger.info("Вычисление суммы чисел от 1 до 1_000_000");

        return Stream.iterate(1L, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0L, Long::sum);
    }


//        быстрый способ (по формуле)
//        int n = 1_000_000;
//        return n * (n + 1) / 2;
}
