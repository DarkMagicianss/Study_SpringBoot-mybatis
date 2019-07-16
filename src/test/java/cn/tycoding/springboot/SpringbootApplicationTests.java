package cn.tycoding.springboot;

import cn.tycoding.controller.MsgProducer;
import cn.tycoding.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootApplicationTests {

    @Autowired
    MsgProducer msgProducer;

    @Test
    public void contextLoads() {
        User[] users = new User[10];
        for (int i = 0; i < 10; i++) {
            String temp = (i % 2 == 0) ? "a.b" : "b.a";
            users[i] = new User(temp + i, "pass" + i);
        }
        for (int i = 0; i < 10; i++) {
            msgProducer.sendMsg(users[i],users[i].getUsername().substring(0,3));
        }
    }

}
