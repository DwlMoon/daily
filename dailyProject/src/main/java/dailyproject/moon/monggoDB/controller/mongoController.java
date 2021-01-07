package dailyproject.moon.monggoDB.controller;

import dailyproject.moon.monggoDB.Bo.MongoBo;
import dailyproject.moon.monggoDB.monggoDao.MongoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @program: moon
 * @description:
 * @create: 2021-01-07 16:13
 **/

@RestController
@RequestMapping("mongo")
public class mongoController {

    @Autowired
    MongoDao mongoDao;


    private String id;
    private Integer price;
    private String name;
    private String info;
    private Integer age;
    private String publish;
    private Date createTime;
    private Date updateTime;

    @RequestMapping(value = "saveData",method = {RequestMethod.GET})
    private String testMongo1(){
        mongoDao.saveTest(new MongoBo("1",15,"张三","lack",32,"abc",new Date(),new Date()));
        return "success";
    }

    @RequestMapping(value = "findData",method = {RequestMethod.GET})
    private String testMongo2(){
        MongoBo test = mongoDao.findTestByName("张三");
        return "success";
    }

}
