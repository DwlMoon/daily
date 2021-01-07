package dailyproject.moon.zookeeper.service;


import dailyproject.moon.zookeeper.entity.UserData;

/**
 * @program: moon
 * @description:
 * @create: 2020-11-19 11:28
 **/

public interface ZkService {
    public int insertUserData (UserData userData);


    public UserData getUserData (Integer id);

}
