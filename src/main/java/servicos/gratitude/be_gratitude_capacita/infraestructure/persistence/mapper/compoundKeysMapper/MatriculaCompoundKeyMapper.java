package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MatriculaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.MatriculaEntityCompoundKey;

public class MatriculaCompoundKeyMapper {
    public static MatriculaEntityCompoundKey toEntity(MatriculaCompoundKey matriculaCompoundKey){
        MatriculaEntityCompoundKey entityCompoundKey = new MatriculaEntityCompoundKey();

        entityCompoundKey.setFkCurso(matriculaCompoundKey.getFkCurso());
        entityCompoundKey.setFkUsuario(matriculaCompoundKey.getFkUsuario());

        return entityCompoundKey;
    }

    public static MatriculaCompoundKey toDoamin(MatriculaEntityCompoundKey entityCompoundKey){
        MatriculaCompoundKey matriculaCompoundKey = new MatriculaCompoundKey();

        matriculaCompoundKey.setFkCurso(entityCompoundKey.getFkCurso());
        matriculaCompoundKey.setFkUsuario(entityCompoundKey.getFkUsuario());

        return matriculaCompoundKey;
    }
}
