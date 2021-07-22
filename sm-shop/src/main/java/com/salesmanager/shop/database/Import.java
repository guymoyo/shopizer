package com.salesmanager.shop.database;

import com.smattme.MysqlImportService;
import org.junit.Assert;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

@Component
public class Import {

    public void doImport() {


        try {
            String sql = new String(Files.readAllBytes(Paths.get("db.sql")));
            boolean res = MysqlImportService.builder()
                    .setDatabase("database-name")
                    .setSqlString(sql)
                    .setUsername("root")
                    .setPassword("root")
                    .setDeleteExisting(true)
                    .setDropExisting(true)
                    .importDatabase();

            Assert.assertTrue(res);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
