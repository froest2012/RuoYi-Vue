package com.ruoyi.system.domain.education.question;

import com.ruoyi.common.base.BaseModel;
import lombok.Data;

/**
 * 题目
 * @author 小旋风
 * @date 2025/6/26 15:15:52
 */
@Data
public class Question extends BaseModel {

    /**
     * 题目内容
     */
    private String content;

    /**
     * 选项内容
     */
    private String option;

    /**
     * 难度(1-9 的区间值。1 代表最简单 - 9 代表最难)
     */
    private int difficulty;

    /**
     * 答案(简洁的纯答案形式)
     */
    private String answer;

    /**
     * 题目类型(选择题、简答题、判断题、填空题)
     */
    private String type;

    /**
     * 学科（语文、英语、数学、生物、地理、化学、政治）
     */
    private String subject;

    /**
     * 题目中是否包含图片(1 题目中包含图片 ,2 题目中不包含图片)
     */
    private int isQuestionImage;

}
