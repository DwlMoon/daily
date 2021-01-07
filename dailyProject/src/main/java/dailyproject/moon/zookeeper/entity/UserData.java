package dailyproject.moon.zookeeper.entity;

import lombok.Data;

/**
 * @program: moon
 * @description: 用户列
 * @create: 2020-11-19 11:29
 **/

@Data
public class UserData {

    private Integer id;

    private String name;

    private Integer age;

    private Integer sex;

    public UserData (Integer id, String name, Integer age, Integer sex) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
    }
}
