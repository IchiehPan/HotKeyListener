package com.pan.listener;

import java.util.HashMap;
import java.util.Map;

import com.lpmas.framework.util.MapKit;
import com.lpmas.framework.util.StringKit;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.sun.jna.platform.win32.WinUser.MSG;

public class KeyboardListener {
    HHOOK keyboardHHK;// 键盘钩子的句柄
    LowLevelKeyboardProc keyboardHook;// 键盘钩子函数

    static Map<Integer, String> keyboardMap = null;
    static {
        keyboardMap = new HashMap<>();
        keyboardMap.put(65, "a");
        keyboardMap.put(66, "b");
        keyboardMap.put(67, "c");
        keyboardMap.put(68, "d");
        keyboardMap.put(69, "e");
        keyboardMap.put(70, "f");
        keyboardMap.put(71, "g");
        keyboardMap.put(72, "h");
        keyboardMap.put(73, "i");
        keyboardMap.put(74, "j");
        keyboardMap.put(75, "k");
        keyboardMap.put(76, "l");
        keyboardMap.put(77, "m");
        keyboardMap.put(78, "n");
        keyboardMap.put(79, "o");
        keyboardMap.put(80, "p");
        keyboardMap.put(81, "q");
        keyboardMap.put(82, "r");
        keyboardMap.put(83, "s");
        keyboardMap.put(84, "t");
        keyboardMap.put(85, "u");
        keyboardMap.put(86, "v");
        keyboardMap.put(87, "w");
        keyboardMap.put(88, "x");
        keyboardMap.put(89, "y");
        keyboardMap.put(90, "z");
    }

    Long triggerTime = null;

    Long totalTime = 0L;
    int clickTimes = 0;
    Long averageTime = null;

    // 安装钩子
    void setHook() {
        HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
        keyboardHHK = User32.INSTANCE.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL, keyboardHook, hMod, 0);
    }

    // 卸载钩子
    void unhook() {
        User32.INSTANCE.UnhookWindowsHookEx(keyboardHHK);
    }

    public void initLowLevelKeyboardProc() {
        keyboardHook = new LowLevelKeyboardProc() {
            @Override
            // 该函数参数的意思参考：http://msdn.microsoft.com/en-us/library/windows/desktop/ms644985(v=vs.85).aspx
            public LRESULT callback(int nCode, WPARAM wParam, KBDLLHOOKSTRUCT lParam) {
                int w = wParam.intValue();
                // 按下alt键时w=.WM_SYSKEYDOWN; 按下其他大部分键时w=WinUser.WM_KEYDOWN
                if (keyboardMap.containsKey(lParam.vkCode)) {
                    if (w == WinUser.WM_KEYDOWN || w == WinUser.WM_SYSKEYDOWN) {
                        if (triggerTime == null) {
                            triggerTime = System.currentTimeMillis();
                        }
                    } else if (w == WinUser.WM_KEYUP || w == WinUser.WM_SYSKEYUP) {
                        String keyMsg = MapKit.getValueFromMap(lParam.vkCode, keyboardMap);

                        if (triggerTime != null) {
                            Long currentTime = System.currentTimeMillis();
                            long second = currentTime - triggerTime;
                            triggerTime = null;

                            totalTime += second;
                            averageTime = totalTime / (++clickTimes);
                            ListenerFrame.setLabel(keyMsg, StringKit.validStr(second), StringKit.validStr(averageTime));
                        }

                    }
                }
                // 如果按下'q'退出程序，'q'的vkCode是81
                // if (lParam.vkCode == 81) {
                // unhook();
                // System.err.println("program terminated.");
                // }
                return User32.INSTANCE.CallNextHookEx(keyboardHHK, nCode, wParam, lParam.getPointer());

            }
        };

        // System.out.println("press 'q' to quit.");
        setHook();

        int result;
        MSG msg = new MSG();
        // 消息循环
        // 实际上while循环一次都不执行，这些代码的作用我理解是让程序在GetMessage函数这里阻塞，不然程序就结束了。
        while ((result = User32.INSTANCE.GetMessage(msg, null, 0, 0)) != 0) {
            if (result == -1) {
                System.err.println("error in GetMessage");
                unhook();
                break;
            } else {
                User32.INSTANCE.TranslateMessage(msg);
                User32.INSTANCE.DispatchMessage(msg);
            }
        }
        unhook();
    }
}
