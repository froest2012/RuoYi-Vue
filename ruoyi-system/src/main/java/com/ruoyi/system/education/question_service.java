package com.ruoyi.system.education;

import com.ruoyi.system.domain.education.question.Question;

import java.util.List;

/**
 * 题库
 * @author 小旋风
 * @date 2025/6/26 15:11:00
 */
public interface question_service {
    List<Question> get_question(List<String> image_url_question_list);
}
