package com.cowrite.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowrite.project.mapper.OrganizationInviteMapper;
import com.cowrite.project.model.entity.OrganizationInvite;
import com.cowrite.project.service.OrganizationInviteService;
import org.springframework.stereotype.Service;

@Service
public class OrganizationInviteServiceImpl extends ServiceImpl<OrganizationInviteMapper, OrganizationInvite> implements OrganizationInviteService {
}
