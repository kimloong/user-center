# 用户中心(User Center)

# 相关请求
## [客户端首面](http://localhost:8081/microblog/index.html)


## 待完成特性
* 动态Client
* 扩展用户相关接口及信息，如用户注册等
* scope显示
* 与内部client进行token共享


## 已完成特性
* 基于OAuth2的用户认证服务
* 基于JDBC用户认证


## 版本履历
1. 初步完成使用Arangodb做为用户数据存储，并实现用户注册及用户登录功能
2. 调试完成Client接入

## 解决问题
### `Client`接入，登录验证成功携带`code`返回`Client`时，报`401 Unauthorized`
* 问题描述
`Client`控制台报错信息
```
There was an unexpected error (type=Unauthorized, status=401).
Authentication Failed: Could not obtain access token
.....
Possible CSRF detected - state parameter was present but no state could be found
```
* 问题原因
通过源码跟踪，发现登录验证成功后在类`AuthorizationCodeAccessTokenProvider`获取`preservedState`为空而抛出了异常，猜可能跟`cookie`同源机制有关

* 解决方案
将`Authorization Server`与`Client`的`Context Path`设置成不一样即可，在两者不同host时，则不会有问题。

> [Unable to get EnableOauth2Sso Working — BadCredentialsException: Could not obtain access token](http://stackoverflow.com/questions/31383514/unable-to-get-enableoauth2sso-working-badcredentialsexception-could-not-obta)

### 登录验证成功且请求完`access token`后（`/oauth/check_token`阶段）报错
* 问题描述
报`Client`报`500 Internal Server Error`，`Server`报`403 Forbidden`

* 问题原因
`Server`端的`/oauth/check_token`默认为`denyAll()`,所以无法进请求成功

* 解决方案
``` java
public class OAuth2ProviderConfig extends AuthorizationServerConfigurerAdapter {

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients()
                .checkTokenAccess("fullyAuthenticated");
    }
}
```
> [How to use RemoteTokenService?](http://stackoverflow.com/questions/26250522/how-to-use-remotetokenservice)