package com.green.greengram.configuration.constants;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "constants.jwt")
@RequiredArgsConstructor
@ToString
public class ConstJwt {
    public final String issuer;
    public final String schema;
    public final String bearerFormat;

    public final String claimKey;
    public final String headerKey;
    public final String secretKey;

    public final String domain;

    public final String accessTokenCookieName;
    public final String accessTokenCookiePath;
    public final int accessTokenCookieValiditySeconds;
    public final int accessTokenValidityMilliseconds;

    public final String refreshTokenCookieName;
    public final String refreshTokenCookiePath;
    public final int refreshTokenCookieValiditySeconds;
    public final int refreshTokenValidityMilliseconds;
}
