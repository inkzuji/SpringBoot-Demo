package com.zuji.common.util;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Component
public class RedisUtil {
    private final static Logger LOG = LoggerFactory.getLogger(RedisUtil.class);

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public RedisUtil(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 使用 eval 脚本执行命令，进行原子操作
     */
    private static final String SCRIPT_UN_LOCK =
            "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";


    private final String REDIS_SCRIPT = buildLimitLuaScript();

    /**
     * 10分钟
     */
    public final static int EXRP_TEN_MINUTE = 10 * 60;
    /**
     * 30分钟
     */
    public final static int EXRP_THIRTY_MINUTE = 30 * 60;

    /**
     * 一小时
     */
    public final static int EXRP_ONE_HOUR = 60 * 60;

    /**
     * 两个小时
     */
    public final static int EXRP_TWO_HOUR = 2 * 60 * 60;

    /**
     * 一天
     */
    public final static int EXRP_ONE_DAY = 60 * 60 * 24;

    /**
     * 3天
     */
    public final static int EXRP_THREE_DAY = 60 * 60 * 24 * 3;

    /**
     * 一个月
     */
    public final static int EXRP_ONE_MONTH = 60 * 60 * 24 * 30;

    // ===================common===================

    /**
     * 设置缓存过期时间（单位：秒）
     *
     * @param key  Redis Key
     * @param time 过期时间
     * @return boolean
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                Boolean expire = redisTemplate.expire(key, time, TimeUnit.SECONDS);
                return expire == null ? false : expire;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取缓存过期时间（单位：秒）
     *
     * @param key Redis Key
     * @return Long
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断 Key 是否存在
     *
     * @param key Redis key
     * @return boolean
     */
    public boolean hashKey(String key) {
        try {
            Boolean hasKey = redisTemplate.hasKey(key);
            return hasKey == null ? false : hasKey;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存，单个/批量
     *
     * @param keys Redis Key
     */
    public void del(String... keys) {
        if (keys == null || keys.length == 0) {
            return;
        }
        if (keys.length == 1) {
            redisTemplate.delete(keys[0]);
        } else {
            redisTemplate.delete(new ArrayList<>(Arrays.asList(keys)));
        }
    }

    // ===============String============

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return Object
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存存入
     *
     * @param key   键
     * @param value 值
     * @return boolean
     */
    public boolean set(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(单位：秒) time要大于0 如果time小于等于0 将设置无限期
     * @return boolean
     */
    public boolean set(String key, String value, long time) {
        try {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public Long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public Long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    // ===================map================

    /**
     * HashGet
     *
     * @param key     键 , not null
     * @param hashKey map key , not null
     * @return value
     */
    public Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取 key 对应的所有键值
     *
     * @param key 键
     * @return 对应多个键值
     */
    public Map<Object, Object> hmGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true=成功; false=失败
     */
    public boolean hmSet(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet, 设置过期时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(单位：秒)
     * @return true=成功; false=失败
     */
    public boolean hmSet(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张 hash 表中放入数据,如果不存在将创建
     *
     * @param key     键
     * @param hashKey 项
     * @param value   值
     * @return true=成功; false=失败
     */
    public boolean hSet(String key, String hashKey, Object value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张 hash 表中放入数据,如果不存在将创建
     *
     * @param key     键
     * @param hashKey 项
     * @param value   值
     * @param time    时间(单位：秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true=成功; false=失败
     */
    public boolean hSet(String key, String hashKey, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除 hash 表中的值
     *
     * @param key      键 不能为null
     * @param hashKeys 项 可以使多个 不能为null
     */
    public void hDel(String key, Object... hashKeys) {
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * 判断 hash 表中是否有该项的值
     *
     * @param key     键 不能为null
     * @param hashKey 项 不能为null
     * @return true=存在; false=不存在
     */
    public boolean hHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * hash 递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key     键
     * @param hashKey 项
     * @param delta   要增加几(大于0)
     * @return
     */
    public double hIncr(String key, String hashKey, long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * hash 递减
     *
     * @param key     键
     * @param hashKey 项
     * @param delta   要减少记(小于0)
     * @return
     */
    public double hDecr(String key, String hashKey, long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, -delta);
    }

    // ==================Set===============

    /**
     * 根据 key 获取 Set 中的所有值
     *
     * @param key 键
     * @return 对应的所有值
     */
    public Set<String> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据 value 从一个 set 中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public Boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入 Set 缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long sSet(String key, String... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将 Set 数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long sSetAndTime(String key, long time, String... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取 Set 缓存的长度
     *
     * @param key 键
     * @return 返回 Set 长度
     */
    public Long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 移除值为 value 的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Long setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // =================List===============

    /**
     * 获取 list 缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return 返回 开始 到 结束 的所有值
     */
    public List<String> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取 list 缓存的长度
     *
     * @param key 键
     * @return 返回 list 的长度
     */
    public Long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过索引 获取 list 中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return 返回索引对应的值
     */
    public String lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 向存于 key 的列表的尾部插入value
     * 如果 key 不存在，那么会创建一个空的列表然后再进行 push 操作
     *
     * @param key   键
     * @param value 值
     * @return 在 push 操作后的列表长度
     */
    public Long lSet(String key, String value) {
        try {
            return redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将 value 放入 list 缓存，带时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return true=成功;false=失败
     */
    public boolean lSet(String key, String value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将 list 放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 在 push 操作后的列表长度
     */
    public Long lSet(String key, List<String> value) {
        try {
            return redisTemplate.opsForList().rightPushAll(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将 list 放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return true=成功;false=失败
     */
    public boolean lSet(String key, List<String> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置 index 位置的list元素的值为 value
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return true=成功;false=失败
     */
    public boolean lUpdateIndex(String key, long index, String value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     * 从存于 key 的列表里移除前 count 次出现的值为 value 的元素
     *
     * @param key   键
     * @param count 移除多少个
     *              > 0: 从头往尾移除值为 value 的元素；
     *              < 0: 从尾往头移除值为 value 的元素；
     *              = 0: 移除所有值为 value 的元素；
     * @param value 值
     * @return 移除的个数
     */
    public Long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 修剪(trim)一个已存在的 list，这样 list 就会只包含指定范围的指定元素
     * 如果 start 超过列表尾部，或者 start > end，结果会是列表变成空表（即该 key 会被移除）。
     * 如果 end 超过列表尾部，Redis 会将其当作列表的最后一个元素。
     *
     * @param key   键
     * @param start 开始下标
     * @param end   结束下标
     */
    public void lTrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    // =============分布式锁============

    /**
     * 获取分布式锁
     * 如果键不存在则新增,存在则不改变已经有的值
     *
     * @param key   键
     * @param value 值
     * @param time  过期时间（单位：秒）
     * @return 是否成功
     */
    public boolean lock(String key, String value, int time) {
        try {
            //如果key值不存在，则返回 true，且设置 value
            Boolean isSuccess = redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
            if (isSuccess != null && isSuccess) {
                return true;
            }

            // 获取key的值，判断是是否超时
            // String curVal = redisTemplate.opsForValue().get(key);
            // if (StringUtils.isNotBlank(curVal) && Long.parseLong(curVal) < System.currentTimeMillis()) {
            //     //获得之前的key值，同时设置当前的传入的value。这个地方可能几个线程同时过来，但是redis本身天然是单线程的，所以getAndSet方法还是会安全执行，
            //     //首先执行的线程，此时curVal当然和oldVal值相等，因为就是同一个值，之后该线程set了自己的value，后面的线程就取不到锁了
            //     String oldVal = redisTemplate.opsForValue().getAndSet(key, value);
            //     return StringUtils.isNotBlank(oldVal) && oldVal.equals(curVal);
            // }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 释放 分布式锁
     *
     * @return
     */
    public boolean unLock(String key, String value) {
        try {
            List<String> list = Lists.newArrayList(key);
            RedisScript<Integer> redisScript = new DefaultRedisScript<>(SCRIPT_UN_LOCK, Integer.class);
            Integer result = redisTemplate.execute(redisScript, list, value);
            // Object result = redisTemplate.execute(redisScript, redisTemplate.getKeySerializer(),
            //         redisTemplate.getStringSerializer(), list, value);
            if (Objects.equals(result, 1)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 限流
     *
     * @param limitKey    限流rediskey
     * @param limitCount  限流数量
     * @param limitPeriod 限流时间
     * @return 返回是否允许
     */
    public boolean limitLock(String limitKey, int limitCount, int limitPeriod) {
        try {
            List<String> list = Lists.newArrayList(limitKey);
            RedisScript<Number> redisScript = new DefaultRedisScript<Number>(REDIS_SCRIPT, Number.class);
            Number count = redisTemplate.execute(redisScript, list, limitCount, limitPeriod);
            if (count != null && count.intValue() <= limitCount) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 限流 脚本
     *
     * @return lua脚本
     */
    private static String buildLimitLuaScript() {
        StringBuilder lua = new StringBuilder();
        lua.append("local c")
                .append("\nc = redis.call('get', KEYS[1])")
                // 调用不超过最大值，则直接返回
                .append("\nif c and tonumber(c) > tonumber(ARGV[1]) then")
                .append("\nreturn c;")
                .append("\nend")
                // 执行计算器自加
                .append("\nc = redis.call('incr', KEYS[1])")
                .append("\nif tonumber(c) == 1 then")
                // 从第一次调用开始限流，设置对应键值的过期
                .append("\nredis.call('expire', KEYS[1], ARGV[2])")
                .append("\nend")
                .append("\nreturn c;");
        return lua.toString();
    }
}
