package com.learning.vps.cache;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis命令操作
 *
 * @author Wang Xu
 * @date 2020/10/20
 */
public interface RedisOption extends Cache {

    /**
     * 切换数据库
     *
     * @param db 要切换的数据库
     * @return true-成功，false-失败
     */
    Boolean transferDb(int db);

    /**
     * 删除键为key的缓存(hash|set|list|String)
     *
     * @param key redis中缓存key
     * @return true-成功，false-失败
     */
    Boolean deleteFromRedis(String key);

    /**
     * 删除键在keys中的缓存(hash|set|list|String)的多个key
     *
     * @param keys 缓存key集合
     * @return true-成功，false-失败
     */
    void deleteListFromRedis(final String... keys);


    /**
     * 指定key在指定的日期过期
     *
     * @param key 缓存key
     * @param date 指定的过期时间
     */
    void expireKeyAt(String key, Date date);

    /**
     * 指定key在指定的日期过期
     *
     * @param key 缓存key
     * @param timeout timeout时间后过期
     * @param unit 时间单位
     */
    void expire(String key, Long timeout, TimeUnit unit);

    /**
     * 将key设置为永久有效
     *
     * @param key 缓存key
     */
    void persistKey(String key);



    interface RedisStringOption {
        /**
         * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。
         * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SEObjecObjecObject key value 一样。
         *
         * @param key   string数据结构key
         * @param value 待设置的值
         * @return 追加value之后，key中字符串的长度
         */
        Integer stringAppendString(String key, String value);

        /**
         * 获取指定键的值
         *
         * @param key string数据结构key
         * @return key对应的value
         */
        Object stringGetStringByKey(String key);

        /**
         * 获取存储在键上的字符串的子字符串
         *
         * @param key   string数据结构key
         * @param start 开始截取的下标位
         * @param end   截止截取的下标位
         * @return 截取后的子字符串
         */
        String stringGetSubStringFromString(String key, long start, long end);

        /**
         * 将键的整数值按给定的长整型数值增加
         *
         * @param key    string数据结构key
         * @param incrBy 操作数
         * @return 返回增长后的key对应的结果值
         */
        Long stringIncrementLongString(String key, Long incrBy);

        /**
         * 将键的整数值按给定的浮点型数值增加
         *
         * @param key    string数据结构key
         * @param incrBy 操作数
         * @return 返回增长后的key对应的结果值
         */
        Double stringIncrementDoubleString(String key, Double incrBy);

        /**
         * 设置指定键的值
         *
         * @param key   string数据结构key
         * @param value 待设置的value
         */
        void stringSetString(String key, Object value);

        /**
         * 设置键的字符串值并返回其旧值
         *
         * @param key   string数据结构key
         * @param value 待设置的value
         * @return 返回key对应的旧值
         */
        Object stringGetAndSet(String key, Object value);

        /**
         * 使用键和到期时间来设置值，时间单位默认为毫秒
         *
         * @param key     string数据结构key
         * @param value   待设置的value
         * @param timeout 失效时间
         */
        void stringSetValueAndExpireTime(String key, String value, long timeout);
    }

    interface RedisHashOption {
        /**
         * 查看哈希表 key 中，给定域 fieldKey 是否存在。
         *
         * @param hashKey  哈希表key
         * @param fieldKey 域的hashKey
         * @return 如果哈希表含有给定域，返回true;如果哈希表不含有给定域，或 hashKey 不存在，返回false。
         */
        Boolean hashCheckExists(String hashKey, String fieldKey);

        /**
         * 查询哈希表 key 中给定域 fieldKey 的值。
         *
         * @param hashKey  哈希表key
         * @param fieldKey 域的hashKey
         * @return 获取给定域的值。当给定域不存在或是给定key不存在时，返回null 。
         */
        Object hashGet(String hashKey, String fieldKey);

        /**
         * 获取所有的散列值
         *
         * @param hashKey 哈希表key
         * @return 哈希表key对应的所有的散列值
         */
        Map<Object, Object> hashGetAll(String hashKey);

        /**
         * 哈希表 key 中的域 fieldKey 的值加上增量 delta 。
         * <p>
         * 增量也可以为负数，相当于对给定域进行减法操作。
         * 如果key不存在，一个新的哈希表被创建并执行 Hincrby 命令。
         * 如果域field不存在，那么在执行命令前，域的值被初始化为 0 。
         * 对一个储存字符串值的域field执行 Hincrby 命令将造成一个错误。
         *
         * @param hashKey  哈希表key
         * @param fieldKey 域的hashKey
         * @param incrBy   操作数
         * @return 执行 Hincrby 命令之后，哈希表hashKey中域fieldKey的值。
         */
        Long hashIncrementLongOfHashMap(String hashKey, String fieldKey, Long incrBy);

        /**
         * 哈希表 hashKey 中的域 fieldKey 的值加上浮点值增量 incrBy 。
         *
         * @param hashKey  哈希表key
         * @param fieldKey 域的hashKey
         * @param incrBy   操作数
         * @return 执行 Hincrby 命令之后，哈希表hashKey中域fieldKey的值。
         */
        Double hashIncrementDoubleOfHashMap(String hashKey, String fieldKey, Double incrBy);

        /**
         * 添加键值对到哈希表key中
         *
         * @param hashKey  哈希表key
         * @param fieldKey 域的hashKey
         * @param value    域的hashKey对应的value值
         */
        void hashPushHashMap(String hashKey, String fieldKey, Object value);

        /**
         * 获取哈希表hashKey中的所有域
         *
         * @param hashKey 哈希表key
         * @return 哈希表key中的所有域
         */
        Set<Object> hashGetAllHashKey(String hashKey);

        /**
         * 获取散列中的字段数量
         *
         * @param hashKey 哈希表key
         * @return 散列中的fieldKey数量
         */
        Long hashGetHashMapSize(String hashKey);

        /**
         * 获取哈希中的所有值
         *
         * @param hashKey 哈希表key
         * @return 哈希表key中的所有值
         */
        List<Object> hashGetHashAllValues(String hashKey);

        /**
         * 删除一个或多个哈希字段
         *
         * @param hashKey   哈希表key
         * @param fieldKeys 域的hashKey集合
         * @return 返回值为被成功删除的数量
         */
        Long hashDeleteHashKey(String hashKey, Object... fieldKeys);
    }

    interface RedisListOption {
        /**
         * 从右向左压栈
         *
         * @param key   list数据结构的key
         * @param value 对应的value值
         */
        void listRightPushList(String key, String value);

        /**
         * 从右出栈
         *
         * @param key list数据结构的key
         * @return key的list中右出栈的value值
         */
        Object listRightPopList(String key);

        /**
         * 从左向右压栈
         *
         * @param key   list数据结构的key
         * @param value 对应的value值
         */
        void listLeftPushList(String key, String value);

        /**
         * 从左出栈
         *
         * @param key list数据结构的key
         * @return key的list中左出栈的value值
         */
        Object listLeftPopList(String key);

        /**
         * 集合list的长度
         *
         * @param key list数据结构的key
         * @return key的list中的元素数量
         */
        Long listSize(String key);

        /**
         * 查询列表 key 中指定区间内的元素，区间以偏移量 starObjecObjecObject 和 stop 指定
         *
         * @param key   list数据结构的key
         * @param start 开始偏移量
         * @param end   截止偏移量
         * @return 指定范围内的元素集合
         */
        List<String> listRangeList(String key, Long start, Long end);

        /**
         * 移除key中值为value的i个,返回删除的个数；如果没有这个元素则返回0
         *
         * @param key   list数据结构的key
         * @param count 移除的数量
         * @param value 需要移除的value
         * @return 实际移除的数量
         */
        Long listRemoveFromList(String key, long count, Object value);

        /**
         * 根据下标查询list中某个值
         *
         * @param key   list数据结构的key
         * @param index 下标
         * @return 根据下标从key的list中获取到的值
         */
        Object listIndexFromList(String key, long index);

        /**
         * 根据下标设置value
         *
         * @param key   list数据结构的key
         * @param index 下标
         * @param value 根据下标设置key的list中对应下标元素的值
         */
        void listSetValueToList(String key, long index, String value);

        /**
         * 裁剪/删除(裁剪[start,end]以外的所有元素)
         *
         * @param key   list数据结构的key
         * @param start 保留元素的起始下标
         * @param end   保留元素的终止下标
         */
        void listTrimByRange(String key, Long start, Long end);
    }

    interface RedisSetOption {
        /**
         * 将一个或多个 value 元素加入到集合 key 当中，已经存在于集合的 value 元素将被忽略
         *
         * @param key set数据结构的key
         * @param values 被添加到集合key中的values
         * @return 实际被添加到集合中的新元素的数量，不包括被忽略的元素
         */
        Long setAddSetMap(String key, Object... values);

        /**
         * 获取set集合的大小
         *
         * @param key set数据结构的key
         * @return key的set集合的大小
         */
        Long setGetSizeForSetMap(String key);

        /**
         * 获取set集合中的元素
         *
         * @param key set数据结构的key
         * @return key的set集合中的所有元素
         */
        Set<String> setGetMemberOfSetMap(String key);

        /**
         * 检查元素是不是set集合中的
         *
         * @param key set数据结构的key
         * @param element 待检测元素
         * @return true-是，false-否
         */
        Boolean setCheckIsMemberOfSet(String key, Object element);
    }

    interface RedisSortedSetOption {
        /**
         * 将一个或多个 value 元素加入到集合 key 当中，已经存在于集合的 value 元素将被忽略
         *
         * @param key set数据结构的key
         * @param values 被添加到集合key中的values
         * @return 实际被添加到集合中的新元素的数量，不包括被忽略的元素
         */
        Long zSetAddSortedSetMap(String key, Object... values);

        // TODO


    }

}
