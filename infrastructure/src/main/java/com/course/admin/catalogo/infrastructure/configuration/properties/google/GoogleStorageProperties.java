package com.course.admin.catalogo.infrastructure.configuration.properties.google;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class GoogleStorageProperties implements InitializingBean {

    public final static Logger log = LoggerFactory.getLogger(GoogleStorageProperties.class);

    private String bucket;
    private int connectTimeout;
    private int readTimeout;
    private int retryDelay;
    private int retryMaxDelay;
    private int retryMaxAttempts;
    private double retryMultiplier;

    public String getBucket() {
        return bucket;
    }

    public GoogleStorageProperties setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public GoogleStorageProperties setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public GoogleStorageProperties setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public int getRetryDelay() {
        return retryDelay;
    }

    public GoogleStorageProperties setRetryDelay(int retryDelay) {
        this.retryDelay = retryDelay;
        return this;
    }

    public int getRetryMaxDelay() {
        return retryMaxDelay;
    }

    public GoogleStorageProperties setRetryMaxDelay(int retryMaxDelay) {
        this.retryMaxDelay = retryMaxDelay;
        return this;
    }

    public int getRetryMaxAttempts() {
        return retryMaxAttempts;
    }

    public GoogleStorageProperties setRetryMaxAttempts(int retryMaxAttempts) {
        this.retryMaxAttempts = retryMaxAttempts;
        return this;
    }

    public double getRetryMultiplier() {
        return retryMultiplier;
    }

    public GoogleStorageProperties setRetryMultiplier(double retryMultiplier) {
        this.retryMultiplier = retryMultiplier;
        return this;
    }

    @Override
    public void afterPropertiesSet() {
        log.debug(toString());
    }

    @Override
    public String toString() {
        return "GoogleStorageProperties{" +
                "bucket='" + bucket + '\'' +
                ", connectTimeout=" + connectTimeout +
                ", readTimeout=" + readTimeout +
                ", retryDelay=" + retryDelay +
                ", retryMaxDelay=" + retryMaxDelay +
                ", retryMaxAttempts=" + retryMaxAttempts +
                ", retryMultiplier=" + retryMultiplier +
                '}';
    }
}
