package com.pan.listener;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.lpmas.framework.util.DateKit;

public class ListenerFrame {
    static JFrame frame = null;

    static JLabel keyLabel = null;
    static JLabel currentLabel = null;
    static JLabel avageLabel = null;

    /**
     * { 创建并显示GUI。出于线程安全的考虑， 这个方法在事件调用线程中调用。
     */
    public static void createAndShowGUI() {
        // 确保一个漂亮的外观风格
        JFrame.setDefaultLookAndFeelDecorated(true);

        // 创建及设置窗口
        frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(frame.getOwner());
        frame.setSize(300, 350);
        frame.setLayout(null);

        Font font = new Font("宋体", Font.BOLD, 32);

        keyLabel = new JLabel("POP", SwingConstants.CENTER);
        keyLabel.setFont(font);
        keyLabel.setBounds(0, 0, 300, 100);

        // 添加 "Hello World" 标签
        currentLabel = new JLabel("Hello", SwingConstants.CENTER);
        currentLabel.setFont(font);
        currentLabel.setBounds(0, 100, 300, 100);

        avageLabel = new JLabel("World", SwingConstants.CENTER);
        avageLabel.setFont(font);
        avageLabel.setBounds(0, 200, 300, 100);

        frame.add(keyLabel);
        frame.add(currentLabel);
        frame.add(avageLabel);
    }

    public static void setLabel(String keyMsg, String currentMsg, String avgMsg) {
        if (keyLabel == null) {
            return;
        }
        keyLabel.setText("key: " + keyMsg);

        if (currentLabel == null) {
            return;
        }
        currentLabel.setText("按键时间: " + currentMsg + DateKit.REGEX_MICROSECOND);

        if (avageLabel == null) {
            return;
        }
        avageLabel.setText("平均时间: " + avgMsg + DateKit.REGEX_MICROSECOND);

    }

}
