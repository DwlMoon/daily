package dailyproject.moon.springSecurity.config;

import dailyproject.moon.springSecurity.common.OauthSecurityConstants;
import dailyproject.moon.springSecurity.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: moon
 * @description: 授权服务器
 * @create: 2020-12-28 16:08
 **/

@Configuration
@EnableAuthorizationServer
public class AuthorizedConfig extends AuthorizationServerConfigurerAdapter {


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomUserDetailsService securityService;


    @Autowired(required = false)
    private RedisConnectionFactory redisConnectionFactory;


    /**
     * 客户端配置 我们的认证服务器会给哪些第三方发令牌
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure (ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory() //基于内存存储
                .withClient("abc")
                .secret(new BCryptPasswordEncoder().encode("long"))
                .scopes("all")
                .redirectUris("http://www.baidu.com/")
                .accessTokenValiditySeconds(7200) //token过期时间7200
                .refreshTokenValiditySeconds(25920000) //更新令牌(此段时间里一直更新token)25920000
                .authorizedGrantTypes(OauthSecurityConstants.REFRESH_TOKEN, OauthSecurityConstants.PASSWORD, OauthSecurityConstants.MOBILE, OauthSecurityConstants.AUTHORIZATION_CODE) //授权模式
                .autoApprove(true);
    }




    /**
     * tokenstore 定制化处理(设置存储token的方式)
     * Spring Security Oauth2 存储Token的方式有多种, 比如JWT、Jdbc（数据库）、Redis等
     * 使用Redis存储Token具有明显的优势
     * @return TokenStore
     */
    @Bean
    public TokenStore redisTokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        tokenStore.setPrefix(OauthSecurityConstants.ZERO_PREFIX);
        return tokenStore;
    }



    /**
     * 定义授权和令牌端点以及令牌服务
     *
     * @param endpoints defines the authorization and token endpoints and the token services.
     * @throws Exception exception
     */
    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //指定认证管理器
                .authenticationManager(authenticationManager)
                //用户账号密码认证
                .userDetailsService(securityService)
                // refresh_token
                .reuseRefreshTokens(false)
                //指定token存储位置
                .tokenStore(redisTokenStore())
                // 配置JwtAccessToken转换器
                .accessTokenConverter(accessTokenConverter());
    }

    /**
     * 定义jwt的生成方式
     *
     * @return JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                final Map<String, Object> additionalInformation = new HashMap<>();
//                UserModel userModel = (UserModel) authentication.getUserAuthentication().getPrincipal();
                //把用户的主键uin放进去
                additionalInformation.put("uin", "lovee");
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
                return super.enhance(accessToken, authentication);
            }
        };
        //非对称加密，但jwt长度过长
//        KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("kevin_key.jks"), "123456".toCharArray())
//                .getKeyPair("kevin_key");
//        converter.setKeyPair(keyPair);
//        return converter;
        //对称加密
        converter.setSigningKey("123");
        return converter;
    }


    /**
     * 定义令牌端点上的安全性约 束
     *
     * @param oauthServer oauthServer defines the security constraints on the token endpoint.
     * @throws Exception exception
     */
    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
        oauthServer.allowFormAuthenticationForClients();

    }



}
