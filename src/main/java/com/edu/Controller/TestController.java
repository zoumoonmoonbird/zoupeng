package main.java.com.edu.Controller;

import com.edu.config.SecurityConfig;
import com.edu.pojo.SysUser;
import com.edu.reposity.SysUserRepository;
import com.edu.service.Impl.SysUserServiceImpl;
import com.edu.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@Controller
@RequestMapping("admin")
public class TestController {
    @Autowired
    SysUserRepository sysUserRepository;

    @GetMapping("/login")
    public String toLogin() {
        return "Login";
    }

    @GetMapping("/list")
    @ResponseBody
    public String list() {
        return "list";
    }

    @GetMapping("/add")
    @ResponseBody
    public String add() {
        return "add";
    }

    @GetMapping("/del")
    @ResponseBody
    public String del() {
        return "del";
    }

    @GetMapping("/update")
    @ResponseBody
    public String update(){
        SysUser sysUser = sysUserRepository.findByName("admin");
        sysUser.setPassword(new BCryptPasswordEncoder().encode("123456"));
        sysUserRepository.save(sysUser);
        return "修改成功";
    }
}