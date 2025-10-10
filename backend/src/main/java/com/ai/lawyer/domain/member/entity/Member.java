package com.ai.lawyer.domain.member.entity;

import com.ai.lawyer.domain.post.entity.Post;
import com.ai.lawyer.domain.poll.entity.PollVote;
import com.ai.lawyer.domain.chatbot.entity.History;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member",
        indexes = {
                @Index(name = "idx_member_login_id", columnList = "login_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "password")
@EqualsAndHashCode(of = "memberId")
public class Member implements MemberAdapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "login_id", nullable = false, unique = true, length = 100)
    @Email(message = "올바른 이메일 형식이 아닙니다")
    @NotBlank(message = "이메일(로그인 ID)은 필수입니다")
    private String loginId;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;

    @Column(name = "age", nullable = false)
    @NotNull(message = "나이는 필수입니다")
    @Min(value = 14, message = "최소 14세 이상이어야 합니다")
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 10)
    @NotNull(message = "성별은 필수입니다")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    @Builder.Default
    private Role role = Role.USER;

    @Column(name = "name", nullable = false, length = 20)
    @NotBlank(message = "이름은 필수입니다")
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 연관 관계: 회원 탈퇴 시 cascade 삭제
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PollVote> pollVotes = new ArrayList<>();

    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<History> histories = new ArrayList<>();

    @Getter
    public enum Gender {
        MALE("남성"), FEMALE("여성"), OTHER("기타");
        private final String description;
        Gender(String description) { this.description = description; }
    }

    @Getter
    public enum Role {
        USER("일반사용자"), ADMIN("관리자");
        private final String description;
        Role(String description) { this.description = description; }
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}