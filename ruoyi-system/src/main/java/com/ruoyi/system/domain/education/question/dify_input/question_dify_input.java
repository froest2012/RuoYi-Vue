package com.ruoyi.system.domain.education.question.dify_input;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.base.base_dify;
import com.ruoyi.system.domain.education.question.Question;

import java.util.List;

/**
 * question_save_dify
 * @author 小旋风
 * @date 2025/6/26 15:29:21
 */
public class question_dify_input extends base_dify {

    public static void main(String[] args) {
        question_dify_input questionDifyInput = new question_dify_input();
        questionDifyInput.setFiles("http://zstt-dual-mentor.oss-cn-hangzhou.aliyuncs.com/correction_inhouse_pre-enhanced_img_c0c5e5ff.png");
        List<Question> difyValue = questionDifyInput.get_dify_value();
        System.out.println("aaaaaaaaaaaa"+JSONObject.toJSONString(difyValue));
    }


    public List<Question> get_dify_value(){
        // 调用【功能矩阵llm】拿到 大模型解析后的功能矩阵
        String value = dify_value();
        return JSONArray.parseArray(value, Question.class);
    }

    // dify_api_key 的 getter 和 setter
    public String getDify_api_key() {
        this.dify_api_key = "app-iZrYd9CjuoboktPSjILdlONY";
        return dify_api_key;
    }

    // dify_api_url 的 getter 和 setter
    public String getDify_api_url() {
        this.dify_api_url = "https://dify-test.91jzx.cn/v1/chat-messages";
        return dify_api_url;
    }

    // query 的 getter 和 setter
    public String getQuery() {
        this.query = "开始吧";
        return query;
    }

    public void setRequest_body(String request_body) {
        this.request_body = "{\n" +
                "        \"query\": \""+request_body+"\"}";
    }

    public void setFiles(String image_url) {
        this.files = "[\n" +
                "        {\n" +
                "            \"type\": \"image\",\n" +
                "            \"transfer_method\": \"remote_url\",\n" +
                "            \"url\": \""+image_url+"\",\n" +
                "            \"upload_file_id\": \"\"\n" +
                "        }\n" +
                "    ]";
    }
}
