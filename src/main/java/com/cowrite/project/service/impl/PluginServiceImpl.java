package com.cowrite.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowrite.project.common.storage.FileStorageAdapter;
import com.cowrite.project.mapper.PluginMapper;
import com.cowrite.project.model.entity.Plugin;
import com.cowrite.project.service.PluginService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Plugin 服务实现类
 * @author Hibiscus-code-generate
 */
@Service
public class PluginServiceImpl extends ServiceImpl<PluginMapper, Plugin> implements PluginService {

    private final FileStorageAdapter fileStorageAdapter;

    private final PluginMapper pluginMapper;

    public PluginServiceImpl(FileStorageAdapter fileStorageAdapter, PluginMapper pluginMapper) {
        this.fileStorageAdapter = fileStorageAdapter;
        this.pluginMapper = pluginMapper;
    }


    public String savePluginFile(MultipartFile file) {
        String upload = fileStorageAdapter.upload("/plugins", file);
        pluginMapper.insert(new Plugin.Builder()
                .setId(1L)
                .setPluginName("TextGenerationPlugin")
                .setPluginType("Text Generation")
                .setDescription("A plugin for generating text.")
                .setVersion("1.0")
                .setWebhookUrl("http://example.com/webhook")
                .setApiUrl("http://example.com/api")
                .setDocumentationUrl("http://example.com/docs")
                .setAuthor("Hibiscus")
                .setEnabled(true)
                .build());
        for (Plugin plugin : pluginMapper.selectList(new LambdaQueryWrapper<>())) {
            System.out.println(plugin);
        }
        return upload;
    }

//    public void parseAndReviewPlugin(String filePath) {
//        try {
//            // 解压插件文件
//            File pluginFile = new File(filePath);
//            File destDir = new File(UPLOAD_DIR + "extracted/");
//            unzip(pluginFile, destDir);
//
//            // 解析插件内容
//            parsePluginContent(destDir);
//
//            // 审核插件
//            reviewPlugin(destDir);
//        } catch (IOException e) {
//            throw new RuntimeException("插件解析失败: " + e.getMessage());
//        }
//    }
//
//    private void unzip(File zipFile, File destDir) throws IOException {
//        byte[] buffer = new byte[1024];
//        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
//            ZipEntry zipEntry = zis.getNextEntry();
//            while (zipEntry != null) {
//                File newFile = newFile(destDir, zipEntry);
//                if (zipEntry.isDirectory()) {
//                    if (!newFile.mkdirs()) {
//                        throw new IOException("创建目录失败: " + newFile);
//                    }
//                } else {
//                    File parent = newFile.getParentFile();
//                    if (!parent.mkdirs()) {
//                        throw new IOException("创建目录失败: " + parent);
//                    }
//
//                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
//                        int len;
//                        while ((len = zis.read(buffer)) > 0) {
//                            fos.write(buffer, 0, len);
//                        }
//                    }
//                }
//                zipEntry = zis.getNextEntry();
//            }
//            zis.closeEntry();
//        }
//    }
//
//    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
//        File destFile = new File(destinationDir, zipEntry.getName());
//        String destDirPath = destinationDir.getCanonicalPath();
//        String destFilePath = destFile.getCanonicalPath();
//
//        if (!destFilePath.startsWith(destDirPath + File.separator)) {
//            throw new IOException("解压路径非法: " + zipEntry.getName());
//        }
//
//        return destFile;
//    }
//
//    private void parsePluginContent(File pluginDir) {
//        // 解析插件内容，比如读取配置文件、检查文件结构等
//    }
//
//    private void reviewPlugin(File pluginDir) {
//        // 审核插件，比如检查插件是否符合规范、是否有恶意代码等
//    }
//
//    public void parsePlugin(String filePath) {
//        try {
//            // 解压插件文件
//            File pluginFile = new File(filePath);
//            File destDir = new File(UPLOAD_DIR + "extracted/");
//            unzip(pluginFile, destDir);
//
//            // 解析插件类型
//            PluginType pluginType = detectPluginType(destDir);
//
//            // 根据插件类型进行解析
//            switch (pluginType) {
//                case JAVA:
//                    parseJavaPlugin(destDir);
//                    break;
//                case GO:
//                    parseGoPlugin(destDir);
//                    break;
//                case PYTHON:
//                    parsePythonPlugin(destDir);
//                    break;
//                default:
//                    throw new RuntimeException("未知的插件类型");
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("插件解析失败: " + e.getMessage());
//        }
//    }
//
//    private PluginType detectPluginType(File pluginDir) {
//        // 检查文件结构或配置文件，确定插件类型
//        if (new File(pluginDir, "pom.xml").exists()) {
//            return PluginType.JAVA;
//        } else if (new File(pluginDir, "go.mod").exists()) {
//            return PluginType.GO;
//        } else if (new File(pluginDir, "requirements.txt").exists()) {
//            return PluginType.PYTHON;
//        } else {
//            throw new RuntimeException("无法确定插件类型");
//        }
//    }
//
//    private void parseJavaPlugin(File pluginDir) {
//        // 解析 Java 插件，比如读取 pom.xml 文件
//    }
//
//    private void parseGoPlugin(File pluginDir) {
//        // 解析 Go 插件，比如读取 go.mod 文件
//    }
//
//    private void parsePythonPlugin(File pluginDir) {
//        // 解析 Python 插件，比如读取 requirements.txt 文件
//    }
//
//
//    @Autowired
//    private AIService aiService;
//
//    @Autowired
//    private PluginReviewService reviewService;
//
//    public void reviewPlugin(String filePath) {
//        // 调用 AI 解析
//        AIParseResult aiResult = aiService.parsePlugin(filePath);
//
//        // 提交给后台人员进行审核
//        reviewService.submitForReview(aiResult);
//    }
//
//
//    public void buildPlugin(File pluginDir, PluginType pluginType) {
//        switch (pluginType) {
//            case JAVA:
//                buildJavaPlugin(pluginDir);
//                break;
//            case GO:
//                buildGoPlugin(pluginDir);
//                break;
//            case PYTHON:
//                buildPythonPlugin(pluginDir);
//                break;
//            default:
//                throw new RuntimeException("未知的插件类型");
//        }
//    }
//
//    private void buildJavaPlugin(File pluginDir) {
//        // 使用 Maven 或 Gradle 打包 Java 插件
//        // 例如：执行 mvn package 命令
//    }
//
//    private void buildGoPlugin(File pluginDir) {
//        // 使用 Go 编译插件
//        // 例如：执行 go build 命令
//    }
//
//    private void buildPythonPlugin(File pluginDir) {
//        // 使用 PyInstaller 打包 Python 插件
//        // 例如：执行 pyinstaller --onefile script.py 命令
//    }
}
