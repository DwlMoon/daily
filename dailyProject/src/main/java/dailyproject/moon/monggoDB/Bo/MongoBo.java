package dailyproject.moon.monggoDB.Bo;

import lombok.Data;

import java.util.Date;

/**
 * @program: moon
 * @description:
 * @create: 2021-01-07 16:10
 **/

@Data
public class MongoBo {

    private String id;
    private Integer price;
    private String name;
    private String info;
    private Integer age;
    private String publish;
    private Date createTime;
    private Date updateTime;

    public MongoBo (String id, Integer price, String name, String info, Integer age, String publish, Date createTime, Date updateTime) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.info = info;
        this.age = age;
        this.publish = publish;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
