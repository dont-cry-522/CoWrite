package com.cowrite.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowrite.project.mapper.KnowledgeBaseMapper;
import com.cowrite.project.model.entity.KnowledgeBase;
import com.cowrite.project.service.KnowledgeBaseService;
import org.springframework.stereotype.Service;

@Service
public class KnowledgeBaseServiceImpl extends ServiceImpl<KnowledgeBaseMapper,KnowledgeBase> implements KnowledgeBaseService {
}
