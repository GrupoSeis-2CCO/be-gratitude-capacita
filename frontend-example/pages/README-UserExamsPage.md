# UserExamsPage - Melhorias Implementadas

## 📋 Mudanças Realizadas

### 1. **Ordenação de Tentativas** ✅
- As tentativas agora são exibidas **do mais recente ao mais antigo**
- Implementado no `useEffect` com `.sort()` comparando as datas
- Garante que a tentativa mais recente sempre apareça no topo da lista

### 2. **Indicador de Tentativa por Curso** ✅
- Cada linha agora mostra **"Tentativa X"** abaixo do nome do curso
- O número da tentativa é calculado dinamicamente com base no curso
- Tentativas do mesmo curso são numeradas sequencialmente (mais recente = 1, mais antiga = maior número)
- Implementado com a função `getTentativaNumero()`

### 3. **Design Inspirado no Wireframe** ✅

#### Elementos Visuais:
- **Borda azul forte** ao redor da tabela (4px solid #1e3a8a)
- **Cabeçalho azul gradiente** (linear-gradient de #1e40af para #3b82f6)
- **Linhas alternadas** com cores azul claro (bg-blue-50) e branco
- **Decorações nos cantos** (bolinhas azuis e laranjas) simulando o wireframe
- **Badge arredondado** para a nota com cores condicionais:
  - Azul para notas com valores
  - Cinza para "Sem respostas" ou dados ausentes

#### Melhorias de UX:
- **Hover animado** nas linhas com elevação sutil
- **Transições suaves** em todas as interações
- **Tipografia melhorada** com labels menores para "Tentativa X"
- **Espaçamento generoso** entre elementos (py-4 px-6)
- **Centralização da nota** para melhor legibilidade

## 🎨 Estrutura de Arquivos

```
frontend-example/
└── pages/
    ├── UserExamsPage.jsx       # Componente principal atualizado
    └── UserExamsPage.css       # Estilos customizados
```

## 🔍 Exemplo de Dados Exibidos

Com base nos dados de teste (usuário 20 - John Doe):

| Curso | Data da Tentativa | Nota |
|-------|-------------------|------|
| **Java Básico**<br><small>Tentativa 1</small> | 5 de Julho de 2025, 11h00 | 1/2 |
| **Node.js Básico**<br><small>Tentativa 1</small> | 4 de Julho de 2025, 10h00 | 0/1 |
| **MySQL Essencial**<br><small>Tentativa 1</small> | 3 de Julho de 2025, 9h00 | 0/1 |
| **Spring Boot**<br><small>Tentativa 1</small> | 2 de Julho de 2025, 8h00 | 1/1 |
| **Java Básico**<br><small>Tentativa 2</small> | 1 de Julho de 2025, 7h00 | 2/2 |
| **Java Básico**<br><small>Tentativa 3</small> | 4 de Junho de 2025, 4h20 | 0/2 |
| **Java Básico**<br><small>Tentativa 4</small> | 3 de Junho de 2025, 3h15 | 0/2 |

## 🎯 Funcionalidades

### Ordenação Automática
```javascript
const tentativasOrdenadas = Array.isArray(tentativasResp) 
  ? tentativasResp.sort((a, b) => {
      const dateA = new Date(a?.dtTentativa || a?.data || a?.dt || 0);
      const dateB = new Date(b?.dtTentativa || b?.data || b?.dt || 0);
      return dateB - dateA; // mais recente primeiro
    })
  : [];
```

### Cálculo de Número da Tentativa
```javascript
function getTentativaNumero(tentativa, currentIndex) {
  const cursoTitulo = getCursoTitulo(tentativa);
  const tentativasMesmoCurso = tentativas.slice(0, currentIndex + 1)
    .filter(t => getCursoTitulo(t) === cursoTitulo);
  return tentativasMesmoCurso.length;
}
```

### Formatação Visual da Nota
```javascript
<span className={`inline-block px-4 py-2 rounded-full font-semibold ${
  nota === 'Sem respostas' || nota === '—' 
    ? 'bg-gray-200 text-gray-600' 
    : 'bg-blue-100 text-blue-800'
}`}>
  {nota}
</span>
```

## 🚀 Como Testar

1. **Reinicie o backend** (se ainda não estiver rodando):
   ```bash
   cd /home/nikiox/Desktop/ProjetoPI/be-gratitude-capacita
   mvn spring-boot:run
   ```

2. **Inicie o frontend** e navegue até a página de provas do usuário

3. **Verifique**:
   - ✅ Tentativas ordenadas do mais recente ao mais antigo
   - ✅ "Tentativa X" aparecendo abaixo do nome do curso
   - ✅ Design com borda azul e decorações nos cantos
   - ✅ Notas em badges arredondados
   - ✅ Linhas alternadas com cores
   - ✅ Hover animado nas linhas

## 📊 Compatibilidade

- ✅ Backend: Spring Boot 3.5.5 com dados de `data.sql`
- ✅ React com Tailwind CSS
- ✅ Navegação com React Router
- ✅ Responsivo e acessível

## 🎨 Customização

Para ajustar cores ou estilos, edite:
- **Cores principais**: `UserExamsPage.css` (variáveis de cor)
- **Espaçamento**: Classes Tailwind no `UserExamsPage.jsx`
- **Decorações**: Classes `.corner-decoration` no CSS

---

**Implementado por**: GitHub Copilot
**Data**: 15 de Outubro de 2025
