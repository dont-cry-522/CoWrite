package com.cowrite.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowrite.project.common.ApiResponse;
import com.cowrite.project.common.context.AuthContext;
import com.cowrite.project.model.entity.Document;
import com.cowrite.project.model.entity.DocumentVersion;
import com.cowrite.project.service.DocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Document 控制器，提供基础增删改查接口
 *
 * @author Hibiscus-code-generate
 */
@Api(tags = "Document 控制器")
@RestController
@RequestMapping("/api/document")
public class DocumentController {

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);

    /**
     * DocumentService
     */
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }


    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        return AuthContext.getCurrentUser().getId();
    }
}
