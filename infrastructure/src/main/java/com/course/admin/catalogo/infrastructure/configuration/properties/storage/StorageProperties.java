package com.course.admin.catalogo.infrastructure.configuration.properties.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class StorageProperties implements InitializingBean {

    public static final Logger log = LoggerFactory.getLogger(StorageProperties.class);
    private String locationPattern;
    private String fileNamePattern;

    public StorageProperties() {
    }

    public String getLocationPattern() {
        return locationPattern;
    }

    public StorageProperties setLocationPattern(String locationPattern) {
        this.locationPattern = locationPattern;
        return this;
    }

    public String getFileNamePattern() {
        return fileNamePattern;
    }

    public StorageProperties setFileNamePattern(String fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
        return this;
    }

    @Override
    public String toString() {
        return "StorageProperties{" +
                "locationPattern='" + locationPattern + '\'' +
                ", fileNamePattern='" + fileNamePattern + '\'' +
                '}';
    }

    @Override
    public void afterPropertiesSet() {
        log.debug(toString());
    }
}
