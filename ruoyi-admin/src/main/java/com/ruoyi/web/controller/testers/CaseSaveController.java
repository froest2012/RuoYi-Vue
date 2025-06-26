package com.ruoyi.web.controller.testers;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.testers.case_save.FunctionMatrix;
import com.ruoyi.system.domain.testers.case_save.TestCase;
import com.ruoyi.system.testers.ICaseSaveService;
import com.ruoyi.web.controller.vo.Function_matrix_rq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类功能描述
 *
 * @author 小旋风
 * @date 2025/6/18 00:57:37
 */
@RestController
@RequestMapping("/testers/case/save")
public class CaseSaveController extends BaseController {


    @Autowired
    private ICaseSaveService caseSaveService;

    @PostMapping("/import-file")
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        String file_name = caseSaveService.importFile(file);
        return success(file_name);
    }

    @PostMapping("/query-function-matrix")
    public AjaxResult query_function_matrix(@RequestBody Function_matrix_rq function_matrix_rq) throws Exception
    {
        List<FunctionMatrix> functionMatrix = caseSaveService.get_function_matrix(function_matrix_rq.getFile_name_list());
        return success(functionMatrix);
    }
    @PostMapping("/save-case")
    public AjaxResult save_case(@RequestBody Function_matrix_rq function_matrix_rq) throws Exception
    {
        List<TestCase> test_case_list = caseSaveService.save_case(function_matrix_rq.getCase_code_list());
        return success(test_case_list);
    }

}
