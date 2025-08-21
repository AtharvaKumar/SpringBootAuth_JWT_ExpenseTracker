package com.mavenprojectdemo.repository;

import com.mavenprojectdemo.entities.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer>
{
    Optional<RefreshToken> findByToken(String token);

}