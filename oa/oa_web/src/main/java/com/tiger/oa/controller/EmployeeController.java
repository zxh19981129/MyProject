package com.tiger.oa.controller;

import com.tiger.oa.biz.DepartmentBiz;
import com.tiger.oa.biz.EmployeeBiz;
import com.tiger.oa.entity.Employee;
import com.tiger.oa.global.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller("employeeController")
@RequestMapping(value = "/employee")
public class EmployeeController {
    @Autowired
    private EmployeeBiz employeeBiz;
    @Autowired
    private DepartmentBiz departmentBiz;
    @RequestMapping(value = "/list")
    public String list(Map<String,Object> map){
        map.put("list",employeeBiz.getAll());
        return "employee_list";
    }
    @RequestMapping(value = "/to_add")
    public String toAdd(Map<String,Object> map){
        map.put("employee",new Employee());
        map.put("dlist",departmentBiz.getAll());
        map.put("plist", Content.getPosts());
        return "employee_add";
    }
    @RequestMapping(value = "/add")
    public String add(Employee employee){
        employeeBiz.add(employee);
        return "redirect:list";
    }
    @RequestMapping(value = "/to_update",params = "sn")
    public String toUpdate(String sn,Map<String,Object> map){
        map.put("employee",employeeBiz.get(sn));
        map.put("dlist",departmentBiz.getAll());
        map.put("plist", Content.getPosts());
        return "employee_update";
    }
    @RequestMapping(value = "/update")
    public String update(Employee employee){
        employeeBiz.edit(employee);
        return "redirect:list";
    }
    @RequestMapping(value = "/remove",params = "sn")
    public String remove(String sn){
        employeeBiz.remove(sn);
        return "redirect:list";
    }
}

