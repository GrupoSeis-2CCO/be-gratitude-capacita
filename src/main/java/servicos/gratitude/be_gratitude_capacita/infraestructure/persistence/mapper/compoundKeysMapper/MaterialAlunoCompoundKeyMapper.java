package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MaterialAlunoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.MaterialAlunoEntityCompoundKey;

public class MaterialAlunoCompoundKeyMapper {
    public static MaterialAlunoEntityCompoundKey toEntity(MaterialAlunoCompoundKey materialAlunoCompoundKey){
        MaterialAlunoEntityCompoundKey entityCompoundKey = new MaterialAlunoEntityCompoundKey();

        entityCompoundKey.setIdMaterialAluno(materialAlunoCompoundKey.getIdMaterialAluno());
        entityCompoundKey.setIdMatriculaComposto(MatriculaCompoundKeyMapper.toEntity(materialAlunoCompoundKey.getIdMatriculaComposto()));

        return entityCompoundKey;
    }

    public static MaterialAlunoCompoundKey toDomain(MaterialAlunoEntityCompoundKey entityCompoundKey){
        MaterialAlunoCompoundKey materialAlunoCompoundKey = new MaterialAlunoCompoundKey();

        materialAlunoCompoundKey.setIdMaterialAluno(entityCompoundKey.getIdMaterialAluno());
        materialAlunoCompoundKey.setIdMatriculaComposto(MatriculaCompoundKeyMapper.toDoamin(entityCompoundKey.getIdMatriculaComposto()));

        return materialAlunoCompoundKey;
    }
}
