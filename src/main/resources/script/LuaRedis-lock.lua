local lock = redis.call('setnx', KEYS[1], ARGV[1])

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
