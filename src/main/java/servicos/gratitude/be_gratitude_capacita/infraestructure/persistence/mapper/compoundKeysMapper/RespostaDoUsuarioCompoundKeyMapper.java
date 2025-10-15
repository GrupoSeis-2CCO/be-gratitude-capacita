package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.RespostaDoUsuarioCompoundKey;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.RespostaDoUsuarioEntityCompoundKey;

public class RespostaDoUsuarioCompoundKeyMapper {
    public static RespostaDoUsuarioEntityCompoundKey toEntity(RespostaDoUsuarioCompoundKey respostaDoUsuarioCompoundKey){
        RespostaDoUsuarioEntityCompoundKey entityCompoundKey = new RespostaDoUsuarioEntityCompoundKey();
        entityCompoundKey.setIdAlternativa(respostaDoUsuarioCompoundKey.getIdAlternativaComposto().getIdAlternativa());
        entityCompoundKey.setIdQuestao(respostaDoUsuarioCompoundKey.getIdAlternativaComposto().getIdQuestao());
        entityCompoundKey.setIdAvaliacao(respostaDoUsuarioCompoundKey.getIdAlternativaComposto().getIdAvaliacao());
        entityCompoundKey.setIdTentativaComposto(TentativaCompoundKeyMapper.toEntity(respostaDoUsuarioCompoundKey.getIdTentativaComposto()));

        return entityCompoundKey;
    }

    public static RespostaDoUsuarioCompoundKey toDomain(RespostaDoUsuarioEntityCompoundKey entityCompoundKey){
        RespostaDoUsuarioCompoundKey respostaDoUsuarioCompoundKey = new RespostaDoUsuarioCompoundKey();

    servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey alternativaCompoundKey = new servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.AlternativaCompoundKey();
    alternativaCompoundKey.setIdAlternativa(entityCompoundKey.getIdAlternativa());
    alternativaCompoundKey.setIdQuestao(entityCompoundKey.getIdQuestao());
    alternativaCompoundKey.setIdAvaliacao(entityCompoundKey.getIdAvaliacao());
    respostaDoUsuarioCompoundKey.setIdAlternativaComposto(alternativaCompoundKey);
    respostaDoUsuarioCompoundKey.setIdTentativaComposto(TentativaCompoundKeyMapper.toDomain(entityCompoundKey.getIdTentativaComposto()));

        return respostaDoUsuarioCompoundKey;
    }
}
