package com.pan.listener;

public class StartListener {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ListenerFrame.createAndShowGUI();
            }
        });

        KeyboardListener keyboardListener = new KeyboardListener();
        keyboardListener.initLowLevelKeyboardProc();
    }
}
