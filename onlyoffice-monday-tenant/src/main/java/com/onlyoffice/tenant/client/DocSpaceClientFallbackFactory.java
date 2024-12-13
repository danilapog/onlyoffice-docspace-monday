package com.onlyoffice.tenant.client;

import com.onlyoffice.common.docspace.transfer.request.command.AuthenticateUser;
import com.onlyoffice.common.docspace.transfer.request.command.ChangeRoomAccess;
import com.onlyoffice.common.docspace.transfer.response.GenericResponse;
import com.onlyoffice.common.docspace.transfer.response.MembersAccess;
import com.onlyoffice.common.docspace.transfer.response.RoomLink;
import com.onlyoffice.common.docspace.transfer.response.UserToken;
import com.onlyoffice.tenant.exception.DocSpaceServiceException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

@Component
public class DocSpaceClientFallbackFactory implements FallbackFactory<DocSpaceClient> {
    public DocSpaceClient create(Throwable cause) {
        return new DocSpaceClient() {
            public GenericResponse<UserToken> generateToken(URI baseUri, AuthenticateUser command) {
                throw new DocSpaceServiceException("Could not generate authentication token", cause);
            }

            public GenericResponse<List<RoomLink>> generateSharedKey(URI baseUri, long roomId, String token) {
                throw new DocSpaceServiceException("Could not generate shared key for room " + roomId, cause);
            }

            public GenericResponse<MembersAccess> changeRoomAccess(URI baseUri, long roomId, String token, ChangeRoomAccess command) {
                throw new DocSpaceServiceException("Could not change room access for room " + roomId, cause);
            }
        };
    }
}
