package dailyproject.moon.zookeeper.dao;

import dailyproject.moon.zookeeper.entity.UserData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @program: moon
 * @description: dao
 * @create: 2020-11-19 11:28
 **/
@Mapper
public interface ZkDao {

    public int insertUserData (@Param("userData") UserData userData);


    public UserData getUserData (Integer id);

}
