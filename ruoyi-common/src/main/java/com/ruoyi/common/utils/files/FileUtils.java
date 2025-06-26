package com.ruoyi.common.utils.files;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * 文件操作具类
 *
 * @author 小旋风
 * @date 2025/6/18 15:50:58
 */
public class FileUtils {

    public static void main(String[] args) throws IOException {
        System.out.println(FileUtils.read_file_content("2023050367+闫婷婷.pdf"));
    }

    /**
     * 根据文件名称在根目录中获取 prd 文件，并读取内容
     * @param fileName 文件名
     * @return 文件文本内容
     */
    public static String read_file_content(String fileName) throws IOException {
        String content = "";
        if (fileName.toLowerCase().endsWith(".pdf")) {
            content = readPdfContent(fileName);
        } else if (fileName.toLowerCase().endsWith(".docx")) {
            content = readDocxContent(fileName);
        }else {
            throw new RuntimeException("文件类型不支持");
        }
        return content.replaceAll("\\r?\\n", " ");
    }

    /**
     * 读取 .docx 文件内容并返回文本
     * @param fileName 文件名（DOCX）
     * @return DOCX 中提取的文本内容
     */
    public static String readDocxContent(String fileName) throws IOException {
        Path filePath = Paths.get(System.getProperty("user.dir"), fileName);
        if (!Files.exists(filePath)) {
            throw new RuntimeException("文件不存在: " + fileName);
        }

        try (FileInputStream fis = new FileInputStream(filePath.toFile());
             XWPFDocument document = new XWPFDocument(fis)) {

            // 提取文档正文文本
            StringBuilder text = new StringBuilder();
            document.getParagraphs().forEach(p -> text.append(p.getText()).append("\n"));

            return text.toString();
        }
    }

    /**
     * 读取 PDF 文件内容并返回文本
     * @param fileName 文件名（PDF）
     * @return PDF 中提取的文本内容
     */
    public static String readPdfContent(String fileName) throws IOException {
        Path filePath = Paths.get(System.getProperty("user.dir"), fileName);
        if (!Files.exists(filePath)) {
            throw new RuntimeException("文件不存在: " + fileName);
        }

        try (PDDocument document = PDDocument.load(filePath.toFile())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }

    /**
     * 文件类型校验
     * @param originalFilename 文件名
     * @param allowedExtensions 允许的文件类型
     * @author 小旋风
     * @creed: Little Black Whirlwind
     * @date 2025/6/18 15:58
     */
    public static void file_type_check(String originalFilename ,String[] allowedExtensions) {
        boolean isValidType = false;
        for (String ext : allowedExtensions) {
            if (originalFilename.toLowerCase().endsWith(ext)) {
                isValidType = true;
                break;
            }
        }
        if (!isValidType) {
            throw new RuntimeException("文件类型不支持");
        }
    }

    /**
     * 文件上传到本地工程的根目录
     * @param file 需要上传的文件
     * @return
     * @author 小旋风
     * @creed: Little Black Whirlwind
     * @date 2025/6/18 15:58
     */
    public static String file_upload_dir(MultipartFile file){
        try {
            // 获取文件 name
            String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
            // 获取项目根目录
            String projectRoot = System.getProperty("user.dir");
            // 创建目标文件路径
            File destinationFile = new File(projectRoot, originalFilename);

            // 将上传的文件写入目标路径
            try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
                fos.write(file.getBytes());
            }

            return originalFilename;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败");
        }
    }
}
