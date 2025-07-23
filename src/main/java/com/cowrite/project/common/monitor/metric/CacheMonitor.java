package com.cowrite.project.common.monitor.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Component
public class CacheMonitor {

    private static final Logger logger = LoggerFactory.getLogger(CacheMonitor.class);

    private final RedisTemplate<String, Object> redisTemplate;
    private final MeterRegistry meterRegistry;

    private Counter cacheHits;
    private Counter cacheMisses;
    private Timer cacheGetTimer;
    private Gauge redisMemoryUsed;
    private Gauge redisClientsConnected;
    private Gauge cacheHitRateGauge;

    public CacheMonitor(RedisTemplate<String, Object> redisTemplate, MeterRegistry meterRegistry) {
        this.redisTemplate = redisTemplate;
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        cacheHits = Counter.builder("cache.hits")
                .description("缓存命中次数")
                .register(meterRegistry);

        cacheMisses = Counter.builder("cache.misses")
                .description("缓存未命中次数")
                .register(meterRegistry);

        cacheGetTimer = Timer.builder("cache.get.time")
                .description("缓存获取耗时")
                .register(meterRegistry);

        // 初始化Redis相关的Gauge
        redisMemoryUsed = Gauge.builder("redis.memory.used", () -> getRedisMemoryUsed())
                .description("Redis已使用的内存")
                .register(meterRegistry);

        redisClientsConnected = Gauge.builder("redis.clients.connected", () -> getRedisClientsConnected())
                .description("Redis当前连接的客户端数")
                .register(meterRegistry);

        // 使用匿名函数传递缓存命中率的计算方式
        cacheHitRateGauge = Gauge.builder("cache.hit.rate", this, monitor -> calculateHitRate(monitor.cacheHits.count(), monitor.cacheMisses.count()))
                .description("缓存命中率")
                .register(meterRegistry);
    }

    /**
     * 监控缓存状态
     */
    @Scheduled(fixedRate = 60000)
    public void monitorCacheStatus() {
        try {
            Properties info = redisTemplate.getConnectionFactory()
                    .getConnection()
                    .info();

            // 更新Redis内存使用和连接的客户端数的Gauge
            redisMemoryUsed.measure();
            redisClientsConnected.measure();

            // 记录命中率
            double hitRate = calculateHitRate(cacheHits.count(), cacheMisses.count());
            logger.info("缓存命中率: {}%", hitRate);
        } catch (Exception e) {
            logger.error("监控缓存状态时发生错误", e);
        }
    }

    /**
     * 记录缓存操作
     */
    public void recordCacheOperation(boolean isHit, long responseTime) {
        if (isHit) {
            cacheHits.increment();
        } else {
            cacheMisses.increment();
        }
        cacheGetTimer.record(responseTime, TimeUnit.MILLISECONDS);
    }

    private double calculateHitRate(double hits, double misses) {
        double total = hits + misses;
        return total == 0 ? 0 : (hits / total) * 100;
    }

    private double getRedisMemoryUsed() {
        try {
            Properties info = redisTemplate.getConnectionFactory()
                    .getConnection()
                    .info();
            String usedMemory = info.getProperty("used_memory");
            return usedMemory != null ? Double.valueOf(usedMemory) : 0;
        } catch (Exception e) {
            logger.error("获取Redis内存使用信息失败", e);
            return 0;
        }
    }

    private int getRedisClientsConnected() {
        try {
            Properties info = redisTemplate.getConnectionFactory()
                    .getConnection()
                    .info();
            String connectedClients = info.getProperty("connected_clients");
            return connectedClients != null ? Integer.valueOf(connectedClients) : 0;
        } catch (Exception e) {
            logger.error("获取Redis连接数失败", e);
            return 0;
        }
    }
}
