FROM anapsix/alpine-java:8_jdk

RUN sed -i 's/dl-cdn.alpinelinux.org/mirror.tuna.tsinghua.edu.cn/g' /etc/apk/repositories
RUN apk add -U tzdata
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 创建文件夹，拷贝字体文件
RUN mkdir -p /usr/share/fonts
COPY docker/msyh.ttf /usr/share/fonts/msyh.ttf

# 设置字符集
ENV LANG en_US.UTF-8

# 安装字体软件，完成字体配置
RUN apk add --update ttf-dejavu fontconfig && rm -rf /var/cache/apk/*

RUN mkdir -p /app/logs && mkdir -p /app/lib
VOLUME ["/tmp", "/app/logs"]
EXPOSE 8080
COPY docker/startServer.sh /app/
RUN chmod +x /app/startServer.sh
COPY school-api/target/school-api-1.0.0.jar /app/app.jar
WORKDIR /app
ENTRYPOINT ["./startServer.sh"]