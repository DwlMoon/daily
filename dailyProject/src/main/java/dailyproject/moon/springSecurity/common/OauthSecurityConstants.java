package dailyproject.moon.springSecurity.common;


public class OauthSecurityConstants {

    /**
     * token请求头名称
     */
    public final static String REQ_HEADER = "Authorization";

    /**
     * token分割符，认证方式
     */
    public final static String TOKEN_SPLIT = "Bearer ";


    public final static String BASIC_ = "Basic ";

    /**
     * jwt签名
     *  public final static String SIGN_KEY = "ZHONGH*-&^1d";
     */
    public final static String SIGN_KEY = "ZERO";
    /**
     * 删除
     */
    public final static String STATUS_DEL = "0";
    /**
     * 正常
     */
    public final static String STATUS_NORMAL = "1";

    /**
     * 锁定
     */
    public final static String STATUS_LOCK = "9";

    /**
     * 未授权信息
     */
    public final static String REQUIRE_LOGIN = "访问的服务需要身份认证，引导用户到登录界面";

    /**
     * 未授权code
     */
    public final static Integer AUTHENTICATION_FAIL = 403;

    /**
     * 编码
     */
    public final static  String UTF8 = "UTF-8";

    /**
     * JSON 资源
     */
    public final static  String CONTENT_TYPE = "application/json; charset=utf-8";

    /**
     * 前缀
     */
    public final static String ZERO_PREFIX = "zero_";
    /**
     * 项目的license
     */
    public final static String ZERO_LICENSE = "made by lanyu";
    /**
     * 基础角色
     */
    public final static String BASE_ROLE = "ROLE_USER";
    /**
     * 授权码模式
     */
    public final static String AUTHORIZATION_CODE = "authorization_code";
    /**
     * 密码模式
     */
    public final static String PASSWORD = "password";
    /**
     * 手机验证码模式
     */
    public final static String MOBILE = "mobile";
    /**
     * 刷新token
     */
    public final static String REFRESH_TOKEN = "refresh_token";
    /**
     * 默认保存code的前缀
     */
    public final static String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY";

    /**
     * 默认生成图形验证码过期时间
     */
    public final static int DEFAULT_IMAGE_EXPIRE = 60;
    /**
     * 默认生成图形验证码宽度
     */
    public final static String DEFAULT_IMAGE_WIDTH = "150";

    /**
     * 默认生成图像验证码高度
     */
    public final static String DEFAULT_IMAGE_HEIGHT = "32";

    /**
     * 默认生成图形验证码长度
     */
    public final static String DEFAULT_IMAGE_LENGTH = "4";
    /**
     * 边框颜色，合法值： r,g,b (and optional alpha) 或者 white,black,blue.
     */
    public final static String DEFAULT_COLOR_FONT = "black";

    /**
     * 图片边框
     */
    public final static String DEFAULT_IMAGE_BORDER = "no";
    /**
     * 默认图片间隔
     */
    public final static String DEFAULT_CHAR_SPACE = "5";
    /**
     * 验证码文字大小
     */
    public final static String DEFAULT_IMAGE_FONT_SIZE = "30";
    /**
     * 手机登录URL
     */
    public final static String MOBILE_TOKEN_URL = "/mobile/token";

    public final static String TOKEN_USER_DETAIL = "token-user-detail";

    /**
     * 登录次数限制前缀
     */
    public final static String LOGIN_FAIL_KEY = "LOGIN_FAIL_KEY";

    /**
     * 登录初始登录次数
     */
    public final static String LOGIN_FAIL_DEFAULT = "1";

    /**
     * 登录错误次数
     */
    public final static Integer LOGIN_FAIL_COUNT = 3;

    /**
     * 默认登录失败过期时间 单位时间
     */
    public final static Integer LOGIN_FAIL_EXPIRE = 10;

    /**
     * 默认手机验证码过期时间
     */
    public final static int DEFAULT_MOBILE_EXPIRE = 180;


}
