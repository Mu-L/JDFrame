
# JDFrame

-------
![travis](https://travis-ci.org/nRo/DataFrame.svg?branch=master)
[![License](http://img.shields.io/badge/license-apache%202-brightgreen.svg)](https://github.com/burukeYou/fast-retry/blob/main/LICENSE)

# Documentation

-------
[![Javadocs](http://javadoc.io/badge/de.unknownreality/dataframe.svg?color=blue)](http://javadoc.io/doc/io.github.burukeyou/jdframe)


# 背景
burukeyou的私房工具， 由于经常记不住stream的一些api每次要复制来复制去，想要更加语意化的api

于是想到了以前写大数据Spark pandnas 等DataFrame模型时的api， 然后发现其实也存在java的JVM层的DataFrame模型比如 tablesaw，joinery

但是他们得硬编码去指定字段名，这对于有代码洁癖的burukeyou实在难以忍受，而且我只是简单统计下数据，我想在一些场景下能不能使用匿名函数去指定的字段处理去处理，于是便有了这个

一个jvm层级的仿DataFrame工具，语意化和简化java8的stream流式处理工具， 也支持窗口函数


# 快速开始
# 版本列表
https://central.sonatype.com/artifact/io.github.burukeyou/jdframe/versions

## 引入依赖
```java
<dependency>
    <groupId>io.github.burukeyou</groupId>
    <artifactId>jdframe</artifactId>
    <version>0.0.5</version>
</dependency>
```



## 快速使用案例
```java
        // 获取学生年龄在9到16岁的学学校合计分数最高的前10名的学校
        SDFrame<FI2<String, BigDecimal>> sdf2 = SDFrame.read(studentList)
                .whereNotNull(Student::getAge)
                .whereBetween(Student::getAge,9,16)
                .groupBySum(Student::getSchool, Student::getScore)
                .sortDesc(FI2::getC2)
                .cutFirst(10);
```

其他具体API见 IFrame接口
JDFrame 与 SDFrame区别 ，JDFrame的所有操作都是实时生效的 

# Frame的API列表
- read()  -读取成Frame进行数据处理
- from()   -从其他流读取成Frame进行数据处理
- toLists() -转换成普通列表
- stream()  -获取Frame的流
- forEachDo()  -迭代处理每个元素等价于forEach
- defaultScale()  -设置统计的数值为小数时的保留精度
- show()   -打印Frame成表到控制台
- columns()  -获取表头列名
- col()    -获取某一列值
- page()  -获取分页数据
- append()  -添加元素
- union()   -合并其他Frame
- join()    -内连接
- leftJoin()  -左边接
- rightJoin()  -右连接
- map()      -矩阵转换
- mapPercent()  -百分数转换
- partition()  -分区
- addRowNumberCol() -添加序号列（从1开始）
- addRankCol()  -添加排名列
- sortDesc()  -降序排序
- sortAsc()   -升序排序
- cutFirst()  -截取前N个
- cutLast()   -截取后N个
- cut()       - 范围截取
- cutPage()   - 分页截取
- cutFirstRank() -截取前N排名数据
- head()       -获取前N个元素
- tail()      -获取后N个元素
- subList()    - 范围截取
- replenish()   -补充缺失条目
- distinct()   -去重
  
==== 筛选 == ======
- where()       -自定义筛选
- whereNull()   -筛选Null值。如果是字符串兼容了空字符串的处理
- whereNotNull()  -筛选非Null值。
- whereBetween()  -筛选范围内的。 前闭后闭
- whereBetweenN()  -筛选范围内的。前开后开  
- whereBetweenR()   -筛选范围内的。 前开后闭
- whereBetweenL()    -筛选范围内的。 前闭后开
- whereNotBetween()   -筛选范围外的。  前闭后闭
- whereNotBetweenN()  -筛选范围外的。  前开后开
- whereIn()    - 筛选在列表内的  
- whereNotIn()   - 筛选不在列表内的
- whereTrue()   - 筛选值为true的
- whereNotTrue()  - 筛选值为false的
- whereEq()    - 筛选等于的
- whereNotEq()    - 筛选不等于的
- whereGt()    - 筛选大于的
- whereGe()   - 筛选大于等于的
- whereLt()   - 筛选小于的
- whereLe()    - 筛选小于等于的
- whereLike()   - 模糊匹配
- whereNotLike()  -不模糊匹配的
- whereLikeLeft() - 前缀匹配
- whereLikeRight() - 后缀匹配

 ===== 汇总 ==== 
- sum()      -对某列求和
- avg()    -对某列求平均值
- maxMin()   -获取最大和最小对象
- maxMinValue()   -获取最大和最小值
- max()      -获取最大对象
- maxValue()  -获取最大值
- minValue()  -获取最小值
- min()    -获取最小对象
- count()  -获取行数
- countDistinct()  -去重后获取行数


==== 分组 ====
- group()   -分组
- groupBySum()   -分组求和
- groupByCount()   -分组求数量
- groupBySumCount()   -分组求和以及求数量
- groupByAvg()   -分组求平均值
- groupByMax()    -分组求最大对象
- groupByMaxValue()   -分组求最大值
- groupByMin()     -分组求最小对象
- groupByMinValue()    -分组求最小值
- groupByMaxMinValue()   -分组求最大值和最小值
- groupByMaxMin()    -分组求最大对象和最小对象

==== 窗口函数 ====  
- window()   -打开窗口函数
- overRowNumber()   -生成行号
- overRank()        -生成排名号。排名不连续
- overDenseRank()   -生成排名号。排名连续
- overPercentRank() -生成百分比排名号。  (rank-1) / (rows-1)
- overCumeDist()  -生成累积分布的比率
- overLag()    -生成当前行的前N行数据
- overLead()   -生成当前行的前后行数据
- overNthValue()  -生成窗口范围内的第N行数据
- overFirstValue() -生成窗口范围内的第1行数据
- overLastValue()  -生成窗口范围内的最后1行数据
- overSum()   -生成和
- overAvg()   -生成平均值
- overMaxValue()  -生成最大值
- overMinValue()  -生成最小值
- overCount()   -生成数量
- overNtile()  -分桶，生成桶编号



# 其他
如果还有api可以扩展，欢迎你的建议，或者一起扩展
