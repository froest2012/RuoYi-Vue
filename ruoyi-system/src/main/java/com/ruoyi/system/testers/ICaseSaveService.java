package com.ruoyi.system.testers;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.testers.case_save.FunctionMatrix;
import com.ruoyi.system.domain.testers.case_save.TestCase;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 用例生成
 * 
 * @author ruoyi
 */
public interface ICaseSaveService extends IService<FunctionMatrix>
{

    /**
     * 上传文件并存储至项目根目录下
     * @param file
     * @return
     */
    String importFile(MultipartFile file);

    /**
     * 解析 prd 文件中的文本，调用【功能矩阵llm】拿到 大模型解析后的功能矩阵
     * @param prd_file_name
     * @return
     * @author 小旋风
     * @creed: Little Black Whirlwind
     * @date 2025/6/18 19:51
     */
    List<FunctionMatrix> get_function_matrix(List<String> prd_file_name) throws IOException;

    /**
     * 根据功能矩阵，生成测试用例
     * @param case_code_list 测试用例生成批次编码
     * @return
     * @author 小旋风
     * @creed: Little Black Whirlwind
     * @date 2025/6/20 04:00
     */
    List<TestCase> save_case(List<String> case_code_list) throws IOException;
}
