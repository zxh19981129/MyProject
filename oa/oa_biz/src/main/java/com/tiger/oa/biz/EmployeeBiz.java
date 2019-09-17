package com.tiger.oa.biz;

import com.tiger.oa.entity.Employee;

import java.util.List;

public interface EmployeeBiz {
    void add(Employee employee);
    void remove(String sn);
    void edit(Employee employee);
    Employee get(String sn);
    List<Employee> getAll();
}
