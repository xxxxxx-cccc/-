package com.milotnt.service;

import com.milotnt.pojo.Employee;

import java.util.List;

/**
 * @author MiloTnT [milotntspace@gmail.com]
 * @date 2021/8/11
 */

public interface EmployeeService {

    //查询所有员工
    List<Employee> findAll();

    //根据员工账号删除员工
    Boolean deleteByEmployeeAccount(Integer employeeAccount);

    //添加新员工
    Boolean insertEmployee(Employee employee);


    //查询员工数
    Integer selectTotalCount();

    Employee employeeLogin(Employee employee); // 员工登录验证

    // 新增：根据账号更新员工信息
    Boolean updateEmployeeByAccount(Employee employee);

    Employee findByAccount(Integer employeeAccount);

}
