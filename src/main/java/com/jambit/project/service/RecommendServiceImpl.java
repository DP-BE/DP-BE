package com.jambit.project.service;

import com.jambit.project.domain.entity.*;
import com.jambit.project.domain.repository.BoardRepository;
import com.jambit.project.domain.repository.ProjectRepository;
import com.jambit.project.domain.repository.RecommendRepository;
import com.jambit.project.domain.repository.ReplyRepository;
import com.jambit.project.dto.BoardDto;
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
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    private final ProjectRepository projectRepository;

    public Long create(RecommendDto recommendDto) {
        if (recommendDto != null) {
            recommendDto.setIsDeleted(false);
            Recommend recommend = RecommendDto.toEntity(recommendDto);
            switch (recommend.getTargetType()) {
                case POST:
                    Optional<Board> findPost = boardRepository.findById(recommend.getRefId());
                    findPost.ifPresent(b -> b.setLikesCount(b.getLikesCount() + 1L));
                    break;
                case REPLY:
                    Optional<Reply> findReply = replyRepository.findById(recommend.getRefId());
                    findReply.ifPresent(r -> r.setLikesCount(r.getLikesCount() + 1L));
                    break;
                case PROJECT:
                    Optional<Project> findProject = projectRepository.findById(recommend.getRefId());
                    findProject.ifPresent(p -> p.setLikesCount(p.getLikesCount() + 1L));
                    break;
            }
            recommendRepository.save(recommend);
            return recommend.getId();
        }
        return null;
    }

    public Boolean delete(Long recommendId) {
        Optional<Recommend> findRecommendWrapper = recommendRepository.findById(recommendId);
        if (findRecommendWrapper.isPresent()) {
            if (!findRecommendWrapper.get().getIsDeleted()) {
                recommendRepository.deleteById(recommendId);
                switch (findRecommendWrapper.get().getTargetType()) {
                    case POST:
                        Optional<Board> findPost = boardRepository.findById(findRecommendWrapper.get().getRefId());
                        findPost.ifPresent(b -> b.setLikesCount(b.getLikesCount() - 1L));
                        break;
                    case REPLY:
                        Optional<Reply> findReply = replyRepository.findById(findRecommendWrapper.get().getRefId());
                        findReply.ifPresent(r -> r.setLikesCount(r.getLikesCount() - 1L));
                        break;
                    case PROJECT:
                        Optional<Project> findProject = projectRepository.findById(findRecommendWrapper.get().getRefId());
                        findProject.ifPresent(p -> p.setLikesCount(p.getLikesCount() - 1L));
                        break;
                }
            }
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
