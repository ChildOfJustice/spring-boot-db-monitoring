package com.example.pi_task1;

import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
@EnableScheduling
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class PiTask1Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PiTask1Application.class);

//    @Value("${spring.datasource.url}")
//    static String url;

    public static void main(String args[]) throws SQLException {
        SpringApplication.run(PiTask1Application.class, args);

//        String url = "jdbc:postgresql://localhost:5432/student";
////        String url = "jdbc:postgresql://localhost:5432/student?user=test-postgres&password=test-postgres";
//        String user = "test-postgres";
//        String password = "test-postgres";
//        Connection connection = DriverManager.getConnection(url, user, password);




//        PGConnection connection = DriverManager.getConnection(url, user, password).unwrap(PGConnection.class);
//        connection.addNotificationListener(new PGNotificationListener() {
//
//            public void notification(int processId, String channelName, String payload) {
//                System.err.println("Received Notification: " + processId + ", " + channelName + ", " + payload);
//            }
//
//            public void closed() {
//                // initiate reconnection & restart listening
//            }
//
//        });
//
//
//        Statement stmt = connection.createStatement();
//        stmt.executeUpdate("LISTEN msgs");
//
//        stmt.executeUpdate("NOTIFY msgs");
    }

    @Override
    public void run(String... strings) throws Exception {

        String url = "jdbc:pgsql://localhost:5432/student";
//        String url = "jdbc:postgresql://localhost:5432/student?user=test-postgres&password=test-postgres";
        String user = "test-postgres";
        String password = "test-postgres";

        log.info("Starting the database observer...");

//        String url = "jdbc:pgsql://localhost/test";
//        String user = "test";
//        String password = "test";
//        Connection connection = DriverManager.getConnection(url, user, password);



        PGConnection connection = DriverManager.getConnection(url, user, password).unwrap(PGConnection.class);
        connection.addNotificationListener(new PGNotificationListener() {

            public void notification(int processId, String channelName, String payload) {
                System.err.println("Received Notification: " + processId + ", " + channelName + ", " + payload);
            }

            public void closed() {
                // initiate reconnection & restart listening
            }

        });

        String sqlTriggerFunction =
                "-- FUNCTION: public.table_update_notify()\n" +
                " \n" +
                "DROP FUNCTION IF EXISTS public.table_update_notify();\n" +
                " \n" +
                "CREATE FUNCTION public.table_update_notify()\n" +
                "    RETURNS trigger\n" +
                "    LANGUAGE 'plpgsql'\n" +
                "    COST 100\n" +
                "    VOLATILE NOT LEAKPROOF\n" +
                "AS $BODY$\n" +
                "DECLARE\n" +
                "  id bigint;" +
                "BEGIN\n" +
                "  IF TG_OP = 'INSERT' OR TG_OP = 'UPDATE' THEN\n" +
                "    id = NEW.id;\n" +
                "  ELSE\n" +
                "    id = OLD.id;\n" +
                "  END IF;\n" +
                "  PERFORM pg_notify('student_channel', json_build_object('table', TG_TABLE_NAME, 'id', id, 'type', TG_OP)::text);\n" +
                "  RETURN NEW;\n" +
                "END;\n" +
                " \n" +
                "$BODY$;\n" +
                " \n" +
                "ALTER FUNCTION public.table_update_notify()\n" +
                "    OWNER TO \"test-postgres\";";

        String sqlCreateTrigger = "" +
                "-- Trigger: student_notify_update\n" +
                " \n" +
                "DROP TRIGGER IF EXISTS student_notify_update ON student;\n" +
                " \n" +
                "CREATE TRIGGER student_notify_update\n" +
                "    BEFORE INSERT OR DELETE OR UPDATE\n" +
                "    ON student\n" +
                "    FOR EACH ROW\n" +
                "    EXECUTE PROCEDURE public.table_update_notify();";

        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sqlTriggerFunction);
        stmt.executeUpdate(sqlCreateTrigger);


        stmt.executeUpdate("LISTEN student_channel");


        String sqlCreateStudent = "" +
                "insert into student (id, name, courses)\n" +
                "values(1, 'TestStudent', 'other');";
        stmt.executeUpdate(sqlCreateStudent);



//        stmt.executeUpdate("NOTIFY msgs");
    }

}
