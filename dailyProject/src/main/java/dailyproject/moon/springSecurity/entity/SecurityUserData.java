package dailyproject.moon.springSecurity.entity;

import lombok.Data;

/**
 * @program: moon
 * @description:
 * @create: 2020-12-25 14:06
 **/

@Data
public class SecurityUserData {
    private Integer id;
    private String name;
    private String pass;
    private String roles;
}
