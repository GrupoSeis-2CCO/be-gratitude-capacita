package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.RespostaDoUsuarioCompoundKey;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.RespostaDoUsuarioEntityCompoundKey;

public class RespostaDoUsuarioCompoundKeyMapper {
    public static RespostaDoUsuarioEntityCompoundKey toEntity(RespostaDoUsuarioCompoundKey respostaDoUsuarioCompoundKey){
        RespostaDoUsuarioEntityCompoundKey entityCompoundKey = new RespostaDoUsuarioEntityCompoundKey();

        entityCompoundKey.setIdAlternativaComposto(AlternativaCompoundKeyMapper.toEntity(respostaDoUsuarioCompoundKey.getIdAlternativaComposto()));
        entityCompoundKey.setIdTentativaComposto(TentativaCompoundKeyMapper.toEntity(respostaDoUsuarioCompoundKey.getIdTentativaComposto()));

        return entityCompoundKey;
    }

    public static RespostaDoUsuarioCompoundKey toDomain(RespostaDoUsuarioEntityCompoundKey entityCompoundKey){
        RespostaDoUsuarioCompoundKey respostaDoUsuarioCompoundKey = new RespostaDoUsuarioCompoundKey();

        respostaDoUsuarioCompoundKey.setIdAlternativaComposto(AlternativaCompoundKeyMapper.toDomain(entityCompoundKey.getIdAlternativaComposto()));
        respostaDoUsuarioCompoundKey.setIdTentativaComposto(TentativaCompoundKeyMapper.toDomain(entityCompoundKey.getIdTentativaComposto()));

        return respostaDoUsuarioCompoundKey;
    }
}
