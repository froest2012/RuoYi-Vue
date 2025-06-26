package com.ruoyi.system.domain.testers.case_save;

import com.ruoyi.system.domain.testers.case_save.dify_input.Function_matrix_dify_input;
import lombok.Data;

import java.util.List;

/**
 * 用例生成
 * @author 小旋风
 * @date 2025/6/18 19:33:31
 */
@Data
public class CaseSave {
    private String prd_value;

    private Function_matrix_dify_input functionmatrix_dify_input;

    private List<FunctionMatrix> functionMatrixList;

    private List<TestCase> testCaseList;

    // prd_url 的 getter 和 setter
    public String getPrd_value() {
        return prd_value;
    }

    public void setPrd_value(String prd_value) {
        this.prd_value = prd_value;
    }

    // functionmatrix_dify_input 的 getter 和 setter
    public Function_matrix_dify_input getCase_save_dify_input() {
        return functionmatrix_dify_input;
    }

    public void setCase_save_dify_input(Function_matrix_dify_input functionmatrix_dify_input) {
        this.functionmatrix_dify_input = functionmatrix_dify_input;
    }

    // functionMatrixList 的 getter 和 setter
    public List<FunctionMatrix> getFunctionMatrixList() {
        return functionMatrixList;
    }

    public void setFunctionMatrixList(List<FunctionMatrix> functionMatrixList) {
        this.functionMatrixList = functionMatrixList;
    }
}
