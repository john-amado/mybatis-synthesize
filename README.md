官方文档：https://mybatis.org/mybatis-3/

####Mybatis 简单介绍
MyBatis 是一款优秀的持久层框架，它支持定制化 SQL、存储过程以及高级映射。MyBatis 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集。MyBatis 可以使用简单的 XML 或注解来配置和映射原生信息，将接口和 Java 的 POJOs(Plain Ordinary Java Object,普通的 Java对象)映射成数据库中的记录。
###### **首先建个表**
待会需要用mybatis操作这个表
```
CREATE TABLE `user`(
`id` INT AUTO_INCREMENT,
`name` VARCHAR(50) NOT NULL,
`gender` TINYINT,# 1男, 2女, 0未选
`head_img` VARCHAR(355) NOT NULL,
 PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

# 插入一条数据
INSERT INTO `user` VALUES (null,'james', '1', 'http://www.yiibai.com');
```
####Mybatis 入门
1.创建一个maven项目：
( 也可以新建 Spring Initializr 项目)
- 在pom.xml 文件下的dependencies 引入依赖，idea 与 eclipse 一样的操作
以下是所用到的相关依赖
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <artifactId>mybatis-synthesize</artifactId>
    <groupId>com.jozs.mybatis</groupId>
    <version>1.0-SNAPSHOT</version>
   <!-- 使用spring boot-->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.5.RELEASE</version>
    </parent>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- 连接池 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.0.15</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.0.1</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.32</version>
        </dependency>
    </dependencies>
</project>
```

2. 配置文件：
  - application.yml 
```
server:
  port: 8081

mybatis:
  type-aliases-package: com.jozs.mybatis.pojo
  mapper-locations: com.jozs.mybatis.mapper/*.xml

spring:
  datasource:
    #driver-class-name: com.mysql.cj.jdbc.Driver
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/mybatis_test?characterEncoding=utf-8&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
    username: root
    password: joh
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
      # 下面为连接池的补充设置，应用到上面所有数据源中
      # 初始化大小，最小，最大
      initial-size: 5
      min-idle: 5
      max-active: 20
      # 配置获取连接等待超时的时间
      max-wait: 60000
```
- 配置 Mapper.xml 
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- mapper代理的开发规则：
1、id 是对应接口的方法名称
2、parameterType 对应方法里面的参数类型
3、resultType直接写对象的全类名 -->
<mapper namespace="com.jozs.mybatis.mapper.UserMapper">
    <!-- 新增记录,单条数据的INSERT操作 -->
    <insert id="saveUser" parameterType="com.jozs.mybatis.pojo.User">
		insert into `user`(`name`,`gender`,`head_img`) values(#{name},#{gender},#{head_img})
	</insert>
    <select id="selectUser" parameterType="int" resultType="com.jozs.mybatis.pojo.User">
        select * from `user` where id = #{id}
    </select>
    <!-- 修改记录 -->
    <update id="updateUser" parameterType="com.jozs.mybatis.pojo.User">
        update `user`
        <set>
            <if test="name!=null">`name`=#{name},</if>
            <if test="gender!=null">`gender`=#{gender},</if>
            <if test="head_img!=null">`head_img`=#{head_img},</if>
        </set>
        where id = #{id}
    </update>
    <!-- 删除记录 -->
    <delete id="deleteUser" parameterType="int" >
		delete from `user`
		where
		id = #{id}
	</delete>
</mapper>

```
3.编写相应代码：
- 实体类
编写对应的实体类
```
package com.jozs.mybatis.pojo;

public class User {
    private Integer id;
    private String name;
    private Integer gender;
    private String head_img;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", head_img='" + head_img + '\'' +
                '}';
    }

}

```
- Mapper 接口
```
package com.jozs.mybatis.mapper;

import com.jozs.mybatis.pojo.User;

public interface UserMapper {
    User selectUser(Integer id);
    int saveUser(User user);
    int updateUser(User user);
    int deleteUser(Integer id);
}

```
- UserController
```
package com.jozs.mybatis.controller;

import com.jozs.mybatis.mapper.UserMapper;
import com.jozs.mybatis.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserMapper userMapper;

    @RequestMapping(value = "/saveUser", method = RequestMethod.GET)
    @ResponseBody
    public Object saveUser() {
        User user = new User();
        user.setName("aises");
        user.setGender(2);
        user.setHead_img("www.baidu.com");
        return userMapper.saveUser(user);
    }
    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    @ResponseBody
    public Object deleteUser() {
        return userMapper.deleteUser(3);
    }



    @RequestMapping(value = "/updateUser", method = RequestMethod.GET)
    @ResponseBody
    public Object updateUser() {
        User user = new User();
        user.setId(2);
        user.setName("amy");
        user.setGender(2);
        user.setHead_img("https://www.baidu.cn");
        return userMapper.updateUser(user);
    }



    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public Object getUser() {
        return userMapper.selectUser(1);
    }
}

```
- Spring boot 启动类
```
package com.jozs.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.jozs.mybatis.mapper")
public class MybatisApp {
    public static void main(String[] args) {
        SpringApplication.run(MybatisApp.class, args);
    }

}

```
测试结果 （查询结果）
```
{
    "id": 1,
    "name": "james",
    "gender": 1,
    "head_img": "http://www.yiibai.com"
}
```
