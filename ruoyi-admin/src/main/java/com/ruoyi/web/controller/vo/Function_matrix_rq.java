package com.ruoyi.web.controller.vo;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * case save 接口参数
 *
 * @author 小旋风
 * @date 2025/6/18 21:32:26
 */
@Data
public class Function_matrix_rq implements Serializable {

    private List<String> file_name_list;

    private List<String> case_code_list;


    // case_code_list 的 getter
    public List<String> getCase_code_list() {
        if (CollectionUtils.isEmpty(case_code_list)) {
            throw new RuntimeException("用例编号列表不能为空");
        }
        return new ArrayList<>(case_code_list); // 返回不可变副本，保护原始数据
    }

    // file_name_list 的 getter
    public List<String> getFile_name_list() {
        if (CollectionUtils.isEmpty(file_name_list)) {
            throw new RuntimeException("图片名称列表不能为空");
        }
        return new ArrayList<>(file_name_list); // 返回不可变副本，保护原始数据
    }


    // file_name_list 的 setter
    public void setFile_name_list(List<String> file_name_list) {
        this.file_name_list = file_name_list != null ?
                new ArrayList<>(file_name_list) : // 防御性拷贝
                new ArrayList<>();
    }
}
