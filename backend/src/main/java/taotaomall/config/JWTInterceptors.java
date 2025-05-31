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
        String uri = request.getRequestURI();

        // 放行不需要校验token的接口和静态资源
        if (uri.equals("/api/goods/new") || uri.equals("/api/goods/hot") || uri.startsWith("/img/")||uri.equals("/api/alipay/toSuccess")) {
            return true;
        }

        String message ="";
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                if (authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                } else {
                    token = authHeader; // 兼容前端直接传token
                }
            }
        }

        message = "";
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
