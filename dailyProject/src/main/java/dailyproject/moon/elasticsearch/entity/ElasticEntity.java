package dailyproject.moon.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @program: moon
 * @description:
 * @create: 2020-11-23 17:52
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElasticEntity {

    /**
     * 主键标识，用户ES持久化
     */
    private String id;

    /**
     * JSON对象，实际存储数据
     */
    private Map data;
}
