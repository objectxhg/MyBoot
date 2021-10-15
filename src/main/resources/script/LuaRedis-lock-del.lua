local lock_value = redis.call('get', KEYS[1])
--判断锁是自己的，删除
if lock_value == ARGV[1] then
   redis.call('del', KEYS[1], ARGV[1])

   return "1"

end

   return "0"


