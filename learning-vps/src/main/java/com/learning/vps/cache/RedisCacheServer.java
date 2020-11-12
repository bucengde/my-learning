package com.learning.vps.cache;

import com.learning.commons.exception.RuntimeServerException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Wang Xu
 * @date 2020/10/20
 */
@Slf4j
@Service
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class RedisCacheServer implements RedisOption {
    private static final String ERROR_CODE = "REDIS_SERVER_ERROR";
    private static final String ERROR_MSG = "redis服务错误";
    private static final long DEFAULT_EXPIRE_TIME = 1000 * 60 * 30;
    private static final TimeUnit DEFAULT_EXPIRE_TIME_UNIT = TimeUnit.MILLISECONDS;

    private final RedisTemplate redisTemplate;
    @Getter
    public final RedisStringCacheServer redisStringCacheServer;
    @Getter
    public final RedisHashCacheServer redisHashCacheServer;
    @Getter
    public final RedisListCacheServer redisListCacheServer;
    @Getter
    public final RedisSetCacheServer redisSetCacheServer;
    @Getter
    public final RedisSortedSetCacheServer redisSortedSetCacheServer;

    public RedisCacheServer(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisStringCacheServer = new RedisStringCacheServer();
        this.redisHashCacheServer = new RedisHashCacheServer();
        this.redisListCacheServer = new RedisListCacheServer();
        this.redisSetCacheServer = new RedisSetCacheServer();
        this.redisSortedSetCacheServer = new RedisSortedSetCacheServer();
    }

    @Override
    public Boolean transferDb(int db) {
        if (0 > db || db > 15) {
            throw new RuntimeServerException("REDIS_DB_SET_ERROR", "db should between 0 and 15.");
        }
        LettuceConnectionFactory connectionFactory = (LettuceConnectionFactory)redisTemplate.getConnectionFactory();
        Optional.ofNullable(connectionFactory).ifPresent(cf -> {
            if(db == cf.getDatabase()) {
                return;
            }
            cf.setDatabase(db);
            // 是否允许多个线程操作共用同一个缓存连接，默认 true，false 时每个操作都将开辟新的连接
            cf.setShareNativeConnection(false);
            redisTemplate.setConnectionFactory(cf);
            cf.resetConnection();
        });
        return true;
    }

    @Override
    public Boolean deleteFromRedis(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public void deleteListFromRedis(final String... keys) {
        Optional.ofNullable(keys).ifPresent((ks) -> {
            Set kss = null;
            Long count = redisTemplate.delete(kss = Stream.of(keys).collect(Collectors.toSet()));
            log.info("删除redis缓存中key为：{} 的缓存成功", StringUtils.collectionToCommaDelimitedString(kss));
        });
    }

    @Override
    public void expireKeyAt(String key, Date date) {
        Optional.ofNullable(date).ifPresent(d -> redisTemplate.expireAt(key, d));
    }

    public void expire(String key) {
        expire(key, null, null);
    }

    @Override
    public void expire(String key, Long timeout, TimeUnit unit) {
        if(Objects.isNull(timeout)) {
            redisTemplate.expire(key, DEFAULT_EXPIRE_TIME, DEFAULT_EXPIRE_TIME_UNIT);
            return;
        }
        redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public void persistKey(String key) {
        Optional.ofNullable(key).ifPresent(k -> redisTemplate.persist(k));
    }

    public class RedisStringCacheServer implements RedisStringOption {
        @Override
        public Integer stringAppendString(String key, String value) {
            return redisTemplate.opsForValue().append(key, value);
        }

        @Override
        public Object stringGetStringByKey(String key) {
            return (redisTemplate.opsForValue().get(key));
        }

        @Override
        public String stringGetSubStringFromString(String key, long start, long end) {
            return redisTemplate.opsForValue().get(key, start, end);
        }

        @Override
        public Long stringIncrementLongString(String key, Long incrBy) {
            return redisTemplate.opsForValue().increment(key, incrBy);
        }

        @Override
        public Double stringIncrementDoubleString(String key, Double incrBy) {
            return redisTemplate.opsForValue().increment(key, incrBy);
        }

        @Override
        public void stringSetString(String key, Object value) {
            redisTemplate.opsForValue().set(key, value);
        }

        @Override
        public Object stringGetAndSet(String key, Object value) {
            return redisTemplate.opsForValue().getAndSet(key, value);
        }

        @Override
        public void stringSetValueAndExpireTime(String key, String value, long timeout) {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
        }
    }

    public class RedisHashCacheServer implements RedisHashOption {
        @Override
        public Boolean hashCheckExists(String hashKey, String fieldKey) {
            return redisTemplate.opsForHash().hasKey(hashKey, fieldKey);
        }

        @Override
        public Object hashGet(String hashKey, String fieldKey) {
            return redisTemplate.opsForHash().get(hashKey, fieldKey);
        }

        @Override
        public Map<Object, Object> hashGetAll(String hashKey) {
            return redisTemplate.opsForHash().entries(hashKey);
        }

        @Override
        public Long hashIncrementLongOfHashMap(String hashKey, String fieldKey, Long incrBy) {
            return redisTemplate.opsForHash().increment(hashKey, fieldKey, incrBy);
        }

        @Override
        public Double hashIncrementDoubleOfHashMap(String hashKey, String fieldKey, Double incrBy) {
            return redisTemplate.opsForHash().increment(hashKey, fieldKey, incrBy);
        }

        @Override
        public void hashPushHashMap(String hashKey, String fieldKey, Object value) {
            redisTemplate.opsForHash().put(hashKey, fieldKey, value);
        }

        @Override
        public Set hashGetAllHashKey(String hashKey) {
            return redisTemplate.opsForHash().keys(hashKey);
        }

        @Override
        public Long hashGetHashMapSize(String hashKey) {
            return redisTemplate.opsForHash().size(hashKey);
        }

        @Override
        public List hashGetHashAllValues(String hashKey) {
            return redisTemplate.opsForHash().values(hashKey);
        }

        @Override
        public Long hashDeleteHashKey(String hashKey, Object... fieldKeys) {
            return redisTemplate.opsForHash().delete(hashKey, fieldKeys);
        }
    }

    public class RedisListCacheServer implements RedisListOption {
        @Override
        public void listRightPushList(String key, String value) {
            redisTemplate.opsForList().rightPush(key, value);
        }

        @Override
        public Object listRightPopList(String key) {
            return redisTemplate.opsForList().rightPop(key);
        }

        @Override
        public void listLeftPushList(String key, String value) {
            redisTemplate.opsForList().leftPush(key, value);
        }

        @Override
        public Object listLeftPopList(String key) {
            return redisTemplate.opsForList().leftPop(key);
        }

        @Override
        public Long listSize(String key) {
            return redisTemplate.opsForList().size(key);
        }

        @Override
        public List<String> listRangeList(String key, Long start, Long end) {
            return redisTemplate.opsForList().range(key, start, end);
        }

        @Override
        public Long listRemoveFromList(String key, long count, Object value) {
            return redisTemplate.opsForList().remove(key, count, value);
        }

        @Override
        public Object listIndexFromList(String key, long index) {
            return redisTemplate.opsForList().index(key, index);
        }

        @Override
        public void listSetValueToList(String key, long index, String value) {
            redisTemplate.opsForList().set(key, index, value);
        }

        @Override
        public void listTrimByRange(String key, Long start, Long end) {
            redisTemplate.opsForList().trim(key, start, end);
        }

    }

    public class RedisSetCacheServer implements RedisSetOption {
        @Override
        public Long setAddSetMap(String key, Object... values) {
            return redisTemplate.opsForSet().add(key, values);
        }

        @Override
        public Long setGetSizeForSetMap(String key) {
            return redisTemplate.opsForSet().size(key);
        }

        @Override
        public Set<String> setGetMemberOfSetMap(String key) {
            return redisTemplate.opsForSet().members(key);
        }

        @Override
        public Boolean setCheckIsMemberOfSet(String key, Object element) {
            return redisTemplate.opsForSet().isMember(key, element);
        }
    }

    public class RedisSortedSetCacheServer implements RedisSortedSetOption {

        @Override
        public Long zSetAddSortedSetMap(String key, Object... values) {
            return redisTemplate.opsForZSet().add(key, Arrays.stream(values).collect(Collectors.toSet()));
        }


    }




}