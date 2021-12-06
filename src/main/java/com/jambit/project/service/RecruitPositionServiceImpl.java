package com.jambit.project.service;

import com.jambit.project.domain.entity.RecruitPosition;
import com.jambit.project.domain.repository.RecruitPositionRepository;
import com.jambit.project.dto.RecruitPositionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitPositionServiceImpl implements RecruitPositionService {
    private final RecruitPositionRepository recruitPositionRepository;

    @Transactional
    public List<RecruitPositionDto> getPositionList(Long postId) {
        List<RecruitPosition> findPositionList = recruitPositionRepository.findByPostIdAndIsDeletedFalse(postId);
        return findPositionList.stream()
                .map(RecruitPosition::toDto)
                .collect(Collectors.toList());
    }


}
