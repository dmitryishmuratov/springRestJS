package com.burst.springRestJS.repository;

import com.burst.springRestJS.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface UserRepo extends JpaRepository<User, Integer> {

    @Override
    @Query("from User u left join fetch u.roles order by u.id")
    List<User> findAll();

    @Query("select  u from User u left join fetch u.roles where u.id = :id")
    User getById(@Param("id") Integer id);

    @Query("select u from User u left join fetch u.roles where u.username = :username")
    User findByUsername(@Param("username") String username);
}
