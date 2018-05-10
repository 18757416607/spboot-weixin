package com.weixin.util;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;

/**
 * Created by Administrator on 2018/4/11.
 */
public class RedisClient {

    private static Jedis jedis = getJedis();

    private static String ADDR = "127.0.0.1";   //Redis服务器IP
    private static int PORT = 6379;   //Redis的端口号
    //private static String AUTH = null;//"admin";//访问密码
    private static int MAX_ACTIVE = 1024;//可用连接实例的最大数目，默认值为8；如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_IDLE = 200; //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_WAIT = 10000;//等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int TIMEOUT = 10000;
    private static boolean TEST_ON_BORROW = true;//在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static JedisPool jedisPool = null;


    /**
     * 初始化Redis连接池
     */
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            //config.setMaxActive(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            //config.setMaxWait(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            /*jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);*/
            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取Jedis实例
     * @return
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 释放jedis资源
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }

    /***
     * 清空所有db
     */
    public static void flushAll(){
        jedis.flushAll();
        RedisClient.returnResource(jedis);
    }

    /***
     * 清空当前db
     */
    public static void flushDb(){
        jedis.flushDB();
        RedisClient.returnResource(jedis);
    }


    /***
     * 存key并且有超时时间
     * @param key
     * @param seconds
     * @param value
     */
    public static void setKeyByTimeout(String key,int seconds,String value){
        if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)){
            jedis.setex(key, seconds, value);
            RedisClient.returnResource(jedis);
        }
    }

    /***
     * 模糊查询所有的key
     *         例如 所有 包含‘name’的key
     * @param key
     *        用法说明：  *key* 包含key,  key* 以key开头， *key 以key结尾
     * @return
     *        返回的是key集合  不是 value
     */
    public static Set<String> getAllKeySetByParrten(String key){
        Set<String> keySet = new HashSet<String>();
        if(StringUtils.isNotBlank(key)){
            keySet = jedis.keys(key);
        }
        RedisClient.returnResource(jedis);
        return keySet;
    }

    /***
     * 判断当前key是否已经存在于redis中
     * @param key
     * @return
     */
    public static boolean isExistsKey(String key){
        boolean isExists = false;
        if(StringUtils.isNotBlank(key)){
            isExists = jedis.exists(key);
        }
        RedisClient.returnResource(jedis);
        return isExists;
    }


    /***
     * 获取key的剩余超时时间
     * @param key
     * @return
     */
    public static long gettime(String key){
        long time = 0;
        if(StringUtils.isNotBlank(key)){
            time = jedis.ttl(key);
        }
        RedisClient.returnResource(jedis);
        return time;
    }

    /***
     * 取消超时时间的设置
     * @param key
     */
    public static void cancleTime(String key){
        if(StringUtils.isNotBlank(key)){
            jedis.persist(key);
        }
        RedisClient.returnResource(jedis);
    }

    /***
     * int类型 递增/递减：incr()/decr()
     * @param key
     *                 key
     * @param type
     *                 0递增/1递减
     */
    public static void valueIncrOrDecr(String key,int type){
        if(StringUtils.isNotBlank(key)){
            switch (type) {
                case 0:
                    jedis.incr(key); // 递增
                    break;
                case 1:
                    jedis.decr(key); ///递减
                    break;
            }
        }
        RedisClient.returnResource(jedis);
    }

    /***
     * 存单个string类型
     * @param key
     * @param value
     */
    public static void setStringVal(String key,String value){
        if(StringUtils.isNotBlank(key)&&StringUtils.isNotBlank(value)){
//            Jedis jedis = RedisClient.getJedis();
            jedis.set(key, value);
            RedisClient.returnResource(jedis);
        }
    }

    /***
     * 根据key获取单个value
     * @param key
     * @return
     */
    public static String getStringVal(String key){
        String value = null;
        if(StringUtils.isNotBlank(key)){
//            Jedis jedis = RedisClient.getJedis();
            value = jedis.get(key);
            RedisClient.returnResource(jedis);
        }
        return value;
    }

    /***
     * 根据key 给value后面拼加
     * @param key
     * @param appendVal
     */
    public static void appendStringVal(String key,String appendVal){
        if(StringUtils.isNotBlank(key)&&StringUtils.isNotBlank(appendVal)){
            jedis.append(key, appendVal);
            RedisClient.returnResource(jedis);
        }
    }

    /***
     * 根据key删除单个
     * @param key
     */
    public static void delStringVal(String key){
        if(StringUtils.isNotBlank(key)){
            jedis.del(key);
            RedisClient.returnResource(jedis);
        }
    }

    /***
     * 存多个String
     * @param keys
     * @param values
     */
//    public static void mSetStringVal(String[] keys,String[] values){
//        if((null != keys && keys.length > 0) && (null != values && values.length > 0)){
//            for (String str : keys) {
//
//            }
//        }
//
//    }

    /***
     * 存Map
     * @param key
     * @param map
     */
    public static void setMapValue(String key,Map<String, String> map){
        if(StringUtils.isNotBlank(key) && null != map){
            jedis.hmset(key, map);
            RedisClient.returnResource(jedis);
        }
    }

    /***
     * 根据key获取map中的值
     * @param key
     *             redis里面的key
     * @param mapKeys
     *             map里面的key  可以传多个
     * @return
     */
    public static List<String> getMapValue(String key,String ... mapKeys){
        List<String> strList = new ArrayList<String>();
        if(StringUtils.isNotBlank(key) && (null != mapKeys && mapKeys.length > 0)){
            strList = jedis.hmget(key, mapKeys);
            RedisClient.returnResource(jedis);
        }
        return strList;
    }

    /***
     * 删除map中的某个键值
     * @param key
     *             redis里面的key
     * @param mapKey
     *             map里面的key
     */
    public static void delMapKey(String key,String... mapKey){
        if(StringUtils.isNotBlank(key) && (null != mapKey && mapKey.length > 0)){
            jedis.hdel(key, mapKey);
            RedisClient.returnResource(jedis);
        }
    }

    /***
     * 获取map里面的所有的key
     * @param key
     */
    public static void getMapKey(String key){
        Iterator<String> iter=jedis.hkeys(key).iterator();
        while (iter.hasNext()){
            String mapKey = iter.next();
            System.out.println(mapKey+":"+jedis.hmget(key,mapKey));
        }
        RedisClient.returnResource(jedis);
    }

    /***
     * 存List
     * lpush方式存入，先进后出
     * @param key
     * @param values
     */
    public static void setListValueByLpush(String key,String... values){
        if(StringUtils.isNotBlank(key) && (null != values && values.length > 0)){
            jedis.lpush(key, values);
        }
        RedisClient.returnResource(jedis);
    }

    /***
     * 存List
     * rpush方式存入，先进先出
     * @param key
     * @param values
     */
    public static void setListValueByRpush(String key,String... values){
        if(StringUtils.isNotBlank(key) && (null != values && values.length > 0)){
            jedis.rpush(key, values);
        }
        RedisClient.returnResource(jedis);
    }

    /***
     * 对List排序
     * @param key
     */
    public static void sortList(String key){
        if(StringUtils.isNotBlank(key)){
            jedis.sort(key);
        }
    }

    /***
     * 获取redis中的list
     *             lrange是按范围取出
     * @param key
     *             key
     * @param beginIndex
     *             起点位置
     * @param endIndex
     *             是结束位置， -1表示取得所有  ，jedis.llen获取长度
     *
     * @return
     */
    public static List<String> getListValue(String key, long beginIndex, long endIndex){
        List<String> listStr = new ArrayList<String>();
        if(StringUtils.isNotBlank(key)){
            listStr = jedis.lrange(key, beginIndex, endIndex);
        }
        RedisClient.returnResource(jedis);
        return listStr;
    }

    /***
     * 清空指定key存放的list
     * @param keys
     */
    public static void flushListValue(String... keys){
        if(null != keys && keys.length > 0){
            jedis.del(keys);
            RedisClient.returnResource(jedis);
        }
    }

    /***
     * 将Set存入redis
     * @param key
     * @param values
     */
    public static void setSetValue(String key,String... values){
        if(StringUtils.isNotBlank(key) && (null != values && values.length > 0)){
            jedis.sadd(key, values);
        }
        RedisClient.returnResource(jedis);
    }

    /***
     * 获取redis存放的set
     * 无序
     * @param key
     * @return
     */
    public static Set<String> getSetListByKey(String key){
        Set<String> valSet = new HashSet<String>();
        if(StringUtils.isNotBlank(key)){
            valSet = jedis.smembers(key);
            RedisClient.returnResource(jedis);
        }
        return valSet;
    }

    /***
     * 移除set中的key
     * @param key
     * @param setKeys
     */
    public static void removeSetValue(String key,String... setKeys){
        if(StringUtils.isNotBlank(key) && (null != setKeys && setKeys.length > 0)){
            jedis.srem(key,setKeys);
            RedisClient.returnResource(jedis);
        }
    }

    /***
     * 判断setKey是否是key的元素
     * @param key
     * @param setKey
     * @return
     */
    public static boolean isKeyInSet(String key,String setKey){
        boolean isExists = false;
        if(StringUtils.isNotEmpty(key) && StringUtils.isNotBlank(setKey)){
            isExists = jedis.sismember(key,setKey);
        }
        RedisClient.returnResource(jedis);
        return isExists;
    }

    /***
     * 获取set中元素的个数
     * @param key
     * @return
     */
    public static long getSetValueCount(String key){
        long count = 0;
        if(StringUtils.isNotBlank(key)){
            count = jedis.scard(key);
        }
        RedisClient.returnResource(jedis);
        return count;
    }

    /***
     * 随机获取一个set元素
     * @param key
     * @return
     */
    public static String getSetValueByRandem(String key){
        String value = null;
        if(StringUtils.isNotBlank(key)){
            value = jedis.srandmember(key);
        }
        RedisClient.returnResource(jedis);
        return value;
    }


    public static void main(String[] args) {
        Jedis jedis = RedisClient.getJedis();
        jedis.setex("name1",600, "wxw1");
        System.out.println(jedis.get("name"));
        RedisClient.returnResource(jedis);
        System.out.println(jedis.get("name1"));
    }


}
