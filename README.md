# multiple-shiro-login

multiple shiro login

> shiro配置文件

> portal: SecurityConfig.java 过滤/**请求，不包含/admin/**

> admin: AdminSecurityConfig.java  过滤/admin/**请求


**因为/\*\*包含/admin/\*\*，所以需要优先过滤/admin/\*\*，代码实现就是先实例化AdminSecurityConfig.java，再实例化SecurityConfig.java**
**可以通过Order注解实现**

----

通过设置不同的sessionId和cookie来分别记录portal或admin是否登录
