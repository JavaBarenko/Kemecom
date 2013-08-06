/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.db;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Future;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author barenko
 */
public class Memcached {
    private Logger log = LoggerFactory.getLogger(Memcached.class);
    
    private MemcachedClient memcached;
    private int ttl;
    
    public Memcached(){}

    public Memcached(String... addresses) throws IOException {
        if(log.isInfoEnabled()) log.info("Iniciando Memcached nos servers: " + Arrays.toString(addresses) + "...");
        
        memcached = new MemcachedClient(new BinaryConnectionFactory(), AddrUtil.getAddresses(Arrays.asList(addresses)));
        
        if(log.isInfoEnabled()) log.info("Memcached iniciado com sucesso!");
    }

    public Memcached setDefaultTTL(int value){
        if(log.isInfoEnabled()) log.info("TTL configurado para "+value);
        this.ttl = value;
        return this;
    }
    
    public Future<Boolean> delete(String key){
        if(log.isDebugEnabled()) log.debug("Excluindo "+key+"...");
        return memcached.delete(key);
    }
    
    public <T> Future<T> get(String key){
        if(log.isDebugEnabled()) log.debug("Obtendo "+key+"...");
        return (Future<T>) memcached.asyncGet(key);
    }
    
    public Future<Boolean> set(String key, Object o){
        return set(key, ttl, o);
    }
    
    public Future<Boolean> set(String key, int ttl, Object o){
        if(log.isDebugEnabled()) log.debug("Setando ["+key+", ttl:"+ttl+"]: "+ ToStringBuilder.reflectionToString(o)+"...");
        return memcached.set(key, ttl, o);
    }
    
    public Future<Boolean> add(String key, Object o){
        return add(key, ttl, o);
    }
    
    public Future<Boolean> add(String key, int ttl, Object o){
        if(log.isDebugEnabled()) log.debug("Adicionando ["+key+", ttl:"+ttl+"]: "+ToStringBuilder.reflectionToString(o)+"...");
        return memcached.add(key, ttl, o);
    }
    
    public Future<Boolean> replace(String key, Object o){
        return replace(key, ttl, o);
    }
    
    public Future<Boolean> replace(String key, int ttl, Object o){
        if(log.isDebugEnabled()) log.debug("Substituindo ["+key+", ttl:"+ttl+"]: "+ToStringBuilder.reflectionToString(o)+"...");
        return memcached.replace(key, ttl, o);
    }
}
