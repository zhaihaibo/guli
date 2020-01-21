package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.common.util.ExcelImportUtil;
import com.guli.edu.entity.Subject;
import com.guli.edu.mapper.SubjectMapper;
import com.guli.edu.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.edu.vo.SubjectNestedVo;
import com.guli.edu.vo.SubjectVo;
import io.swagger.models.Swagger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2019-08-03
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {
    //获取Excel记录并逐条导入
    @Override
    public List<String> batchImport(MultipartFile file) throws Exception {

        //错误消息列表
        List<String> errorMsg = new ArrayList<>();

        //创建工具类对象
        ExcelImportUtil excelHSSFUtil = new ExcelImportUtil(file.getInputStream());
        //获取工作表
        Sheet sheet = excelHSSFUtil.getSheet();

        //如果行数<=1 ,发送错误消息
        int rowCount = sheet.getPhysicalNumberOfRows();
        if (rowCount <= 1) {
            errorMsg.add("请填写数据");
            return errorMsg;
        }
        //遍历
        for (int rowNum = 1; rowNum < rowCount; rowNum++) {

            Row rowData = sheet.getRow(rowNum);
            if (rowData != null) {// 行不为空

                //获取一级分类
                String levelOneValue = "";
                Cell levelOneCell = rowData.getCell(0);
                if(levelOneCell != null){
                    levelOneValue = excelHSSFUtil.getCellValue(levelOneCell).trim();
                    if (StringUtils.isEmpty(levelOneValue)) {
                        errorMsg.add("第" + rowNum + "行一级分类为空");
                        continue;
                    }
                }

                //判断一级分类是否重复
                Subject subject = this.getByTitle(levelOneValue);
                String parentId = null;
                if(subject == null){
                    //将一级分类存入数据库
                    Subject subjectLevelOne = new Subject();
                    subjectLevelOne.setTitle(levelOneValue);
                    subjectLevelOne.setSort(rowNum);
                    baseMapper.insert(subjectLevelOne);//添加
                    parentId = subjectLevelOne.getId();
                }else{
                    parentId = subject.getId();
                }

                //获取二级分类 TODO
                String levelTwoValue = "";
                Cell levelTwoCell = rowData.getCell(1);
                if (levelOneCell!=null){
                    String trim = excelHSSFUtil.getCellValue(levelTwoCell).trim();
                    if (StringUtils.isEmpty(trim)){
                        errorMsg.add("第"+rowNum+"行二级分类为空");
                        continue;
                    }
                }


                //判断二级分类是否重复 TODO
                Subject subjectSub = this.getSubByTitle(levelOneValue, parentId);
                Subject subjectLevelTwo = null;

                //将二级分类存入数据库 TODO
                if (subjectSub==null){
                    //将二级分类存入数据库
                    subjectLevelTwo = new Subject();
                    subjectLevelTwo.setTitle(levelTwoValue);
                    subjectLevelTwo.setParentId(parentId);
                    subjectLevelTwo.setSort(rowNum);
                    baseMapper.insert(subjectLevelTwo);//添加
                }


            }
        }

        return errorMsg;
    }

    //显示课程列表
    @Override
    public List<SubjectNestedVo> nestedList() {
        //创建返回列表
        ArrayList<SubjectNestedVo> subjectNestedVos = new ArrayList<>();

        //获取一级分类
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",0).orderByAsc("sort","id");
        List<Subject> subjects = baseMapper.selectList(wrapper);


        //获取二级分类
        QueryWrapper<Subject> wrapper1 = new QueryWrapper<>();
        wrapper1.ne("parent_id", 0);
        wrapper1.orderByAsc("sort", "id");
        List<Subject> subjects1 = baseMapper.selectList(wrapper1);

        //填充一级分类
        for (int i = 0; i < subjects.size(); i++) {

            SubjectNestedVo subjectNestedVo = new SubjectNestedVo();
            Subject subject = subjects.get(i);
            BeanUtils.copyProperties(subject,subjectNestedVo);



            //填充二级分类vo数据
            ArrayList<SubjectVo> subjectVoArrayList = new ArrayList<>();
            int count2 = subjects1.size();
            for (int j = 0; j < count2; j++) {

                Subject subSubject = subjects1.get(j);
                if(subject.getId().equals(subSubject.getParentId())){

                    //创建二级类别vo对象
                    SubjectVo subjectVo = new SubjectVo();
                    BeanUtils.copyProperties(subSubject, subjectVo);
                    subjectVoArrayList.add(subjectVo);
                }
            }
            subjectNestedVo.setChildren(subjectVoArrayList);
            //把一级分类加入创建好的数组
            subjectNestedVos.add(subjectNestedVo);
        }


        return subjectNestedVos;
    }

    //一级分类的方法
    private Subject getByTitle(String title) {

        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title);
        queryWrapper.eq("parent_id", "0");//一级分类
        return baseMapper.selectOne(queryWrapper);
    }
    //二级分类自定义方法
    private Subject getSubByTitle( String title,String parentId){

        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",title).eq("parent_id",parentId);

        return  baseMapper.selectOne(wrapper);
    }


}
