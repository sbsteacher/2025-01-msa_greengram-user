package com.green.greengram.configuration;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShutdownHandler {
    private final DataSource dataSource;

    /*
    HikariCP 같은 DataSource Bean은 Spring 종료 시 자동으로 close 되지만, 혹시 모를 리소스를 위해
    서버가 종료 전 딱 한번 실행하여 커넥션 닫기
     */
    @PreDestroy
    public void onShutdown() {
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
            log.info("✅ HikariCP connection pool closed gracefully");
        }
    }
}
