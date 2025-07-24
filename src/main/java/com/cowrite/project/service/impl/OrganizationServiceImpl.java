package com.cowrite.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowrite.project.mapper.OrganizationMapper;
import com.cowrite.project.mapper.OrganizationMemberMapper;
import com.cowrite.project.mapper.UserMapper;
import com.cowrite.project.model.entity.Organization;
import com.cowrite.project.model.entity.OrganizationMember;
import com.cowrite.project.model.entity.User;
import com.cowrite.project.service.OrganizationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService {

    private final OrganizationMemberMapper organizationMemberMapper;

    private final UserMapper userMapper;

    public OrganizationServiceImpl(OrganizationMemberMapper organizationMemberMapper, UserMapper userMapper) {
        this.organizationMemberMapper = organizationMemberMapper;
        this.userMapper = userMapper;
    }

    /**
     * 用户获取已参与的组织
     */
    @Override
    public List<Organization> getOrganizationsByUser(User currentUser) {
        return super.list(new LambdaQueryWrapper<Organization>().in(
                Organization::getId,
                organizationMemberMapper.selectList(
                         new LambdaQueryWrapper<OrganizationMember>()
                         .eq(OrganizationMember::getUserId, currentUser.getId()))
                        .stream()
                        .map(OrganizationMember::getOrganizationId)
                        .collect(Collectors.toList())
        ));
    }

    /**
     * 获取组织成员列表
     */
    @Override
    public List<User> getMembersByOrganizationId(Long organizationId) {
        List<Long> userIds = organizationMemberMapper.selectList(
                        new LambdaQueryWrapper<OrganizationMember>()
                                .eq(OrganizationMember::getOrganizationId, organizationId))
                .stream()
                .map(OrganizationMember::getUserId)
                .collect(Collectors.toList());
        if (userIds.isEmpty()) {
            return new ArrayList<>();
        }
        return userMapper.selectBatchIds(userIds);
    }

    /**
     * 设置组织成员角色
     */
    @Override
    public Boolean setMemberRole(Long organizationId, Long userId, String role) {
        OrganizationMember organizationMember = organizationMemberMapper.selectOne(
                new LambdaQueryWrapper<OrganizationMember>()
                        .eq(OrganizationMember::getOrganizationId, organizationId)
                        .eq(OrganizationMember::getUserId, userId)
        );
        if (organizationMember == null) {
            return false;
        }
        organizationMember.setRole(role);
        return organizationMemberMapper.updateById(organizationMember) > 0;
    }

    /**
     * 移除组织成员
     */
    @Override
    public Boolean removeMember(Long organizationId, Long userId) {
        OrganizationMember organizationMember = organizationMemberMapper.selectOne(
                new LambdaQueryWrapper<OrganizationMember>()
                        .eq(OrganizationMember::getOrganizationId, organizationId)
                        .eq(OrganizationMember::getUserId, userId)
        );
        if (organizationMember == null) {
            return false;
        }
        return organizationMemberMapper.deleteById(organizationMember.getId()) > 0;
    }

    /**
     * 切换当前选中的组织
     */
    @Override
    public void switchCurrentOrganization(User currentUser, Long organizationId) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getCurrentOrganizationId, organizationId);
        userMapper.update(currentUser, updateWrapper);
    }
}
