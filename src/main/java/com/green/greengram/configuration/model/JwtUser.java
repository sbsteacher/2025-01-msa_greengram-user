package com.green.greengram.configuration.model;

import com.green.greengram.configuration.enumcode.model.EnumUserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class JwtUser {
    private long signedUserId;
    private List<EnumUserRole> roles;
}
