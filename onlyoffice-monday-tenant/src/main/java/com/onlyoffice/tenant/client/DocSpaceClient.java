package com.onlyoffice.tenant.client;

import com.onlyoffice.common.docspace.transfer.request.command.AuthenticateUser;
import com.onlyoffice.common.docspace.transfer.request.command.ChangeRoomAccess;
import com.onlyoffice.common.docspace.transfer.response.GenericResponse;
import com.onlyoffice.common.docspace.transfer.response.MembersAccess;
import com.onlyoffice.common.docspace.transfer.response.RoomLink;
import com.onlyoffice.common.docspace.transfer.response.UserToken;
import com.onlyoffice.tenant.exception.DocSpaceServiceException;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.net.URI;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;

// TODO: Distributed caching in v2?
@FeignClient(name = "docSpaceClient")
public interface DocSpaceClient {
  @Cacheable(value = "userTokens", key = "#command.userName")
  @RequestLine("POST /api/2.0/authentication")
  @Headers("Content-Type: application/json")
  @Retry(name = "docSpaceClientRetry")
  @CircuitBreaker(name = "docSpaceClientCircuitBreaker", fallbackMethod = "generateTokenFallback")
  GenericResponse<UserToken> generateToken(URI baseUri, AuthenticateUser command);

  @Cacheable(value = "roomLinks", key = "#roomId")
  @RequestLine("GET /api/2.0/files/rooms/{roomId}/links?type=1")
  @Headers({"Authorization: {token}", "Content-Type: application/json"})
  @Retry(name = "docSpaceClientRetry")
  @CircuitBreaker(
      name = "docSpaceClientCircuitBreaker",
      fallbackMethod = "generateSharedKeyFallback")
  GenericResponse<List<RoomLink>> generateSharedKey(
      URI baseUri, @Param("roomId") long roomId, @Param("token") String token);

  @RequestLine("PUT /api/2.0/files/rooms/{roomId}/share")
  @Headers({"Authorization: {token}", "Content-Type: application/json"})
  @Retry(name = "docSpaceClientRetry")
  @CircuitBreaker(
      name = "docSpaceClientCircuitBreaker",
      fallbackMethod = "changeRoomAccessFallback")
  GenericResponse<MembersAccess> changeRoomAccess(
      URI baseUri,
      @Param("roomId") long roomId,
      @Param("token") String token,
      ChangeRoomAccess command);

  default GenericResponse<UserToken> generateTokenFallback(
      URI baseUri, AuthenticateUser command, Exception ex) {
    throw new DocSpaceServiceException("Could not generate authentication token", ex);
  }

  default GenericResponse<List<RoomLink>> generateSharedKeyFallback(
      URI baseUri, long roomId, String token, Exception ex) {
    throw new DocSpaceServiceException("Could not generate shared key for room " + roomId, ex);
  }

  default GenericResponse<MembersAccess> changeRoomAccessFallback(
      URI baseUri, long roomId, String token, ChangeRoomAccess command, Exception ex) {
    throw new DocSpaceServiceException("Could not change room access for room " + roomId, ex);
  }
}
