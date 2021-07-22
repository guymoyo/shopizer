package com.salesmanager.shop.database;

import com.smattme.MysqlExportService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
@EnableScheduling
public class Export {

    //@Scheduled(cron = "0 0 0 * * * ")
    public void doExport() {

        //required properties for exporting of db
        Properties properties = new Properties();
        properties.setProperty(MysqlExportService.DB_NAME, "database-name");
        properties.setProperty(MysqlExportService.DB_USERNAME, "root");
        properties.setProperty(MysqlExportService.DB_PASSWORD, "root");

        //properties relating to email config
        properties.setProperty(MysqlExportService.EMAIL_HOST, "smtp.mailtrap.io");
        properties.setProperty(MysqlExportService.EMAIL_PORT, "25");
        properties.setProperty(MysqlExportService.EMAIL_USERNAME, "mailtrap-username");
        properties.setProperty(MysqlExportService.EMAIL_PASSWORD, "mailtrap-password");
        properties.setProperty(MysqlExportService.EMAIL_FROM, "test@smattme.com");
        properties.setProperty(MysqlExportService.EMAIL_TO, "backup@smattme.com");

        //set the outputs temp dir
        properties.setProperty(MysqlExportService.TEMP_DIR, new File("external").getPath());

        MysqlExportService mysqlExportService = new MysqlExportService(properties);
        try {
            mysqlExportService.export();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
