<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header"><span><i class="el-icon-monitor"></i> PRD 上传</span></div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <tbody>
                <tr>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">
                      <el-col :span="1.5">
                        <el-button
                          type="info"
                          plain
                          icon="el-icon-upload2"
                          size="mini"
                          @click="handleImport"
                          v-hasPermi="['testers:case:import']"
                        >PRD 导入</el-button>
                      </el-col>
                    </div>
                  </td>
                </tr>
                <!-- 每条导入结果一行，并带有PRD预览按钮 -->
                <tr v-for="(result, idx) in importResults" :key="idx">
                  <td class="el-table__cell is-leaf">
                    <div class="cell" style="display: flex; align-items: center;">
                      <el-button type="primary" plain size="mini" style="margin-right: 10px;">PRD 预览</el-button>
                      <div v-html="result" style="flex: 1;"></div>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls, .docx, .pdf"
        :headers="upload.headers"
        :action="upload.url"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :on-change="handleFileChange"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许导入.xlsx, .xls, .docx, .pdf格式文件。</span>
        </div>
      </el-upload>
      <el-button
        v-if="previewFile"
        type="primary"
        size="mini"
        style="margin-top: 10px"
        @click="handlePreview"
      >文件预览</el-button>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 文件预览弹窗 -->
    <el-dialog title="文件预览" :visible.sync="previewVisible" width="80%" append-to-body>
      <div v-if="previewUrl" style="text-align: center;">
        <iframe :src="previewUrl" style="width: 100%; height: 500px; border: none;"></iframe>
      </div>
      <div v-else style="text-align: center; padding: 20px;">
        <p>暂无预览内容</p>
      </div>
    </el-dialog>

    <!-- 功能矩阵表格 -->
    <el-card style="margin-top: 20px;">
      <div slot="header">
        <span><i class="el-icon-menu"></i> 功能矩阵</span>
        <el-button
          type="primary"
          size="mini"
          style="float: right;"
          @click="generateFunctionMatrix"
        >生成功能矩阵</el-button>
      </div>
      <el-table :data="functionMatrix" border style="width: 100%">
        <el-table-column type="index" label="序号" width="50"></el-table-column>
        <el-table-column prop="module" label="模块"></el-table-column>
        <el-table-column prop="functionDesc" label="功能"></el-table-column>
        <el-table-column prop="demandPoint" label="需求点"></el-table-column>
        <el-table-column prop="coreConcepts" label="核心概念字段"></el-table-column>
      </el-table>
    </el-card>

    <!-- 测试用例卡片 -->
    <el-card style="margin-top: 20px;">
      <div slot="header">
        <span><i class="el-icon-document"></i> 测试用例</span>
        <el-button
          type="primary"
          size="mini"
          style="float: right;"
          @click="generateTestCases"
        >生成测试用例</el-button>
      </div>
      <el-table :data="testCases" border style="width: 100%">
        <el-table-column type="index" label="序号" width="50"></el-table-column>
        <el-table-column prop="caseNumber" label="用例编号" width="100"></el-table-column>
        <el-table-column prop="title" label="用例标题" width="220"></el-table-column>
        <el-table-column prop="module" label="模块" width="120"></el-table-column>
        <el-table-column prop="functionDesc" label="功能" width="180"></el-table-column>
        <el-table-column prop="demandPoint" label="需求点" width="180"></el-table-column>
        <el-table-column prop="coreConcepts" label="核心概念字段" width="180"></el-table-column>
        <el-table-column prop="precondition" label="前置条件" width="180"></el-table-column>
        <el-table-column prop="testSteps" label="测试步骤" width="220"></el-table-column>
        <el-table-column prop="testData" label="测试数据" width="180"></el-table-column>
        <el-table-column prop="expectedResult" label="预期结果" width="220"></el-table-column>
        <el-table-column prop="priority" label="优先级" width="80"></el-table-column>
        <el-table-column prop="caseType" label="用例类型" width="100"></el-table-column>
        <el-table-column prop="tags" label="标签" width="100"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { getToken } from "@/utils/auth"
import request from '@/utils/request'

export default {
  data() {
    return {
      importResults: [],
      upload: {
        open: false,
        title: "",
        isUploading: false,
        headers: { Authorization: "Bearer " + getToken() },
        url: process.env.VUE_APP_BASE_API + "/testers/case/save/import-file"
      },
      previewVisible: false,
      previewUrl: "",
      previewFile: null,
      functionMatrix: [],
      testCases: []
    }
  },
  methods: {
    handleImport() {
      this.upload.title = "导入"
      this.upload.open = true
    },
    submitFileForm() {
      this.$refs.upload.submit()
    },
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true
    },
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false
      this.upload.isUploading = false
      this.$refs.upload.clearFiles()
      this.importResults.push(response.msg)
      this.$alert(
        "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
          response.msg +
          "</div>",
        "导入结果",
        { dangerouslyUseHTMLString: true }
      )
    },
    handleFileChange(file, fileList) {
      if (file && file.raw) {
        this.previewFile = file.raw
      } else {
        this.previewFile = null
      }
    },
    handlePreview() {
      if (this.previewFile) {
        this.previewUrl = URL.createObjectURL(this.previewFile)
        this.previewVisible = true
      }
    },
    generateFunctionMatrix() {
      const file_name_list = this.importResults;
      const loadingMessage = this.$message({
        type: 'info',
        message: '正在生成功能矩阵，请稍候...',
        duration: 0,
        onClose: () => {
          console.log('loading 提示已手动关闭');
        }
      });
      request({
        url: '/testers/case/save/query-function-matrix',
        method: 'post',
        data: { file_name_list }
      }).then(response => {
        if (response.code === 200) {
          this.functionMatrix = response.data;
          this.$message.success('功能矩阵生成成功');
        } else {
          this.$message.error(response.msg || '生成失败');
        }
      }).catch(error => {
        this.$message.error('生成功能矩阵失败：' + error);
      }).finally(() => {
        loadingMessage.close();
      });
    },
    generateTestCases() {
      // 从功能矩阵数据中提取所有 caseSaveCode，去重
      const case_code_set = new Set();
      this.functionMatrix.forEach(item => {
        if (item.caseSaveCode) {
          case_code_set.add(item.caseSaveCode);
        }
      });
      const case_code_list = Array.from(case_code_set);

      if (case_code_list.length === 0) {
        this.$message.warning('没有可用的用例编码，无法生成测试用例');
        return;
      }

      const loadingMessage = this.$message({
        type: 'info',
        message: '正在生成测试用例，请稍候...',
        duration: 0,
        onClose: () => {
          console.log('loading 提示已手动关闭');
        }
      });
      request({
        url: '/testers/case/save/save-case',
        method: 'post',
        data: { case_code_list }
      }).then(response => {
        if (response.code === 200) {
          this.testCases = response.data;
          this.$message.success('测试用例生成成功');
        } else {
          this.$message.error(response.msg || '生成失败');
        }
      }).catch(error => {
        this.$message.error('生成测试用例失败：' + error);
      }).finally(() => {
        loadingMessage.close();
      });
    }
  }
}
</script>