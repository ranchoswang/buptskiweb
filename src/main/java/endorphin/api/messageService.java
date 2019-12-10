package endorphin.api;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import endorphin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/message")
public class messageService {

    private final UserService userService;
    private final int appid=1400254050;
    private final String appkey="9bf89685f8b4472f4afe3d4c38497773";
    String smsSign = "高校雪联";
    int templateId=415534;
    int templateId2=417086;


    @Autowired
    public messageService(UserService userService) {
        this.userService = userService;
    }


@RequestMapping("/firstjoin")//发送成功加入短信 https://buptski.cn/message/firstjoin
@ResponseBody
public Map<String, Object> getUserInfo(@RequestParam(value = "phonenum",required=true)String phonenum, @RequestParam(value = "name",required=true)String name, @RequestParam(value = "password",required=true)String pwd)
{
    Map<String, Object> map = new HashMap<>();


    try {
        String[] params = {""+name};
        SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
        SmsSingleSenderResult result = ssender.sendWithParam("86", phonenum,templateId, params, smsSign, "", "");
        String[] params2={};
        SmsSingleSenderResult result2 = ssender.sendWithParam("86", phonenum,templateId2, params2, smsSign, "", "");

        System.out.println(result);
    } catch (Exception e) {
        // HTTP 响应码错误
        e.printStackTrace();
    }
    map.put("state","success");
    return map;
}

}