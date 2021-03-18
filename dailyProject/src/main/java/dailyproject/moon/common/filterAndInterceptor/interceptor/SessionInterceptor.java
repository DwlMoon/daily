package dailyproject.moon.common.filterAndInterceptor.interceptor;

import dailyproject.moon.common.filterAndInterceptor.RequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @program: game
 * @description:
 * @create: 2021-03-18 11:25
 **/

@Component
@Slf4j
public class SessionInterceptor implements HandlerInterceptor {

    /**
     * 日志
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("========================SessionInterceptor================================");
        // 获取地址
        String url = request.getRequestURL().toString();
        System.out.println("url: " + url);
        // 获取 session
        HttpSession session = request.getSession();
        String id = session.getId();
        System.out.println("sessionId: " + id);
        String requestMethod = request.getMethod();
        System.out.println("requestMethod: " + requestMethod);
        String servletPath = request.getServletPath();
        System.out.println("servletPath: " + servletPath);

        if (isJson(request)) {
            String body = new RequestWrapper(request).getBody();
            System.out.println("body: " + body);
        }
        System.out.println("=================================================================");

        return true;
    }

    /**
     * 判断本次请求的数据类型是否为json
     *
     * @param request request
     * @return true: 是 JSON 数据; false: 非 json 数据
     */
    private boolean isJson(HttpServletRequest request) {
        if (request.getContentType() != null) {
            return request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE) ||
                    request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE);
        }

        return false;
    }
}