package com.ruoyi.system.domain.testers.case_save.dify_input;

import com.alibaba.fastjson2.JSONArray;
import com.ruoyi.common.base.base_dify;
import com.ruoyi.common.utils.dify.DifyUtil;
import com.ruoyi.system.domain.testers.case_save.FunctionMatrix;
import com.ruoyi.system.domain.testers.case_save.TestCase;

import java.util.List;

/**
 * case save dify input
 * @author 小旋风
 * @creed: Little Black Whirlwind
 * @date 2025/6/18 21:14
 */
public class Test_case_save_dify_input extends base_dify {

        public List<TestCase> get_dify_value(){
            // 调用【功能矩阵llm】拿到 大模型解析后的功能矩阵
            String value = dify_value();
            return JSONArray.parseArray(value, TestCase.class);
        }

        // dify_api_key 的 getter 和 setter
        public String getDify_api_key() {
            this.dify_api_key = "app-zXTeqIG4P7SOi1hDqjbS4JIA";
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

        public void setRequest_body(FunctionMatrix functionMatrix) {
            this.request_body = "{\n" +
                    "        \"functionDesc\": \""+functionMatrix.getFunctionDesc()+"\",\n" +
                    "        \"demandPoint\": \""+functionMatrix.getDemandPoint()+"\",\n" +
                    "        \"coreConcepts\": \""+functionMatrix.getCoreConcepts()+"\",\n" +
                    "        \"prd\": \""+functionMatrix.getPrd_value()+"\",\n" +
                    "        \"module\": \""+functionMatrix.getModule()+"\"\n" +
                    "    }";
        }
}