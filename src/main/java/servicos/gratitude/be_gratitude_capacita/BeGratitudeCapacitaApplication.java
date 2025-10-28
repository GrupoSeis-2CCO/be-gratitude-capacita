package servicos.gratitude.be_gratitude_capacita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.annotation.PostConstruct;

import java.util.TimeZone;

@SpringBootApplication
public class BeGratitudeCapacitaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeGratitudeCapacitaApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// Define timezone padrão da JVM para America/Sao_Paulo (Horário de Brasília)
		TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
	}

}
// mateiralaluno matricula questao
//..
