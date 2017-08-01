package com.datasynchronous.controller;


import com.bigqueue.BigArrayImpl;
import com.bigqueue.BigQueueImpl;
import com.bigqueue.IBigQueue;
import com.bigqueue.utils.TestUtil;
import com.datasynchronous.service.EntityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.IOException;


/**
 * Created by Administrator on 2017/7/6.
 */

@Controller
//@Configuration
//@EnableScheduling
public class SchedulingConfig {

    @Autowired
    private EntityService entityService;
    //
    private String testDir = TestUtil.TEST_BASE_DIR + "bigqueue/unit";
    private IBigQueue bigQueue;
    private final Logger logger = LoggerFactory.getLogger( SchedulingConfig.class );

    //@Scheduled(cron = "0/1 * * * * ?") // 每20秒执行一次
   /* public void scheduler() {
        logger.info(">>>>>>>>>>>>> scheduled ... ");
    }*/

   // @Scheduled(cron = "0/10 * * * * ?")
    public void executeUploadTask() throws IOException {
   /*    entityService.savetest("E://logs//recivelogs");
       System.out.println("定时任务3------->");
       Thread current = Thread.currentThread();
       System.out.println("定时任务2:"+current.getId());*/

        bigQueue = new BigQueueImpl( testDir, "testApplyForEachDoNotChangeTheQueue", BigArrayImpl.MINIMUM_DATA_PAGE_SIZE );
        // assertNotNull(bigQueue);
        for (int i = 0; i < 10; i++) {
            bigQueue.enqueue( ("big" + i).getBytes() );
        }



       /* DefaultItemIterator dii2 = new DefaultItemIterator();
        bigQueue.applyForEach(dii2);
        System.out.println("[" + dii2.getCount() + "] " + dii2.toString());//统计计数，以及打印出元素*/

        for (int i = (int) bigQueue.size(); i > 0; i--) {

            byte[] by = bigQueue.dequeue();
            String str = new String( by );
            System.out.println( str );

            DefaultItemIterator dii = new DefaultItemIterator();
            bigQueue.applyForEach( dii );
            System.out.println( "[" + dii.getCount() + "] " + dii.toString() );//统计计数，以及打印出元素

        }


    }

    public void clean() throws IOException {
        if (bigQueue != null) {
            bigQueue.removeAll();
        }
    }

    public static class DefaultItemIterator implements IBigQueue.ItemIterator {
        private long count = 0;
        private StringBuilder sb = new StringBuilder();

        public void forEach(byte[] item) throws IOException {
            try {
                if (count<20) {
                    sb.append(new String(item));
                    sb.append(", ");

                }
                count++;
            } catch (Exception e) {
                throw new IOException(e);
            }
        }

        public long getCount() {
            return count;
        }

        @Override
        public String toString() {
            return sb.toString();
        }
    }
}