package com.milotnt.service.impl;

import com.milotnt.mapper.EmployeeMapper;
import com.milotnt.pojo.Employee;
import com.milotnt.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MiloTnT [milotntspace@gmail.com]
 * @date 2021/8/11
 */

@Service
public class EmployeeServiceImpl implements EmployeeService {


    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<Employee> findAll() {
        return employeeMapper.findAll();
    }

    @Override
    public Boolean deleteByEmployeeAccount(Integer employeeAccount) {
        return employeeMapper.deleteByEmployeeAccount(employeeAccount);
    }

    @Override
    public Boolean insertEmployee(Employee employee) {
        return employeeMapper.insertEmployee(employee);
    }



    @Override
    public Integer selectTotalCount() {
        return employeeMapper.selectTotalCount();
    }

    @Override
    public Employee employeeLogin(Employee employee) {
        return employeeMapper.selectByAccountAndPassword(employee);
    }

    // 新增：更新员工信息
    @Override
    public Boolean updateEmployeeByAccount(Employee employee) {
        return employeeMapper.updateEmployeeByAccount(employee);
    }

    @Override
    public Employee findByAccount(Integer employeeAccount) {
        return employeeMapper.selectByEmployeeAccount(employeeAccount);
    }
}
