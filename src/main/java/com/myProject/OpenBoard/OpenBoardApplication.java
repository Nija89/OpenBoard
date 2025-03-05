package com.myProject.OpenBoard;

import com.myProject.OpenBoard.entity.User;
import com.myProject.OpenBoard.service.MainService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Timestamp;

@SpringBootApplication
public class OpenBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenBoardApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner commandLineRunner(MainService mainService){
//		return runnder->{
//			User currentUser = mainService.findUserByName("apple");
//
//			currentUser.setUsername("xxxx");
//			currentUser.setDescription("I am a Apple. I like Apple too.");
//			currentUser.setRoleList(currentUser.getRoleList());
//
//			mainService.updateUser(currentUser);
//
//		};
//	}


}
