package taotaomall.config;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import taotaomall.utils.JWTUtils;
import taotaomall.utils.Result;
import taotaomall.utils.ResultCodeEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JWTInterceptors implements HandlerInterceptor {
    //ctrl + i
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 设置CORS响应头（每次都设置）
        response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Authorization,token");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // 放行OPTIONS预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return false;
        }

        // 放行登录和注册接口
        String uri = request.getRequestURI();
        if (uri.contains("/user/login") || uri.contains("/user/register")) {
            return true;
        }

        // 支持 Authorization 和 token 两种头
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }

        String message = "";
        try {
            JWTUtils.verify(token);
            return true; // 直接放行
        } catch (SignatureVerificationException e) {
            message = "无效签名";
        } catch (TokenExpiredException e) {
            message = "token过期了！";
        } catch (AlgorithmMismatchException e) {
            message = "算法不一致";
        } catch (Exception e) {
            message = "token为空或无效！";
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(Result.failure(ResultCodeEnum.UNAUTHORIZED, message));
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }
}
