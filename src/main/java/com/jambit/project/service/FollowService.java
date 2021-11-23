package com.jambit.project.service;

import com.jambit.project.domain.entity.Follow;
import com.jambit.project.dto.FollowDto;

import java.util.List;

public interface FollowService {
    List<FollowDto> findFollowerListByUserId(String userId);

    List<FollowDto> findFollowingListByUserId(String userId);

    Long create(FollowDto followDto);

    void delete(Long id);

    Integer countFollowingListByUserId(String userId);

    Integer countFollowerListByUserId(String userId);
}
