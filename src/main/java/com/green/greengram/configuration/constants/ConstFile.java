package com.green.greengram.configuration.constants;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "constants.file")
@RequiredArgsConstructor
@ToString
public class ConstFile {
    public final String uploadDirectory;
    public final String profilePic;
}
