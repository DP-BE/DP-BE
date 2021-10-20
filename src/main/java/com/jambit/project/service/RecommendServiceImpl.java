package com.jambit.project.service;

import com.jambit.project.domain.entity.Recommend;
import com.jambit.project.domain.entity.TargetType;
import com.jambit.project.domain.repository.RecommendRepository;
import com.jambit.project.dto.RecommendDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendServiceImpl implements RecommendService {
    private final RecommendRepository recommendRepository;

    public Long create(RecommendDto recommendDto) {
        if (recommendDto != null) {
            recommendDto.setIsDeleted(false);
            Recommend recommend = RecommendDto.toEntity(recommendDto);
            recommendRepository.save(recommend);
            return recommend.getId();
        }
        return null;
    }

    public Boolean delete(Long recommendId) {
        Optional<Recommend> findRecommendWrapper = recommendRepository.findById(recommendId);
        if (findRecommendWrapper.isPresent()) {
            if (!findRecommendWrapper.get().getIsDeleted())
                recommendRepository.deleteById(recommendId);
            return true;
        }
        return false;
    }

    public Integer countRecommendByType(Long refId, TargetType targetType) {
        switch(targetType) {
            case POST:
                List<Recommend> byPostId = recommendRepository.findByPostId(refId);
                return byPostId.size();
            case REPLY:
                List<Recommend> byReplyId = recommendRepository.findByReplyId(refId);
                return byReplyId.size();
            case PROJECT:
                List<Recommend> byProjectId = recommendRepository.findByProjectId(refId);
                return byProjectId.size();
        }
        return null;
    }

    public Boolean getRecommendByTypeAndUser(Long refId, TargetType targetType, String nickname) {
        switch(targetType) {
            case POST:
                Optional<Recommend> isRecommendedOnPost = recommendRepository.findByPostIdAndNicknameAndIsDeletedFalse(refId, nickname);
                return isRecommendedOnPost.isPresent();
            case REPLY:
                Optional<Recommend> isRecommendedOnReply = recommendRepository.findByReplyIdAndNicknameAndIsDeletedFalse(refId, nickname);
                return isRecommendedOnReply.isPresent();
            case PROJECT:
                Optional<Recommend> isRecommendedOnProject = recommendRepository.findByProjectIdAndNicknameAndIsDeletedFalse(refId, nickname);
                return isRecommendedOnProject.isPresent();
        }
        return false;
    }
}
