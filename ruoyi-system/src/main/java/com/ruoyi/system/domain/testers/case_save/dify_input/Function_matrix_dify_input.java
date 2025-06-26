package com.ruoyi.system.domain.testers.case_save.dify_input;

import com.alibaba.fastjson2.JSONArray;
import com.ruoyi.common.base.base_dify;
import com.ruoyi.common.utils.dify.DifyUtil;
import com.ruoyi.system.domain.testers.case_save.FunctionMatrix;
import lombok.Data;

import java.util.List;

/**
 * case save dify input
 * @author 小旋风
 * @creed: Little Black Whirlwind
 * @date 2025/6/18 21:14
 */
@Data
public class Function_matrix_dify_input extends base_dify {

        public List<FunctionMatrix> get_dify_value(String file_name){
            // 调用【功能矩阵llm】拿到 大模型解析后的功能矩阵
            String value = dify_value();
            List<FunctionMatrix> function_matrix_value = JSONArray.parseArray(value, FunctionMatrix.class);
            for (FunctionMatrix functionMatrix : function_matrix_value) {
                functionMatrix.setPrd(file_name);
            }
            return function_matrix_value;
        }

        // dify_api_key 的 getter 和 setter
        public String getDify_api_key() {
            this.dify_api_key = "app-aPHBPG5VzJJlKZ2OTp2VEhPT";
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
    }