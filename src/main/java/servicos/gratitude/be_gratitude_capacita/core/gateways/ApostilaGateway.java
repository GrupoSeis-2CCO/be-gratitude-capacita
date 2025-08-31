package servicos.gratitude.be_gratitude_capacita.core.gateways;

import servicos.gratitude.be_gratitude_capacita.core.domain.Apostila;

import java.util.List;

public interface ApostilaGateway {
    Apostila save(Apostila apostila);
    List<Apostila> findAllByCurso();
    Apostila findById();
    Boolean existsById();
    void deleteById(Integer id);
}
