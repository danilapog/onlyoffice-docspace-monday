package com.onlyoffice.gateway.client;

import com.onlyoffice.common.user.transfer.request.command.RegisterUser;
import com.onlyoffice.common.user.transfer.response.DocSpaceUsers;
import com.onlyoffice.common.user.transfer.response.UserCredentials;
import java.util.Set;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserServiceClientFallbackFactory implements FallbackFactory<UserServiceClient> {
  public UserServiceClient create(Throwable cause) {
    return new UserServiceClient() {
      public ResponseEntity<UserCredentials> findUser(long tenantId, long mondayId) {
        return ResponseEntity.badRequest().build();
      }

      public ResponseEntity<UserCredentials> findUser(long tenantId, long mondayId, int timeout) {
        return ResponseEntity.badRequest().build();
      }

      public ResponseEntity<DocSpaceUsers> findDocSpaceUsers(long tenantId, Set<Long> ids) {
        return ResponseEntity.badRequest().build();
      }

      public ResponseEntity<DocSpaceUsers> findDocSpaceUsers(
          long tenantId, Set<Long> ids, int timeout) {
        return ResponseEntity.badRequest().build();
      }

      public ResponseEntity<?> registerUser(RegisterUser command) {
        return ResponseEntity.badRequest().build();
      }
    };
  }
}
