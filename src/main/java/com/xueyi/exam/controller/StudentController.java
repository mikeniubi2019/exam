package com.xueyi.exam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.xueyi.exam.beans.*;
import com.xueyi.exam.service.*;
import com.xueyi.exam.utils.SchoolUtils;
import jxl.Sheet;
import jxl.Workbook;

import jxl.read.biff.BiffException;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mike
 * @since 2020-12-03
 */
@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;
    @Autowired
    StudentCourseService studentCourseService;
    @Autowired
    ExamService examService;
    @Autowired
    CourseService courseService;
    @Autowired
    ProfessionService professionService;
    @Autowired
    DictionnaryController dictionnaryController;
    @Autowired
    SchoolUtils schoolUtils;

    @RequestMapping("/addOneStudent")
    @ResponseBody
    public R addOneStudent(Student student){
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("username",student.getUsername().trim());
        Student s1 = studentService.getOne(studentQueryWrapper);
        if (s1!=null) return new R(500,"用户已存在");
        List<Course> courseList = courseService.getCoursesByProfessionId(student.getProfessionId());
        if (courseList.size()<1) return new R(500,"请先把必修课程添加到专业");

        if (studentService.save(student)){
            s1 = studentService.getOne(studentQueryWrapper);

            Profession profession =professionService.getById(student.getProfessionId());
            profession.setStudentCout(profession.getStudentCout()+1);
            professionService.updateById(profession);

            Student finalS = s1;
            LocalDateTime now = LocalDateTime.now();
            courseList.forEach(course -> {
                StudentCourse studentCourse = new StudentCourse();
                studentCourse.setStudentId(finalS.getId());
                Exam exam = examService.getOne(new QueryWrapper<Exam>().lambda().eq(Exam::getNianji, finalS.getNianJi()).eq(Exam::getCourseId,course.getId()).ge(Exam::getEndTime,now));
                if (exam!=null){
                    studentCourse.setExamId(exam.getId());
                    studentCourse.setPageId(exam.getPageId());
                }
                studentCourse.setCourseId(course.getId());
                studentCourseService.save(studentCourse);
            });
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/studentSearch")
    @ResponseBody
    public LaiuiPage<Student> studentSearch(LaiuiPage laiuiPage, Student student){
        Page page = studentService.page(new Page<>(laiuiPage.getPage(),laiuiPage.getLimit())
                ,new QueryWrapper<Student>().lambda()
                        .eq(!StringUtils.isEmpty(student.getNianJi()),Student::getNianJi,student.getNianJi())
                        .eq(null!=student.getProfessionId(),Student::getProfessionId,student.getProfessionId())
                        .eq(!StringUtils.isEmpty(student.getName()),Student::getName,student.getName())
                        .eq(!StringUtils.isEmpty(student.getUsername()),Student::getUsername,student.getUsername())
        );
        return LaiuiPage.creatDataPage(page.getRecords(),(int)page.getTotal());
    }

    @RequestMapping("/delOneStudent")
    @ResponseBody
    public R delOneStudent(int id){
        Student student = studentService.getById(id);
        if (studentService.removeById(id)){
            studentCourseService.remove(new QueryWrapper<StudentCourse>().lambda().eq(StudentCourse::getStudentId,id));

            Profession profession = professionService.getById(student.getProfessionId());
            profession.setStudentCout(profession.getStudentCout()-1);
            professionService.updateById(profession);

            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/delSelectStudent")
    @ResponseBody
    public R delSelectStudent(String ids){
        String[] stringList = ids.split(",");
        if (stringList.length>0){
            for (String string : stringList){
                try {
                    int id = Integer.parseInt(string.trim());
                    delOneStudent(id);
                }catch (Exception e){
                    continue;
                }
            }
        }
        return R.success;
    }

    @RequestMapping("/findOneStudentById")
    @ResponseBody
    public Student findOneStudentById(int id){
       return studentService.getById(id);
    }


    @RequestMapping("/updateOneStudent")
    @ResponseBody
    public R updateOneStudent(Student student){
        if (studentService.updateById(student)){
            List<Course> courseList = courseService.getCoursesByProfessionId(student.getProfessionId());
            LocalDateTime now = LocalDateTime.now();
            courseList.forEach(course -> {
                StudentCourse studentCourse = studentCourseService.getOne(new QueryWrapper<StudentCourse>().lambda().eq(StudentCourse::getCourseId,course.getId()).eq(StudentCourse::getStudentId,student.getId()));
                if (studentCourse==null) {
                    studentCourse = new StudentCourse();
                    studentCourse.setStudentId(student.getId());
                    Exam exam = examService.getOne(new QueryWrapper<Exam>().lambda().eq(Exam::getNianji, student.getNianJi()).eq(Exam::getCourseId,course.getId()).le(Exam::getEndTime,now));
                    if (exam!=null){
                        studentCourse.setExamId(exam.getId());
                        studentCourse.setPageId(exam.getPageId());
                    }
                    studentCourse.setCourseId(course.getId());
                    studentCourseService.save(studentCourse);
                }else {
                    Exam exam = examService.getOne(new QueryWrapper<Exam>().lambda().eq(Exam::getNianji, student.getNianJi()).eq(Exam::getCourseId,course.getId()).le(Exam::getEndTime,now));
                    if (exam!=null){
                        studentCourse.setExamId(exam.getId());
                        studentCourse.setPageId(exam.getPageId());
                    }
                    studentCourseService.updateById(studentCourse);
                }
            });
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/studentList")
    public String studentList(Model model){
        model.addAttribute("professionList",professionService.list());
        model.addAttribute("nianJiList",dictionnaryController.getListByName("nianJi"));
        return "pages/student/studentList";
    }

    @RequestMapping("/studentDetails")
    public String studentDetails(int id, Model model){
        Student student = studentService.getById(id);
        model.addAttribute(student);
        model.addAttribute("professionList",professionService.list());
        model.addAttribute("nianJiList",dictionnaryController.getListByName("nianJi"));
        return "pages/student/studentDetails";
    }

    @RequestMapping("/studentDetailsNoId")
    public String studentDetails(Model model){
        Student student = schoolUtils.getCurrentStudent();
        model.addAttribute(student);
        model.addAttribute("professionList",professionService.list());
        model.addAttribute("nianJiList",dictionnaryController.getListByName("nianJi"));
        return "pages/student/studentDetails";
    }

    @RequestMapping("/studentEdit")
    public String studentEdit(int id, Model model){
        Student student = studentService.getById(id);
        model.addAttribute(student);
        model.addAttribute("professionList",professionService.list());
        model.addAttribute("nianJiList",dictionnaryController.getListByName("nianJi"));
        return "pages/student/studentEdit";
    }

    @RequestMapping("/studentAdd")
    public String studentAdd(Model model){
        model.addAttribute("professionList",professionService.list());
        model.addAttribute("nianJiList",dictionnaryController.getListByName("nianJi"));
        return "pages/student/studentAdd";
    }

    @RequestMapping("/studentPage")
    public String studentPage(Model model){
        Student student = schoolUtils.getCurrentStudent();
        Profession profession = professionService.getOne(new QueryWrapper<Profession>().lambda().eq(Profession::getId,student.getProfessionId()));
        model.addAttribute("student",student);
        model.addAttribute("profession",profession);
        return "pages/student/studentPage";
    }

    @RequestMapping("/studentUpload")
    public String studentUpload(Model model){
        model.addAttribute("professionList",professionService.list());
        return "pages/upload/studentUpload";
    }

    @RequestMapping("/upload")
    @ResponseBody
    public R upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return R.fail;
        }
        int successfulCount=0;
        StringBuilder stringBuilder = new StringBuilder();
        String fileName = file.getOriginalFilename();
        if (fileName.endsWith(".xls")){
            List<Student> studentList = new ArrayList<>();
            InputStream inputStream = null;
            try {
                inputStream = file.getInputStream();
                Workbook workbook = Workbook.getWorkbook(inputStream);
                int sheet_size = workbook.getNumberOfSheets();
                for (int index=0;index<sheet_size-1;index++){
                    Sheet sheet = workbook.getSheet(index);
                    for (int row=0;row<sheet.getRows();row++){
                        if (sheet.getCell(0,row)==null||sheet.getCell(0,row).getContents()==null||sheet.getCell(0,row).getContents().equals(""))
                            continue;
                        Student student = new Student();
                        String name = sheet.getCell(0,row).getContents();
                        String username = sheet.getCell(1,row).getContents();
                        String password = sheet.getCell(2,row).getContents();
                        String idCard = sheet.getCell(3,row).getContents();
                        String nianji = sheet.getCell(4,row).getContents();
                        int professionId = Integer.parseInt(sheet.getCell(5,row).getContents());
                        student.setUsername(username);
                        student.setPassword(password);
                        student.setNianJi(nianji);
                        student.setName(name);
                        student.setIdCard(idCard);
                        student.setProfessionId(professionId);
                        studentList.add(student);
                    }
                }
            } catch (IOException | BiffException e) {
                e.printStackTrace();
                return R.fail;
            }finally {
                if (inputStream!=null){
                    inputStream.close();
                }
            }

            for (Student student : studentList){
                R r=null;
                try {
                    r = addOneStudent(student);
                }catch (Exception e){
                    continue;
                }
                if (r!=null&&r.code==100){
                    successfulCount+=1;
                }
                stringBuilder.append(" "+student.getName());
            }
            R r = new R();
            r.setCode(100);
            r.setM("成功了"+successfulCount+"个人数，以下为成功的人名单："+stringBuilder.toString());
            return r;

        }else {
            return R.fail;
        }
    }

    @RequestMapping("/studentExamSearch")
    @ResponseBody
    public LaiuiPage<StudentCourseVo> studentExamSearch(LaiuiPage laiuiPage, Student student,Integer courseId){
        List<StudentCourseVo > studentCourseVoList = studentService.findStudentExamByCondiction(laiuiPage,student,courseId);
        return LaiuiPage.creatDataPage(studentCourseVoList,studentCourseVoList.size());
    }

    @RequestMapping("/studentExamList")
    public String studentExamList(Model model) throws JsonProcessingException {

        List professionList = professionService.list();
        List courseList = courseService.list();

        model.addAttribute("nianJiList",dictionnaryController.getListByName("nianJi"));
        model.addAttribute("professionList",professionList);
        model.addAttribute("courseList",courseList);

        return "pages/student/studentExamList";
    }

    @RequestMapping("/professionMap")
    @ResponseBody
    public Map professionMap(){
        List<Profession> professionList = professionService.list();
        Map map = new HashMap();
        for (Profession profession : professionList){
            map.put(profession.getId(),profession.getProfessionName());
        }
        return map;
    }

    @RequestMapping("/courseMap")
    @ResponseBody
    public Map courseMap(){
        List<Course> courseList = courseService.list();
        Map map = new HashMap();
        for (Course course : courseList){
            map.put(course.getId(),course.getCourseName());
        }
        return map;
    }
}

