package com.fundamentosplatzi.springboot.fundamentos;

import com.fundamentosplatzi.springboot.fundamentos.bean.MyBean;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentosplatzi.springboot.fundamentos.component.ComponentDependency;
import com.fundamentosplatzi.springboot.fundamentos.entity.User;
import com.fundamentosplatzi.springboot.fundamentos.pojo.UserPojo;
import com.fundamentosplatzi.springboot.fundamentos.repository.UserRepository;
import com.fundamentosplatzi.springboot.fundamentos.service.UserService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

	private final Log LOGGER = LogFactory.getLog(FundamentosApplication.class);

	private ComponentDependency componentDependency;
	private MyBean myBean;

	private MyBeanWithDependency myBeanWithDependency;
	private MyBeanWithProperties myBeanWithProperties;
	private UserPojo userPojo;
	private UserRepository userRepository;
	private UserService userService;

	public FundamentosApplication(@Qualifier("componentTwoImplement") ComponentDependency componentDependency,
								  MyBean myBean,
								  MyBeanWithDependency myBeanWithDependency,
								  MyBeanWithProperties myBeanWithProperties,
								  UserPojo userPojo,
								  UserRepository userRepository,
								  UserService userService
								  ){
		this.componentDependency = componentDependency;
		this.myBean = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}
	@Override
	public void run(String... args) {
		//ejemplosAnteriores();
		saveUsersInDataBase();
		getInformationJpqlFromUser();
		saveWithErrorTransactional();
	}

	public void saveWithErrorTransactional(){
		User test1 = new User("test1Transactional1", "test1Transactional1@domain.com", LocalDate.now());
		User test2 = new User("test2Transactional2", "test2Transactional2@domain.com", LocalDate.now());
		User test3 = new User("test3Transactional3", "test1Transactional1@domain.com", LocalDate.now());
		User test4 = new User("test4Transactional4", "test4Transactional4@domain.com", LocalDate.now());

		List<User> users = Arrays.asList(test1, test2, test3, test4);

		try{
			userService.saveTransactional(users);
		}catch (Exception e){
			LOGGER.error("Esta es una exception dentro del metodo transactional "+ e);
		}

		userService.getAllUsers().stream()
				.forEach(user -> LOGGER.info("Este es el usuario dentro del metodo transaccional " + user));
	}

	private void getInformationJpqlFromUser(){
		/*LOGGER.info("Usuario con el metodo userRepository.findByUserEmail " +
				userRepository.findByUserEmail("julie@domain.com")
						.orElseThrow(()-> new RuntimeException("No se encontro el usuario")));

		userRepository.findAndSort("user", Sort.by("id").ascending())
				.stream()
				.forEach(user -> LOGGER.info("User with method sort "+ user));

		userRepository.findByName("John")
				.stream()
				.forEach(user -> LOGGER.info("Usuario with query method"+ user));

		LOGGER.info("Usuario con query method findByEmailAndName" +
		userRepository.findByEmailAndName("daniela@domain.com", "Daniela" )
				.orElseThrow(()-> new RuntimeException("Usuario no encontrado")));

		userRepository.findByNameLike("%user%")
				.stream()
				.forEach(user -> LOGGER.info("Usuario findByNameLike"+ user));

		userRepository.findByNameOrEmail(null, "user")
				.stream()
				.forEach(user -> LOGGER.info("Usuario findByNameOrEmail"+ user));*/

		userRepository.findByBirthDateBetween(LocalDate.of(2021,3,1), LocalDate.of(2021,4,25))
				.stream()
				.forEach(user -> LOGGER.info("Usuario con intervalo de fechas"+ user));

		userRepository.findByNameLikeOrderByIdAsc("%user%")
				.stream()
				.forEach(user -> LOGGER.info("Usuario encontrado con like y ordenado "+ user));

		userRepository.findByNameContainingOrderByIdAsc("user")
				.stream()
				.forEach(user -> LOGGER.info("Usuario encontrado con containing y ordenado "+ user));

		LOGGER.info("El usuario a partir del named parameter es: " +
		userRepository.getAllByBirthDateAndMail(LocalDate.of(2021, 9, 8),
				"daniela@domain.com")
				.orElseThrow(()-> new RuntimeException("Usuario no encontrado apartir del named parameter")));



	}

	public void saveUsersInDataBase(){
		User user1 = new User("John", "john@domain.com", LocalDate.of(2021, 8, 03));
		User user2 = new User("Julie", "julie@domain.com", LocalDate.of(2021, 5, 21));
		User user3 = new User("Daniela", "daniela@domain.com", LocalDate.of(2021, 9, 8));
		User user4 = new User("user4", "user4@domain.com", LocalDate.of(2021, 6, 18));
		User user5 = new User("user5", "user5@domain.com", LocalDate.of(2021, 1, 1));
		User user6 = new User("user6", "user6@domain.com", LocalDate.of(2021, 7, 7));
		User user7 = new User("Enrique", "enrique@domain.com", LocalDate.of(2021, 11, 12));
		User user8 = new User("Luis", "luis@domain.com", LocalDate.of(2021, 2, 27));
		User user9 = new User("Paola", "paola@domain.com", LocalDate.of(2021, 4, 10));
		List<User> list = Arrays.asList(user1, user2, user3, user4, user5, user6, user7, user8, user9);
		//recorre la lista de usuarios y realiza un registro en la base de datos
		list.stream().forEach(userRepository::save);
	}

	private void ejemplosAnteriores() {
		componentDependency.saludar();
		myBean.print();
		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithProperties.function());
		System.out.println(userPojo.getEmail() + "-"+ userPojo.getPassword());
		try{
			//error
			int value = 10/0;
			LOGGER.info("Mi valor :" + value);
		}catch (Exception e){
			LOGGER.error("Esto es un error al dividir por cero"+ e.getMessage());
		}
	}
}
