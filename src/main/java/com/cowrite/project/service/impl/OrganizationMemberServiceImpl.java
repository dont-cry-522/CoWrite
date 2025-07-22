package com.cowrite.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowrite.project.mapper.OrganizationMemberMapper;
import com.cowrite.project.model.entity.OrganizationMember;
import com.cowrite.project.service.OrganizationMemberService;
import org.springframework.stereotype.Service;

@Service
public class OrganizationMemberServiceImpl extends ServiceImpl<OrganizationMemberMapper,OrganizationMember> implements OrganizationMemberService {
}
