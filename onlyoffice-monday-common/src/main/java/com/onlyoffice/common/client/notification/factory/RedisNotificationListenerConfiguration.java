package com.onlyoffice.common.client.notification.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@AutoConfiguration
@AutoConfigureAfter(RedisNotificationPublisherFactory.class)
@ConditionalOnBean({RedisConnectionFactory.class, NotificationProcessor.class})
public class RedisNotificationListenerConfiguration {
  @Value("${redis.notification.channel:notifications}")
  private String channel;

  @Bean
  public RedisMessageListenerContainer listenerContainer(
      RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
    var container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(listenerAdapter, new PatternTopic(channel));
    return container;
  }

  @Bean
  public MessageListenerAdapter listenerAdapter(NotificationProcessor processor) {
    return new MessageListenerAdapter(processor, "onMessage");
  }
}
