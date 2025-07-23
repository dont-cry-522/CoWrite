package com.cowrite.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowrite.project.mapper.OrganizationMapper;
import com.cowrite.project.model.entity.Organization;
import com.cowrite.project.service.OrganizationService;
import org.springframework.stereotype.Service;

@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService {
}
