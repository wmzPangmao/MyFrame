package com.pangmao.mframe.utils.http;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class MHttp implements IHttpEngine {

    private static IHttpEngine httpEngine;
    private static MHttp mHttp;
//    public static MHandler handler = new MHandler();

    public static void init(IHttpEngine engine){
        httpEngine=engine;
    }

    public static MHttp obtain(){
        if (httpEngine==null){
            throw new NullPointerException("Call XFrame.initXHttp(IHttpEngine httpEngine) within your Application onCreate() method." +
                    "Or extends XApplication");
        }
        if (mHttp == null) {
            mHttp = new MHttp();
        }
        return mHttp;
    }

    /**
     * 获取实体类的类型
     * @param obj
     * @return
     */
    public static Class<?> analysisClassInfo(Object obj){
        Type genType=obj.getClass().getGenericSuperclass();
        Type[] params=((ParameterizedType)genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }

    @Override
    public void get(String url, Map<String, Object> params, HttpCallBack callBack) {
        httpEngine.get(url,params,callBack);
    }

    @Override
    public void post(String url, Map<String, Object> params, HttpCallBack callBack) {
        httpEngine.post(url,params,callBack);
    }
}
