package com.milotnt.controller;

import com.milotnt.pojo.ClassOrder;
import com.milotnt.pojo.Employee;
import com.milotnt.service.ClassOrderService;
import com.milotnt.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author MiloTnT [milotntspace@gmail.com]
 * @date 2021/8/18
 */

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;



    //查询员工
    @RequestMapping("/selEmployee")
    public String selectEmployee(Model model) {
        List<Employee> employeeList = employeeService.findAll();
        model.addAttribute("employeeList", employeeList);
        return "selectEmployee";
    }

    //跳转新增员工页面
    @RequestMapping("/toAddEmployee")
    public String toAddEmployee() {
        return "addEmployee";
    }

    //新增员工
    @RequestMapping("/addEmployee")
    public String addEmployee(Employee employee) {
        //工号随机生成
        Random random = new Random();
        String account1 = "1010";
        for (int i = 0; i < 5; i++) {
            account1 += random.nextInt(10);
        }
        Integer account = Integer.parseInt(account1);

        //获取当前日期
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowDay = simpleDateFormat.format(date);


        // 设置初始密码为123456
        employee.setEmployeePassword("123456");
        employee.setEmployeeAccount(account);
        employee.setEntryTime(nowDay);

        employeeService.insertEmployee(employee);

        return "redirect:selEmployee";

    }

    //删除员工
    @RequestMapping("/delEmployee")
    public String deleteEmployee(Integer employeeAccount) {
        employeeService.deleteByEmployeeAccount(employeeAccount);
        return "redirect:selEmployee";
    }

    @Autowired
    private ClassOrderService classOrderService;

    // 查看所有课程报名信息
    @RequestMapping("/viewClassSignups")
    public String viewClassSignups(Model model) {
        List<ClassOrder> allClassOrders = classOrderService.selectAll();
        model.addAttribute("allClassOrders", allClassOrders);
        return "employeeClassSignups";
    }

    // 新增：跳转到员工个人信息页面
    @RequestMapping("/toEmployeeInfo")
    public String toEmployeeInfo(Model model, HttpSession session) {
        Employee employee = (Employee) session.getAttribute("employee");
        if (employee == null) {
            return "redirect:/toEmployeeLogin"; // 未登录，重定向到登录
        }
        model.addAttribute("employee", employee);
        return "employeeInformation";
    }

    // 新增：跳转到编辑员工个人信息页面
    @RequestMapping("/toUpdateEmployeeInfo")
    public String toUpdateEmployeeInfo(Model model, HttpSession session) {
        Employee employee = (Employee) session.getAttribute("employee");
        if (employee == null) {
            return "redirect:/toEmployeeLogin";
        }
        model.addAttribute("employee", employee);
        return "updateEmployeeInformation";
    }

    // 新增：更新员工个人信息
    // EmployeeController.java 中的这个方法替换掉原来的
    @RequestMapping("/updateEmployeeInfo")
    public String updateEmployeeInfo(Employee employee, HttpSession session, Model model) {
        Employee currentEmployee = (Employee) session.getAttribute("employee");

        // 安全校验：防止越权修改他人信息
        if (currentEmployee == null ||
                !currentEmployee.getEmployeeAccount().equals(employee.getEmployeeAccount())) {
            return "redirect:/toEmployeeLogin";
        }

        // 关键修复：只更新允许修改的字段，不要动密码！
        Employee toUpdate = new Employee();
        toUpdate.setEmployeeAccount(employee.getEmployeeAccount());
        toUpdate.setEmployeeName(employee.getEmployeeName());
        toUpdate.setEmployeeGender(employee.getEmployeeGender());
        toUpdate.setEmployeeAge(employee.getEmployeeAge());
        toUpdate.setEmployeeMessage(employee.getEmployeeMessage());

        // 执行更新
        employeeService.updateEmployeeByAccount(toUpdate);

        // 关键修复：不要用 employeeLogin()！它需要密码！
        // 直接从数据库重新查一次完整信息（不依赖密码）
        Employee updatedEmployee = employeeService.findByAccount(employee.getEmployeeAccount());

        // 如果没查到（异常情况），保留旧的也行
        if (updatedEmployee == null) {
            updatedEmployee = currentEmployee; // 保底
            updatedEmployee.setEmployeeName(employee.getEmployeeName());
            updatedEmployee.setEmployeeGender(employee.getEmployeeGender());
            updatedEmployee.setEmployeeAge(employee.getEmployeeAge());
            updatedEmployee.setEmployeeMessage(employee.getEmployeeMessage());
        }

        // 更新 session 中的登录用户信息
        session.setAttribute("employee", updatedEmployee);

        // 可选：提示修改成功
        model.addAttribute("msg", "个人信息修改成功！");

        return "redirect:/employee/toEmployeeInfo";
    }
}
