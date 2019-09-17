package com.tiger.oa.controller;

import com.tiger.oa.biz.DepartmentBiz;
import com.tiger.oa.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
@Controller("departmentController")
@RequestMapping(value = "/department")
public class DepartmentController {
    @Autowired
    private DepartmentBiz departmentBiz;
    @RequestMapping(value = "/list")
    public String list(Map<String,Object> map){
        map.put("list",departmentBiz.getAll());
        return "department_list";
    }
    @RequestMapping(value = "/to_add")
    public String toAdd(Map<String,Object> map){
        map.put("department",new Department());
        return "department_add";
    }
    @RequestMapping(value = "/add")
    public String add(Department department){
        departmentBiz.add(department);
        return "redirect:list";
    }
    @RequestMapping(value = "/to_update",params = "sn")
    public String toUpdate(String sn,Map<String,Object> map){
        map.put("department",departmentBiz.get(sn));
        return "department_update";
    }
    @RequestMapping(value = "/update")
    public String update(Department department){
        departmentBiz.edit(department);
        return "redirect:list";
    }
    @RequestMapping(value = "/remove",params = "sn")
    public String remove(String sn){
        departmentBiz.remove(sn);
        return "redirect:list";
    }
}
