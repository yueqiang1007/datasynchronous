package com.datasynchronous.controller;

import com.datasynchronous.service.bigqueueService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by Administrator on 2017/8/1.
 */

public class controllerTest {

    @Autowired
    private SchedulingConfig schedulingConfig;
    bigqueueService bi = new bigqueueService();
    @Test
    public void test1() throws IOException {
        bi.save();
    }

}
