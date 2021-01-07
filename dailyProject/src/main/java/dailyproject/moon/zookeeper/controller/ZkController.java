package dailyproject.moon.zookeeper.controller;

import dailyproject.moon.zookeeper.entity.UserData;
import dailyproject.moon.zookeeper.service.ZkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: moon
 * @description:
 * @create: 2020-11-19 10:31
 **/

@RestController
@RequestMapping(value = "/testzk")
public class ZkController {

    @Autowired
    ZkService zkService;

    @RequestMapping(value = "/insertZkData",method = {RequestMethod.GET,RequestMethod.POST})
    public String insertZkData(){
        zkService.insertUserData(new UserData(2,"张三",2,2) );
        return "nice";
    }

    @RequestMapping(value = "/getZkData",method = {RequestMethod.GET,RequestMethod.POST})
    public UserData getZkData(){
        UserData userData = zkService.getUserData(2);
        return userData;
    }




}
