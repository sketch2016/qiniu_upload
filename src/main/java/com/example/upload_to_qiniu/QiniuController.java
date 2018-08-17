package com.example.upload_to_qiniu;

import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
public class QiniuController {
    String ACCESS_KEY = "lm6f3peedWHayH-xyZdd-xSCqyoNyvRhS7jkdT0e";
    String SECRET_KEY = "f78vnIyeozEbZgqOxemFNVfg4cs9Q0YifnuK9WS_";
    //要上传的空间
    String bucketname = "xiangantest";
    String doman = "7xko2c.com1.z0.glb.clouddn.com";

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    @Autowired
    QiniuDao qiniuDao;

    @Autowired
    MyWebSocket myWebSocket;

    @RequestMapping("/test1")
    @ResponseBody
    public String test() {
        return "welcome to upload page1!";
    }

    @RequestMapping("/uptoken")
    @ResponseBody
    public Map getUpToken() {
        Map result = new HashMap<>();
        String uptoken = auth.uploadToken(bucketname);

        result.put("uptoken", uptoken);
        result.put("domain", doman);
        System.out.println("token:" + result);
        return result;
    }

    @RequestMapping("/downloadtoken")
    @ResponseBody
    public String getDownloadToken(@RequestParam("baseUrl") String baseUrl) {
        String downloadToken = auth.privateDownloadUrl(baseUrl);
        System.out.println("downloadToken:" + downloadToken);

        //storeDownloadUrl(downloadToken);
        return downloadToken;
    }

    @RequestMapping("/storeDownloadUrl")
    @ResponseBody
    public String storeDownloadUrl(@RequestParam("key") String key, @RequestParam("downloadUrl") String downloadUrl) {
        System.out.println("key:" + key + " storeDownloadUrl:" + downloadUrl);

        FileInfo fileInfo = new FileInfo();
        fileInfo.setName(key);
        fileInfo.setDownloadUrl(downloadUrl);
        fileInfo.setSended("false");
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
        String time = df.format(System.currentTimeMillis());
        fileInfo.setStoreTime(String.valueOf(time));

        try {
            if (myWebSocket.sendInfo(fileInfo.toString())) {
                fileInfo.setSended("true");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        qiniuDao.save(fileInfo);

        return "store downloadUrl success!";
    }
}
