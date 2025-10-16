This frontend-example folder contains demo pages and services used by the backend integration.

What I added
- `services/UserExamsService.js` — service that calls GET `/tentativas/{fkCurso}/{fkUsuario}` and returns an array of tentativas (handles 204/404).
- `pages/UserExamsPage.jsx` — React page that loads tentativas for a participant and renders a table (Curso / Data da Tentativa / Nota).
- `services/ClassDetailsPageService.js` — service que consome GET `/cursos/{idCurso}/detalhes` para exibir métricas do curso (alunos, materiais, duração).
- `pages/ClassDetailsPage.jsx` — página "detalhes do curso" que carrega do backend e mostra os números reais.

How to wire the page into your app
1. Import the component in your router (wherever you register React Router routes). Example:

```jsx
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import UserExamsPage from './pages/UserExamsPage.jsx';
import ClassDetailsPage from './pages/ClassDetailsPage.jsx';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* other routes */}
        <Route path="/cursos/:cursoId/participantes/:participanteId/provas" element={<UserExamsPage />} />
        {/* detalhes do curso */}
        <Route path="/cursos/:idCurso" element={<ClassDetailsPage />} />
      </Routes>
    </BrowserRouter>
  );
}
```

2. Optionally add a link or navigation to the page, for example from a participant card or menu.

Notes
- The `UserExamsPage` uses the same `api` axios instance defined in `api.js`. Ensure `REACT_APP_API_BASE_URL` is set in your environment or the backend runs on http://localhost:8080.
- The `ClassDetailsPage` busca os dados em `/cursos/{idCurso}/detalhes`. Rotas de materiais e participantes continuam funcionando: `/cursos/{idCurso}/material` e `/matriculas/curso/{idCurso}/participantes`.
- The page displays a placeholder if the Tentativa object doesn't include a score. If you need a computed grade, add a backend endpoint to compute score per tentativa (or expose the score in the Tentativa DTO), and I can wire that in.
