package com.cowrite.project.controller;

import com.cowrite.project.service.KnowledgeBaseService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Knowledge Base 控制器")
@RestController
@RequestMapping("/api/knowledge-base")
public class KnowledgeBaseController {

    /**
     * Logger
     */

    private static final Logger log = LoggerFactory.getLogger(KnowledgeBaseController.class);

    /**
     * KnowledgeBase Service
     */

    private final KnowledgeBaseService knowledgeBaseService;

    public KnowledgeBaseController(KnowledgeBaseService knowledgeBaseService) {
        this.knowledgeBaseService = knowledgeBaseService;
    }
}