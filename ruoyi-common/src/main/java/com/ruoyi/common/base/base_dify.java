package com.ruoyi.common.base;

import com.alibaba.fastjson2.JSONArray;
import com.ruoyi.common.utils.dify.DifyUtil;
import lombok.Data;
import org.apache.xmlbeans.impl.xb.ltgfmt.TestCase;

import java.util.List;

/**
 * 类功能描述
 *
 * @author 小旋风
 * @date 2025/6/26 15:43:42
 */
@Data
public class base_dify {

    public String dify_api_key;

    public String dify_api_url;

    public String query;

    public String request_body;

    public String files;

    public String dify_value(){
        // 调用【功能矩阵llm】拿到 大模型解析后的功能矩阵
        return DifyUtil.get_value(
                this.getDify_api_url(),
                this.getDify_api_key(),
                this.getRequest_body(),
                this.getQuery(),
                this.getFiles()
        );
    }
}
