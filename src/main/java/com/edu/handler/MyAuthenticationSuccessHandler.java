package main.java.com.edu.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//请求验证成功处理器
@Component("myAuthenticationSuccessHandler")
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    /**
     *     * 用户名和密码正确时执行
     *     * @param request   请求
     *     * @param response   响应
     *     * @param authentication   已认证主体
     *     * @throws ServletException
     *     * @throws IOException
     *    
     */
    @Override
    public void
    onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {
        //通过authentication获取用户认证信息
        Object principal = authentication.getPrincipal();
        //判断获取到的用户认证信息是否符合
        if (null != principal && principal instanceof UserDetails) {
            UserDetails user = (UserDetails) principal;
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            String msg = "{\"status\":\"ok\",\"message\":\"登录成功\"}";
            writer.write(msg);
            writer.flush();
            writer.close();
        }
    }
}