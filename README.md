# aboot
aboot系统
> 基于eladmin的后端开发。采用SpringBoot2+Jpa+SpringSecurity+jwt+redis+Vue 共同打造的前后端分离的管理系统。
> 通过安全,放心,底成本,最方便维护的开发模式，为各企业带来最好的开发服务。
## 快速运行
导入项目到开发工具，推荐`Intellij IDEA`
##### 1.检查`JDK8`、`Mysql5.7+`和`Redis 6+`已经正常运行。
> 若缺少环境，请自行安装。

##### 2.配置连接库、端口号、静态文件目录。
aboot-starter模块下src\main\resources包打开application-dev.yml
``` yaml
  datasource:
    ## 配置数据源
    url: jdbc:log4jdbc:mysql://127.0.0.1:3306/aboot4_beta?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root
```
```yaml
  flyway:
    # 启用或禁用 flyway
    enabled: true
    # 需要 flyway 管控的 schema list,这里我们配置为flyway  缺省的话, 使用spring.datasource.url 配置的那个 schema,
    # 可以指定多个schema, 但仅会在第一个schema下建立 metadata 表, 也仅在第一个schema应用migration sql 脚本.
    # 但flyway Clean 命令会依次在这些schema下都执行一遍. 所以 确保生产 spring.flyway.clean-disabled 为 true
    schemas: aboot4_beta
```
```yaml
  #配置 redis
  redis:
    database: 1
    host: 127.0.0.1
    port: 6379
    password:
```
```yaml
## 配置端口号和静态文件目录
server:
  port: 8087
# 文件存储路径
file:
  url-prefix: /file/ #"/"结尾 自定义上传时字段无意义
  path: D:\projectdev\aboot
  maxSize: 100   # 文件大小 /M
```

##### 3.创建一个指定的空数据库, 编码为utf8mb4
```sql
CREATE DATABASE IF NOT EXISTS `aboot4_beta`  DEFAULT CHARACTER SET utf8mb4 ;
```

##### 4.指定JDK8
若使用Intellij IDEA，则在File->Project structure中打开,
`Project SDK`选择为 `1.8`
`Project Language Level` 选择为 8-Lambdas

##### 5. 安装`lombok`插件
若使用Intellij IDEA，则在File->Settings中打开,
选择Plugins, 搜索lombok, 选择第一个安装, 然后重启开发工具

##### 5. 安装进maven
在项目跟目录下运行
```yaml
mvn clean install -Dmaven.test.skip=true
```
##### 7. 运行
aboot-starter模块中src\main\java打开
```
com.wteam.AbootRun
```
> 运行main方法，如果运行正确日志最后会打印访问页面路径。

#### FAQ
##### Q: 我运行不起来?
- 确保选择使用JDK8
- 刷新Maven maven->reimport
- `maven clean`、`maven compile`、`maven package`、`maven install`命令多运行几次，确定jar包已经安装好
##### Q: 我运行报错?
- 确保MySQL数据库已经创建，同时删除所有表
- 确保Redis在跑，且连接正确
##### Q: 我运行有点慢?
- 暂时关闭权限生成
```yaml
# 是否允许permission生成
permission:
  generate: false
```
- 暂时关闭flyway版本控制 [[关于flyway]](/aboot-system/src/main/resources/db/migration/README.md)
```yaml
  flyway:
    # 启用或禁用 flyway
    enabled: false
```
##### Q: 我想运行前端?
要求安装`node.js`环境和`cnpm`
front-end目录下运行命令
安装：在项目目录下`cnpm install`
运行：在项目目录下`cnpm run serve`
##### Q：我想热部署开发
> 安装并激活JRebel插件运行
##### Q: 我想打包部署?
- aboot-system模块下找到`package`脚本，根据系统环境运行相应的package脚本
- 运行后在aboot-system模块下的target目录下找到压缩包，该压缩包即是打包部署文件，
- 在config目录中修改配置文件后，在bin目录启动项目
## 快速开发
首先创建数据库`表`，表中包括表注释和字段注释
然后登录Web系统 默认账号admin 密码123456
打开`代码生成`菜单，生成器配置后一键生成代码

:egg:心情不错，赏赠支持作者：z1165996866[支付宝]