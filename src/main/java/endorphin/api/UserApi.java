package endorphin.api;


import endorphin.domain.User;
import endorphin.domain.UserLoginLog;
import endorphin.json.DataResult;
import endorphin.service.LoginLogService;
import endorphin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class UserApi {
    private final UserService userService;

    @Autowired

    public UserApi(UserService userService, LoginLogService loginLogService) {
        this.userService = userService;

    }
    @RequestMapping("/getUserinfo")//获取用户信息（登陆）https://buptski.cn/api/getUserinfo
    @ResponseBody
    public Map<String, Object> getUserInfo(@RequestParam(value = "phonenum",required=true)String phonenum,@RequestParam(value = "isskiteam",required=true)int type,@RequestParam(value = "password",required=true)String pwd)
    {
        Map<String, Object> map = new HashMap<>();
        User user = userService.getUserByPhone(phonenum);
        String password ;
        if (user != null) {
            if(user.getUserType()!=type) {
            map.put("name","");//类别不符，按照未发现处理（0为管理员，1为普通用户）
                return map;//若未发现用户，返回姓名且为空
            }
            //用户存在

            password = userService.getPassword(user.getUserName());

            if(pwd.equals(password)){
                //密码正确

                map.put("name", user.getUserName());

                map.put("id",user.getUserId());
                map.put("email",user.getUserEmail());
                map.put("phone",user.getUserPhone());
                map.put("sex",user.getUserSex());

            }else{
                //密码错误
                map.put("name", "wrong pwd");//密码错误返回name="wrong pwd"
            }
        }else{
            //不存在该用户
            map.put("name","");
        }



        return map;//正常情况返回如上字段
    }


    @RequestMapping("/registuser")
    @ResponseBody
    public Map<String, Object> registuser(@RequestParam(value = "name",required=true)String username,@RequestParam(value = "phonenum",required=true)String phonenum,@RequestParam(value = "password",required=true)String password,@RequestParam(value = "openId",required=true)String openId) {
        Map<String, Object> map = new HashMap<>();




            // 如果数据库中没有该用户，可以注册，否则跳转页面
            if (userService.getUserByUserName(username) == null&&userService.getUserByPhone(phonenum) == null) {
                // 添加用户
                User newuser = new User();
                newuser.setUserName(username);
                newuser.setPassword(password);
                newuser.setUserType(1);
                newuser.setUserPhone(phonenum);
                Timestamp createLoginTime = new Timestamp(System.currentTimeMillis());
                newuser.setCreateTime(createLoginTime);
                newuser.setLastLoginTime(createLoginTime);
                newuser.setLastIp("0:0:0:0:0:0:0:1");
                newuser.setOpenId(openId);
                userService.addUser(newuser);
                // 注册成功跳转
                map.put("name",newuser.getUserName());

            } else {
                map.put("erro","exist");
            }


        return map;
    }

    @RequestMapping("/updateByphone")
    @ResponseBody
    public Map<String, Object> update(@RequestParam(value = "name",required=true)String username,@RequestParam(value = "phonenum",required=true)String phonenum,@RequestParam(value = "password",required=true)String password) {
        Map<String, Object> map = new HashMap<>();

        try {
            User user = userService.getUserByPhone(phonenum);
            // 如果数据库中有该用户，可以更新
            if (user != null) {
                // 更新

                //验证新信息
                if(!(user.getUserName().equals(username))&&(userService.getUserByUserName(username)!=null)){
                    map.put("erro","exist");//有相同name已存在
                    return map;
                }


                user.setUserName(username);
                user.setPassword(password);
                user.setUserPhone(phonenum);


                userService.updateUserByPhone(user);
                // 更新成功
                map.put("name",user.getUserName());
                map.put("pwd",user.getPassword());

            } else {
                map.put("erro","not exist");//不存在该用户，无法更新
            }

        } catch (Exception e) {
            System.out.println(e);
            map.put("erro","unknown");//不知名错误（可能服务器出问题）
        }
        return map;
    }
}
