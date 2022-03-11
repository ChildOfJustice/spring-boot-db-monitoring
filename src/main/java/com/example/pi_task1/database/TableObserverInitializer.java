package com.example.pi_task1.database;

import com.example.pi_task1.monitoring.tables.TableObserver;
import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@PropertySource({"classpath:myapp.properties"})
public class TableObserverInitializer {

    private static final Logger log = LoggerFactory.getLogger(TableObserverInitializer.class);

    @Value("${database.url}")
    private String url;
    @Value("${database.password}")
    private String password;
    @Value("${database.username}")
    private String userName;

    @Autowired
    TableObserver tableObserver;

    public void initObserver(String tableName, String channelName) throws SQLException {

        SqlCodePart sqlCodePart = new SqlCodePart(
                channelName,
                userName,
                tableName
        );

        PGConnection connection = DriverManager.getConnection(url, userName, password).unwrap(PGConnection.class);
        connection.addNotificationListener(new PGNotificationListener() {

            public void notification(int processId, String channelName, String payload) {
                log.info("Received Notification: " + processId + ", " + channelName + ", " + payload);
                tableObserver.notifySubscribers(payload);
            }

            public void closed() {
                // initiate reconnection & restart listening
            }

        });

        Statement stmt = connection.createStatement();

        stmt.executeUpdate(sqlCodePart.getSqlNotifyFunctionCode());
        stmt.executeUpdate(sqlCodePart.getSqlCreateTriggerCode());

        stmt.executeUpdate("LISTEN " + channelName);
    }
}
