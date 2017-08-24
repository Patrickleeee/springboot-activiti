import com.patrick.example.dao.CompRepository;
import com.patrick.example.dao.PersonRepository;
import com.patrick.example.model.Comp;
import com.patrick.example.model.Person;
import com.patrick.example.service.ActivitiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Desciption main app
 * Create By  li.bo
 * CreateTime 2017/8/21 17:42
 * UpdateTime 2017/8/21 17:42
 */

@SpringBootApplication
@ComponentScan("com.patrick.example")
@EnableJpaRepositories("com.patrick.example.dao")
@EntityScan("com.patrick.example.model")
@EnableScheduling
public class ActivitiApplication {
	@Autowired
	private CompRepository compRepository;
	@Autowired
	private PersonRepository personRepository;
	
	
	public static void main(String[] args) {
		SpringApplication.run(ActivitiApplication.class, args);
	}
	
	//初始化模拟数据
	@Bean
	public CommandLineRunner init(final ActivitiService myService) {
		return new CommandLineRunner() {
			public void run(String... strings) throws Exception {
				if (personRepository.findAll().size() == 0) {
					personRepository.save(new Person("wtr"));
					personRepository.save(new Person("wyf"));
					personRepository.save(new Person("admin"));
				}
				if (compRepository.findAll().size() == 0) {
					Comp group = new Comp("great company");
					compRepository.save(group);
					Person admin = personRepository.findByPersonName("admin");
					Person wtr = personRepository.findByPersonName("wtr");
					admin.setComp(group); wtr.setComp(group);
					personRepository.save(admin); personRepository.save(wtr);
				}
			}
		};
	}
}