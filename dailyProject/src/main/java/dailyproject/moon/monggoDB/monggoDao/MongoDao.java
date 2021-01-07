package dailyproject.moon.monggoDB.monggoDao;

import dailyproject.moon.monggoDB.Bo.MongoBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * @program: moon
 * @description: 操作mongo数据库
 * @create: 2021-01-07 16:08
 **/
@Component
public class MongoDao {

    @Autowired
    MongoTemplate mongoTemplate;


    /**
     * 创建对象
     */
    public void saveTest(MongoBo test) {
        mongoTemplate.save(test);
    }

    /**
     * 根据用户名查询对象
     * @return
     */
    public MongoBo findTestByName(String name) {
        Query query=new Query(Criteria.where("name").is(name));
        MongoBo mgt =  mongoTemplate.findOne(query , MongoBo.class);
        System.out.println(mgt);
        return mgt;
    }

    /**
     * 更新对象
     */
    public void updateTest(MongoBo test) {
        Query query=new Query(Criteria.where("id").is(test.getId()));
        Update update= new Update().set("age", test.getAge()).set("name", test.getName());
        //更新查询返回结果集的第一条
        mongoTemplate.updateFirst(query,update,MongoBo.class);
        //更新查询返回结果集的所有
        // mongoTemplate.updateMulti(query,update,TestEntity.class);
    }

    /**
     * 删除对象
     * @param id
     */
    public void deleteTestById(Integer id) {
        Query query=new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query,MongoBo.class);
    }


}
