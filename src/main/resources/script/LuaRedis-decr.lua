local key = KEYS[1]
local maxNum = ARGV[1]
local time = tonumber(ARGV[2])


-- 设置value 以及 失效时间

redis.call('set', key, maxNum, 'nx', 'ex' ,time)

-- 递减操作

if redis.call('decr', key) >= 0

then
    -- return value 此时value为long类型， 如果你java代码里面使用Integer接收会报转换异常，需将value转为字符串 tostring(value)
    return "1"

end
    return "0"