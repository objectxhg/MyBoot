-- set lock 1 改为 set lock uuid  
-- 为了避免被其他线程解锁 设key值为uuid 解锁时判断是否是自己的uuid才能解锁 
local lock = redis.call('setnx', KEYS[1], ARGV[1])

-- 加锁成功 则设置过期时间 这里分为了2步 也可以一步执行 SET lock uuid EX 10 NX
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
