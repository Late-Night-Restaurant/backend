package com.backend.simya.domain.user.entity;

import com.backend.simya.domain.profile.entity.Profile;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity    // @Entity 어노테이션: 자동으로 JPA 연동
@Table(name = "`user`")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimeEntity implements UserDetails {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type")
    private LoginType loginType;

    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "pw", length = 100)
    private String pw;

    @Column(name = "activated")
    private boolean activated;

    // 연관관계 매핑
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Profile> profileList = new ArrayList<>();


    //== Spring Security 사용자 인증 필드 ==//

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> {
            return getRole().value();
        });
        return authorities;
    }

    @Override
    public String getPassword() {
//        return null;
        return getPw();
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    /**
     * 계정 만료 여부
     * true : 만료 X
     * false : 만료 O
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠김 여부
     * true : 잠기지 않음
     * false : 잠김
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 비밀번호 만료 여부
     * true : 만료 안됨
     * false : 만료
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 사용자 활성화 여부
     * true : 활성화
     * false :
     * @return
     */
    @Override
    public boolean isEnabled() {
        return this.activated;
    }

    //== 연관관계 매핑 메서드 ==//
    public void addProfile(Profile profile) {
        profileList.add(profile);
        profile.setUserProfile(this);
    }

    public int getMainProfile() {
        for (int i=0; i<profileList.size(); i++) {
            if (profileList.get(i).isRepresent() && profileList.get(i).isActivated()) {
                return i;
            }
        }
        return -1;
    }

    /*public User toEntity() {
        return User.builder()
                .
    }*/
}