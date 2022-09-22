## 单点登录相关示例说明

下面以客户端地址(client.com)，ssoserver地址(sso.com)举例

### 统一登录交互步骤：

    1. 用户访问客户端首页("http://client.com/index")
    
    2. 页面检测到用户未登录则携带back参数用来记录当前页面地址，跳转到客户端登录地址("http://client.com/custom/login?back='http://client.com/index'")。
    
    3. 客户端登录接口构建登录回调地址redirect并将请求重定向到ssoserver的认证地址("http://sso.com/sso/auth/?redirect='http://client.com/custom/login?back="http://client.com/index"'"),back作为redirect地址中的一个参数，所以url中会有两个问号。
    
    4. ssoserver的认证接口判断用户
    
        未登录：
    
        给用户返回统一登录页面，用户在统一登录页面成功登录后，ssoserver签发ticket并将请求重定向到第3步中提供的redirect地址("http://client.com/custom/login?ticket=xxxxxx&back='http://client.com/index'")
    
        已登录：
    
        直接签发ticket并将请求重定向到第3步中提供的redirect地址("http://client.com/custom/login?ticket=xxxxxx&back='http://client.com/index'")
    
    5. 客户端的登录回调地址在接收到重定向请求后，判断ticket有值，使用ticket参数向ssoserver发起请求来校验ticket("http://sso.com/sso/checkTicket), 验证成功后会得到登录人的userId和mobile, 
       客户端根据响应的结果来对用户进行客户端的登陆操作, 在校验ticket时会携带客户端的统一登出地址("http://client.com/custom/logout_notify"), 当有任意客户端执行了登出操作后， ssoserver会通知其他相关的客户端进行统一登出操作。

### 统一登出交互步骤：
    
    1. 用户在客户端页面请求客户端登出接口("http://client.com/custom/logout")
    
    2. 客户端登出接口携带用户的userId请求ssoserver的登出接口("http://sso.com/sso/logout")
    
    3. ssoserver的登出接口验证参数有效的情况下，携带userId调用其他相关客户端的统一登出接口("http://client.com/custom/logout_notify")
    
    4. 各个客户端在接收到ssoserver的登出通知时进行客户端的用户登出操作。
    
### ssoserver接口文档

#### 统一登录
   
    
    url: /sso/auth
    type: GET
    params: 
        redirect String 必须
    response: 
        redirect:${redirect}?ticket
    
    

#### 检查ticket

    
    url: /sso/checkTicket
    type: POST
    content-type: application/json
    params: 
        ticket String 必须
        ssoLogoutCall String 必须
        timestamp Long 必须
        clientCode String 必须
        signature String 必须
    response:
        {
            "status": 1, 
            "message": "success", 
            "data": {
                "userId": "", 
                "mobile": "", 
                "cfcaKeyId": ""
            }
        }

#### 统一登出


    url: /sso/logout
    type: POST
    content-type: application/json
    params: 
        userId String 必须
        timestamp Long 必须
        clientCode String 必须
        signature String 必须
    response:
        {
             "status": 1, 
             "message": "success", 
             "data": null
        }
    
    
        
    
        
#### 推送用户


    url: /sso/pushUser
    type: POST
    content-type: application/json
    params: 
        mobile String 必须
        company Strubg 必须
        uscc String 必须
        cfcaKeyId String 必须
        realName String 必须
        idCard String 必须
        timestamp Long 必须
        clientCode String 必须
        signature String 必须
    response:
        {
             "status": 1, 
             "message": "success", 
             "data": userId
        }

#### 获取用户详情


    url: /sso/userInfo
    type: POST
    content-type: application/json
    params: 
        userId String 必须
        timestamp Long 必须
        clientCode String 必须
        signature String 必须
    response:
        {
             "status": 1, 
             "message": "success", 
             "data": {
                  "userId": "",
                  "mobile": "",
                  "cfcaKeyId": "",
                  "company": "",
                  "uscc": "",
                  "realName": "",
                  "idCard": "",
             }
        }

### 签名步骤说明

    1. 将所有除signature之外的参数以`key=value`格式按照key的ASCII码顺序进行排序并使用字符 “&” 连接
    
    2. 将secretKey参数拼接到步骤1中生成的字符串之后
    
    3. 将步骤2中生成的字符串进行sha256编码并转大写
    
    具体代码在SSOSignUtil已有实现。
    
### 其他注意事项


    1. clientCode 和 clientSecretKey 由统一认证中心分配，统一认证中心做白名单管理。
    
    2. 客户端的相关接口路径/custom/login /custom/login_verify /custom/logout /custom/logout_notify /index 皆为举例，各客户端可自定义。



