package com.onlyoffice.gateway.client;

import com.onlyoffice.common.tenant.transfer.request.command.*;
import com.onlyoffice.common.tenant.transfer.response.BoardInformation;
import com.onlyoffice.common.tenant.transfer.response.TenantCredentials;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class TenantServiceClientFallbackFactory implements FallbackFactory<TenantServiceClient> {
    public TenantServiceClient create(Throwable cause) {
        return new TenantServiceClient() {
            public ResponseEntity<TenantCredentials> createTenant(RegisterTenant command) {
                return ResponseEntity.badRequest().build();
            }

            public ResponseEntity<TenantCredentials> findTenant(long tenantId) {
                return ResponseEntity.badRequest().build();
            }

            public ResponseEntity<BoardInformation> findBoard(long boardId) {
                return ResponseEntity.badRequest().build();
            }

            public ResponseEntity<?> createRoom(RegisterRoom command) {
                return ResponseEntity.badRequest().build();
            }

            public ResponseEntity<?> removeRoom(RemoveRoom command) {
                return ResponseEntity.badRequest().build();
            }

            public ResponseEntity<?> removeTenant(RemoveTenant command) {
                return ResponseEntity.badRequest().build();
            }

            public ResponseEntity<?> updateDocSpace(RegisterDocSpace command) {
                return ResponseEntity.badRequest().build();
            }
        };
    }
}
