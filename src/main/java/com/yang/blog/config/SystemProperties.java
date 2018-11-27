package com.yang.blog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@ConfigurationProperties(prefix = "system")
public class SystemProperties {
    public static SystemProperties config;

    private Boolean allowRegister;
    private UsernameLength usernameLength;
    private PasswordLength passwordLength;
    private Salt salt;
    private FileUpload fileUpload;

    public static Boolean getAllowRegister() {
        return config.allowRegister;
    }

    public void setAllowRegister(Boolean allowRegister) {
        this.allowRegister = allowRegister;
    }

    public static UsernameLength getUsernameLength() {
        return config.usernameLength;
    }

    public void setUsernameLength(UsernameLength usernameLength) {
        this.usernameLength = usernameLength;
    }

    public static PasswordLength getPasswordLength() {
        return config.passwordLength;
    }

    public void setPasswordLength(PasswordLength passwordLength) {
        this.passwordLength = passwordLength;
    }

    public static Salt getSalt() {
        return config.salt;
    }

    public void setSalt(Salt salt) {
        this.salt = salt;
    }

    public static FileUpload getFileUpload() {
        return config.fileUpload;
    }

    public void setFileUpload(FileUpload fileUpload) {
        this.fileUpload = fileUpload;
    }

    @PostConstruct
    public void init() {
        config = this;
    }

    public static class UsernameLength {
        private Integer min;
        private Integer max;

        public Integer getMin() {
            return min;
        }

        public void setMin(Integer min) {
            this.min = min;
        }

        public Integer getMax() {
            return max;
        }

        public void setMax(Integer max) {
            this.max = max;
        }
    }

    public static class PasswordLength {
        private Integer min;
        private Integer max;

        public Integer getMin() {
            return min;
        }

        public void setMin(Integer min) {
            this.min = min;
        }

        public Integer getMax() {
            return max;
        }

        public void setMax(Integer max) {
            this.max = max;
        }
    }

    public static class Salt {
        private Integer size;
        private Integer hashCount;

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public Integer getHashCount() {
            return hashCount;
        }

        public void setHashCount(Integer hashCount) {
            this.hashCount = hashCount;
        }
    }

    public static class FileUpload {
        private String path = "/upload";
        private Boolean absolute = false;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Boolean getAbsolute() {
            return absolute;
        }

        public void setAbsolute(Boolean absolute) {
            this.absolute = absolute;
        }
    }
}
