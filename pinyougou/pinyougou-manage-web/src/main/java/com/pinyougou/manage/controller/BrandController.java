package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;
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
     * 根据条件分页查询
     * @param brand 查询条件
     * @param page 页号
     * @param rows 页大小
     * @return
     */
    @PostMapping("/search")
    public PageResult search(@RequestBody TbBrand brand, @RequestParam(value = "page", defaultValue = "1")Integer page, @RequestParam(value = "rows", defaultValue = "10")Integer rows) {
        return brandService.search(brand, page, rows);
    }

    @GetMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            brandService.deleteByIds(ids);
            return Result.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Result.fail("删除失败");
    }

    @GetMapping("/findOne")
    public TbBrand findOne(Long id) {
        return brandService.findOne(id);
    }

    @GetMapping("/update")
    public Result update(@RequestBody TbBrand brand) {
        try {
            brandService.update(brand);
            return Result.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Result.fail("修改失败");
    }

    @PostMapping("/add")
    public Result add(@RequestBody TbBrand brand) {

        try {
            brandService.add(brand);
            return Result.ok("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Result.fail("新增失败");

    }

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
