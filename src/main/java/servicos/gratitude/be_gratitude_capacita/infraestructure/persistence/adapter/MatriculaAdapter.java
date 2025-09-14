package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.springframework.stereotype.Service;
import servicos.gratitude.be_gratitude_capacita.core.domain.Curso;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.Usuario;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MatriculaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MatriculaGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MatriculaEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.CursoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.MatriculaMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.UsuarioMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.MatriculaCompoundKeyMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.MatriculaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MatriculaAdapter implements MatriculaGateway {
    private final MatriculaRepository matriculaRepository;

    public MatriculaAdapter(MatriculaRepository matriculaRepository) {
        this.matriculaRepository = matriculaRepository;
    }

    @Override
    public Matricula save(Matricula matricula) {
        MatriculaEntity entity = MatriculaMapper.toEntity(matricula);

        return MatriculaMapper.toDomain(matriculaRepository.save(entity));
    }

    @Override
    public List<Matricula> findAllByUsuario(Usuario usuario) {
        return MatriculaMapper.toDomains(matriculaRepository.findAllByUsuario(UsuarioMapper.toEntity(usuario)));
    }

    @Override
    public List<Matricula> findAllByCurso(Curso curso) {
        return MatriculaMapper.toDomains(matriculaRepository.findAllByCurso(CursoMapper.toEntity(curso)));
    }

    @Override
    public Matricula findById(MatriculaCompoundKey idComposto) {
        Optional<MatriculaEntity> entity = matriculaRepository.findById(MatriculaCompoundKeyMapper.toEntity(idComposto));

        return entity.map(MatriculaMapper::toDomain).orElse(null);
    }

    @Override
    public Boolean existsById(MatriculaCompoundKey idComposto) {
        return matriculaRepository.existsById(MatriculaCompoundKeyMapper.toEntity(idComposto));
    }

    @Override
    public void deleteById(MatriculaCompoundKey idComposto) {
        matriculaRepository.deleteById(MatriculaCompoundKeyMapper.toEntity(idComposto));
    }
}
