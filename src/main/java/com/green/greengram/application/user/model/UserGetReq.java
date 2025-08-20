package com.green.greengram.application.user.model;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class UserGetReq {
    private List<Long> writerUserIdList;
}
