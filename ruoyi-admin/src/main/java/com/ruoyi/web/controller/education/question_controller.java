package com.ruoyi.web.controller.education;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.education.question.Question;
import com.ruoyi.system.education.question_service;
import com.ruoyi.web.controller.vo.question_rq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 题库
 * @author 小旋风
 * @date 2025/6/26 14:15:03
 */
@RestController
@RequestMapping("/question")
public class question_controller extends BaseController {

    @Autowired
    private question_service questionService;

    @PostMapping("/question_restore")
    public AjaxResult questionRestore(@RequestBody question_rq questionRq)
    {
        List<Question> question = questionService.get_question(questionRq.getImage_url_question_list());
        return success(question);
    }
}
