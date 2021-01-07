package dailyproject.moon.springSecurity.dao;


import dailyproject.moon.springSecurity.entity.SecurityUserData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @program: moon
 * @description: dao
 * @create: 2020-11-19 11:28
 **/
@Mapper
public interface SecurityDao {

    public int insertUserData (@Param("userData") SecurityUserData userData);


    public SecurityUserData getUserData (Integer id);

}
