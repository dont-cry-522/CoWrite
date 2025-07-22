package com.cowrite.project.controller;

import com.cowrite.project.model.entity.Organization;
import com.cowrite.project.service.OrganizationService;
import com.j256.simplemagic.logger.Logger;
import com.j256.simplemagic.logger.LoggerFactory;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Organization 控制器")
@RestController
@RequestMapping("/api/organization")
public class OrganizationController {

    /**
     * logger for this class
     */

   private static final Logger log = LoggerFactory.getLogger(Organization.class);

    /**
     * organization service
     */

   private  final OrganizationService organizationService ;


    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

}
