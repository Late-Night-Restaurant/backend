package com.backend.simya.domain.profile.repository;

import com.backend.simya.domain.profile.entity.Profile;
import com.backend.simya.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findProfilesByUser(User user);

}
