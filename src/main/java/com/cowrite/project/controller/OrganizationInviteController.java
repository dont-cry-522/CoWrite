package com.cowrite.project.controller;

import com.cowrite.project.model.entity.Organization;
import com.cowrite.project.model.entity.OrganizationInvite;
import com.cowrite.project.service.OrganizationService;
import com.j256.simplemagic.logger.Logger;
import com.j256.simplemagic.logger.LoggerFactory;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Organization Invite 控制器")
@RestController
@RequestMapping("/api/organization/invite")
public class OrganizationInviteController {

    /**
     * log for this class.
     */
    private static final Logger log = LoggerFactory.getLogger(OrganizationInvite.class);

    /**
     * organization service.
     */
    private final OrganizationService organizationService;

    public OrganizationInviteController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

}