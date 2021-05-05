package main.java.com.edu.service.Impl;

import com.edu.pojo.SysRole;
import com.edu.pojo.SysUser;
import com.edu.reposity.SysPermissionRepository;
import com.edu.reposity.SysUserRepository;
import com.edu.service.RbacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Component("rbacService")
public class RbacServiceImpl implements RbacService {
    @Autowired
    private SysPermissionRepository sysPermissionRepository;
    @Autowired
    private SysUserRepository sysUserRepository;
    private AntPathMatcher antPathMatcher = new AntPathMatcher(); 
    /**
     *     * 实现rbac模型接口 重写hasPermission用于判断当前用户
     * 的权限信息
     *     *
     *     * 这里应该注入用户和该用户所拥有的权限（权限在登录成功的
     * 时候已经保存起来，当需要访问该用户的权限时，直接从缓存取出！）
     *     * 然后验证该请求是否有权限，有就返回true，否则则返回
     * false不允许访问该Url。
     *     * 还传入了request,可以使用request获取该次请求的类型。
     *     * 根据restful风格使用它来控制我们的权限
     *     * 例如当这个请求是post请求，证明该请求是向服务器发送一个
     * 新建资源请求，
     *     * 我们可以使用request.getMethod()来获取该请求的方式，
     * 然后在配合角色所允许的权限路径进行判断和授权操作！
     *     * 如果能获取到Principal对象不为空证明，授权已经通过
     *    
     */
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        //获取用户认证信息
        Object principal = authentication.getPrincipal();
        boolean hasPermission = false;
        //判断数据是否为空 以及类型是否正确
        if (null != principal && principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            //用户的url权限
            Set<String> urls = new HashSet<>();
            //保存用户的资源权限
            Set<String> curds = new HashSet<>();
            SysUser sysUser = sysUserRepository.findByName(username);
            //通过获取的认证信息，将用户的url权限以及资源权限进行封装
            for (SysRole role : sysUser.getRoles()) {
                role.getPermissions().forEach(sysPermission -> {
                    urls.add(sysPermission.getUrl());
//curds.add(sysPermission.getPermission() +
                    sysPermission.getUrl();
                });
            }
            //通过实际的url和保存的允许访问的url权限进行对
            for (String url : urls) {
                if (antPathMatcher.match(url, request.getRequestURI())) {
                    hasPermission = true;
                    break;
                }
            }
        }
        return hasPermission;
    }
}
