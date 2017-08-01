package com.datasynchronous.service;

import com.bigqueue.BigArrayImpl;
import com.bigqueue.BigQueueImpl;
import com.bigqueue.IBigQueue;
import com.bigqueue.utils.TestUtil;
import com.datasynchronous.controller.SchedulingConfig;
import com.datasynchronous.utils.Acceder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */
//@Service
public class bigqueueService {


    Acceder acceder = new Acceder();

    private String testDir = TestUtil.TEST_BASE_DIR + "bigqueue/unit";
    private IBigQueue bigQueue;

    public void queue() throws IOException {
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

            SchedulingConfig.DefaultItemIterator dii = new SchedulingConfig.DefaultItemIterator();
            bigQueue.applyForEach( dii );
            System.out.println( "[" + dii.getCount() + "] " + dii.toString() );//统计计数，以及打印出元素

        }
    }
    //添加元素到队列
    public void save() throws IOException {
        bigQueue = new BigQueueImpl( testDir, "testApplyForEachDoNotChangeTheQueue", BigArrayImpl.MINIMUM_DATA_PAGE_SIZE );

        List<List<String>> list = acceder.textSql();
        for (List<String> sqllist :list){
            for (String str:sqllist){
                bigQueue.enqueue( str.getBytes() );//添加元素
            }
        }

        for (int i = (int) bigQueue.size(); i>0; i--){
            byte[] by = bigQueue.dequeue();
            String str = new String(by);
            SchedulingConfig.DefaultItemIterator dii = new SchedulingConfig.DefaultItemIterator();
            bigQueue.applyForEach( dii );
            System.out.println( "[" + dii.getCount() + "] " + dii.toString() );//统计计数，以及打印出元素
        }

    }
    //读取指定文件夹
    public void lireQueue(){

    }


    public void clean() throws IOException {
        if (bigQueue != null) {
            bigQueue.removeAll();
        }
    }

    private class DefaultItemIterator implements IBigQueue.ItemIterator {
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
