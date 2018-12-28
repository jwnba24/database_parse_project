package com.jwnba24.database_parse_project;

import com.jwnba24.database_parse_project.service.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Created by jiwen on 2018/12/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestInsert {
    @Resource
    private TestService testService;
    @Test
    public  void testInvoke(){
        String sql = "insert into table1 (col1,col2) values (jiwen1,jiwen2)";
        testService.testInsert(sql);
    }
}
