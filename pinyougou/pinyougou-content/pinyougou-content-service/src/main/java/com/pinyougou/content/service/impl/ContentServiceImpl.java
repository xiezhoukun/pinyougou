package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.ContentMapper;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Service(interfaceClass = ContentService.class)
public class ContentServiceImpl extends BaseServiceImpl<TbContent> implements ContentService {

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    //在redis中内容对应的key
    private static final String REDIS_CONTENT = "content";

    @Override
    public void add(TbContent tbContent) {
        super.add(tbContent);
        //更新内容分类对应在redis中的内容列表缓存
        updateContentInRedisByCategoryId(tbContent.getCategoryId());
    }

    private void updateContentInRedisByCategoryId(Long categoryId) {
        try {
            redisTemplate.boundHashOps(REDIS_CONTENT).delete(categoryId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(TbContent tbContent) {
        TbContent oldContent = super.findOne(tbContent.getId());

        super.update(tbContent);

        //是否修改了内容分类,如果修改了分类则需要将新旧分类对应的内容列表都得更新
        if (!oldContent.getCategoryId().equals(tbContent.getCategoryId())) {
            updateContentInRedisByCategoryId(oldContent.getCategoryId());
        }
        updateContentInRedisByCategoryId(tbContent.getCategoryId());
    }

    @Override
    public void deleteByIds(Serializable[] ids) {
        //根据内容id集合查询内容列表,然后更新该内容分类对应的内容列表缓存
        Example example = new Example(TbContent.class);
        example.createCriteria().andIn("id", Arrays.asList(ids));
        List<TbContent> contentList = contentMapper.selectByExample(example);
        if (contentList != null && contentList.size() > 0) {
            for (TbContent content : contentList) {
                updateContentInRedisByCategoryId(content.getCategoryId());
            }
        }

        //删除内容
        super.deleteByIds(ids);
    }

    @Override
    public PageResult search(Integer page, Integer rows, TbContent content) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbContent.class);
        Example.Criteria criteria = example.createCriteria();
        /*if(!StringUtils.isEmpty(content.get***())){
            criteria.andLike("***", "%" + content.get***() + "%");
        }*/

        List<TbContent> list = contentMapper.selectByExample(example);
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public List<TbContent> findContentListByCategoryId(Long categoryId) {

        List<TbContent> list = null;
        try {
            //先从缓存中查找
            list = (List<TbContent>) redisTemplate.boundHashOps(REDIS_CONTENT).get(categoryId);
            if (list != null) {
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        /**
         * --查询内容分类id为1并且有效的广告并且按照排序字段降序排序
         * select * from tb_content where category_id=? and status=1 order by sort_order desc
         */
        Example example = new Example(TbContent.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("categoryId", categoryId);
        //查询启用状态的
        criteria.andEqualTo("status", "1");

        //降序排序
        example.orderBy("sortOrder").desc();

        list = contentMapper.selectByExample(example);

        try {
            //设置某个分类对应的广告内容到缓存中
            redisTemplate.boundHashOps(REDIS_CONTENT).put(categoryId, list);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
