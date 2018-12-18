package cn.itcast.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.FileWriter;
import java.util.*;

public class FreemarkerTest {

    @Test
    public void test() throws Exception {
        //创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //设置默认生成文件编码
        configuration.setDefaultEncoding("utf-8");
        //设置模板路径
        configuration.setClassForTemplateLoading(FreemarkerTest.class, "/ftl");
        //获取模板
        Template template = configuration.getTemplate("test.ftl");
        //加载数据
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("name", "传智播客");
        dataModel.put("message", "欢迎使用Freemarker");

        List<Map<String, Object>> goodsList = new ArrayList<>();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "橘子");
        map1.put("price", 4.5);
        goodsList.add(map1);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "柚子");
        map2.put("price", 7);
        goodsList.add(map2);

        dataModel.put("goodsList", goodsList);

        //创建输出对象
        FileWriter fileWriter = new FileWriter("D:\\doc\\test\\test.html");
        template.process(dataModel, fileWriter);
        //关闭输出
        fileWriter.close();
    }
}
