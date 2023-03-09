package com.intellisoft.mailserver;

import com.sendgrid.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class MailServerApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(MailServerApplication.class, args);

    }

}
