package com.ascend.data;

import com.ascend.data.model.Product;
import com.ascend.data.util.EmailGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by thinhly on 4/27/16.
 */
public class Tester {
    public static Map<String, String> shareList = null;

    public static void main(String args[]) throws InterruptedException {
        shareList = new ConcurrentHashMap<>(1000);
        int loop = 0;
        int numberOfThread = 10;
        System.out.println(350 * 75 / 100);
        ExecutorService es = Executors.newFixedThreadPool(numberOfThread);
        for(int i = 0; i < loop; i++) {
            String temp = "Index" + i;
            es.execute(new AddingShareList(temp, "something here"));
        }
        es.shutdown();
        while (!es.awaitTermination(5, TimeUnit.SECONDS));
        System.out.println(shareList.toString());
    }

    public static class AddingShareList implements Runnable {
        private String key;
        private String value;

        public AddingShareList(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public void run() {
            long startTime = System.nanoTime();
            Tester.shareList.put(this.key, this.value);
            long stopTime = System.nanoTime();
            System.out.println(this.key + ": " + (stopTime - startTime));
        }
    }
}
