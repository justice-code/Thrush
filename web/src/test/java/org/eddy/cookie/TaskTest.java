package org.eddy.cookie;

import org.eddy.ApplicationStart;
import org.eddy.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Justice-love on 2017/7/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ApplicationStart.class})
public class TaskTest {

    @Autowired
    private TaskService taskService;

    @Test
    public void test() throws Exception {
        taskService.submit();
        taskService.submit();
        taskService.submit();
        taskService.submit();
        Thread.sleep(1_000);
    }
}
