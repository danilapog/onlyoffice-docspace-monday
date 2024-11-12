package com.onlyoffice.user.persistence.repository;

import com.onlyoffice.user.persistence.entity.User;
import com.onlyoffice.user.persistence.entity.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, UserId>, TimeoutUserRepository {
  @Modifying
  @Query("DELETE FROM User u WHERE u.tenantId = :tenantId AND u.updatedAt <= :timestamp")
  void deleteAllByTenantIdAndUpdatedAtLessThanEqual(
      @Param("tenantId") int tenantId, @Param("timestamp") long timestamp);
}
