package dailyproject.moon.common.filterAndInterceptor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/***
 * @author mazaiting
 * @date 2019-06-27
 * @decription HttpServletRequest 包装器
 * 解决: request.getInputStream()只能读取一次的问题
 * 目标: 流可重复读
 */
@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper {
    /**
     * 日志
     */
    private static final Logger mLogger = LoggerFactory.getLogger(RequestWrapper.class);

    /**
     * 请求体
     */
    private String body;

    public RequestWrapper(HttpServletRequest request) {
        super(request);

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader=null;
        ServletInputStream inputStream=null;

        try {
            inputStream = request.getInputStream();
            if (inputStream != null){
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] chars = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(chars)) > 0) {
                    stringBuilder.append(chars, 0, bytesRead);
                }
            }else {
                stringBuilder.append("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
            body = stringBuilder.toString();
        }

    @Override
    public ServletInputStream getInputStream () throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }
            @Override
            public boolean isReady() {
                return false;
            }
            @Override
            public void setReadListener(ReadListener readListener) {
            }
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
        return servletInputStream;
    }

    @Override
    public BufferedReader getReader () throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }


    public String getBody(){
        return this.body;
    }

}
