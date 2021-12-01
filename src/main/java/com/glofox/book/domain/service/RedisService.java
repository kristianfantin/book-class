package com.glofox.book.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class RedisService {

    private static Logger log = Logger.getLogger(RedisService.class.getName());

    private final CacheManager cacheManager;

    public void clean() {
        cacheManager.getCacheNames()
                .parallelStream()
                .forEach(n -> Objects.requireNonNull(cacheManager.getCache(n)).clear());
        log.log(configLevel(), "<<cleaned cache>>");
    }

    private Level configLevel() {
        return Level.INFO;
    }

}
