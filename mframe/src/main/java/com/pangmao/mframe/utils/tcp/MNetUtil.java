package com.pangmao.mframe.utils.tcp;

import android.annotation.SuppressLint;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

public class MNetUtil {

    private static final String JVM_VERSION = System.getProperty("java.vm.version");

    public MNetUtil() {
    }

    public static void closeSocketQuietly(Socket socket) {
        closeSocketQuietly(socket, false, false);
    }

    public static void closeSocketQuietly(Socket socket, boolean isShutdownInput, boolean isShutdownOutput) {
        if (socket != null) {
            try {
                if (isShutdownInput && !socket.isInputShutdown()) {
                    socket.shutdownInput();
                }
            } catch (Exception var6) {
                ;
            }

            try {
                if (isShutdownOutput && !socket.isOutputShutdown()) {
                    socket.shutdownOutput();
                }
            } catch (Exception var5) {
                ;
            }

            try {
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException var4) {
                ;
            }

            socket = null;
        }
    }

    public static void setProxySelectorToNull() {
        try {
            Class ownerClass;
            try {
                ownerClass = Class.forName("java.net.ProxySelector");
            } catch (ClassNotFoundException var4) {
                return;
            }

            Class[] argsClass = new Class[]{ownerClass};
            Method method = ownerClass.getMethod("setDefault", argsClass);
            Object[] args = new Object[]{null};
            method.invoke((Object)null, args);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    @SuppressLint("NewApi")
    public static Socket createNoProxySocket() {
        Socket socket = null;
        if ("1.5".compareTo(Objects.requireNonNull(JVM_VERSION)) <= 0) {
            try {
                Class p = Class.forName("java.net.Proxy");
                Field f = p.getField("NO_PROXY");
                Class[] cs = new Class[]{p};
                Object[] os = new Object[]{f.get(null)};
                Class s = Socket.class;
                Constructor sc = s.getConstructor(cs);
                socket = (Socket)sc.newInstance(os);
            } catch (Throwable var7) {
                var7.printStackTrace();
            }
        }

        if (socket == null) {
            socket = new Socket();
        }

        return socket;
    }

    public static Socket connect(String address, int port, int timeout) throws IOException {
        Socket socket = null;

        try {
            socket = createNoProxySocket();
            socket.connect(new InetSocketAddress(address, port), timeout);
            socket.setSoTimeout(timeout);
            return socket;
        } catch (IOException var5) {
            closeSocketQuietly(socket);
            throw var5;
        }
    }
}
