package com.job;

import com.javamenc.constant.RedisConstant;
import com.javamenc.utils.AliYunOssUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class clearJobs {

    @Autowired
    private JedisPool jedisPool;
    //清楚垃圾图片
    public void clearRubbishImg(){
        Set<String> set=jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,RedisConstant.SETMEAL_PIC_DB_RESOURCES);

        if (set !=null &&set.size()>0){
            for (String filename:set) {
                AliYunOssUtils.deleteFile(filename);

                //删除redis中的缓存
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,filename);
                System.out.println("自定义任务执行, 删除的垃圾图片是：" + filename);
            }
        }
    }

    //将整个缓存清空
    public void unit(){
        this.clearRubbishImg();

        Set<String> set = jedisPool.getResource().smembers(RedisConstant.SETMEAL_PIC_RESOURCES);
        for (String filename:set){
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, filename);
        }

        set = jedisPool.getResource().smembers(RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        for (String imgName : set) {
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, imgName);
        }

    }

}
