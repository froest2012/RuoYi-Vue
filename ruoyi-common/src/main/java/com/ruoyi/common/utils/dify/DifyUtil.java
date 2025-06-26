package com.ruoyi.common.utils.dify;

import cn.hutool.core.text.UnicodeUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.http.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.formula.functions.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * 模型调用工具类
 *
 * @author 小旋风
 * @date 2025/6/18 16:05:52
 */
public class DifyUtil {

    private static final Logger log = LoggerFactory.getLogger(DifyUtil.class);
    static String requestBody1 = "";
    static String requestBody = "{\n" +
            "    \"inputs\": {\n" +
            "        \"query\": \""+requestBody1+"" +
            "    },\n" +
            "    \"query\": \""+requestBody1+"\",\n" +
            "    \"response_mode\": \"streaming\",\n" +
            "    \"conversation_id\": \"\",\n" +
            "    \"user\": \"abc-123\"\n" +
            "}";

    public static void main(String[] args) {
//        completion("https://dify-test.91jzx.cn/v1/chat-messages","app-aPHBPG5VzJJlKZ2OTp2VEhPT",requestBody);
    }

    /**
     * blocking模式下的文本生成
     *
     * @return 文本生成结果
     */
    public static String get_value(String url, String apiKey, String requestBody, String query , String files) {
        // http query dify value
        String responseBody = get_dify_value(url, apiKey, requestBody,query,files);

        // 解析dify value
        String analysis_dify_value = analysis_dify_value(responseBody);

        try {
            log.info("dify value serializable------------------{}", JSONObject.toJSONString(analysis_dify_value));
            return analysis_dify_value;
        } catch (Exception e) {
            log.error("dify value serializable error 响应解析失败 requestBody: {}; responseBody: {} ", requestBody, responseBody, e);
            throw new RuntimeException("dify value serializable error 响应解析失败", e);
        }
    }

    /**
     * 解析 dify return value
     * @param responseBody dify 返回的内容
     * @return
     * @author 小旋风
     * @creed: Little Black Whirlwind
     * @date 2025/6/18 19:19
     */
    private static String analysis_dify_value(String responseBody){
        try {
            // 清理响应字符串，确保其格式正确
            responseBody = responseBody.replace("event: ping", "");
            responseBody = responseBody.trim(); // 去除首尾空白字符
            String[] split_responseBody_arr = responseBody.split("data: ");
            String result_str = "";
            for (String split_responseBody : split_responseBody_arr) {
                try {
                    if (!StringUtils.contains(split_responseBody, "\"answer\":")){
                        continue;
                    }
                    JSONObject json = JSONObject.parseObject(split_responseBody);
                    if (Objects.nonNull(json) && json.containsKey("answer")){
                        String jsonString = String.valueOf(json.get("answer"));
                        result_str += jsonString;
                    }
                }catch (Exception e){
                    log.error("dify chat completion 响应解析失败 requestBody: {}; responseBody: {} ", requestBody, split_responseBody, e);
                }
            }
             result_str = (StringUtils.contains(result_str , "```json")) ? result_str.replace("```json", "") : result_str;
             result_str = (StringUtils.contains(result_str , "```")) ? result_str.replace("```", "") : result_str;
            // 将单个反斜杠替换为双反斜杠
            return result_str.replaceAll("\\\\", "\\\\\\\\");
        } catch (Exception e) {
            log.error("dify chat completion 响应解析失败 requestBody: {}; responseBody: {} ", requestBody, responseBody, e);
            throw new RuntimeException("dify chat completion 响应解析失败", e);
        }
    }

    /**
     * http query dify value
     * @param url dify url
     * @param apiKey dify 密钥
     * @param requestBody dify 请求体
     * @return
     * @author 小旋风
     * @creed: Little Black Whirlwind
     * @date 2025/6/18 19:18
     */
    private static String get_dify_value(String url, String apiKey, String requestBody,String query,String files) {
        requestBody = "{\n" +
                "    \"inputs\": "+requestBody+",\n" +
                "    \"files\": "+files+"," +
                "    \"query\": \""+query+"\",\n" +
                "    \"response_mode\": \"streaming\",\n" +
                "    \"conversation_id\": \"\",\n" +
                "    \"user\": \"abc-123\"\n" +
                "}";
        String responseBody = "";
        Long startTimeStamp = System.currentTimeMillis();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            log.info("start invoke dify chat completion url:{}; apiKey:{}; requestBody:{};", url, apiKey, requestBody);
            ResponseEntity<String> response = new RestTemplate().postForEntity(url, entity, String.class);
            log.info("end invoke dify chat completion url:{}; apiKey:{}; requestBody:{}; responseBody:{}; cost:{}ms;",
                    url, apiKey, requestBody, response.getBody(), System.currentTimeMillis() - startTimeStamp);

            if (!response.getStatusCode().isError() && Objects.nonNull(response.getBody())) {
                responseBody = response.getBody();
                responseBody = UnicodeUtil.toString(responseBody);
            }
        } catch (HttpStatusCodeException he) {
            long endTime = System.currentTimeMillis();
            responseBody = he.getResponseBodyAsString();
            int responseCode = he.getStatusCode().value();
            log.info("end invoke dify chat completion with http error url:{}; apiKey:{}; requestBody:{}; cost:{}ms; responseCode:{}; responseBody:{}",
                    url, apiKey, requestBody, endTime - startTimeStamp, responseCode, responseBody);
            throw new RuntimeException("dify chat completion invoke error", he);
        } catch (Exception e) {
            log.info("end invoke dify chat completion with error url:{}; apiKey:{}; requestBody:{}; cost:{}ms; ",
                    url, apiKey, requestBody, System.currentTimeMillis() - startTimeStamp);
            throw new RuntimeException("dify chat completion invoke error", e);
        }
        return responseBody;
    }
}
