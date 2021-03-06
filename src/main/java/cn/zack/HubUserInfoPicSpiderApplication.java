package cn.zack;

import cn.zack.client.HubUserInfoPicSpiderUI;
import cn.zack.utils.SpringContextUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.awt.*;

@SpringBootApplication
public class HubUserInfoPicSpiderApplication {
    public static void main(String[] args) {
        /**
         * 初始化spring
         * java.awt.headless是J2SE的一种模式, 用于在缺失显示屏、鼠标或者键盘时的系统配置, springboot默认将这个属性设置为true
         */
        new SpringApplicationBuilder(HubUserInfoPicSpiderApplication.class).headless(false).run(args);

        /**
         * 加载UI界面
         */
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 从spring容器中取出客户端界面并初始化
                HubUserInfoPicSpiderUI hubUserInfoPicSpiderUI = SpringContextUtils.getBean(HubUserInfoPicSpiderUI.class);
                hubUserInfoPicSpiderUI.initUI();
            }
        });
    }
}
