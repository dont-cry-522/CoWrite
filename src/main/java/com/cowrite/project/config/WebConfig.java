package com.cowrite.project.config;

import com.cowrite.project.interceptor.UserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    private final UserInterceptor userInterceptor;

    public WebConfig(UserInterceptor userInterceptor) {
        this.userInterceptor = userInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/api/users/upload-avatar","/api/users/update")
                .addPathPatterns("/api/notification/**")
                .addPathPatterns("/api/organization/organized","/api/organization/switch","/api/organization/{organizationId}/member/{userId}",
                                 "/api/organization/{organizationId}/member/{userId}/role","/api/organization/{organizationId}/members");
    }

    @Bean
    public Docket docket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("CoWrite API 文档")  // 标题
                .description("CoWrite Backend")  // 描述
                .version("1.0.0")  // 版本号
                .termsOfServiceUrl("https://www.hibiscus.fit")  // 服务条款 URL
                .contact(new Contact("CoWrite 开发团队", "https://www.hibiscus.fit", "heathcetide@zoho.com"))  // 联系信息
                .license("Apache 2.0")  // 许可证
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0.html")  // 许可证 URL
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cowrite.project.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}