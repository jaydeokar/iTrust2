package edu.ncsu.csc.itrust2.utils;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;


public class RedisUtil {


    static private String url = null;

    static {
        InputStream input = null;
        final Properties properties = new Properties();

        try {
            final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            input = classLoader.getResourceAsStream("redis.properties");
            properties.load(input);
//            url = "localhost";
            url = properties.getProperty("url");

        } catch (final Exception e) {
            System.out.println("Unable to find `.properties` file for database!");
            e.printStackTrace();
            // The file couldn't be loaded
            // Set some default values and maybe we'll get lucky
            url = "localhost";
        } finally {
            if (null != input) {
                try {
                    input.close();
                } catch (final Exception e2) {
                    // Exception ignored
                }
            }
        }
    }


    private static JedisPoolConfig buildPoolConfig() {

        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }


    static public JedisPool jedisPool(){
        final JedisPoolConfig poolConfig = buildPoolConfig();
        return new JedisPool(poolConfig,url);
    }
}
