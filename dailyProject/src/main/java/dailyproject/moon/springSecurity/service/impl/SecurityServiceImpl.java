package dailyproject.moon.springSecurity.service.impl;

import dailyproject.moon.springSecurity.dao.SecurityDao;
import dailyproject.moon.springSecurity.entity.SecurityUserData;
import dailyproject.moon.springSecurity.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @program: moon
 * @description:
 * @create: 2020-12-25 14:12
 **/

@Service
@Transactional
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    SecurityDao securityDao;

    @Override
    public int insertUserData () {
        SecurityUserData userData = new SecurityUserData();
        userData.setName("luka");
        String encode = new BCryptPasswordEncoder().encode("123");
        userData.setPass(encode);
        userData.setRoles("USER");
        securityDao.insertUserData(userData);
        return 0;
    }



    @Override
    public SecurityUserData getUserData (Integer id) {
        SecurityUserData userData = securityDao.getUserData(1);

        return null;
    }


}
