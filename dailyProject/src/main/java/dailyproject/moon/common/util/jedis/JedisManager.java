package dailyproject.moon.common.util.jedis;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import dailyproject.moon.common.util.MoonStringUtils;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *Jedis 管理的工具类
 * 
 */
@Slf4j
@Component
public class JedisManager {

    @Autowired
    JedisPool jedisPool;


    public Jedis getJedis()  {
        Jedis jedis=null;
        try {
            jedis = jedisPool.getResource();
        } catch (JedisConnectionException e) {
        	String message = StringUtils.trim(e.getMessage());
        	if("Could not get a resource from the pool".equalsIgnoreCase(message)){
        		System.out.println("++++++++++请检查你的redis服务++++++++");
        		System.out.println("|①.redis 没有正常连接，请检查是否启动或者连接 redis");
                System.out.println("++++++++++请检查你的redis服务++++++++");
        	}
        	throw new JedisConnectionException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return jedis;
    }

    public void returnResource(Jedis jedis, boolean isBroken) {
        if (jedis == null)
            return;
        /**
         * @deprecated starting from Jedis 3.0 this method will not be exposed.
         * Resource cleanup should be done using @see {@link Jedis#close()}
        if (isBroken){
            getJedisPool().returnBrokenResource(jedis);
        }else{
            getJedisPool().returnResource(jedis);
        }
        */
        jedis.close();
    }

    public byte[] getValueByKey(int dbIndex, byte[] key) throws Exception {
        Jedis jedis = null;
        byte[] result = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            result = jedis.get(key);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
        return result;
    }

    public void deleteByKey(int dbIndex, byte[] key) throws Exception {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            Long result = jedis.del(key);
            log.info("删除Session结果：%s" , result);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
    }

    public void saveValueByKey(int dbIndex, byte[] key, byte[] value, int expireTime)
            throws Exception {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            jedis.set(key, value);
            if (expireTime > 0)
                jedis.expire(key, expireTime);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
    }


//    public void setJedisPool(JedisPool jedisPool) {
//        this.jedisPool = jedisPool;
//    }

//    /**
//     * 获取所有Session
//     * @param dbIndex
//     * @param redisShiroSession
//     * @return
//     * @throws Exception
//     */
//    @SuppressWarnings("unchecked")
//    public Collection<Session> AllSession(int dbIndex, String redisShiroSession) throws Exception {
//        Jedis jedis = null;
//        boolean isBroken = false;
//        Set<Session> sessions = new HashSet<Session>();
//        try {
//            jedis = getJedis();
//            jedis.select(dbIndex);
//
//            Set<byte[]> byteKeys = jedis.keys((JedisSessionDao.REDIS_SHIRO_ALL).getBytes());
//            if (byteKeys != null && byteKeys.size() > 0) {
//                for (byte[] bs : byteKeys) {
//                    Session obj = SerializeUtil.deserialize(jedis.get(bs),
//                            Session.class);
//                    if(obj instanceof Session){
//                        sessions.add(obj);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            isBroken = true;
//            throw e;
//        } finally {
//            returnResource(jedis, isBroken);
//        }
//        return sessions;
//    }


    public boolean isExistsValueByKey(int dbIndex, byte[] key)
            throws Exception {
        Jedis jedis = null;
        boolean isBroken = false;
        boolean result = false;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            result = jedis.exists(key);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
        return result;
    }


    public void saveStrKeyValue( int dbIndex , String key ,String value ,int expireTime){

        Jedis jedis = null;
        boolean isBroken = false;

        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            jedis.set( key,value);
            if ( 0 < expireTime )
                jedis.expire(key, expireTime);

        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }

    }


    public String getStrValueByStrKey( int dbIndex , String key){
        Jedis jedis = null;
        String result = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            result = jedis.get(key);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
        return result;
    }




    public boolean isExistsKeyByStringKey(int dbIndex, String Strkey)
            throws Exception {
        Jedis jedis = null;
        boolean isException = false;
        boolean result = false;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            result = jedis.exists(Strkey);
        } catch (Exception e) {
            isException = true;
            throw e;
        } finally {
            returnResource(jedis, isException);
        }
        return result;
    }

/**=================================================================================*/
    /**
     * redis中关于list的操作（添加）
     *
     * */
    public boolean setRedisList(int dbIndex, String listName,String value)
            throws Exception {
        Jedis jedis = null;
        boolean isException = false;
        boolean result = false;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            jedis.rpush(listName, value);
        } catch (Exception e) {
            isException = true;
            throw e;
        } finally {
            returnResource(jedis, isException);
        }
        return result;
    }


    /**
     * redis中关于list的操作（查找全部list集合，不执行弹出操作）
     *
     * */
    public List<String> searchRedisList(int dbIndex,String listName)
            throws Exception {
        Jedis jedis = null;
        boolean isException = false;
        boolean result = false;
        List<String> list=null;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            list = jedis.lrange(listName, 0, -1);
        } catch (Exception e) {
            isException = true;
            list=new ArrayList<String>();
            throw e;
        } finally {
            returnResource(jedis, isException);
        }
        return list;
    }


    /**
     * redis中关于list的操作（查找全部list集合长度）
     *
     * */

    public Integer searchListLength(int dbIndex,String listName)
            throws Exception {
        Jedis jedis = null;
        boolean isException = false;
        Integer llen = 0;
        List<String> list=null;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            llen = jedis.llen(listName).intValue();
        } catch (Exception e) {
            isException = true;
            throw e;
        } finally {
            returnResource(jedis, isException);
        }
        return llen;
    }


    /**
     * 获取collections固定下标的元素
     *
     * */
    public String searchOnebyPosition(int dbIndex,String listName,Integer position)
            throws Exception {
        Jedis jedis = null;
        boolean isException = false;
        String need = null;
        List<String> list=null;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            need = jedis.lindex(listName,position);
        } catch (Exception e) {
            isException = true;
            throw e;
        } finally {
            returnResource(jedis, isException);
        }
        return need;
    }




/**=================================================================================*/



    /**
     * 往redis 里面设置 用户登录的hashMap 信息
     * @param dbIndex  存放数据的库id
     * @param hashName  hash对象的名字
     * @param filedKey  哈希的 key值
     * @param value      哈希的value 值
     * @param expireTime  过期时间
     * @return
     */
    public boolean hashSet( int dbIndex ,String hashName ,String filedKey ,String value ,int expireTime){

        Jedis jedis = null;
        boolean isBroken = false;

        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            jedis.hset( hashName,filedKey ,value);
            if ( 0 < expireTime )
                jedis.expire( hashName, expireTime);

        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
        return isBroken;
    }


    /**
     * 往redis 里面设置 用户登录的hashMap 信息
     * @param dbIndex  存放数据的库id
     * @param hashName  hash对象的名字
     * @param filedKey  哈希的 key值
     * @param value      哈希的value 值
     * @return
     */
    public boolean hashSet1( int dbIndex ,String hashName ,String filedKey ,String value ){

        Jedis jedis = null;
        boolean isBroken = false;

        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            jedis.hset( hashName,filedKey ,value);

        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
        return isBroken;
    }


    /**
     * 获取redis 里面的hash 的值
     * @param dbIndex    库id
     * @param hashName    hash 的name 值
     * @param filedKey   value 值
     * @return
     */
    public String hashGet ( int dbIndex ,String hashName ,String filedKey ){
        Jedis jedis = null;
        String hashValue = null;

        try{
            jedis = getJedis();
            jedis.select(dbIndex);
            hashValue = jedis.hget(hashName,filedKey);
        }catch (Exception e){
            throw e;
        }finally {
            returnResource(jedis, true);
        }
        return hashValue;
    }


    /**
     * 判断 redis 里面的hash中的fild是否存在
     * @param dbIndex    库id
     * @param hashName    hash 的name 值
     * @param filedKey   value 值
     * @return
     */
    public Boolean hashExist( int dbIndex ,String hashName ,String filedKey ){
        Jedis jedis = null;
        Boolean hexists = null;

        try{
            jedis = getJedis();
            jedis.select(dbIndex);
            hexists = jedis.hexists(hashName, filedKey);
        }catch (Exception e){
            throw e;
        }finally {
            returnResource(jedis, true);
        }
        return hexists;
    }


    /**
     * 删除 redis 里面的hash 的值
     * @param dbIndex    库id
     * @param hashName    hash 的name 值
     * @param filedKey   value 值
     * @return
     */
    public Long hashdel ( int dbIndex ,String hashName ,String filedKey ){
        Jedis jedis = null;
        Long hashValue = null;

        try{
            jedis = getJedis();
            jedis.select(dbIndex);
            hashValue= jedis.hdel(hashName, filedKey);
        }catch (Exception e){
            throw e;
        }finally {
            returnResource(jedis, true);
        }
        return hashValue;
    }


    /**
     * 获取 redis 里面的hash 中的所有的键
     * @param dbIndex    库id
     * @param hashName    hash 的name 值
     * @return
     */
    public Set<String> hashkeys ( int dbIndex ,String hashName ){
        Jedis jedis = null;
        Set<String> hkeys= null;

        try{
            jedis = getJedis();
            jedis.select(dbIndex);
            hkeys = jedis.hkeys(hashName);
        }catch (Exception e){
            throw e;
        }finally {
            returnResource(jedis, true);
        }
        return hkeys;
    }


    /**
     * 获取 redis 里面的hash 中的所有的值
     * @param dbIndex    库id
     * @param hashName    hash 的name 值
     * @return
     */
    public List<String> hashValues ( int dbIndex ,String hashName ){
        Jedis jedis = null;
        List<String> hValues= null;

        try{
            jedis = getJedis();
            jedis.select(dbIndex);
            List<String> hvals = jedis.hvals(hashName);
        }catch (Exception e){
            throw e;
        }finally {
            returnResource(jedis, true);
        }
        return hValues;
    }


    /**
     * 获取 redis 里面的hash 中的所有的值
     * @param dbIndex    库id
     * @param hashName    hash 的name 值
     * @return
     */
    public Boolean hashdeleteAll ( int dbIndex ,String hashName ){
        Jedis jedis = null;
        Boolean result=false;
        Set<String> hashkeys;
        try{
            hashkeys = hashkeys(dbIndex, hashName);
            if (hashkeys.size()>0){
                for (String s:hashkeys) {
                    hashdel(dbIndex,hashName,s);
                }
            }
            result= true;
        }catch (Exception e){
            throw e;
        }finally {
            returnResource(jedis, true);
        }
        return result;
    }



    /**
     * redis 里面的hash 中的值+1
     * @param dbIndex    库id
     * @param hashName    hash 的name 值
     * @return
     */
    public Boolean hashincre ( int dbIndex ,String hashName ,String hashKey,Long addCount){
        Jedis jedis = null;
        Boolean result=false;

        try{
            jedis = getJedis();
            jedis.select(dbIndex);
            Long aLong = jedis.hincrBy(hashName, hashKey, addCount);
            if (aLong>0){
                result=true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            returnResource(jedis, true);
        }
        return result;
    }



/**=================================================================================*/

    /**
     * 向set中 添加记录，如果已经存在，返回0 否则返回1
     * sadd 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 value 元素将被忽略。
     *
     * @param key
     * @param value
     * @return
     */
    public  long setAdd( int dbIndex ,String key, String... value) throws Exception{
        Jedis jedis = null;
        long status = 0;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            status = jedis.sadd(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            returnResource(jedis, true);
        }
        return status;
    }


    /**
     * 删除set中 指定的元素
     *
     * @param key
     * @param member
     * @return
     */
    public  long setDelete( int dbIndex ,String key, String... member) throws Exception {
        Jedis jedis=null;
        long returnStatus=0;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            returnStatus = jedis.srem(key, member);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            returnResource(jedis, true);
        }
        return returnStatus;
    }

    /**
     * 返回set中 的元素数量
     * 如果set不存在，返回0
     *
     * @param key
     * @return
     */
    public  long setLength( int dbIndex ,String key)throws Exception {
        Jedis jedis = null;
        long length=0;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            length = jedis.scard(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            returnResource(jedis, true);
        }
        return length;
    }




    /**
     * 判断元素是否存在
     *
     * @param key
     * @param value
     * @return
     */
    public  boolean setIfExist( int dbIndex , String key, String value) throws Exception{
        Jedis jedis = null;
        boolean result = false;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            result = jedis.sismember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            returnResource(jedis, true);
        }
        return result;
    }



    /**
     * 获取set 中的所有元素
     *
     * @param key
     * @return
     */
    public  Set<String> setGetAllValues( int dbIndex , String key)throws Exception {
        Jedis jedis = null;
        Set<String> result=null;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            result = jedis.smembers(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            returnResource(jedis, true);
        }
        return result;
    }



/**=================================================================================*/






    /**
     * 获取缓存
     * @param key 键
     * @return 值
     */
    public  String get(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis.exists(key)) {
                value = jedis.get(key);
                value = MoonStringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
                //log.debug("get {} = {}", key, value);
            }
        } catch (Exception e) {
            log.warn("get {} = {}", key, value, e);
        } finally {
            returnResource(jedis, true);
        }
        return value;
    }


    /**
     * 获取byte[]类型Key
     * @param
     * @return
     */
    public static byte[] getBytesKey(Object object){
        if(object instanceof String){
            return MoonStringUtils.getBytes((String)object);
        }else{
            return serialize(object);
        }
    }

    /**
     * 序列化对象
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            if (object != null){
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                return baos.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main (String[] args) {
        JedisManager jedisManager = new JedisManager();
        boolean b = false;
        try {
            b = jedisManager.setRedisList(0, "haha", "lala");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(b);
    }


}
