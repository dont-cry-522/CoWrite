package com.cowrite.project.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 程序注解配置
 *
 * @author heathcetide
 */
@Component
@ConfigurationProperties(prefix = "cowrite")
public class ApplicationConfig {

    /**
     * 项目名称，如 "hibiscus"
     */
    private String name;
    /**
     * 项目版本号，由 Maven 属性注入
     */
    private String version;
    /**
     * 构建时间，例如 "2024-03-19 12:00:00"
     */
    private String buildTime;
    /**
     * 版权信息
     */
    private Copyright copyright;

    // Getter 和 Setter 方法

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public Copyright getCopyright() {
        return copyright;
    }

    public void setCopyright(Copyright copyright) {
        this.copyright = copyright;
    }

    /**
     * 内部类用于封装版权相关的配置信息
     */
    public static class Copyright {
        /**
         * 版权所有者，如 "hibiscus Team"
         */
        private String owner;
        /**
         * 版权起始年份，如 2024
         */
        private int sinceYear;
        /**
         * 许可证类型，如 "MIT"
         */
        private String license;


        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public int getSinceYear() {
            return sinceYear;
        }

        public void setSinceYear(int sinceYear) {
            this.sinceYear = sinceYear;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }
    }
}
