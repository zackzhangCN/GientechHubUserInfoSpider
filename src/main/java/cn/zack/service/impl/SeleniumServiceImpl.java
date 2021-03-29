package cn.zack.service.impl;

import cn.zack.service.SeleniumService;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class SeleniumServiceImpl implements SeleniumService {

    private static final Logger logger = LoggerFactory.getLogger(SeleniumServiceImpl.class);

    private static final String BASE_USER_INFO_URL = "https://hub.gientech.com/?q=email_serach&username=";

    private RemoteWebDriver remoteWebDriver;

    /**
     * 打开hub页面, 等待数据账号登录
     * @param driverPath eg: E:\chromeDriver.exe
     */
    @Override
    public void getHubPage(String driverPath) {
        // 指定selenium插件驱动位置
        System.setProperty("webdriver.chrome.driver", driverPath);
        // 创建selenium设置对象
        ChromeOptions chromeOptions = new ChromeOptions();
        // 设置为不打开浏览器模式
        // chromeOptions.addArguments("--headless");
        remoteWebDriver = new ChromeDriver(chromeOptions);
        remoteWebDriver.manage().window().setSize(new Dimension(1000, 600));
        //控制selenium去加载网址
        remoteWebDriver.get("https://hub.gientech.com/?q=home-page");
    }

    /**
     * 读取员工号文件, 截取每张图片
     * @param savePath eg: D:\test
     * @param sourceFile eg: D:\test\test.txt
     * @return
     */
    @Override
    public String getUserPage(String savePath, String sourceFile) {
        AtomicReference<String> result = new AtomicReference<>("OK");
        ArrayList<String> list = new ArrayList<>();

        File userFile;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            logger.info("读取信息文件");
            userFile = new File(sourceFile);
            fileReader = new FileReader(userFile);
            bufferedReader = new BufferedReader(fileReader);

            String thisLine;
            while ((thisLine = bufferedReader.readLine()) != null) {
                list.add(thisLine);
            }
        } catch (Exception e) {
            logger.error("读取信息文件发生异常, error: {}", e.toString());
            result.set("出错啦");
        } finally {
            try {
                logger.info("关闭流");
                fileReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                logger.error("关闭流异常, error: {}", e.toString());
            }
        }

        list.forEach(c -> {
            // 查询此员工详情
            remoteWebDriver.get(BASE_USER_INFO_URL + c);
            // 页面保存为图片
            File screenFile = remoteWebDriver.getScreenshotAs(OutputType.FILE);
            // 裁剪
            String thisFile = savePath + "\\" + c + ".png";
            try {
                cutAndSavePic(screenFile, thisFile);
            } catch (Exception e) {
                result.set("出错啦");
            }
        });
        return result.get();
    }

    /**
     * 将原有图片裁剪后保存到指定的位置
     *
     * @param screenFile
     * @param saveFile
     */
    public void cutAndSavePic(File screenFile, String saveFile) {
        ImageInputStream imageInputStream = null;
        try {
            logger.info("开始裁剪图片, 图片名: {}", saveFile);
            // 声明指定解码格式
            Iterator<ImageReader> pngIterator = ImageIO.getImageReadersByFormatName("png");
            ImageReader imageReader = pngIterator.next();
            // 读取截屏图片流
            imageInputStream = ImageIO.createImageInputStream(screenFile);
            // 向前搜索
            imageReader.setInput(imageInputStream, true);
            // 指定如何在输入时从 Java Image I/O框架的上下文中的流转换一幅图像或一组图像
            ImageReadParam readParam = imageReader.getDefaultReadParam();
            // 定义空间中的一块区域(截取区域)
            Rectangle rectangle = new Rectangle(10, 400, 1200, 100);
            readParam.setSourceRegion(rectangle);
            BufferedImage bufferedImage = imageReader.read(0, readParam);
            ImageIO.write(bufferedImage, "png", new File(saveFile));
        } catch (Exception e) {
            try {
                imageInputStream.close();
            } catch (IOException ioException) {
                logger.error("关闭流发生异常");
            }
            logger.error("裁剪图片发生异常, error: {}", e.toString());

        }
    }
}
