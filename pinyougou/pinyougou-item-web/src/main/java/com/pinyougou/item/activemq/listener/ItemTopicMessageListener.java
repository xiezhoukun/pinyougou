package com.pinyougou.item.activemq.listener;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.sellergoods.service.ItemCatService;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.listener.adapter.AbstractAdaptableMessageListener;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

/**
 * 在详情系统配置消息监听器接收商品审核之后的主题，根据接收到的spu id数组利用Freemarker生成静态页面
 */
public class ItemTopicMessageListener extends AbstractAdaptableMessageListener {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;


    @Reference
    private GoodsService goodsService;

    @Reference
    private ItemCatService itemCatService;


    @Value("${ITEM_HTML_PATH}")
    private String ITEM_HTML_PATH;

    @Override
    public void onMessage(Message message, Session session) throws JMSException {

        //接收小修，转换为数组
        ObjectMessage objectMessage = (ObjectMessage) message;
        Long[] goodsIds = (Long[]) objectMessage.getObject();

        //遍历每一个商品spu id 生成具体的静态页面
        if (goodsIds != null && goodsIds.length > 0) {
            for (Long goodsId : goodsIds) {
                getHtml(goodsId);
            }
        }
    }

    /**
     * 根据spu id 生成静态页面
     * @param goodsId spu id
     */
    private void getHtml(Long goodsId) {
        try {
            Configuration configuration = freeMarkerConfigurer.getConfiguration();

            //模板

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
