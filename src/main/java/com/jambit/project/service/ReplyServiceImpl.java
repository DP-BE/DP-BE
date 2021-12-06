package com.jambit.project.service;

import com.jambit.project.domain.entity.Board;
import com.jambit.project.domain.entity.Member;
import com.jambit.project.domain.entity.Project;
import com.jambit.project.domain.entity.Reply;
import com.jambit.project.domain.repository.BoardRepository;
import com.jambit.project.domain.repository.MemberRepository;
import com.jambit.project.domain.repository.ProjectRepository;
import com.jambit.project.domain.repository.ReplyRepository;
import com.jambit.project.dto.ReplyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public List<ReplyDto> findAllPostRepliesList(Long postId) {
        List<ReplyDto> findReplyList = replyRepository.findAllReplyListByPostId(postId).stream().map(Reply::toDTO).collect(Collectors.toList());
        findReplyList.forEach(reply -> {
            String nickname = reply.getNickname();
            Optional<Member> byNickname = memberRepository.findByNickname(nickname);
            byNickname.ifPresent(member -> reply.setProfileImage(member.getProfileImage()));
        });
        return findReplyList;
    }

    @Transactional
    public List<ReplyDto> findAllProjectRepliesList(Long projectId) {
        List<ReplyDto> projectReplyList = replyRepository.findByProjectId(projectId).stream().map(Reply::toDTO).collect(Collectors.toList());
        projectReplyList.forEach(reply -> {
            String nickname = reply.getNickname();
            Optional<Member> byNickname = memberRepository.findByNickname(nickname);
            byNickname.ifPresent(member -> reply.setProfileImage(member.getProfileImage()));
        });
        return projectReplyList;
    }

    @Transactional
    public String create(ReplyDto replyDto) {
        if (replyDto != null) {
            replyDto.setLikesCount(0L);
            replyDto.setIsDeleted(false);
            Reply reply = ReplyDto.toEntity(replyDto);
            replyRepository.save(reply);
            switch (reply.getTargetType()) {
                case POST:
                    Optional<Board> findPost = boardRepository.findById(reply.getPostId());
                    findPost.ifPresent(b -> b.setReplyCount(b.getReplyCount() + 1L));
                    break;
                case PROJECT:
                    Optional<Project> findProject = projectRepository.findById(reply.getProjectId());
                    findProject.ifPresent(p -> p.setReplyCount(p.getReplyCount() + 1L));
                    break;
                default:
                    break;
            }
            return reply.getNickname();
        }
        return null;
    }

    @Transactional
    public String modify(ReplyDto replyDto) {
        Optional<Reply> findModifyingReply = replyRepository.findById(replyDto.getId());
        if (findModifyingReply.isPresent()) {
            Reply findReply = findModifyingReply.get();
            findReply.setContent(replyDto.getContent());
            replyRepository.save(findReply);
            return findReply.getNickname();
        }
        return null;
    }

    @Transactional
    public boolean deleteReply(Long reply_id) {
        Optional<Reply> deleteReply = replyRepository.findById(reply_id);
        if (deleteReply.isPresent()) {
            Reply reply = deleteReply.get();
            if (reply.getIsDeleted()) {
                return false;
            }
            reply.setIsDeleted(true);
            return true;
        }
        return false;
    }

}
