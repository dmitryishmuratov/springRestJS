package com.burst.springRestJS.repositories;

import com.burst.springRestJS.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {

    Role findByRole(@Param("role") String role);

    Role getRoleById(@Param("id") Integer id);
}
