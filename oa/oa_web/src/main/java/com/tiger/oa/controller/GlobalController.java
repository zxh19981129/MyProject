package com.tiger.oa.controller;

import com.tiger.oa.biz.GlobalBiz;
import com.tiger.oa.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller("globalController")
public class GlobalController {
    @Autowired
    private GlobalBiz globalBiz;
    @RequestMapping(value = "/to_login")
    public String toLogin(){
        return "login";
    }
    @RequestMapping(value = "/login")
    public String login(HttpSession session,@RequestParam String sn, @RequestParam String password){
        Employee employee = globalBiz.login(sn,password);
        if (employee == null){
            return "redirect:to_login";
        }
        session.setAttribute("employee",employee);
        return "redirect:self";
    }
    @RequestMapping(value = "/self")
    public String self(){
        return "self";
    }
    @RequestMapping(value = "/quit")
    public String quit(HttpSession session){
        session.setAttribute("employee",null);
        return "redirect:to_login";
    }
    @RequestMapping(value = "/to_change_password")
    public String toChangePassword(){
        return "change_password";
    }
    @RequestMapping(value = "/change_password")
    public String changePassword(HttpSession session,@RequestParam String old,@RequestParam String new1,@RequestParam String new2){
        Employee employee = (Employee) session.getAttribute("employee");
        if (employee.getPassword().equals(old)){
            if (new1.equals(new2)){
                employee.setPassword(new1);
                globalBiz.changePassword(employee);
                return "self";
            }
        }
        return "redirect:to_change_password";
    }
}
