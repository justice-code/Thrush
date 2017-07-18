package org.eddy.cookie;

import org.eddy.CaptchaController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Justice-love on 2017/7/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(CaptchaController.class)
public class ControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void test() throws Exception {
        mvc.perform(post("/captcha/login").param("pipeline", "pipeline").param("numbers", "1,2,3,4,5").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}
