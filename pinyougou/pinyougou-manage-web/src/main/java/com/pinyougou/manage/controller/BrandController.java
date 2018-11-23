package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.vo.PageResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/brand")
//@Controller
@RestController //组合了@ResponseBody 和 @Controller ；对类中的所有方法生效
public class BrandController {

    //引入远程的服务对象
    @Reference
    private BrandService brandService;

    /**
     * 根据页号和页大小查询品牌列表
     * @param page 页号
     * @param rows 页大小
     * @return 分页结果
     */
    @GetMapping("/findPage")
    public PageResult findPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer rows){
        return brandService.findPage(page, rows);
    }

    /**
     * 根据页号和页大小查询品牌列表
     * @param page 页号
     * @param rows 页大小
     * @return 品牌列表
     */
    @GetMapping("/testPage")
    public List<TbBrand> testPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer rows){
        //return brandService.testPage(page, rows);
        return (List<TbBrand>) brandService.findPage(page, rows).getRows();
    }

    /**
     * 查询所有品牌数据
     * @return 品牌列表json格式字符串
     */
    /*@RequestMapping(value="/findAll", method = RequestMethod.GET)
    @ResponseBody*/
    @GetMapping("/findAll")
    public List<TbBrand> findAll(){
        //return brandService.queryAll();
        return brandService.findAll();
    }
}
