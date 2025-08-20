package com.green.greengram.application.user.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserGetItem {
    private String writerUid;
    private String writerNickName;
    private String writerPic;
}
