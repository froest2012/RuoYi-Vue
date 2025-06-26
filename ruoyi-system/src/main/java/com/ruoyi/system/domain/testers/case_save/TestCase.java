package com.ruoyi.system.domain.testers.case_save;

import lombok.Data;

@Data
public class TestCase {
    /**
     * 测试用例所属的模块名称
     */
    private String module;
    /**
     * 测试用例对应的功能点描述
     */
    private String functionDesc;
    /**
     * 测试用例关联的需求点编号或描述
     */
    private String demandPoint;
    /**
     * 测试用例涉及的核心概念或技术点
     */
    private String coreConcepts;
    /**
     * 测试用例的唯一标识编号
     */
    private String caseNumber;
    /**
     * 测试用例的标题描述
     */
    private String title;
    /**
     * 执行测试用例需要满足的前置条件
     */
    private String precondition;
    /**
     * 测试用例的具体执行步骤
     */
    private String testSteps;
    /**
     * 测试用例执行过程中使用的测试数据
     */
    private String testData;
    /**
     * 测试用例执行后预期产生的结果
     */
    private String expectedResult;
    /**
     * 测试用例实际执行后产生的结果
     */
    private String actualResult;
    /**
     * 测试用例的优先级，如：High/Medium/Low
     */
    private String priority;
    /**
     * 测试用例的类型，如：Functional/Performance/UI
     */
    private String caseType;
    /**
     * 测试用例的标签集合，用于分类和筛选
     */
    private String tags;

}