# myspikesystem
### 工具
Intellij IDEA，Mysql 
### 关键技术
java，spring, springmvc, mybatis，bootsrtap
### 项目介绍
本项目是为了更好地理解ssm 框架而进行的一个个人项目，模仿双十一秒杀商品的整个
过程，整个项目分为前端和后台两大块。

客户首先进入商品清单页面，选择商品进行秒杀，如果不在规定时间或者秒杀数据被重
写会给出错误提示信息。DAO 层使用mybatis整合spring 实现对数据库（秒杀商品信
息表和成功秒杀记录表）的访问；Service层实现逻辑代码与具体事务代码分离；web 
层用SpringMVC 整合spring, 实现秒杀restful 接口。

最后我对秒杀进行高并发优化：1.使用redis 进行后端缓存优化编码 2.使用canal中
间件保证redis和mysql的数据一致性 3.调整insert和update语句减少行锁时间。
