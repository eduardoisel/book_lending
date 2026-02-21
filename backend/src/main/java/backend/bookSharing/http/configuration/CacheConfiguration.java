package backend.bookSharing.http.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import org.springframework.boot.cache.autoconfigure.CacheManagerCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Uses {@link org.springframework.boot.cache.autoconfigure.GenericCacheConfiguration} if not defined
 */
@Configuration
@EnableCaching
public class CacheConfiguration {

    /*
    Allows to configure the cache separately for organization
     */
    @Bean
    public CacheManagerCustomizer<@NonNull CaffeineCacheManager> cacheManagerCustomizer() {
        return (cacheManager) -> cacheManager.setAllowNullValues(false);
    }

    static List<String> cacheNames = new LinkedList<>();

    static {
        cacheNames.add("getBooks");
    }


    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(cacheNames); // Define the cache names
        cacheManager.setCaffeine(caffeineConfig());
        return cacheManager;
    }

    public Caffeine<Object,Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES) // Cache entries expire after 15 minutes
                .maximumSize(100) // Maximum of 100 entries in the cache
                .recordStats(); // For monitoring cache statistics (optional)
    }


}
