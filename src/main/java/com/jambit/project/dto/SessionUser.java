package com.jambit.project.dto;

import com.jambit.project.domain.entity.Member;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(Member member) {
        this.name = member.getNickname();
        this.email = member.getUserId();
        this.picture = member.getPicture();
    }
}