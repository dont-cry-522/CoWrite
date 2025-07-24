package com.cowrite.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cowrite.project.common.anno.Loggable;
import com.cowrite.project.common.enums.LogType;
import com.cowrite.project.model.dto.Organization.InviteCreateDTO;
import com.cowrite.project.model.entity.OrganizationInvite;
import com.cowrite.project.model.vo.InviteResponseVO;
import com.cowrite.project.model.vo.OrganizationInviteVO;
import com.hibiscus.signal.spring.anno.SignalEmitter;

public interface OrganizationInviteService extends IService<OrganizationInvite> {

     /**
      * 创建邀请码
      * @param invite 封装后的OrganizationInvite实例
      * @return 邀请码
      */
     @SignalEmitter("invite.create")
     @Loggable(type = LogType.CONFIGURATION, value = "创建邀请码")
     public InviteResponseVO createInvite(InviteCreateDTO invite);

     /**
      * 根据邀请码获取邀请信息
      * @param code 邀请码
      * @return 邀请信息
      */
     @Loggable(type = LogType.CONFIGURATION, value = "获取邀请信息")
     public OrganizationInvite getByCode(String code);

     /**
      * 验证邀请码
      * @param code 邀请码
      * @return 邀请信息
      */
     @Loggable(type = LogType.CONFIGURATION, value = "验证邀请码")
     public OrganizationInviteVO validateInviteCode(String code);

     /**
      * 使用邀请码
      * @param code 邀请码
      * @param userId 用户ID
      */
     @SignalEmitter("invite.use")
     @Loggable(type = LogType.CONFIGURATION, value = "使用邀请码")
     public void useInvite(String code, Long userId);

}
