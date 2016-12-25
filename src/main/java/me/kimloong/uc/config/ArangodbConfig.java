package me.kimloong.uc.config;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Arangodb数据库配置
 *
 * @author KimLoong
 */
@Configuration
public class ArangodbConfig {

    @Bean
    public Queue<ArangoDatabase> arangoDBs() {
        Queue<ArangoDatabase> queue = new ArrayBlockingQueue<>(20);
        ArangoDatabase database = new ArangoDB.Builder()
                .user("root").password("root").build().db("user-center");
        queue.add(database);
        return queue;
    }
}
