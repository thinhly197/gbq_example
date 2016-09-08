package com.ascend.data.util;

import com.ascend.data.model.Product;
import com.ascend.data.model.ProductNotFoundException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.TimeUnit;

/**
 * Created by thinhly on 4/25/16.
 */
public class ProductCache {
    private static LoadingCache<String, Product> cache;
    private static long MAX_SIZE = 10000;

    public static void initialize() {
        if(cache == null) {
            int awsNumberOfThread = Integer.parseInt(AutoReloadProperty.getProperty("awsNumberOfThread"));
            int itmNumberOfThread = Integer.parseInt(AutoReloadProperty.getProperty("itmNumberOfThread"));
            int nunberOfThread = awsNumberOfThread > itmNumberOfThread ? awsNumberOfThread : itmNumberOfThread;
            long numberRecommendedProducts = 0;
            try{
                numberRecommendedProducts = Long.parseLong(
                        AutoReloadProperty.getProperty("numberRecommendedProducts"));
            } catch (Exception e){
                numberRecommendedProducts = MAX_SIZE;
            }

            cache = CacheBuilder.newBuilder()
                    .maximumSize(numberRecommendedProducts)
                    .expireAfterAccess(24, TimeUnit.HOURS)
                    .concurrencyLevel(nunberOfThread)
                    .build(new CacheLoader<String, Product>() {
                               @Override
                               public Product load(String key) throws Exception {
                                   Product p = ItmProductGetter.getProductDetail(key);
                                   return p;
                               }
                           }
                    );
        }
    }

    public static Product getProduct(String key) {
        return cache.getUnchecked(key);
    }
}
