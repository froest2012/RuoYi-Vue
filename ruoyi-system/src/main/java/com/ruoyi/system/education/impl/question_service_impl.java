package com.ruoyi.system.education.impl;

import com.ruoyi.common.utils.CompletableFutureUtil;
import com.ruoyi.common.utils.dify.DifyUtil;
import com.ruoyi.system.domain.education.question.Question;
import com.ruoyi.system.domain.education.question.dify_input.question_dify_input;
import com.ruoyi.system.education.question_service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题库
 * @author 小旋风
 * @date 2025/6/26 15:11:00
 */
@Service
public class question_service_impl implements question_service {

    private static final Logger log = LoggerFactory.getLogger(DifyUtil.class);

    @Resource
    @Qualifier("difyExecutor")
    private ThreadPoolTaskExecutor difyExecutor;

    /**
     * 将图片中的每一个题目进行还原
     * @param image_url_question_list 题目图片地址列表
     * @return
     * @author 小旋风
     * @creed: Little Black Whirlwind
     * @date 2025/6/26 17:01
     */
    @Override
    public List<Question> get_question(List<String> image_url_question_list) {
        // 异步组装对象处理
        return CompletableFutureUtil.asyncProcess(image_url_question_list, image_url_question -> {
            try {
                question_dify_input question_dify_input = new question_dify_input();
                question_dify_input.setFiles(image_url_question);
                return question_dify_input.get_dify_value();
            } catch (Exception e) {
                throw e;
            }
        }, difyExecutor).stream().flatMap(Collection::stream).collect(Collectors.toList());
    }
}
