//package com.ai.lawyer.global.init;
//
//import com.ai.lawyer.domain.member.entity.Member;
//import com.ai.lawyer.domain.member.entity.Member.Gender;
//import com.ai.lawyer.domain.member.repositories.MemberRepository;
//import com.ai.lawyer.domain.poll.dto.PollForPostDto;
//import com.ai.lawyer.domain.post.entity.Post;
//import com.ai.lawyer.domain.post.repository.PostRepository;
//import com.ai.lawyer.domain.poll.entity.Poll;
//import com.ai.lawyer.domain.poll.entity.PollOptions;
//import com.ai.lawyer.domain.poll.entity.PollVote;
//import com.ai.lawyer.domain.poll.repository.PollOptionsRepository;
//import com.ai.lawyer.domain.poll.repository.PollRepository;
//import com.ai.lawyer.domain.poll.repository.PollVoteRepository;
//import com.ai.lawyer.domain.post.dto.PostRequestDto;
//import com.ai.lawyer.domain.poll.dto.PollOptionCreateDto;
//import com.ai.lawyer.domain.poll.dto.PollCreateDto;
//import com.ai.lawyer.domain.post.dto.PostWithPollCreateDto;
//import com.ai.lawyer.domain.post.dto.PostDetailDto;
//import com.ai.lawyer.domain.post.service.PostService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.Set;
//
//@Component
//public class DummyData implements CommandLineRunner {
//    private final MemberRepository memberRepository;
//    private final PostRepository postRepository;
//    private final PollRepository pollRepository;
//    private final PollOptionsRepository pollOptionsRepository;
//    private final PollVoteRepository pollVoteRepository;
//    private final PostService postService;
//
//    @Autowired
//    public DummyData(MemberRepository memberRepository, PostRepository postRepository,
//                     PollRepository pollRepository, PollOptionsRepository pollOptionsRepository,
//                     PollVoteRepository pollVoteRepository, PostService postService) {
//        this.memberRepository = memberRepository;
//        this.postRepository = postRepository;
//        this.pollRepository = pollRepository;
//        this.pollOptionsRepository = pollOptionsRepository;
//        this.pollVoteRepository = pollVoteRepository;
//        this.postService = postService;
//    }
//
//    @Override
//    public void run(String... args) {
//        if (memberRepository.existsByLoginId("dummy1@test.com")) {
//            System.out.println("더미 데이터가 이미 존재합니다. 전체 더미 생성 로직을 건너뜁니다.");
//            return;
//        }
//
//        if (true) {
//            List<String> allLoginIds = new ArrayList<>();
//            for (int i = 1; i <= 1000; i++) {
//                allLoginIds.add("dummy" + i + "@test.com");
//            }
//            List<Member> existingMembers = memberRepository.findByLoginIdIn(allLoginIds);
//            Set<String> existingLoginIds = new java.util.HashSet<>();
//            for (Member m : existingMembers) {
//                existingLoginIds.add(m.getLoginId());
//            }
//            List<Member> membersToSave = new ArrayList<>();
//            Random random = new Random();
//            for (int i = 1; i <= 1000; i++) {
//                String loginId = "dummy" + i + "@test.com";
//                if (!existingLoginIds.contains(loginId)) {
//                    int age = 14 + random.nextInt(67); // 14~60
//                    Gender gender = (i % 2 == 0) ? Gender.MALE : Gender.FEMALE;
//                    Member member = Member.builder()
//                            .loginId(loginId)
//                            .password("password")
//                            .age(age)
//                            .gender(gender)
//                            .name("투표자" + i)
//                            .build();
//                    membersToSave.add(member);
//                }
//            }
//            if (!membersToSave.isEmpty()) {
//                memberRepository.saveAll(membersToSave);
//            }
//            System.out.println("더미 멤버 생성 완료");
//        } else {
//            System.out.println("더미 멤버가 이미 존재합니다. 멤버 생성은 건너뜁니다.");
//        }
//
//        // 게시글 중복 체크 및 생성
//        if (!postRepository.existsByPostName("바로게시글 1")) {
//            List<Member> members = memberRepository.findAll();
//            Random random = new Random();
//            for (int i = 1; i <= 3; i++) {
//                PostRequestDto postDto = PostRequestDto.builder()
//                        .postName("바로게시글 " + i)
//                        .postContent("바로바로내용" + i)
//                        .category("카테고리" + i)
//                        .build();
//                List<PollOptionCreateDto> pollOptionsDtos = new ArrayList<>();
//                for (int j = 1; j <= 2; j++) {
//                    pollOptionsDtos.add(PollOptionCreateDto.builder()
//                            .content("바로투표 " + j)
//                            .build());
//                }
//                PollForPostDto pollDto = PollForPostDto.builder()
//                        .voteTitle("더미 투표 " + i)
//                        .reservedCloseAt(null)
//                        .pollOptions(pollOptionsDtos)
//                        .build();
//                PostWithPollCreateDto createDto = PostWithPollCreateDto.builder()
//                        .post(postDto)
//                        .poll(pollDto)
//                        .build();
//                Member dummyMember1 = memberRepository.findByLoginId("dummy1@test.com").orElseThrow();
//                Long memberId = dummyMember1.getMemberId();
//                PostDetailDto postDetail = postService.createPostWithPoll(createDto, memberId);
//                Long postId = postDetail.getPost().getPostId();
//                Post post = postRepository.findById(postId).orElseThrow();
//                Poll poll = post.getPoll();
//                List<PollOptions> pollOptionsList = pollOptionsRepository.findByPoll_PollId(poll.getPollId());
//                List<Long> votedMemberIds = pollVoteRepository.findMemberIdsByPoll(poll);
//                List<PollVote> votesToSave = new ArrayList<>();
//                for (Member member : members) {
//                    if (!votedMemberIds.contains(member.getMemberId())) {
//                        PollOptions selectedOption = pollOptionsList.get(random.nextInt(pollOptionsList.size()));
//                        PollVote pollVote = PollVote.builder()
//                                .poll(poll)
//                                .member(member)
//                                .pollOptions(selectedOption)
//                                .build();
//                        votesToSave.add(pollVote);
//                    }
//                }
//                if (!votesToSave.isEmpty()) {
//                    pollVoteRepository.saveAll(votesToSave);
//                }
//            }
//            System.out.println("더미 게시글/투표/투표자 생성 완료");
//        } else {
//            List<Member> members = memberRepository.findAll();
//            Random random = new Random();
//            for (int i = 1; i <= 3; i++) {
//                List<Post> posts = postRepository.findByPostName("바로게시글 " + i);
//                Post post = posts.isEmpty() ? null : posts.get(0);
//                Poll poll = post != null ? post.getPoll() : null;
//                List<PollOptions> pollOptionsList = poll != null ? pollOptionsRepository.findByPoll_PollId(poll.getPollId()) : new ArrayList<>();
//                for (Member member : members) {
//                    PollOptions selectedOption = pollOptionsList.isEmpty() ? null : pollOptionsList.get(random.nextInt(pollOptionsList.size()));
//                    if (poll != null && selectedOption != null && !pollVoteRepository.existsByPollAndMember(poll, member)) {
//                        PollVote pollVote = PollVote.builder()
//                                .poll(poll)
//                                .member(member)
//                                .pollOptions(selectedOption)
//                                .build();
//                        pollVoteRepository.save(pollVote);
//                    }
//                }
//            }
//            System.out.println("기존 더미 게시글 1~3에 모든 멤버 투표 추가 완료");
//        }
//    }
//}
