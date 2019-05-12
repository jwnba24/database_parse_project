package com.jwnba24.database_parse_project;

import com.jwnba24.database_parse_project.MySdkTest.SDK;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by jiwen on 2018/12/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestInsert {
    @Autowired
    private SDK sdk;

    @Test
    public void test(){
        sdk.listener();
    }
}
