package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import servicos.gratitude.be_gratitude_capacita.core.domain.MaterialAluno;
import servicos.gratitude.be_gratitude_capacita.core.domain.Matricula;
import servicos.gratitude.be_gratitude_capacita.core.domain.compoundKeys.MaterialAlunoCompoundKey;
import servicos.gratitude.be_gratitude_capacita.core.gateways.MaterialAlunoGateway;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MaterialAlunoEntity;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.MaterialAlunoMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.MatriculaMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.MaterialAlunoCompoundKeyMapper;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.MaterialAlunoRepository;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.projection.MaterialAlunoFlatRow;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialAlunoAdapter implements MaterialAlunoGateway {
    private static final Logger logger = LoggerFactory.getLogger(MaterialAlunoAdapter.class);

    private final MaterialAlunoRepository materialAlunoRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public MaterialAlunoAdapter(MaterialAlunoRepository materialAlunoRepository) {
        this.materialAlunoRepository = materialAlunoRepository;
    }

    @Override
    public MaterialAluno save(MaterialAluno materialAluno) {
        MaterialAlunoEntity entity = MaterialAlunoMapper.toEntity(materialAluno);

        // Ensure the Matricula association is attached using only the embedded id reference
        try {
            if (materialAluno.getIdMaterialAlunoComposto() != null && materialAluno.getIdMaterialAlunoComposto().getIdMatriculaComposto() != null) {
                servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.MatriculaEntityCompoundKey mk =
                        servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.MatriculaCompoundKeyMapper.toEntity(
                                materialAluno.getIdMaterialAlunoComposto().getIdMatriculaComposto()
                        );
                servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MatriculaEntity mref =
                        entityManager.getReference(servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MatriculaEntity.class, mk);
                entity.setMatricula(mref);
            } else if (materialAluno.getMatricula() != null && materialAluno.getMatricula().getIdMatriculaComposto() != null) {
                servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.entitiesCompoundKeys.MatriculaEntityCompoundKey mk =
                        servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper.compoundKeysMapper.MatriculaCompoundKeyMapper.toEntity(
                                materialAluno.getMatricula().getIdMatriculaComposto()
                        );
                servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MatriculaEntity mref =
                        entityManager.getReference(servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MatriculaEntity.class, mk);
                entity.setMatricula(mref);
            }
        } catch (Exception e) {
            logger.warn("Failed to set Matricula reference for MaterialAluno before save; leaving mapper-provided value", e);
        }

        // Ensure associations are set using references by ID to avoid null FKs on save
        try {
            if (materialAluno.getFkVideo() != null && materialAluno.getFkVideo().getIdVideo() != null) {
                servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.VideoEntity vref =
                        entityManager.getReference(servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.VideoEntity.class,
                                materialAluno.getFkVideo().getIdVideo());
                entity.setFkVideo(vref);
            }
        } catch (Exception e) {
            logger.warn("Failed to set Video reference for MaterialAluno before save; leaving mapper-provided value", e);
        }

        try {
            if (materialAluno.getFkApostila() != null && materialAluno.getFkApostila().getIdApostila() != null) {
                servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.ApostilaEntity aref =
                        entityManager.getReference(servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.ApostilaEntity.class,
                                materialAluno.getFkApostila().getIdApostila());
                entity.setFkApostila(aref);
            }
        } catch (Exception e) {
            logger.warn("Failed to set Apostila reference for MaterialAluno before save; leaving mapper-provided value", e);
        }

    MaterialAlunoEntity saved = materialAlunoRepository.save(entity);

        // reload the saved entity to ensure associations are initialized
        try {
            Optional<MaterialAlunoEntity> reloaded = materialAlunoRepository.findById(saved.getIdMaterialAlunoComposto());
            if (reloaded.isPresent()) {
                return MaterialAlunoMapper.toDomainWithAssociations(reloaded.get());
            }
        } catch (Exception e) {
            logger.warn("Could not reload saved MaterialAluno entity for full mapping, returning best-effort", e);
        }

        return MaterialAlunoMapper.toDomainWithAssociations(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaterialAluno> findAllByMatricula(Matricula matricula) {
        try {
            List<servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.MaterialAlunoEntity> entities = materialAlunoRepository.findAllByMatricula(MatriculaMapper.toEntity(matricula));
            List<MaterialAluno> domains = MaterialAlunoMapper.toDomains(entities);

            // Fallback enrichment: if associations are missing, fill idVideo/idApostila from flat rows directly
            try {
                Integer u = matricula != null && matricula.getIdMatriculaComposto() != null ? matricula.getIdMatriculaComposto().getFkUsuario() : null;
                Integer c = matricula != null && matricula.getIdMatriculaComposto() != null ? matricula.getIdMatriculaComposto().getFkCurso() : null;
                if (u != null && c != null) {
                    List<MaterialAlunoFlatRow> flat = materialAlunoRepository.listFlatByMatricula(u, c);
                    if (flat != null && !flat.isEmpty()) {
                        java.util.Map<Integer, MaterialAlunoFlatRow> byId = new java.util.HashMap<>();
                        for (MaterialAlunoFlatRow r : flat) {
                            if (r.getIdMaterialAluno() != null) byId.put(r.getIdMaterialAluno(), r);
                        }
                        for (MaterialAluno d : domains) {
                            try {
                                Integer id = d.getIdMaterialAlunoComposto() != null ? d.getIdMaterialAlunoComposto().getIdMaterialAluno() : null;
                                if (id == null) continue;
                                MaterialAlunoFlatRow row = byId.get(id);
                                if (row == null) continue;
                                if (row.getIdVideo() != null) {
                                    if (d.getFkVideo() == null || d.getFkVideo().getIdVideo() == null) {
                                        servicos.gratitude.be_gratitude_capacita.core.domain.Video v = new servicos.gratitude.be_gratitude_capacita.core.domain.Video();
                                        v.setIdVideo(row.getIdVideo());
                                        d.setFkVideo(v);
                                    }
                                    d.setIdVideo(row.getIdVideo());
                                }
                                if (row.getIdApostila() != null) {
                                    if (d.getFkApostila() == null || d.getFkApostila().getIdApostila() == null) {
                                        servicos.gratitude.be_gratitude_capacita.core.domain.Apostila a = new servicos.gratitude.be_gratitude_capacita.core.domain.Apostila();
                                        a.setIdApostila(row.getIdApostila());
                                        d.setFkApostila(a);
                                    }
                                    d.setIdApostila(row.getIdApostila());
                                }
                            } catch (Exception ignored) {}
                        }
                    }
                }
            } catch (Exception enrichErr) {
                logger.warn("MaterialAlunoAdapter.findAllByMatricula: enrichment from flat rows failed", enrichErr);
            }
            if (logger.isInfoEnabled()) {
                long withVideo = domains.stream().filter(d -> d.getFkVideo() != null && d.getFkVideo().getIdVideo() != null).count();
                long withApo = domains.stream().filter(d -> d.getFkApostila() != null && d.getFkApostila().getIdApostila() != null).count();
                logger.info("MaterialAlunoAdapter.findAllByMatricula: matricula={} returned {} materials (videoIds={}, apostilaIds={})",
                        matricula != null ? matricula.getIdMatriculaComposto() : null, domains.size(), withVideo, withApo);
                try {
                    Integer u = matricula != null && matricula.getIdMatriculaComposto() != null ? matricula.getIdMatriculaComposto().getFkUsuario() : null;
                    Integer c = matricula != null && matricula.getIdMatriculaComposto() != null ? matricula.getIdMatriculaComposto().getFkCurso() : null;
                    if (u != null && c != null) {
                        List<MaterialAlunoFlatRow> flat = materialAlunoRepository.listFlatByMatricula(u, c);
                        logger.info("MaterialAlunoAdapter.findAllByMatricula: flatRows size={} examples={}", flat != null ? flat.size() : -1, flat != null && !flat.isEmpty() ? flat.subList(0, Math.min(3, flat.size())) : "[]");
                    }
                } catch (Exception x) {
                    logger.warn("MaterialAlunoAdapter.findAllByMatricula: flat query failed", x);
                }
            }
            return domains;
        } catch (Exception e) {
            logger.error("Error fetching MaterialAluno for matricula {}", matricula != null ? matricula.getIdMatriculaComposto() : null, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MaterialAluno findById(MaterialAlunoCompoundKey idComposto) {
    Optional<MaterialAlunoEntity> entity = materialAlunoRepository.findById(MaterialAlunoCompoundKeyMapper.toEntity(idComposto));

        return entity.map(MaterialAlunoMapper::toDomainWithAssociations).orElse(null);
    }

    @Override
    public Boolean existsById(MaterialAlunoCompoundKey idComposto) {
        return materialAlunoRepository.existsById(MaterialAlunoCompoundKeyMapper.toEntity(idComposto));
    }

    @Override
    public void deleteById(MaterialAlunoCompoundKey idComposto) {
        materialAlunoRepository.deleteById(MaterialAlunoCompoundKeyMapper.toEntity(idComposto));
    }
}
