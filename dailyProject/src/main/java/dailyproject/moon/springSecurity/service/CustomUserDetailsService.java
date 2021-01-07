package dailyproject.moon.springSecurity.service;

import dailyproject.moon.springSecurity.dao.SecurityDao;
import dailyproject.moon.springSecurity.entity.SecurityUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @program: moon
 * @description:
 * @create: 2020-12-25 14:38
 **/

@Component("CustomUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    SecurityDao securityDao;

    @Override
    public UserDetails loadUserByUsername (String s) throws UsernameNotFoundException {
        s="123";
        if (s==null){
            throw new UsernameNotFoundException("User " + s + " was not found in db");
        }

        SecurityUserData userData = securityDao.getUserData(1);

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        //自定义权限验证中就要配置用户的权限:ROLE_ADMIN 需要加上ROLE_开头
        GrantedAuthority  admin = new SimpleGrantedAuthority("ROLE_"+userData.getRoles());
        grantedAuthorities.add(admin);

        return new org.springframework.security.core.userdetails.User
                //  new BCryptPasswordEncoder().encode("123") 指的是加密以后的密码（应该在注册的时候进行加密存储）
                // 若要解密,可以用匹配函数，如：
                //  new BCryptPasswordEncoder().matches("123",encode)
                (userData.getName(),userData.getPass(),grantedAuthorities);

    }
}
