package com.cowrite.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowrite.project.mapper.DocumentMapper;
import com.cowrite.project.model.entity.Document;
import com.cowrite.project.service.DocumentService;
import com.cowrite.project.utils.RedisUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Document 服务实现类
 * @author Hibiscus-code-generate
 */
@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document> implements DocumentService {

    private final RedisUtils redisUtils;

    public DocumentServiceImpl(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public List<Document> listDucumentByKnowledgeBase(Long knowledgeBaseId, Long currentUserId) {
        // 1. 查询符合条件的文档基础信息（不含 title）
        QueryWrapper<Document> query = new QueryWrapper<>();
        query.eq("knowledge_base_id", knowledgeBaseId);
        query.eq("owner_id", currentUserId);
        query.eq("deleted", 0);
        query.orderByAsc("level", "sort_order");

        List<Document> dbDocs = list(query);
        if (dbDocs.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 批量获取 Redis 中的 title 缓存
        List<Long> docIds = dbDocs.stream().map(Document::getId).collect(Collectors.toList());
        List<String> redisKeys = docIds.stream().map(id -> "doc:" + id + ":title").collect(Collectors.toList());
        List<String> cachedTitles = redisUtils.multiGet(redisKeys);

        // 3. 填充 title（缓存优先，不足的回源 DB）
        for (int i = 0; i < dbDocs.size(); i++) {
            Document doc = dbDocs.get(i);
            String cachedTitle = cachedTitles.get(i);

            if (cachedTitle != null) {
                doc.setTitle(cachedTitle);
            } else {
                // 如果缓存没有，使用数据库中已有的 title（你 list 查询时可能已包含）
                // 若你只查了 id，可以单独查 title
                String dbTitle = doc.getTitle();
                if (dbTitle != null) {
                    redisUtils.set("doc:" + doc.getId() + ":title", dbTitle, Duration.ofHours(1));
                }
            }
        }
        return dbDocs;
    }
}
