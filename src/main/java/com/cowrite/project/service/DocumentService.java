package com.cowrite.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cowrite.project.model.entity.Document;

import java.util.List;

/**
 * Document 服务接口
 * @author Hibiscus-code-generate
 */
public interface DocumentService extends IService<Document> {

    List<Document> listDucumentByKnowledgeBase(Long knowledgeBaseId, Long currentUserId);
}
