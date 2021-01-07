package com.xueyi.exam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xueyi.exam.beans.Dictionnary;
import com.xueyi.exam.beans.LaiuiPage;
import com.xueyi.exam.beans.Question;
import com.xueyi.exam.beans.R;
import com.xueyi.exam.service.DictionnaryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mike
 * @since 2020-12-06
 */
@Controller
@RequestMapping("/dictionnary")
public class DictionnaryController {
    @Autowired
    DictionnaryService dictionnaryService;


    @RequestMapping("/addOneDictionnary")
    @ResponseBody
    public R addOneDictionnary(Dictionnary dictionnary){
        if (dictionnaryService.save(dictionnary)){
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/delOneDictionnary")
    @ResponseBody
    public R delOneDictionnary(int id){
        if (dictionnaryService.removeById(id)){
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/updateOneDictionnary")
    @ResponseBody
    public R updateOneDictionnary(Dictionnary dictionnary){
        if (dictionnaryService.updateById(dictionnary)){
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/dictionnarySearch")
    @ResponseBody
    public LaiuiPage<Dictionnary> dictionnarySearch(Dictionnary dictionnary , LaiuiPage laiuiPage){
        com.baomidou.mybatisplus.extension.plugins.pagination.Page page = dictionnaryService.page(
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(laiuiPage.getPage(),laiuiPage.getLimit())
                ,new QueryWrapper<Dictionnary>().lambda()
                        .eq(dictionnary.getId()!=null,Dictionnary::getId,dictionnary.getId())
                        .eq(StringUtils.isNotBlank(dictionnary.getName()),Dictionnary::getName,dictionnary.getName())
                        .eq(StringUtils.isNotBlank(dictionnary.getValue()),Dictionnary::getValue,dictionnary.getValue())
                        .orderByDesc(Dictionnary::getId));
        return LaiuiPage.creatDataPage(page.getRecords(),(int)page.getTotal());
    }

    @RequestMapping("/nianJiAddList")
    public String nianJiAddList(){
        return "pages/dictionnary/nianJiAddList";
    }

    @RequestMapping("/xueQiAddList")
    public String xueQiAddList(){
        return "pages/dictionnary/xueQiAddList";
    }

    @RequestMapping("/globalConfigEdit")
    public String globalConfigEdit(Model model){
        model.addAttribute("nianJiList",getListByName("nianJi"));
        model.addAttribute("xueQiList",getListByName("xueQi"));
        model.addAttribute("globalXueQi",getOneByName("globalXueQi"));
        model.addAttribute("globalXueNian",getOneByName("globalXueNian"));
        return "pages/dictionnary/globalConfigEdit";
    }

    @RequestMapping("/updateGlobalConfig")
    @ResponseBody
    public R updateGlobalConfig(String globalXueQi,String globalXueNian){
        Dictionnary dictionnary = new Dictionnary();
        dictionnary.setName("globalXueQi");
        dictionnary.setValue(globalXueQi);
        dictionnaryService.saveOrUpdate(dictionnary,new QueryWrapper<Dictionnary>().lambda().eq(Dictionnary::getName,"globalXueQi"));
        Dictionnary dictionnary2 = new Dictionnary();
        dictionnary2.setName("globalXueNian");
        dictionnary2.setValue(globalXueNian);
        dictionnaryService.saveOrUpdate(dictionnary2,new QueryWrapper<Dictionnary>().lambda().eq(Dictionnary::getName,"globalXueNian"));
        return R.success;

    }

    public List<Dictionnary> getListByName(String name){
        return dictionnaryService.list(new QueryWrapper<Dictionnary>().lambda().eq(Dictionnary::getName,name));
    }
    public Dictionnary getOneByName(String name){
        return dictionnaryService.getOne(new QueryWrapper<Dictionnary>().lambda().eq(Dictionnary::getName,name));
    }
}

