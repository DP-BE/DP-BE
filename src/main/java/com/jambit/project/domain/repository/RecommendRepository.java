package com.jambit.project.domain.repository;

import com.jambit.project.domain.entity.Recommend;
import com.jambit.project.domain.entity.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {
    List<Recommend> findByRefIdAndTargetType(Long refId, TargetType targetType);
    Optional<Recommend> findTop1ByRefIdAndNicknameAndTargetTypeAndIsDeletedFalse(Long refId, String nickname, TargetType targetType);
    List<Recommend> findByTargetTypeAndNickname(TargetType targetType, String nickname);
    List<Recommend> findByNickname(String nickname);
}
