package com.example.upload_to_qiniu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebsocketConfig {

    @Autowired
    QiniuDao qiniuDao;

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        QiniuService.mQiniuDao = qiniuDao;

        return new ServerEndpointExporter();
    }
}
