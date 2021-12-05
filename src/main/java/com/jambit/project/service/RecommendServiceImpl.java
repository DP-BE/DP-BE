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
import java.util.stream.Collectors;

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
                recommendRepository.deleteById(recommendId);
            }
            return true;
        }
        return false;
    }

    public Integer countRecommendByType(Long refId, TargetType targetType) {
        List<Recommend> recommendList = recommendRepository.findByRefIdAndTargetType(refId, targetType);
        return recommendList.size();
    }

    public Boolean getRecommendByTypeAndUser(Long refId, TargetType targetType, String nickname) {
        Optional<Recommend> recommendList = recommendRepository.findTop1ByRefIdAndNicknameAndTargetTypeAndIsDeletedFalse(refId, nickname, targetType);
        return recommendList.isPresent();
    }

    public Long findIdByTypeAndUserAndRef(TargetType targetType, String nickname, Long refId) {
        List<Recommend> recommendList = recommendRepository.findByTargetTypeAndNickname(targetType, nickname);
        Long ret = -1L;
        for(Recommend iter : recommendList) {
            if (iter.getRefId().equals(refId)) {
                ret = iter.getId();
                break;
            }
        }
        return ret;
    }
}
