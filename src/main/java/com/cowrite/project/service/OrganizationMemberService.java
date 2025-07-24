package com.cowrite.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cowrite.project.model.entity.OrganizationMember;
import com.cowrite.project.model.vo.OrganizationVO;

import java.util.List;

public interface OrganizationMemberService extends IService<OrganizationMember> {

    /**
     * 根据用户id获取用户加入的组织
     * @param userId 用户id
     * @return 组织列表
     */
    List<OrganizationVO> getOrganizationsByUserId(Long userId);

}
