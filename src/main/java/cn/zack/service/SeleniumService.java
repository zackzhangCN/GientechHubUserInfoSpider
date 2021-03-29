package cn.zack.service;

public interface SeleniumService {

    /**
     * 打开指定页面(hub主页)
     * @param driverPath
     */
    void getHubPage(String driverPath);

    /**
     * 打开用户信息查询页, 并裁剪图片保存
     * @param savePath
     * @param sourceFile
     * @return
     */
    String getUserPage(String savePath, String sourceFile);
}
