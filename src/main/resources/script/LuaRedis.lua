local count = redis.call('get', KEYS[1])
if count == "0" then
    return 0
end

if count > ARGV[1]  then
   redis.call('decr', KEYS[1])
    return 1

end
    return -1

