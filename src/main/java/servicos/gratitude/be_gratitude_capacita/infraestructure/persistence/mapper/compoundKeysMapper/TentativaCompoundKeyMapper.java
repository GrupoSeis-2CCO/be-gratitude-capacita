package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.TentativaCompoundKey;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.TentativaEntityCompoundKey;

public class TentativaCompoundKeyMapper {
    public static TentativaEntityCompoundKey toEntity(TentativaCompoundKey tentativaCompoundKey){
        TentativaEntityCompoundKey entityCompoundKey = new TentativaEntityCompoundKey();

        entityCompoundKey.setIdTentativa(tentativaCompoundKey.getIdTentativa());
        entityCompoundKey.setIdMatriculaComposto(MatriculaCompoundKeyMapper.toEntity(tentativaCompoundKey.getIdMatriculaComposto()));

        return entityCompoundKey;
    }

    public static TentativaCompoundKey toDomain(TentativaEntityCompoundKey entityCompoundKey){
        TentativaCompoundKey tentativaCompoundKey = new TentativaCompoundKey();

        tentativaCompoundKey.setIdTentativa(entityCompoundKey.getIdTentativa());
        tentativaCompoundKey.setIdMatriculaComposto(MatriculaCompoundKeyMapper.toDoamin(entityCompoundKey.getIdMatriculaComposto()));

        return tentativaCompoundKey;
    }
}
