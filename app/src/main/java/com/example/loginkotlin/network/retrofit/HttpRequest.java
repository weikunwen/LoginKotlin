package com.example.loginkotlin.network.retrofit;

import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 属性：
 * text/html ： HTML格式
 * text/plain ：纯文本格式
 * text/xml ：  XML格式
 * image/gif ：gif图片格式
 * image/jpeg ：jpg图片格式
 * image/png：png图片格式
 * 以application开头的媒体格式类型：
 * <p>
 * application/xhtml+xml ：XHTML格式
 * application/xml     ： XML数据格式
 * application/atom+xml  ：Atom XML聚合格式
 * application/json    ： JSON数据格式
 * application/pdf       ：pdf格式
 * application/msword  ： Word文档格式
 * application/octet-stream ： 二进制流数据（如常见的文件下载）
 * application/x-www-form-urlencoded ： <form encType=””>中默认的encType，form表单数据被编码为key/value格式发送到服务器（表单默认的提交数据的格式）
 * <p>
 * 另外一种常见的媒体格式是上传文件之时使用的：
 * multipart/form-data ： 需要在表单中进行文件上传时，就需要使用该格式
 * 注意：MediaType.parse("image/png")里的"image/png"不知道该填什么，可以参考---》http://www.w3school.com.cn/media/media_mimeref.asp
 * 如何使用呢？(在请求体里面写入类型和需要写入的数据，通过post请求)
 */

/**
 * 构建Api请求参数
 */

public class  HttpRequest {

    public static final MediaType TEXT = MediaType.parse("text/plain; charset=utf-8");
    public static final MediaType STREAM = MediaType.parse("application/octet-stream");
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

//    //构建字符串请求体
//    RequestBody body1 = RequestBody.create(TEXT, string);
//    //构建字节请求体
//    RequestBody body2 = RequestBody.create(STREAM, byte);
//    //构建文件请求体
//    RequestBody body3 = RequestBody.create(STREAM, file);
//    //post上传json
//    RequestBody body4 = RequestBody.create(JSON, json);//json为String类型的

    public static Map<String, Object>
    getRequestMap() {
        Map<String, Object> map = new HashMap<>();
//        map.put(k_key, appKey);
        return map;
    }


    public static RequestBody getJsonRequestBody(Map<String, Object> map) {
        map = convertMap(map);
        return RequestBody.create(JSON, new Gson().toJson(map));
    }

    public static RequestBody getStringRequestBody(Map<String, Object> map) {
        map = convertMap(map);
        return RequestBody.create(TEXT, new Gson().toJson(map));
    }

    public static RequestBody getFileRequestBody(File file) {
        return RequestBody.create(STREAM, file);
    }

    public static RequestBody getByteRequestBody(byte[] bytes) {
        return RequestBody.create(STREAM, bytes);
    }

    public static Map<String, Object> convertMap(Map<String, Object> map) {
        if (map == null) {
            map = getRequestMap();
        }
//        if (!map.containsKey("versionNo")) {
//            map.put("versionNo", "123");
//        }
//        if (BuProcessor.getInstance().isLogin()){
//            map.put("token",BuProcessor.getInstance().getUserInfo().getToken());
//        }
//        String sign = MD5Sign.sortAndMD5(map,"123456");
//        map.put("requestToken",sign);
        return map;
    }

}
