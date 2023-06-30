package com.example.demo.service;

import com.example.demo.config.RequestCountExceededException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Log4j2
public class RequestCountServiceImpl implements RequestCountService {

    private final ConcurrentHashMap<String, BlockingQueue<LocalDateTime>> store = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    @Value("${request-count-limit.count}")
    private int requestCount = 2;

    @Value("${request-count-limit.seconds}")
    private int seconds = 10;

    @Override
    public void registerRequest(String ip, String aValue) throws InterruptedException {

        String key = key(ip, aValue);

        BlockingQueue<LocalDateTime> queue;
        if (store.containsKey(key)) {
            queue = store.get(key);
        } else {
            lock.lock();
            if (!store.containsKey(key)) {
                queue = new LinkedBlockingQueue<>(requestCount + 1);
                store.put(key, queue);
            } else {
                queue = store.get(key);
            }
            lock.unlock();
        }

        LocalDateTime now = LocalDateTime.now();

        queue.put(now);
        if (queue.remainingCapacity() == 0) {
            LocalDateTime border = queue.take();
            LocalDateTime borderTime = now.minusSeconds(seconds);
            if (border.isAfter(borderTime)) {
                throw new RequestCountExceededException();
            }
        }

        log.info("QUEUE SIZE: {}, THREAD: {}", queue.size(), Thread.currentThread().getName());
    }

    private String key(String ip, String aValue) {

        return String.format("%s%s", ip, aValue);
    }
}
