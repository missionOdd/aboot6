1.运行环境
    系统: WIN、LINUX、MAC
    环境: JDK8
    数据库：Redis6+、MySQL5.7+或MariaDb10+

2.运行方式
    2.1 WINDOW环境下运行
        进入config目录，打开application-prod.yml文件，修改mysql和redis连接信息
        然后,进入bin目录,双击startup.bat
        若想关闭项目,直接关闭命令窗口
    2.2 LINUX或MAC环境下运行
        进入config目录，打开application-prod.yml文件，修改mysql和redis连接信息
        然后,进入bin目录,输入命令
        1) cd xxx/bin                               # 切换到根目录
        2) chmod u+x *.sh                           # 设置脚本权限
        3) vi +':w ++ff=unix' +':q' init.sh         # 脚本转码
        4) bash init                                # 执行初始化命名
        6) bash startup.sh                          # 执行启动命令
        若想关闭项目,执行bash shutdown.sh
        若想重启项目,执行bash restart.sh

3.运行遇到问题
    3.1.问题描述：运行linux脚本时,无权限
        chmod u+x *.sh
    3.2.问题描述：运行linux脚本时。项目在WIN打包，上传到linux服务器，会出现如下错误：
        -bash: ./start.sh: /bin/sh^M: bad interpreter: No such file or directory
        解决方案：这是win的编码引起的，win环境下编辑是doc格式，linux下需要unix格式的文件。
        可通过如下解决。
        3.2.1.查看该文件：vim startup.sh
        3.2.2.查看该错误文件的格式（一般报错的文件格式是DOS）：
        :set ff
        3.2.3.修改该文件格式为UNIX：
        :set ff=unix
        3.2.4.再保存。
        :wq!
    3.3.运行linux脚本时，遇到上述权限问题和编码问题，除了手动执行3.1和3.2的命令之外,
        我们提供了init.sh批处理脚本，可以通过执行该脚本进行批量修复处理。
        不过，该脚本也会存在权限问题和编码问题，请按照3.1和3.2方法手动命令修复该脚本。

4. 测试是否运行
    进入config目录，打开application-prod.yml文件，server.port查看端口号;
    window环境，请用浏览器打开http://localhost:{port}
    LINUX或MAC环境，执行命令 curl http://localhost:{port}
    若运行日志无报错，访问返回结果:"Welcome Aboot System !"，则说明项目运行成功！
