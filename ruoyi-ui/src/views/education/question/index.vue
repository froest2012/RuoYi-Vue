<template>
  <div class="app-container">
    <el-card>
      <div slot="header">题目解析</div>
      <el-form :model="form" ref="form" label-width="100px" style="max-width: 600px;">

        <el-form-item label="题目图片">
          <el-upload
            :action="uploadUrl"
            :data="{ folder: 'aaa' }"
            :on-success="handleImageUploadSuccess"
            :on-error="handleImageUploadError"
            :before-upload="handleImageUploadStart"
            :show-file-list="false"
          >
            <el-button type="primary">上传图片</el-button>
          </el-upload>
          <div v-if="form.image" style="margin-top: 10px;">
            <el-image :src="imageBaseUrl + form.image" style="max-width: 200px;" />
          </div>
        </el-form-item>
        <el-form-item>
          <el-button
            type="success"
            icon="el-icon-check"
            style="min-width: 120px;"
            @click="saveQuestion"
          >保存题目</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card style="margin-top: 30px;">
      <div slot="header">题目列表</div>
      <el-table :data="questionList" style="width: 100%">
        <el-table-column label="题目内容" min-width="200">
          <template slot-scope="scope">
            <div style="white-space: pre-wrap; word-break: break-all; max-width: 500px;">
              {{ scope.row.content }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="option" label="选项内容" />
        <el-table-column prop="difficulty" label="难度" />
        <el-table-column prop="answer" label="答案" />
        <el-table-column prop="type" label="题目类型" />
        <el-table-column prop="subject" label="学科" />
        <el-table-column prop="isQuestionImage" label="是否有图片">
          <template slot-scope="scope">
            <span v-if="scope.row.isQuestionImage == 1">有</span>
            <span v-else>无</span>
          </template>
        </el-table-column>
        <el-table-column prop="image" label="图片" width="120">
          <template slot-scope="scope">
            <el-image v-if="scope.row.image" :src="imageBaseUrl + scope.row.image" style="max-width: 80px;" />
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'
import {getToken} from "@/utils/auth";

export default {
  data() {
    return {
      form: {
        content: '',
        option: '',
        difficulty: 1,
        answer: '',
        type: '',
        subject: '',
        isQuestionImage: 2,
        image: ''
      },
      questionList: [],
      uploadUrl: 'http://test.91jzx.cn/jzx-server/eve/rest/file/operate/upload',
      imageBaseUrl: 'http://oss-image.91jzx.cn/aaa/', // 假设图片预览前缀
      uploadLoadingMessage: null
    }
  },
  methods: {
    handleImageUploadStart() {
      this.uploadLoadingMessage = this.$message({
        type: 'info',
        message: '正在上传图片，请稍候...',
        duration: 0
      });
    },
    handleImageUploadSuccess(response) {
      if (this.uploadLoadingMessage) {
        this.uploadLoadingMessage.close();
        this.uploadLoadingMessage = null;
      }
      if (response.code === '00000' && response.data && response.data[0]) {
        this.form.image = response.data[0].fileName
        this.form.isQuestionImage = 1
        this.$message.success('图片上传成功')
      } else {
        this.$message.error('图片上传失败')
      }
    },
    handleImageUploadError() {
      if (this.uploadLoadingMessage) {
        this.uploadLoadingMessage.close();
        this.uploadLoadingMessage = null;
      }
      this.$message.error('图片上传失败')
    },
    saveQuestion() {
      const imageUrl = this.form.image ? this.imageBaseUrl + this.form.image : '';
      const loadingMessage = this.$message({
        type: 'info',
        message: '正在保存题目，请稍候...',
        duration: 0
      });
      axios.post(
        '/dev-api/question/question_restore',
        {
          image_url_question_list: imageUrl ? [imageUrl] : []
        },
        {
          headers: { Authorization: 'Bearer ' + getToken() }
        }
      ).then(res => {
        if (res.data.code === 200) {
          this.$message.success('题目保存成功')
          if (Array.isArray(res.data.data)) {
            // 用id去重，假设题目有id字段
            this.questionList = res.data.data;
          }
          // 不清空表单内容
        } else {
          this.$message.error(res.data.msg || '题目保存失败')
        }
      }).catch(error => {
        this.$message.error('保存题目失败：' + error)
      }).finally(() => {
        loadingMessage.close();
      });
    }
  }
}
</script>
