package com.ruoyi.system.testers.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.CompletableFutureUtil;
import com.ruoyi.common.utils.dify.DifyUtil;
import com.ruoyi.common.utils.files.FileUtils;
import com.ruoyi.system.domain.testers.case_save.TestCase;
import com.ruoyi.system.domain.testers.case_save.dify_input.Test_case_save_dify_input;
import com.ruoyi.system.domain.testers.case_save.dify_input.Function_matrix_dify_input;
import com.ruoyi.system.domain.testers.case_save.FunctionMatrix;
import com.ruoyi.system.mapper.FunctionMatrixMapper;
import com.ruoyi.system.testers.ICaseSaveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类功能描述
 *
 * @author 小旋风
 * @date 2025/6/18 01:08:03
 */
@Service
public class CaseSaveServiceImpl extends ServiceImpl<FunctionMatrixMapper, FunctionMatrix> implements ICaseSaveService  {
    // 定义支持的文件类型
    private final static String[] allowedExtensions = {".xlsx", ".xls", ".docx", ".pdf"};

    private static final Logger log = LoggerFactory.getLogger(DifyUtil.class);

    @Resource
    @Qualifier("difyExecutor")
    private ThreadPoolTaskExecutor difyExecutor;

    @Override
    public String importFile(MultipartFile file){
        // file :是一个【.xlsx, .xls, .docx, .pdf】的 文件，将文件存储至项目根目录下，并返回文件名
        if (file.isEmpty()) {
            throw new RuntimeException("文件为空，上传失败");
        }

        // 文件类型校验
        FileUtils.file_type_check(Objects.requireNonNull(file.getOriginalFilename()), allowedExtensions);
        // 文件上传到本地工程的根目录
        return FileUtils.file_upload_dir(file);
    }

    /**
     * 解析 prd 文件中的文本，调用【功能矩阵llm】拿到 大模型解析后的功能矩阵
     * @param prd_file_name
     * @return
     * @author 小旋风
     * @creed: Little Black Whirlwind
     * @date 2025/6/18 19:51
     */
    @Override
    public List<FunctionMatrix> get_function_matrix(List<String> prd_file_name) throws IOException {
        List<FunctionMatrix> function_matrix_list = new ArrayList<>();
        for (String file_name : prd_file_name) {
            // 根据文件名称在根目录中获取 prd 文件，取到文件中的文本内容
            String file_value = FileUtils.read_file_content(file_name);
            // 初始化 case save dify config
            Function_matrix_dify_input functionmatrix_dify_input = new Function_matrix_dify_input();
            functionmatrix_dify_input.setRequest_body(file_value);
            //get dify
            function_matrix_list.addAll(functionmatrix_dify_input.get_dify_value(file_name));
        }
        saveBatch(FunctionMatrix.init(function_matrix_list));
        return function_matrix_list;
    }

    /**
     * 根据功能矩阵，生成测试用例
     * @param case_code_list 测试用例生成批次编码
     * @return
     * @author 小旋风
     * @creed: Little Black Whirlwind
     * @date 2025/6/20 04:00
     */
    @Override
    public List<TestCase> save_case(List<String> case_code_list) throws IOException {
        List<FunctionMatrix> caseCodeList = get_case_code_list(case_code_list);
        log.error("生成对应的功能矩阵-case_code_list:{}", JSONObject.toJSONString(case_code_list));
        if (CollectionUtils.isEmpty(caseCodeList)){
            throw new RuntimeException("没有找到对应的功能矩阵");
        }
        caseCodeList = FunctionMatrix.get_prd_value(caseCodeList);
        // 异步组装对象处理
        List<TestCase> testCaseList = CompletableFutureUtil.asyncProcess(caseCodeList, functionMatrix -> {
            try {
                Test_case_save_dify_input testcase_save_dify_input = new Test_case_save_dify_input();
                testcase_save_dify_input.setRequest_body(functionMatrix);
                return testcase_save_dify_input.get_dify_value();
            } catch (Exception e) {
                throw e;
            }
        }, difyExecutor).stream().flatMap(Collection::stream).collect(Collectors.toList());

        return testCaseList;
    }





    private List<FunctionMatrix> get_case_code_list(List<String> case_code_list) {
        QueryWrapper<FunctionMatrix> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("case_save_code", case_code_list);
        return list(queryWrapper);
    }


}
