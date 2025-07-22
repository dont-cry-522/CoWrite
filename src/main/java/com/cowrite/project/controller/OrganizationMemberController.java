package com.cowrite.project.controller;

import com.cowrite.project.service.OrganizationMemberService;
import com.j256.simplemagic.logger.Logger;
import com.j256.simplemagic.logger.LoggerFactory;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Organization Member 控制器")
@RestController
@RequestMapping("/api/organization/member")
public class OrganizationMemberController {

    /**
     * log for this class.
     */

    private static final Logger log = LoggerFactory.getLogger(OrganizationMemberController.class);

    private final OrganizationMemberService organizationMemberService ;

    public OrganizationMemberController(OrganizationMemberService organizationMemberService) {
        this.organizationMemberService = organizationMemberService;
    }
}
