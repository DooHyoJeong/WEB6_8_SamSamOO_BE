package com.ai.lawyer.domain.post.repository;

import com.ai.lawyer.domain.member.entity.Member;
import com.ai.lawyer.domain.post.entity.Post;
import com.ai.lawyer.domain.poll.entity.Poll.PollStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByMember(Member member);
    Page<Post> findByPoll_Status(PollStatus status, Pageable pageable);
    Page<Post> findByPoll_StatusAndPoll_PollIdIn(PollStatus status, List<Long> pollIds, Pageable pageable);
    Page<Post> findByPoll_PollIdIn(List<Long> pollIds, Pageable pageable);
}