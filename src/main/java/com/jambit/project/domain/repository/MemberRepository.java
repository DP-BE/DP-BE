package com.jambit.project.domain.repository;

import com.jambit.project.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserId(String userId);
    Optional<Member> findByNickname(String nickname);
    List<Member> findByNicknameContaining(String nickname);
}
