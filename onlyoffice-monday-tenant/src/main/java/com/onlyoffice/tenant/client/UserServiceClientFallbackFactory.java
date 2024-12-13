package com.onlyoffice.tenant.client;

import com.onlyoffice.common.user.transfer.response.DocSpaceUsers;
import com.onlyoffice.tenant.exception.UserServiceException;
import java.util.Set;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserServiceClientFallbackFactory implements FallbackFactory<UserServiceClient> {
  public UserServiceClient create(Throwable cause) {
    return new UserServiceClient() {
      public ResponseEntity<DocSpaceUsers> findDocSpaceUsers(long tenantId, Set<String> ids) {
        throw new UserServiceException("Could not find DocSpace users", cause);
      }

      public ResponseEntity<DocSpaceUsers> findDocSpaceUsers(
          long tenantId, Set<String> ids, int timeout) {
        throw new UserServiceException("Could not find DocSpace users with timeout", cause);
      }
    };
  }
}
