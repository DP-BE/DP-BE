package com.jambit.project.service;

import com.jambit.project.domain.entity.Follow;
import com.jambit.project.domain.entity.Member;
import com.jambit.project.domain.repository.FollowRepository;
import com.jambit.project.domain.repository.MemberRepository;
import com.jambit.project.dto.FollowDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    /*
    나를 팔로우 하는 사람(Follower)
     */
    @Transactional
    public List<FollowDto> findFollowerListByUserId(String userId) {
        List<FollowDto> followList = followRepository.findByFollowee(userId).stream().map(Follow::toDto).collect(Collectors.toList());
        followList.forEach(follow -> {
            Optional<Member> findMember = memberRepository.findByNickname(follow.getNickname());
            findMember.ifPresent(member -> follow.setProfileImage(member.getProfileImage()));
        });
        return followList;
    }

    @Transactional
    public List<FollowDto> findFollowingListByUserId(String userId) {
        List<FollowDto> followList = followRepository.findByNickname(userId).stream().map(Follow::toDto).collect(Collectors.toList());
        followList.forEach(follow -> {
            Optional<Member> findMember = memberRepository.findByNickname(follow.getFollowee());
            findMember.ifPresent(member -> follow.setProfileImage(member.getProfileImage()));
        });
        return followList;
    }

    @Transactional
    public Boolean checkIsFollow(String nickname, String name) {
        Optional<Follow> findFollow = followRepository.findByNicknameAndFollowee(nickname, name);
        return findFollow.isPresent();
    }

    /*
    내가 팔로우 하는 사람(Following)
     */
    @Transactional
    public Integer countFollowingListByUserId(String userId) {
        return followRepository.findByNickname(userId).size();
    }

    @Transactional
    public Integer countFollowerListByUserId(String userId) {
        return followRepository.findByFollowee(userId).size();
    }

    @Transactional
    public Long create(FollowDto followDto) {
        if (followDto != null) {
            Follow follow = FollowDto.toEntity(followDto);
            followRepository.save(follow);
            return follow.getId();
        }
        return null;
    }

    @Transactional
    public void delete(Long follow_id){
        Optional<Follow> findFollow = followRepository.findById(follow_id);
        findFollow.ifPresent(followRepository::delete);
    }
}
