package servicos.gratitude.be_gratitude_capacita.core.domain;

import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.RespostaDoUsuarioCompoundKey;

public class RespostaDoUsuario {
    private RespostaDoUsuarioCompoundKey respostaDoUsuarioCompoundKey;
    private Tentativa tentativa;
    private Alternativa alternativa;

    public RespostaDoUsuarioCompoundKey getRespostaDoUsuarioCompoundKey() {
        return respostaDoUsuarioCompoundKey;
    }

    public void setRespostaDoUsuarioCompoundKey(RespostaDoUsuarioCompoundKey respostaDoUsuarioCompoundKey) {
        this.respostaDoUsuarioCompoundKey = respostaDoUsuarioCompoundKey;
    }

    public Tentativa getTentativa() {
        return tentativa;
    }

    public void setTentativa(Tentativa tentativa) {
        this.tentativa = tentativa;
    }

    public Alternativa getAlternativa() {
        return alternativa;
    }

    public void setAlternativa(Alternativa alternativa) {
        this.alternativa = alternativa;
    }
}
