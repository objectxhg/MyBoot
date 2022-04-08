-- 加锁成功 则设置过期时间 一步执行
-- key
-- value
-- nx:意思是SET IF NOT EXIST，即当key不存在时，我们进行set操作
-- px:时间类型 PX毫秒，EX秒
-- time:时间
-- 设置成功则返回‘OK’
if redis.call('set', KEYS[1], ARGV[1], 'nx', 'ex' ,tonumber(ARGV[2])) == 'OK' then
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
