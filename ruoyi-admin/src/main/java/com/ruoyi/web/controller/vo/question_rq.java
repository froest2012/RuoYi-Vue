package com.ruoyi.web.controller.vo;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * question save 接口参数
 *
 * @author 小旋风
 * @date 2025/6/18 21:32:26
 */
@Data
public class question_rq implements Serializable {

    private List<String> image_url_question_list;

    public List<String> getImage_url_question_list() {
        if (CollectionUtils.isEmpty(image_url_question_list)) {
            throw new RuntimeException("题目图片地址列表 is not null");
        }
        return new ArrayList<>(image_url_question_list); // 返回不可变副本，保护原始数据
    }

}
