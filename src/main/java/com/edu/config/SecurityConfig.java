package main.java.com.edu.config;

import com.edu.handler.MyAuthenticationFailHandler;
import com.edu.handler.MyAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SysSecurityService sysSecurityService;
    @Resource
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Resource
    private MyAuthenticationFailHandler myAuthenticationFailHandler;
    @Autowired
    private DataSource dataSource;

    //http配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/admin/**")
                .formLogin().loginPage("/admin/login")
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailHandler)
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/admin/login");
        //认证权限
        http.authorizeRequests()
                .antMatchers("/admin/login").permitAll()
                //rbac 访问admin/rbac时，使用自定义hasPermission验证是否允许访问
                .antMatchers("/admin/list", "/admin/add", "/admin/del")
                .access("@rbacService.hasPermission(request , authentication) ");
        //记住我
        http.rememberMe()
                .key("test")
                .tokenRepository(new JdbcTokenRepositoryImpl())
                .tokenValiditySeconds(24 * 60 * 60);
        //csrf忽略的请求配置
//       http.csrf().ignoringAntMatchers("");
        //关闭csrf
        http.csrf().disable();
    }

    //认证配置
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(sysSecurityService).passwordEncoder(new BCryptPasswordEncoder());
    }

    // 密码编译器
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //remember_me中，jdbc持久化操作
    @Autowired
    JdbcTokenRepositoryImpl tokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        //自动创建保存token持久化的表
        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }
}
