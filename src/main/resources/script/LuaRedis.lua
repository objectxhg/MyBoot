local count = redis.call('get', KEYS[1])

if count > ARGV[1]  then
   redis.call('decr', KEYS[1])
    return 1

end
    return 0