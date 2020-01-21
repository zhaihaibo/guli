package com.guli.edu.controller.admin;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.vo.R;
import com.guli.edu.entity.Teacher;
import com.guli.edu.query.TeacherQuery;
import com.guli.edu.service.TeacherService;
import io.swagger.annotations.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2019-08-03
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/admin/edu/teacher")
@CrossOrigin
public class TeacherAdminController {

    @Autowired
    private TeacherService teacherService;


    @GetMapping  //这里没有写任何路径，因为在类上的requestmapping里的路径已经引用到这里了
    @ApiOperation("查询所有讲师")
    public R lsit(){
        List<Teacher> list = teacherService.list(null);
       return R.ok().message("获取讲师列表成功！").data("items",list);
    }

    //根据id删除
    @ApiOperation("根据id删除讲师")
    @DeleteMapping("{id}")
    public R removeById(@ApiParam(value = "讲师id",name = "请输入 id" ,required = true) @PathVariable String id){
        boolean b = teacherService.removeById(id);
        if (b==true) {
            return R.ok().message("根据id删除讲师成功！");
        }else {
            return R.error().message("根据id删除讲师失败！");
        }
    }


    @ApiOperation(value = "讲师分页列表")
    @GetMapping("{page}/{limit}")
    public R pageList(
            @ApiParam(value = "当前页码", name="page", required = true)
            @PathVariable Long page,
            @ApiParam(value = "每页记录数", name="limit", required = true)
            @PathVariable Long limit,
//比较简单的对象，可以不使用表单来出传递，使用GetMAPPING
            @ApiParam(value = "查询对象", name="teacherQuery", required = false)
                    TeacherQuery teacherQuery){

        Page<Teacher> pageParam = new Page<>(page,limit);
//		teacherService.page(pageParam, null);  这一步已经在pageQuery中实现了，方法是selectPage，mapper级别的分页
        teacherService.pageQuery(pageParam, teacherQuery);

        List<Teacher> records = pageParam.getRecords();
        long total = pageParam.getTotal();

        return R.ok().data("rows", records).data("total", total);
    }

    //新增讲师
    @ApiOperation(value = "新增讲师")
    @PostMapping
    public R save(
            @ApiParam(value = "讲师对象",name = "teacher",required = true)
            @RequestBody Teacher teacher){

        boolean save = teacherService.save(teacher);
        if (save==true) {
            return R.ok().message("新增讲师成功！");
        }else {
            return R.error().message("新增讲师失败！");
        }
    }

    @ApiOperation(value = "根据ID查询讲师")
    @GetMapping("{id}")
    public R getById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id){

        Teacher teacher = teacherService.getById(id);
        return R.ok().data("item", teacher);
    }

    @ApiOperation(value = "根据ID修改讲师")
    @PutMapping("{id}")
    public R updateById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id,

            @ApiParam(name = "teacher", value = "讲师对象", required = true)
            @RequestBody Teacher teacher){

        teacher.setId(id);
        teacherService.updateById(teacher);
        return R.ok();
    }

}

