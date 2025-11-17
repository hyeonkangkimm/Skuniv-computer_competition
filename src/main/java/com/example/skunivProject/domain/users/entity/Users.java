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

    // 추가 컬럼
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

    public void addPoint(int points) {
        this.point += points;
    }

    // ================= Spring Security =================
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 모든 사용자는 "ROLE_USER" 권한을 가집니다.
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
