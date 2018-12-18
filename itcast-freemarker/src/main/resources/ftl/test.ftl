<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Freemarker测试</title>
</head>
<body>
<#--这是freemarker注释, 不会输出到文件中-->

<h1>${name}: ${message}</h1>

<#--assign-->
<#--简单类型-->
<#assign linkman="黑马"/>
联系人: ${linkman}
<br>

<#--对象-->
<#assign info={"mobile":"1380000000", "address":"广州天河吉山村"}/>
联系电话:${info.mobile}, 联系地址: ${info.address}
<br>

<#--include-->
<#include "head.ftl"/>
<br>

<#--if-->
<#assign bool=true/>
<#if bool>
    bool的值为true
<#else>
    bool的值为false
</#if>

<br>

<#--list-->
<#list goodsList as goods>
    ${goods_index}, 名称为:${goods.name}, 价格为:${goods.price}<br>
</#list>
</body>
</html>