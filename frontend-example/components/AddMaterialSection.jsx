import React, { useState } from 'react';
import Button from './Button';
import { uploadFileToS3, createVideoCommand, createApostilaCommand, updateApostilaUrl, updateApostila, updateVideo } from '../services/UploadService.js';
import { useParams } from 'react-router-dom';

export default function AddMaterialSection({ cursoId: propCursoId = null, onAdded = null, initialMaterial = null, onCancelEdit = null }) {
  const [isEditing, setIsEditing] = useState(false);
  const [materialType, setMaterialType] = useState('pdf'); // 'pdf' or 'video'

  // form fields
  const [titulo, setTitulo] = useState('');
  const [descricao, setDescricao] = useState('');
  const [file, setFile] = useState(null);
  const [urlVideo, setUrlVideo] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const params = useParams();
  const idCursoFromParams = params.idCurso ?? params.id ?? null;
  const cursoId = propCursoId || idCursoFromParams || 1;

  async function handleConcluir() {
    setError(null);
    setLoading(true);
    try {
      if (!titulo || titulo.trim().length === 0) throw new Error('Título é obrigatório');

      if (materialType === 'video') {
        if (!urlVideo || urlVideo.trim().length === 0) throw new Error('A URL do vídeo é obrigatória');
        if (initialMaterial && initialMaterial.type === 'video' && initialMaterial.id) {
          // update existing video
          await updateVideo(initialMaterial.id, { nomeVideo: titulo, descricaoVideo: descricao || null, urlVideo, ordemVideo: 1 });
        } else {
          await createVideoCommand({ nomeVideo: titulo, descricaoVideo: descricao || null, urlVideo, ordemVideo: 1, fkCurso: cursoId });
        }
      } else {
        // apostila
        let arquivoUrl = null;
        if (file) {
          arquivoUrl = await uploadFileToS3(file, 'bronze');
        }
        if (initialMaterial && initialMaterial.type === 'pdf' && initialMaterial.id) {
          // update existing apostila
          await updateApostila(initialMaterial.id, { nomeApostila: titulo, descricaoApostila: descricao || null, tamanhoBytes: file ? file.size : (initialMaterial.tamanhoBytes || 0) });
          if (arquivoUrl) {
            await updateApostilaUrl(initialMaterial.id, arquivoUrl);
          }
        } else {
          const created = await createApostilaCommand({
            nomeApostilaOriginal: titulo,
            nomeApostilaArmazenamento: file ? file.name : titulo,
            descricaoApostila: descricao || null,
            tamanhoBytes: file ? file.size : 0,
            isApostilaOculto: 0,
            ordemApostila: 1,
            fkCurso: cursoId,
            urlApostila: arquivoUrl
          });

          // tentar atualizar a url da apostila via endpoint específico, se tivermos id
          try {
            const id = created && (created.id || created.idApostila || created.id_apostila || null);
            if (arquivoUrl && id) {
              await updateApostilaUrl(id, arquivoUrl);
            }
          } catch (err) {
            console.warn('Falha ao atualizar URL da apostila via PATCH:', err);
          }
        }
      }

      // sucesso
      setIsEditing(false);
      setTitulo(''); setDescricao(''); setFile(null); setUrlVideo('');
      if (onAdded) onAdded();
      if (initialMaterial && onCancelEdit) onCancelEdit();
    } catch (err) {
      console.error(err);
      setError(err?.response?.data || err.message || String(err));
    } finally {
      setLoading(false);
    }
  }

  // when parent provides initialMaterial, open editor and prefill fields
  React.useEffect(() => {
    if (initialMaterial) {
      setIsEditing(true);
      setTitulo(initialMaterial.title || initialMaterial.nomeApostila || initialMaterial.nomeVideo || '');
      setDescricao(initialMaterial.description || initialMaterial.descricaoApostila || initialMaterial.descricaoVideo || '');
      if (initialMaterial.type === 'video') {
        setMaterialType('video');
        setUrlVideo(initialMaterial.url || initialMaterial.urlVideo || '');
      } else {
        setMaterialType('pdf');
        setFile(null);
      }
    }
  }, [initialMaterial]);

  return (
    <div>
      {!isEditing ? (
        <div className="bg-white border border-gray-200 rounded-lg shadow-md p-4 flex justify-center items-center mb-8">
          <Button 
            variant="Default" 
            label="Adicionar Material" 
            onClick={() => {
              // open fresh editor for creating a new material
              setTitulo(''); setDescricao(''); setFile(null); setUrlVideo(''); setMaterialType('pdf');
              setIsEditing(true);
            }} 
          />
        </div>
      ) : (
        <div className="bg-[#1D262D] rounded-lg p-6 mb-8">
          <div className="flex items-center gap-4 mb-4">
            <input
              type="text"
              placeholder="Adicionar Título"
              className="flex-1 p-3 border border-gray-300 rounded-lg bg-white text-black placeholder-gray-500"
              value={titulo}
              onChange={e => setTitulo(e.target.value)}
            />
            <Button variant="Confirm" label={loading ? 'Salvando...' : 'Concluir'} onClick={handleConcluir} />
            <Button variant="Exit" label="Cancelar" onClick={() => {
              setIsEditing(false);
              if (initialMaterial && onCancelEdit) onCancelEdit();
            }} />
          </div>

          <div className="bg-white rounded-lg p-6">
            {materialType === 'pdf' ? (
              <div className="border border-dashed border-gray-400 rounded-lg h-24 flex flex-col items-center justify-center mb-4 text-center">
                <p className="text-gray-600 text-sm">Arraste e solte o arquivo PDF aqui</p>
                <p className="text-xs text-gray-500">ou</p>
                <label className="text-gray-600 border border-gray-400 rounded-md px-3 py-1 text-xs mt-1 cursor-pointer">
                  Selecione o Arquivo
                  <input type="file" accept="application/pdf" className="hidden" onChange={e => setFile(e.target.files[0])} />
                </label>
                {/* show attached filename when a file is selected */}
                {file && (
                  <div className="w-full mt-3 flex items-center justify-between px-4">
                    <span className="text-sm text-gray-700">Arquivo anexado: <strong>{file.name}</strong></span>
                    <button className="text-sm text-red-600 underline" onClick={() => setFile(null)}>Remover</button>
                  </div>
                )}
                {/* if editing an existing apostila and no new file selected, show current file name from URL */}
                {!file && initialMaterial && (initialMaterial.url || initialMaterial.urlApostila || initialMaterial.urlArquivo) && (
                  <div className="w-full mt-3 flex items-center justify-between px-4">
                    <span className="text-sm text-gray-700">Arquivo atual: <strong>{(initialMaterial.url || initialMaterial.urlApostila || initialMaterial.urlArquivo).split('/').pop()}</strong></span>
                    {/* do not remove existing file by default; user can upload a new one to replace it */}
                  </div>
                )}
              </div>
            ) : (
              <div className="mb-4">
                <input
                  type="text"
                  placeholder="Cole a URL do vídeo aqui..."
                  className="w-full p-3 border border-gray-300 rounded-lg bg-white text-black placeholder-gray-500"
                  value={urlVideo}
                  onChange={e => setUrlVideo(e.target.value)}
                />
              </div>
            )}

            <div className="flex gap-6">
              <div className="flex-shrink-0 space-y-2">
                <div className="flex items-center gap-2">
                  <span className="font-semibold text-sm">Arquivo</span>
                  <button 
                    className={`px-3 py-1 text-xs rounded border ${materialType === 'pdf' ? 'bg-gray-300 border-gray-500' : 'bg-white border-gray-400'}`}
                    onClick={() => setMaterialType('pdf')}
                  >
                    PDF
                  </button>
                </div>
                <div className="flex items-center gap-2">
                  <span className="font-semibold text-sm">Vídeo</span>
                  <button 
                    className={`px-3 py-1 text-xs rounded border ${materialType === 'video' ? 'bg_gray-300 border-gray-500' : 'bg-white border-gray-400'}`}
                    onClick={() => setMaterialType('video')}
                  >
                    URL
                  </button>
                </div>
              </div>
              <div className="flex-1">
                <label className="block text-sm font-semibold mb-1">Sobre o {materialType === 'video' ? 'vídeo' : 'arquivo'}:</label>
                <textarea
                  placeholder="Adicionar Descrição..."
                  className="w-full h-20 p-2 border border-gray-300 rounded-lg resize-none"
                  value={descricao}
                  onChange={e => setDescricao(e.target.value)}
                />
              </div>
            </div>

            {error && <div className="text-red-600 mt-2">{String(error)}</div>}
          </div>
        </div>
      )}
    </div>
  );
}
