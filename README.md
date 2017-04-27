# 用户中心(User Center)

# 如何运行本项目
1. 启动arangodb
```
sudo docker run -p 8529:8529 -e ARANGO_ROOT_PASSWORD=root --name user-center-db -d arangodb
```
2. 访问arangodb
```
http://localhost:8529
```
3. 启动服务
* 使用`maven`执行`spring-boot:run`

4. 注册用户
```
[POST] http://localhost:8080/uc/users
Header:
  无
Body:
  {
    "username":"user",
    "password":"123456"
  }
```

5. 在浏览器请求以下链接(以下链接为模拟第三方客户端登录)
```
http://localhost:8080/uc/oauth/authorize?response_type=code&client_id=microblog-client&redirect_uri=http://localhost:8082/microblog-client
```
请求会跳转至登录页面，输入用户名密码(`步骤4`注册的用户)，请求会再次跳转至
```
http://localhost:8082/microblog-client?code=I5l6xR
```
复制跳转链接中的`code`（即这里的`I5l6xR`）

6. 获取`token`
```
[POST] http://localhost:8080/uc/oauth/token?grant_type=authorization_code&redirect_uri=http://localhost:8082/microblog-client&code=I5l6xR
Header:
  Authorization:Basic bWljcm9ibG9nLWNsaWVudDpzZWNyZXQxMjM=
Body:
  无
```

`Header`里的`Authorization`为`clientId`:`clientSecret`进行`Base`加密得来的，可以到[这里](http://tool.chinaz.com/Tools/Base64.aspx)
响应如下：
```
{
  "access_token": "f5970310-ed94-4c0f-8acb-809fe0e626c8",
  "token_type": "bearer",
  "expires_in": 43199,
  "scope": "read"
}
```

7. 向`Resource Server`请求
我们从上一步中得到了`access_token`就可以使用它来请求资源服务器，这里以[microblog](https://github.com/kimloong/microblog)项目为例
```
[GET] http://localhost:8081/microblog/hello
Header:
  Authorization Bearer f5970310-ed94-4c0f-8acb-809fe0e626c8
```

# 相关请求
## [客户端首面](http://localhost:8081/microblog/index.html)


## 待完成特性
* 动态Client
* scope显示


## 已完成特性
* 基于OAuth2的用户认证服务
* 基于JDBC用户认证
* 初步使用ArangoDB进行用户数据存储，及联调完成`OAuth2Provider`、`ResourceServer`及`Client`三者的交互


## 版本履历
1. 初步完成使用Arangodb做为用户数据存储，并实现用户注册及用户登录功能
2. 调试完成Client接入
3. 初步使用ArangoDB进行用户数据存储，及联调完成`OAuth2Provider`、`ResourceServer`及`Client`三者的交互

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

# 参考文档
>