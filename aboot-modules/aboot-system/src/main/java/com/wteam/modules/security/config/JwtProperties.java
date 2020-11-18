package com.wteam.modules.security.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author mission
 * @since 2019/07/10 10:27
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /*Request Headers :Authorization*/
    private String header="Authorization";
    /*令牌前缀, 必须使用最少88位的Base64对该令牌进行编码*/
    private String secret="ZmQyZGI5NjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE2ODVkZDQ5Y2Y3OGYwZWE3NzMzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzM=";
    /*令牌过期时间 1小时 = 3600000 毫秒 ,7天*/
    private Long expiration=604800000L;
    /*token 续期检查时间（默认30分钟，单位毫秒），在token即将过期的一段时间内用户操作了，则给用户的token续期*/
    private Long detect=1800000L;
    /* 续期时间，默认1小时，单位毫秒*/
    private Long renew=3600000L;
    /*在线登录用户key ,用于redis*/
    private String onlineKey="online-token";
    /*验证码key*/
    private String codeKey ="code-key";
    /*只允许一个设备登录 */
    private Boolean singleLogin= false;

}
