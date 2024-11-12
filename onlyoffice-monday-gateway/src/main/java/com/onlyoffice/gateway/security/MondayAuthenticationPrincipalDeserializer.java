package com.onlyoffice.gateway.security;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

public class MondayAuthenticationPrincipalDeserializer
    extends JsonDeserializer<MondayAuthenticationPrincipal> {
  public MondayAuthenticationPrincipal deserialize(
      JsonParser parser, DeserializationContext deserializationContext) throws IOException {
    JsonNode node = parser.getCodec().readTree(parser);
    return MondayAuthenticationPrincipal.builder()
        .userId(node.get("user_id").asInt())
        .accountId(node.get("account_id").asInt())
        .slug(node.get("slug").asText())
        .isAdmin(node.get("is_admin").asBoolean())
        .isViewOnly(node.get("is_view_only").asBoolean())
        .isGuest(node.get("is_guest").asBoolean())
        .build();
  }
}
