package dailyproject.moon.springSecurity.service;


import dailyproject.moon.springSecurity.entity.SecurityUserData;

/**
 * @program: moon
 * @description:
 * @create: 2020-11-19 11:28
 **/

public interface SecurityService {

    public int insertUserData ();


    public SecurityUserData getUserData (Integer id);

}
