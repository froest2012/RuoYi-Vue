package com.ruoyi.system.domain.testers.case_save;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.base.BaseModel;
import com.ruoyi.common.utils.files.FileUtils;
import com.ruoyi.common.utils.uuid.UUID;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 功能矩阵
 *
 * @author 小旋风
 * @date 2025/6/18 19:36:38
 */
@TableName("ry_function_matrix")
@Data
public class FunctionMatrix extends BaseModel {


    @TableId(value = "fm_id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 系统模块名称
     * 描述功能所属的系统模块或子系统
     */
    @TableField("module")
    private String module;

    /**
     * 功能描述
     * 说明该模块提供的具体功能或服务
     */
    @TableField("function_desc")
    private String functionDesc;

    /**
     * 需求点说明
     * 记录该功能对应的业务需求或设计目标
     */
    @TableField("demand_point")
    private String demandPoint;

    /**
     * 核心概念解释
     * 定义功能实现涉及的关键业务概念或技术概念
     */
    @TableField("core_concepts")
    private String coreConcepts;

    /**
     * 功能矩阵编码
     * 保存该功能对应的用例代码
     */
    @TableField("case_save_code")
    private String caseSaveCode;

    @TableField("prd")
    private String prd;

    @TableField(exist = false)
    private String prd_value;

    public static List<FunctionMatrix> get_prd_value(List<FunctionMatrix> caseCodeList) throws IOException {
        Set<String> prd_set = caseCodeList.stream().map(FunctionMatrix::getPrd).collect(Collectors.toSet());
        for (String prd : prd_set) {
            // 根据文件名称在根目录中获取 prd 文件，取到文件中的文本内容
            String file_value = FileUtils.read_file_content(prd);
            caseCodeList = caseCodeList.stream()
                    .filter(functionMatrix -> Objects.equals(functionMatrix.getPrd(),prd))
                    .map(functionMatrix -> {
                        functionMatrix.setPrd_value(file_value);
                        return functionMatrix;
                    }).collect(Collectors.toList());
        }
        return caseCodeList;
    }

    public static List<FunctionMatrix> init(List<FunctionMatrix> functionMatrixList){
        if (CollectionUtils.isEmpty(functionMatrixList)){
            return null;
        }
        String code = UUID.randomUUID().toString();
        for (FunctionMatrix functionMatrix : functionMatrixList) {
            functionMatrix.setCaseSaveCode(code);
        }
        return functionMatrixList;
    }

    // Getter方法 - 用于访问对象属性
    public String getModule() {
        return module;
    }

    // Setter方法 - 用于修改对象属性
    public void setModule(String module) {
        this.module = module;
    }

}
