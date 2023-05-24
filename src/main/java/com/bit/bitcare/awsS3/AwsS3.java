package com.bit.bitcare.awsS3;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AwsS3 {
    private String key;
    private String path;
    private String url;

    public AwsS3() {

    }

    @Builder
    public AwsS3(String key, String path) {
        this.key = key;
        this.path = path;
    }
    @Builder
    public AwsS3(String key, String path, String url) {
        this.key = key;
        this.path = path;
        this.url = url;
    }
}
