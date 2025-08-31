package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.CargoEntity;

public interface CargoRepository extends JpaRepository<CargoEntity, Integer> {
}
