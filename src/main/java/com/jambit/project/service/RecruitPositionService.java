package com.jambit.project.service;

import com.jambit.project.dto.RecruitPositionDto;

import java.util.List;

public interface RecruitPositionService {
    List<RecruitPositionDto> getPositionList(Long postId);
}
