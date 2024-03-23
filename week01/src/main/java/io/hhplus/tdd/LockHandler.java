package io.hhplus.tdd;

import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class LockHandler {

    private final ConcurrentHashMap<Long, Lock> lockMap = new ConcurrentHashMap<>();

    public boolean tryLock(Long userId) throws InterruptedException {
        Lock lock = lockMap.computeIfAbsent(userId, key -> new ReentrantLock());
        return lock.tryLock(5, TimeUnit.SECONDS);
    }

    public void unlock(Long userId) {
        Lock lock = lockMap.get(userId);
        if (lock != null) {
            lock.unlock();
        }
    }

    public <T> T withLock(Long userId, Callable<T> task) throws Exception {
        if (!tryLock(userId)) {
            throw new RuntimeException("Could not acquire the lock");
        }

        try {
            return task.call();
        } finally {
            unlock(userId);
        }
    }
}
