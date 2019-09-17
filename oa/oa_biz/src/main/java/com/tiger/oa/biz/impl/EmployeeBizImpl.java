package com.tiger.oa.biz.impl;

import com.tiger.oa.biz.EmployeeBiz;
import com.tiger.oa.dao.EmployeeDao;
import com.tiger.oa.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("employeeBiz")
public class EmployeeBizImpl implements EmployeeBiz {
    @Qualifier("employeeDao")
    @Autowired
    private EmployeeDao employeeDao;
    public void add(Employee employee) {
        employee.setPassword("000000");
        employeeDao.insert(employee);
    }

    public void remove(String sn) {
        employeeDao.delete(sn);
    }

    public void edit(Employee employee) {
        employeeDao.update(employee);
    }

    public Employee get(String sn) {
        return employeeDao.select(sn);
    }

    public List<Employee> getAll() {
        return employeeDao.selectAll();
    }
}
