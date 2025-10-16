import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Button from '../components/Button';
import Header from '../components/Header';
import GradientSideRail from '../components/GradientSideRail';
import ClassDetailsPageService from '../services/ClassDetailsPageService.js';

function ClassDetailsPage() {
  const { idCurso } = useParams();
  const navigate = useNavigate();

  const [dados, setDados] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function load() {
      setLoading(true);
      setError(null);
      try {
        const info = await ClassDetailsPageService.getCursoDetalhes(idCurso);
        setDados(info);
      } catch (e) {
        setError(e.message || String(e));
      } finally {
        setLoading(false);
      }
    }
    load();
  }, [idCurso]);

  const totalAlunos = dados?.totalAlunos ?? '-';
  const totalHoras = dados?.duracaoEstimada != null ? `${dados.duracaoEstimada}h` : '—';
  const totalMateriais = dados?.totalMateriais ?? '-';

  return (
    <>
      <div className="min-h-screen bg-gray-50 relative">
        <GradientSideRail className="left-10" />
        <GradientSideRail className="right-10" variant="inverted" />
        <Header />

        <div className="max-w-4xl mx-auto px-4 py-8 pt-28">
          <div className="text-center mb-8">
            <h2 className="text-2xl font-bold text-gray-800">Cursos de Capacitação</h2>
            <div className="w-24 h-1 bg-gradient-to-r from-blue-500 to-orange-500 mx-auto mt-2"></div>
          </div>

          <div className="bg-white rounded-lg shadow-lg overflow-hidden">
            <div className="px-6 py-4 border-b border-gray-200">
              <h3 className="text-xl font-semibold text-gray-800">{dados?.tituloCurso || `Curso ${idCurso}`}</h3>
            </div>

            <div className="px-6 py-4">
              <div className="w-full h-48 bg-black rounded-lg flex items-center justify-center">
                <div className="w-16 h-16 border-4 border-white rounded-full flex items-center justify-center">
                  <div className="w-4 h-4 bg-white rounded-full"></div>
                </div>
              </div>
            </div>

            <div className="px-6 py-4">
              <h4 className="text-lg font-semibold text-gray-800 mb-3">Conteúdo</h4>
              {loading ? (
                <p className="text-gray-600">Carregando...</p>
              ) : error ? (
                <p className="text-red-600">{error}</p>
              ) : (
                <p className="text-gray-600 leading-relaxed">
                  {dados?.descricao || 'Sem descrição para este curso.'}
                </p>
              )}
            </div>

            <div className="px-6 py-4 bg-gray-50">
              <div className="grid grid-cols-3 gap-4">
                <div className="text-center">
                  <div className="text-2xl font-bold text-gray-800">{totalAlunos}</div>
                  <div className="text-sm text-gray-600">Quantidade de Alunos</div>
                </div>
                <div className="text-center border-l border-r border-gray-300">
                  <div className="text-2xl font-bold text-gray-800">{totalHoras}</div>
                  <div className="text-sm text-gray-600">Total de Horas</div>
                </div>
                <div className="text-center">
                  <div className="text-2xl font-bold text-gray-800">{totalMateriais}</div>
                  <div className="text-sm text-gray-600">Quantidade de Materiais</div>
                </div>
              </div>
            </div>

            <div className="px-6 py-6 bg-white">
              <div className="grid grid-cols-3 gap-4">
                <Button 
                  variant="Default" 
                  label="Ver Alunos e Desempenho"
                  onClick={() => navigate(`/cursos/${idCurso}/participantes`)} 
                />
                <Button 
                  variant="Default" 
                  label="Analisar Feedbacks"
                  onClick={() => navigate(`/cursos/${idCurso}/feedbacks`)} 
                />
                <Button 
                  variant="Default" 
                  label="Visualizar Materiais do Curso"
                  onClick={() => navigate(`/cursos/${idCurso}/material`)} 
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default ClassDetailsPage;
