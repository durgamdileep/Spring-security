package com.dileep.BasicAuthenticationAndAuthorization.Repository;

import com.dileep.BasicAuthenticationAndAuthorization.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
    Optional<UserInfo> findByUsername(String username);
}
