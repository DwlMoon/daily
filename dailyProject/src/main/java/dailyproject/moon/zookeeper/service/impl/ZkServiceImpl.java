package dailyproject.moon.zookeeper.service.impl;

import dailyproject.moon.zookeeper.dao.ZkDao;
import dailyproject.moon.zookeeper.entity.UserData;
import dailyproject.moon.zookeeper.service.ZkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: moon
 * @description:
 * @create: 2020-11-19 11:43
 **/

@Service
@Transactional(rollbackFor = Exception.class)
public class ZkServiceImpl implements ZkService {

    @Autowired
    ZkDao zkDao;

    @Override
    public int insertUserData (UserData userData) {
        int data = zkDao.insertUserData(userData);
        return data;
    }

    @Override
    public UserData getUserData (Integer id) {
        return zkDao.getUserData(id);
//        return null;
    }
}
