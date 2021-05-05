package main.java.com.edu.config;

import com.edu.pojo.SysUser;
import com.edu.reposity.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class SysSecurityService implements UserDetailsService {
    @Autowired
    private SysUserRepository sysUserRepository;
    @Autowired
    BCryptPasswordEncoder BCryptPasswordEncoder;

    //自定义登录认证过程,根据用户名获取用户信息，如果用户被锁定，则抛出异常
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserRepository.findByName(username);
        System.out.println(sysUser.getName());
        if (null == sysUser){
            throw new UsernameNotFoundException("用户名不存在");
        }
        else if (!sysUser.getEnabled()){
            throw new LockedException("用户被锁定");
        }
        else{
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            User user = new User(sysUser.getUsername(),sysUser.getPassword(),authorities);
            return user;
        }
    }
}