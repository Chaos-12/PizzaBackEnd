package com.example.demo.infraestructure.redisInfraestructure;

import java.util.HashMap;
import java.util.Map;

import reactor.core.publisher.Mono;

public class RedisRepositoryMock<T,ID> implements RedisRepositoryInterface<T,ID>{

    public Map<ID, T> entityMap = new HashMap<ID, T>();
    public Map<ID, Long> timeMap = new HashMap<ID, Long>();
    
    public long presentTime = 0;

    public Mono<T> set(ID id, T t, long hours){
        this.add(id, t, presentTime + hours);
        return Mono.just(t);
    }

    public Mono<T> getFromID(ID id) {
        if (!entityMap.containsKey(id)){
            return Mono.empty();
        }
        long expirationTime = timeMap.get(id);
        if (expirationTime < presentTime){
            return Mono.empty();
        }
        return Mono.just(entityMap.get(id));
    }

    public Mono<Boolean> removeFromID(ID id){
        if (!entityMap.containsKey(id)){
            return Mono.just(false);
        }
        long expirationTime = timeMap.get(id);
        if (expirationTime < presentTime){
            return Mono.just(false);
        }
        this.remove(id);
        return Mono.just(true);
    }

    private void add(ID id, T t, Long time){
        this.entityMap.put(id, t);
        this.timeMap.put(id, time);
    }

    private void remove(ID id){
        if(this.entityMap.containsKey(id)){
            this.entityMap.remove(id);
            this.timeMap.remove(id);
        }
    }
}
