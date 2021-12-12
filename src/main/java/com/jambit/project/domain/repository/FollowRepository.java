package com.jambit.project.domain.repository;

import com.jambit.project.domain.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByNickname(String nickname);
    List<Follow> findByFollowee(String followee);
    Optional<Follow> findByNicknameAndFollowee(String nickname, String followee);
}
