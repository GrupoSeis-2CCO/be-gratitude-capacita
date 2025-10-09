package servicos.gratitude.be_gratitude_capacita.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.infraestructure.web.responses.ParticipanteCursoResponse;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.CursoRepository;

import java.util.List;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<ParticipanteCursoResponse> getParticipantes(Long idCurso) {
        return cursoRepository.findParticipantesByCurso(idCurso);
    }
}
