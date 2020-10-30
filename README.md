> 偷得浮生半日闲。刚好利用空闲时候，整合了一下工程,方便以后使用

## Modify by 2019.08.027

- 新增敏感词过滤功能
- 修复无法插入数据bug

### 快速敏感词过滤

#### 优化方式

主要的优化目标是速度，从以下方面优化：

1. 敏感词都是2个字以上的，
2. 对于句子中的一个位置，用2个字符的hash在稀疏的hash桶中查找，如果查不到说明一定不是敏感词，则继续下一个位置。
3. 2个字符（2x16位），可以预先组合为1个int（32位）的mix，即使hash命中，如果mix不同则跳过。
4. StringPointer，在不生成新实例的情况下计算任意位置2个字符的hash和mix
5. StringPointer，尽量减少实例生成和char数组的拷贝。 

#### 敏感词库

默认敏感词库拷贝自 [https://github.com/observerss/textfilter](https://github.com/observerss/textfilter) ，并删除如女人、然后这样的几个常用词。 使用默认敏感词库的示例如下

```java
// 使用默认单例（加载默认敏感词库）
SensitiveFilter filter = SensitiveFilter.DEFAULT;
// 向过滤器增加一个词
filter.put("婚礼上唱春天在哪里");
	
// 待过滤的句子
String sentence = "然后，市长在婚礼上唱春天在哪里。";
// 进行过滤
String filted = filter.filter(sentence, '*');
	
// 如果未过滤，则返回输入的String引用
if(sentence != filted){
	// 句子中有敏感词
	System.out.println(filted);
}
```

#### 参考文档

[https://github.com/inkzuji/sensitive-words](https://github.com/inkzuji/sensitive-words)

## Modify by 2019.08.08 

- 增加阿里云OSS相关配置
- 增加微信支付相关jar
- 增加Sagger配置类是否可以实体
- wagger2.9.2访问主页面编译器控制台异常

```$xslt
io.swagger.models.parameters.AbstractSerializableParameter 421 getExample - Illegal DefaultValue 0 for parameter type integer java.lang.NumberFormatException: For input string: ""
```

----------

- 整合 MyBatis,实现自动生成xml，并实现批量插入，更新
- 整合 log4j2 日志系统
- 整合 pagehelper 分页插件
- 配置打包规则，打包时跳过测试
- 使用 Druid 数据源，并启用监听功能
- 整合 Swagger2 UI 构建RESTful API文档
- 整合 Redis ,包含常用方法
- 整合 JWT，用于用户 toke 登陆
- 整合 全局异常监听

## 依赖Jar作用

`MyBatis-Spring-Boot-Starter`

- 自动检测现有的**DataSource**
- 将创建并注册**SqlSessionFactory**的实例，该实例使用**SqlSessionFactoryBean**将该**DataSource**作为输入进行传递。
- 将创建并注册从**SqlSessionFactory**中获取的**SqlSessionTemplate**的实例。
- 自动扫描您的**mappers**，将它们链接到**SqlSessionTemplate**并将其注册到**Spring**上下文，以便将它们注入到您的**bean**中。
- 使用了该**Starter**之后，只需要定义一个**DataSource**即可（**application.properties**或**application.yml**中可配置），它会自动创建使用该**DataSource**的**SqlSessionFactoryBean**以及**SqlSessionTemplate**。会自动扫描你的**Mappers**，连接到**SqlSessionTemplate**，并注册到**Spring**上下文中