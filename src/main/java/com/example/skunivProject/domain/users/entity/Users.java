package com.example.skunivProject.domain.users.entity;

import com.example.skunivProject.domain.users.enums.Gender;
import com.example.skunivProject.domain.users.enums.Rank;
import com.example.skunivProject.global.baseentity.BaseIdEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "users")
public class Users extends BaseIdEntity implements UserDetails {

    @Column(nullable = false, unique = true)
    private String username; // 로그인용 ID

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate birth;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "users_rank", nullable = false)
    private Rank rank = Rank.SEED;

    @Builder.Default
    @Column(nullable = false)
    private int point = 0;

    @Column(nullable = false)
    private String email;

    /**
     * 포인트를 추가하고, 100점이 넘으면 랭크업을 처리하는 메소드
     * @param points 추가할 포인트
     */
    public void addPointsAndRankUp(int points) {
        this.point += points;
        if (this.point >= 100) {
            if (this.rank == Rank.SEED) {
                this.rank = Rank.FLOWER;
                this.point -= 100; // 100 포인트를 랭크업에 사용하고 차감
            } else if (this.rank == Rank.FLOWER) {
                this.rank = Rank.TREE;
                this.point -= 100;
            }
            // TREE 등급은 최고 등급이므로 더 이상 랭크업하지 않음
        }
    }

    // ================= Spring Security =================
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
