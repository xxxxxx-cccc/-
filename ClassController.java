package com.milotnt.controller;

import com.milotnt.pojo.ClassOrder;
import com.milotnt.pojo.ClassTable;
import com.milotnt.pojo.Employee;
import com.milotnt.service.ClassOrderService;
import com.milotnt.service.ClassTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author MiloTnT [milotntspace@gmail.com]
 * @date 2021/8/19
 */

@Controller
@RequestMapping("/employee/class")  // 修改为员工路径
public class ClassController {

    @Autowired
    private ClassTableService classTableService;

    @Autowired
    private ClassOrderService classOrderService;



    //查询课程
    @RequestMapping("/selClass")
    public String selectClass(Model model, HttpSession session) {
        List<ClassTable> classList = classTableService.findAll();
        model.addAttribute("classList", classList);
        Employee employee = (Employee) session.getAttribute("employee");
        if (employee != null) {
            model.addAttribute("currentCoach", employee.getEmployeeName());
        }
        return "employeeSelectClass";  // 修改为员工视图
    }

    //跳转新增课程页面
    @RequestMapping("/toAddClass")
    public String toAddClass(Model model, HttpSession session) {
        Employee employee = (Employee) session.getAttribute("employee");
        if (employee != null) {
            model.addAttribute("coachName", employee.getEmployeeName());
        }
        return "employeeAddClass";
    }

    //新增课程
    @RequestMapping("/addClass")
    public String addClass(ClassTable classTable) {
        classTableService.insertClass(classTable);
        return "redirect:selClass";
    }

    //删除课程
    @RequestMapping("/delClass")
    public String deleteClass(Integer classId) {
        classTableService.deleteClassByClassId(classId);
        classTableService.deleteOrderByClassId(classId);
        return "redirect:selClass";
    }

    //查询课程报名信息
    @RequestMapping("/selClassOrder")
    public String selectClassOrder(Integer classId, Model model) {
        List<ClassOrder> classOrderList = classOrderService.selectMemberOrderList(classId);
        model.addAttribute("classOrderList", classOrderList);
        return "employeeSelectClassOrder";  // 修改为员工视图
    }

}