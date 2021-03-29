package cn.zack.client;

import cn.zack.service.SeleniumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author zack
 * 客户端UI界面
 */
@Component("HubUserInfoPicSpiderUI")
public class HubUserInfoPicSpiderUI extends JFrame {
    private static HubUserInfoPicSpiderUI instance = null;

    @Autowired
    private SeleniumService seleniumService;

    public HubUserInfoPicSpiderUI() {
    }

    // 单例窗口对象
    public static HubUserInfoPicSpiderUI getInstance() {
        if (null == instance) {
            synchronized (HubUserInfoPicSpiderUI.class) {
                if (null == instance) {
                    instance = new HubUserInfoPicSpiderUI();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化窗口
     */
    public void initUI() {
        // 窗口标题
        this.setTitle("Hub员工信息");
        // 窗口大小
        this.setSize(350, 180);
        // 不可调整窗口大小
        this.setResizable(false);
        // 窗口关闭时退出程序
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 居中显示
        this.setLocationRelativeTo(null);

        // 流式布局
        FlowLayout flowLayout = new FlowLayout();
        this.setLayout(flowLayout);

        // 驱动选择
        JButton driverButton = new JButton("选择驱动");
        this.add(driverButton);
        JTextField driverJTextField = new JTextField();
        Dimension driverDimension = new Dimension(215, 30);
        driverJTextField.setPreferredSize(driverDimension);
        this.add(driverJTextField);

        // 文件选择
        JButton fileButton = new JButton("选择文件");
        this.add(fileButton);
        JTextField fileJTextField = new JTextField();
        Dimension fileDimension = new Dimension(215, 30);
        fileJTextField.setPreferredSize(fileDimension);
        this.add(fileJTextField);

        // 目录选择
        JButton pathButton = new JButton("保存到");
        this.add(pathButton);
        JTextField pathJTextField = new JTextField();
        Dimension pathDimension = new Dimension(215, 30);
        pathJTextField.setPreferredSize(pathDimension);
        this.add(pathJTextField);

        // 按钮
        JButton loginButton = new JButton("登录");
        this.add(loginButton);

        // 按钮
        JButton spiderButton = new JButton("开始爬取");
        this.add(spiderButton);

        // 展示窗口
        this.setVisible(true);


        // 驱动选择按钮点击事件
        driverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("选择驱动");
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int i = jFileChooser.showOpenDialog(null);
                if (i == JFileChooser.APPROVE_OPTION) {
                    String selectPath = jFileChooser.getSelectedFile().getPath();
                    driverJTextField.setText(selectPath);
                }
            }
        });

        // 文件选择按钮点击事件
        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("选择文件");
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int i = jFileChooser.showOpenDialog(null);
                if (i == JFileChooser.APPROVE_OPTION) {
                    String selectPath = jFileChooser.getSelectedFile().getPath();
                    fileJTextField.setText(selectPath);
                }
            }
        });

        // 目录选择按钮点击事件
        pathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("选择保存目录");
                jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int i = jFileChooser.showOpenDialog(null);
                if (i == JFileChooser.APPROVE_OPTION) {
                    String selectPath = jFileChooser.getSelectedFile().getPath();
                    pathJTextField.setText(selectPath);
                }
            }
        });

        // 登录按钮点击事件
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取选择的驱动文件
                String driverPath = driverJTextField.getText();
                seleniumService.getHubPage(driverPath);
            }
        });

        // 爬取按钮点击事件
        spiderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取选择的源文件
                String sourceFile = fileJTextField.getText();
                // 获取选择的保存路径
                String savePath = pathJTextField.getText();
                // 启动爬虫
                String result = seleniumService.getUserPage(savePath, sourceFile);
                JOptionPane.showMessageDialog(null, result, "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
}
