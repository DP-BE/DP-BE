package com.jambit.project.domain.repository;

import com.jambit.project.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserId(String userId);
    Optional<Member> findByNickname(String nickname);
    List<Member> findByNicknameContaining(String nickname);
    Page<Member> findAllByOrderByProjectCntDesc(Pageable pageable);

    @Modifying
    @Query("update Member m set m.projectCnt = m.projectCnt + 1 where m.id = :id")
    void incProjectCount(@Param("id") Long memberId);

    @Modifying
    @Query("update Member m set m.projectCnt = m.projectCnt - 1 where m.id = :id")
    void decProjectCount(@Param("id") Long memberId);

}
