package dailyproject.moon.springSecurity.controller;

import dailyproject.moon.springSecurity.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: moon
 * @description:
 * @create: 2020-12-24 17:32
 **/

@Controller
@RequestMapping(value = "SecurityController")
public class SecurityController {

    @Autowired
    SecurityService securityService;

    @RequestMapping(value = "insertUserData",method = {RequestMethod.GET,RequestMethod.POST})
    public String insertUserData(){
        securityService.insertUserData();
        return "success";
    }


    @RequestMapping(value = "test",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String test(){
        return "success";
    }

    @RequestMapping(value = "fail",method = {RequestMethod.GET,RequestMethod.POST})
    public String fail(){
        return "fail";
    }

    @RequestMapping(value = "login",method = {RequestMethod.GET,RequestMethod.POST})
    public String login(){
        return "haha";
    }




}
