package pnu.classplus.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
public class EmbeddedRedisConfig {
    @Value("${spring.redis.port}")
    private int port;

    private RedisServer server;

    @PostConstruct
    public void startRedis() throws IOException {
        server = new RedisServer(port);
        server.start();
    }

    @PreDestroy
    public void stopRedis() {
        server.stop();
    }
}
