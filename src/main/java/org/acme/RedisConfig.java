package org.acme;

import javax.enterprise.context.ApplicationScoped;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

@ApplicationScoped
public class RedisConfig {

    private RedissonClient redisson;

    {
        Config config = new Config();
        config.useSingleServer()
          .setAddress("redis://127.0.0.1:6379");
        
          redisson = Redisson.create(config);
    }
    
    public RedissonClient get(){
        return redisson;
    }

}
