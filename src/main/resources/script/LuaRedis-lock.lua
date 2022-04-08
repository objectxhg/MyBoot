-- set lock 1 改为 set lock uuid  
-- 为了避免被其他线程解锁 设key值为uuid 解锁时判断是否是自己的uuid才能解锁
-- setnx 设置key的值，如果key已经存在，则setnx不做任何操作 设置成功返回1，设置失败返回0
-- 1.
local lock = redis.call('setnx', KEYS[1], ARGV[1])

-- 2.如果第一步set值后 redis 挂了,没有走到第二步 那么这里就成死锁了  改进方案 LuaRedis-lock-new 加强版 第一步和第二步合为一条命令
-- 加锁成功 则设置过期时间 这里分为了2步
if  lock == 1 then
    redis.call('expire', KEYS[1], tonumber(ARGV[2]))

    return "1"

else
    -- 如果value相同，则认为是同一个线程的请求，则认为重入锁
    local value = redis.call('get', KEYS[1])

    if value == ARGV[1] then
    redis.call('expire', KEYS[1], tonumber(ARGV[2]))

        return "1"

    end

end
    return "0"
