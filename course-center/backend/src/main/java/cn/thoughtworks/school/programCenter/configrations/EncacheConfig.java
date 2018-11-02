package cn.thoughtworks.school.programCenter.configrations;

import cn.thoughtworks.school.programCenter.services.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
@EnableCaching
@EnableScheduling
public class EncacheConfig {
    @Autowired
    private ProgramService programService;
    private static final String LEADER_BOARD_CACHE = "LEADER_BOARD_CACHE";
    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager(LEADER_BOARD_CACHE);

        return cacheManager;
    }

    @CachePut(LEADER_BOARD_CACHE)
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 6 ,  initialDelay = 500)
    public List reportCachePut() {
        return programService.cacheLeaderBoard();
    }
}