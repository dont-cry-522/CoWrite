package com.cowrite.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowrite.project.common.context.AuthContext;
import com.cowrite.project.config.ServerConfig;
import com.cowrite.project.exception.BusinessException;
import com.cowrite.project.mapper.OrganizationInviteMapper;
import com.cowrite.project.model.dto.Organization.InviteCreateDTO;
import com.cowrite.project.model.entity.Organization;
import com.cowrite.project.model.entity.OrganizationInvite;
import com.cowrite.project.model.entity.OrganizationMember;
import com.cowrite.project.model.vo.InviteResponseVO;
import com.cowrite.project.model.vo.OrganizationInviteVO;
import com.cowrite.project.service.OrganizationInviteService;
import com.cowrite.project.service.OrganizationMemberService;
import com.cowrite.project.service.OrganizationService;
import com.cowrite.project.service.UserService;
import com.cowrite.project.utils.CodeUtil;
import com.cowrite.project.utils.SnowflakeIdGenerator;
import com.hibiscus.signal.spring.configuration.SignalContextCollector;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.cowrite.project.common.constants.UserEventConstants.EVENT_INTER_MEDIATE_USER;
import static com.cowrite.project.common.enums.ResponseCodeEnum.*;


@Service
public class OrganizationInviteServiceImpl extends ServiceImpl<OrganizationInviteMapper, OrganizationInvite> implements OrganizationInviteService {

    private final ServerConfig serverConfig;

    private final UserService userService;

    private final OrganizationService organizationService;

    private final OrganizationMemberService organizationMemberService;

    public OrganizationInviteServiceImpl (ServerConfig serverConfig, UserService userService, OrganizationService organizationService, OrganizationMemberService organizationMemberService) {
        this.serverConfig = serverConfig;
        this.userService = userService;
        this.organizationService = organizationService;
        this.organizationMemberService = organizationMemberService;
    }

    @Override
    public InviteResponseVO createInvite(InviteCreateDTO inviteCreateDTO) {
        SignalContextCollector.collect(EVENT_INTER_MEDIATE_USER, AuthContext.getCurrentUser());
        OrganizationInvite invite = new OrganizationInvite();
        invite.setId(SnowflakeIdGenerator.nextId());
        invite.setInviterId(AuthContext.getCurrentUser().getId());
        invite.setOrganizationId(invite.getOrganizationId());
        invite.setRole(inviteCreateDTO.getRole());
        invite.setMaxUses(invite.getMaxUses());
        invite.setUsedCount(0);
        invite.setExpiresAt(inviteCreateDTO.getExpiresAt());
        save(invite); // 插入后自动生成ID
        // 生成短码
        String shortCode = CodeUtil.encode(invite.getId());
        invite.setInviteCode(shortCode);
        this.updateById(invite); // 保存邀请码字段

        String baseUrl = serverConfig.getInviteBaseUrl();
        String fullUrl = baseUrl + "/" + invite.getInviteCode();
        return new InviteResponseVO(invite.getInviteCode(), fullUrl, fullUrl);
    }

    @Override
    public OrganizationInvite getByCode(String code) {
        // 解码短码 -> 邀请ID
        Long inviteId = CodeUtil.decode(code);
        OrganizationInvite invite = this.getById(inviteId);

        if (invite == null || invite.getExpiresAt().isBefore(LocalDateTime.now())) {
            return null;
        }
        return invite;
    }

    @Override
    public OrganizationInviteVO validateInviteCode(String code) {
        OrganizationInvite invite = getByCode(code);
        if (invite == null){
            throw new BusinessException(NOT_FOUND.code(), "邀请码不存在");
        }
        // 构造返回信息
        OrganizationInviteVO vo = new OrganizationInviteVO();
        vo.setStatus(OrganizationInviteVO.Status.VALID.getStatus());
        if (invite.getExpiresAt() != null && invite.getExpiresAt().isBefore(LocalDateTime.now())) {
            vo.setStatus(OrganizationInviteVO.Status.EXPIRED.getStatus());
        }
        if (invite.getMaxUses() != null && invite.getUsedCount() >= invite.getMaxUses()) {
            vo.setStatus(OrganizationInviteVO.Status.USED_UP.getStatus());
        }

        Organization org = organizationService.getById(invite.getOrganizationId());
        if (org == null) {
            vo.setStatus(OrganizationInviteVO.Status.INVALID.getStatus());
        }
        vo.setInviteCode(invite.getInviteCode());
        vo.setOrganizationId(org.getId());
        vo.setOrganizationName(org.getName());
        vo.setOrganizationDescription(org.getDescription());
        vo.setInviterUsername(userService.getById(invite.getInviterId()).getUsername());
        vo.setRole(invite.getRole());
        vo.setMaxUses(invite.getMaxUses());
        vo.setUsedCount(invite.getUsedCount());
        vo.setExpiresAt(invite.getExpiresAt());
        vo.setValid(true);
        vo.setReasonIfInvalid(null);
        return vo;
    }

    public OrganizationInvite getOrgIdByCode(String code) {
        LambdaQueryWrapper<OrganizationInvite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrganizationInvite::getInviteCode, code);
        return getOne(queryWrapper);
    }

    @Override
    public void useInvite(String code, Long userId) {
        // 1. 查询邀请码
        OrganizationInvite invite = this.getOrgIdByCode(code);
        if (invite == null){
            throw new BusinessException(NOT_FOUND.code(), "邀请码不存在");
        }

        // 2. 校验有效性
        if (invite.getExpiresAt() != null && invite.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException(EXPIRED.code(), "邀请码已过期");
        }

        if (invite.getMaxUses() != null && invite.getUsedCount() >= invite.getMaxUses()) {
            throw new BusinessException(USAGE_LIMIT_EXCEEDED.code(), "邀请码已达最大使用次数");
        }

        // 3. 判断用户是否已加入组织
        boolean alreadyMember = organizationMemberService.lambdaQuery()
                .eq(OrganizationMember::getOrganizationId, invite.getOrganizationId())
                .eq(OrganizationMember::getUserId, userId)
                .exists();

        if (alreadyMember) {
            throw new BusinessException(ALREADY_MEMBER.code(), "您已是该组织成员");
        }

        // 4. 插入新成员
        OrganizationMember member = new OrganizationMember();
        member.setId(SnowflakeIdGenerator.nextId());
        member.setOrganizationId(invite.getOrganizationId());
        member.setUserId(userId);
        member.setRole(invite.getRole()); // 从邀请码获取角色
        member.setStatus("ACTIVE");
        member.setJoinedAt(LocalDateTime.now());
        organizationMemberService.save(member);

        // 5. 更新使用次数
        invite.setUsedCount(invite.getUsedCount() + 1);
        this.updateById(invite);
        SignalContextCollector.collect(EVENT_INTER_MEDIATE_USER, AuthContext.getCurrentUser());
    }
}



