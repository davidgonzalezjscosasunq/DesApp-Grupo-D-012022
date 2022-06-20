package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.aspects.ThrowableSupplier;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.clock.Clock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CacheService {

    @Autowired
    Clock clock;

    private Map<String, CacheEntry> cache = new HashMap<>();

    public Object handle(String key, ThrowableSupplier valueSupplier, Long validityDurationInSeconds) throws Throwable {
        var now = clock.now();

        if (shouldUpdateValue(key, now)) {
            var expirationLocalDate = now.plusSeconds(validityDurationInSeconds);
            var value = valueSupplier.get();

            save(key, value, expirationLocalDate);

            return value;
        }
        else {
            return find(key);
        }
    }

    public Object handle1231231(ProceedingJoinPoint joinPoint, Long validityDurationInSeconds) throws Throwable {
        var key = joinPoint.getSignature().toString();
        var now = clock.now();

        if (shouldUpdateValue(key, now)) {
            var expirationLocalDate = now.plusSeconds(validityDurationInSeconds);
            var value = joinPoint.proceed();

            save(key, value, expirationLocalDate);

            return value;
        }
        else {
            return find(key);
        }
    }

    private CacheEntry save(String key, Object objectToSave, LocalDateTime expirationLocalDate) {
        return cache.put(key, new CacheEntry(expirationLocalDate, objectToSave));
    }

    private Object find(String key) {
        return cache.get(key).value();
    }

    private boolean shouldUpdateValue(String key, LocalDateTime now) {
        return !hasKey(key) || isKeyExpiredOn(key, now);
    }

    private boolean hasKey(String key) {
        return cache.containsKey(key);
    }

    private boolean isKeyExpiredOn(String key, LocalDateTime now) {
        return cache.get(key).expiredOn(now);
    }

}

class CacheEntry {

    private LocalDateTime expirationLocalDate;
    private Object value;

    public CacheEntry(LocalDateTime expirationLocalDate, Object value) {
        this.expirationLocalDate = expirationLocalDate;
        this.value = value;
    }

    public Object value() {
        return value;
    }

    public boolean expiredOn(LocalDateTime dateToCheck) {
        return dateToCheck.isAfter(expirationLocalDate);
    }

}