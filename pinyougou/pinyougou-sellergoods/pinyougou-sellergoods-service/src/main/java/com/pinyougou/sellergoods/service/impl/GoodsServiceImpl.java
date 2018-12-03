package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service(interfaceClass = GoodsService.class)
public class GoodsServiceImpl extends BaseServiceImpl<TbGoods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsDescMapper goodsDescMapper;

    @Autowired
    private ItemCatMapper itemCatMapper;

    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public PageResult search(Integer page, Integer rows, TbGoods goods) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbGoods.class);
        Example.Criteria criteria = example.createCriteria();
        /*if(!StringUtils.isEmpty(goods.get***())){
            criteria.andLike("***", "%" + goods.get***() + "%");
        }*/

        List<TbGoods> list = goodsMapper.selectByExample(example);
        PageInfo<TbGoods> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void addGoods(Goods goods) {
        //新增商品基本信息
        goodsMapper.insertSelective(goods.getGoods());

        //新增商品描述信息
        goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
        goodsDescMapper.insertSelective(goods.getGoodsDesc());

        saveItemList(goods);


    }

    @Override
    public void deleteGoodsByIds(Long[] ids) {
        //根据商品spu id更新商品的删除状态(is_delete)为已删除
        TbGoods goods = new TbGoods();
        goods.setIsDelete("1");

        Example example = new Example(TbGoods.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        goodsMapper.updateByExampleSelective(goods, example);

    }

    private void saveItemList(Goods goods) {

        if ("1".equals(goods.getGoods().getIsEnableSpec())) {

            //如果启动规格,则需要按照规格生成不同的SKU商品
            //新增商品SKU
            for (TbItem item : goods.getItemList()) {

                String title = goods.getGoods().getGoodsName();
                //组合规格选项形成SKU标题
                Map<String, Object> map = JSON.parseObject(item.getSpec());
                Set<Map.Entry<String, Object>> entries = map.entrySet();
                for (Map.Entry<String, Object> entry : entries) {
                    title += " " + entry.getValue().toString();
                }

                item.setTitle(title);

                setItemValue(item, goods);

                itemMapper.insertSelective(item);
            }
        } else {
            //如果没有启动规格,则只存在SKU信息
            TbItem tbItem = new TbItem();
            tbItem.setTitle(goods.getGoods().getGoodsName());
            tbItem.setPrice(goods.getGoods().getPrice());
            tbItem.setNum(9999);
            tbItem.setStatus("0");
            tbItem.setIsDefault("1");
            tbItem.setSpec("{}");

            itemMapper.insertSelective(tbItem);
        }
    }

    private void setItemValue(TbItem item, Goods goods) {
        //图片
        List<Map> imgList = JSONArray.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
        if (imgList != null && imgList.size() > 0) {
            //将商品的第一张图作为sku的图片
            item.setImage(imgList.get(0).get("url").toString());
        }

        //商品分类id
        item.setCategoryid(goods.getGoods().getCategory3Id());

        //商品分类名称
        TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
        item.setCategory(itemCat.getName());

        //创建时间
        item.setCreateTime(new Date());

        //更新时间
        item.setUpdateTime(item.getCreateTime());
        //SPU商品id
        item.setGoodsId(goods.getGoods().getId());
        //商家id
        item.setSellerId(goods.getGoods().getSellerId());
        //商家名称
        TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
        item.setSeller(seller.getName());
        //品牌名称
        TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
        item.setBrand(brand.getName());
    }
}
