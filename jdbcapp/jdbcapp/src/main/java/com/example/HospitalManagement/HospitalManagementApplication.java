package com.example.HospitalManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HospitalManagementApplication {

    
	public static void main(String[] args) {
		ApplicationContext context =  SpringApplication.run(HospitalManagementApplication.class, args);

        System.out.println("ok");

        //Student s = context.getBean(Student.class);

        //s.setsId(1);
        //s.setName("AAA");
        //s.setRoll(100);


        //System.out.println(Student.);
	}



}
