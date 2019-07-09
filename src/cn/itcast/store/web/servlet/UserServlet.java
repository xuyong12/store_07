package cn.itcast.store.web.servlet;

import cn.itcast.store.domain.User;
import cn.itcast.store.service.ServiceImp.UserServiceImp;
import cn.itcast.store.service.UserService;
import cn.itcast.store.utils.MailUtils;
import cn.itcast.store.utils.MyBeanUtils;
import cn.itcast.store.utils.UUIDUtils;
import cn.itcast.store.web.base.BaseServlet;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/UserServlet")
public class UserServlet extends BaseServlet {
    public String registUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return "jsp/register.jsp";
    }
    public String register(HttpServletRequest request,HttpServletResponse response) throws MessagingException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        User user=new User();
        MyBeanUtils.populate(user,parameterMap);
        user.setUid(UUIDUtils.getId());
        user.setState(0);
        user.setCode(UUIDUtils.getUUID64());
        user.setTelephone("15544545");
        System.out.println(user);
        UserService service=new UserServiceImp();
        try {
            service.register(user);
            request.setAttribute("msg","注册成功，请激活");
            //发送邮件功能,激活
            MailUtils.sendMail("1771560366@qq.com",user.getCode());
            System.out.println("发送邮件成功");
            return "/jsp/info.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("msg","注册失败，请重新注册");
            return "/jsp/info.jsp";
        }

    }
    public String active(HttpServletRequest request,HttpServletResponse response){
        String code=request.getParameter("code");
        UserService userService=new UserServiceImp();
        try {
            boolean active = userService.active(code);
            //激活成功
            if(active==true){
                request.setAttribute("msg","激活成功，请登录");
                return "/jsp/login.jsp";
            }else {
                request.setAttribute("msg","激活失败，请重新尝试");
                return "/jsp/info.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("msg","激活失败，请重新尝试");
            return "/jsp/info.jsp";
        }
    }
    //跳转登陆界面
    public String loginUI(HttpServletRequest request,HttpServletResponse response){
        return "jsp/login.jsp";
    }

    //用户登陆，判断账号密码是否正确，激活码是否为1
    public String login(HttpServletRequest request,HttpServletResponse response) throws IOException{
        Map<String, String[]> parameterMap = request.getParameterMap();
        //出现问题：user=null，还没有创建user，导致populate时调用不了set方法
        User user=new User();
        MyBeanUtils.populate(user,parameterMap);
        System.out.println(user);
        //记录了全部信息的用户
        User user1=null;
        UserService userService=new UserServiceImp();
        //判断用户的账号密码是否存在
        try {

            user1 = userService.login(user);
            System.out.println(user1);
            HttpSession session=request.getSession();
            session.setAttribute("user",user1);
            //重定向,路径必须完整
            response.sendRedirect(request.getContextPath()+"/jsp/index.jsp");
            return null;
        } catch (RuntimeException e) {
            request.setAttribute("msg",e.getMessage());
            return "/jsp/login.jsp";
        }catch (SQLException e){
            e.printStackTrace();
            return "jsp/login.jsp";
        }
    }
    //退出登陆,清空session，并返回登陆界面
    public String quit(HttpServletRequest request,HttpServletResponse response){
        HttpSession session = request.getSession();
        //清空session
        session.removeAttribute("user");
        session.invalidate();
        return "jsp/login.jsp";
    }
}
