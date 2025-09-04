package servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.mapper;

import servicos.gratitude.be_gratitude_capacita.core.domain.Video;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.entity.VideoEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VideoMapper {

    public static VideoEntity toEntity(Video video){

        if (Objects.isNull(video)){
            return null;
        }

        VideoEntity entity = new VideoEntity();
        entity.setDataAtualizacaoVideo(video.getDataAtualizacaoVideo());
        entity.setDescricaoVideo(video.getDescricaoVideo());
        entity.setNomeVideo(video.getNomeVideo());
        entity.setOrdemVideo(video.getOrdemVideo());
        entity.setDataPostadoVideo(video.getDataPostadoVideo());
        entity.setIdVideo(video.getIdVideo());
        entity.setFkCurso(CursoMapper.toEntity(video.getFkCurso()));
        entity.setUrlVideo(video.getUrlVideo());
        entity.setVideoOculto(video.getVideoOculto());

        return entity;
    }

    public static Video toDomain(VideoEntity entity){

        if (Objects.isNull(entity)){
            return null;
        }

        Video video = new Video();
        video.setDataAtualizacaoVideo(entity.getDataAtualizacaoVideo());
        video.setDescricaoVideo(entity.getDescricaoVideo());
        video.setNomeVideo(entity.getNomeVideo());
        video.setOrdemVideo(entity.getOrdemVideo());
        video.setDataPostadoVideo(entity.getDataPostadoVideo());
        video.setIdVideo(entity.getIdVideo());
        video.setFkCurso(CursoMapper.toDomain(entity.getFkCurso()));
        video.setUrlVideo(entity.getUrlVideo());
        video.setVideoOculto(entity.getVideoOculto());

        return video;
    }

    public static List<VideoEntity> toEntities(List<Video> video){

        if (Objects.isNull(video)){
            return null;
        }

        List<VideoEntity> entities = new ArrayList<>();

        for (Video videoDaVez : video) {
        VideoEntity entity = new VideoEntity();
        entity.setDataAtualizacaoVideo(videoDaVez.getDataAtualizacaoVideo());
        entity.setDescricaoVideo(videoDaVez.getDescricaoVideo());
        entity.setNomeVideo(videoDaVez.getNomeVideo());
        entity.setOrdemVideo(videoDaVez.getOrdemVideo());
        entity.setDataPostadoVideo(videoDaVez.getDataPostadoVideo());
        entity.setIdVideo(videoDaVez.getIdVideo());
        entity.setFkCurso(CursoMapper.toEntity(videoDaVez.getFkCurso()));
        entity.setUrlVideo(videoDaVez.getUrlVideo());
        entity.setVideoOculto(videoDaVez.getVideoOculto());

        entities.add(entity);

        }
        return entities;
    }

    public static List<Video> toDomains(List<VideoEntity> entity){

        if (Objects.isNull(entity)){
            return null;
        }

        List<Video> videos = new ArrayList<>();

        for (VideoEntity entityDaVez : entity) {
            Video video = new Video();
            video.setDataAtualizacaoVideo(entityDaVez.getDataAtualizacaoVideo());
            video.setDescricaoVideo(entityDaVez.getDescricaoVideo());
            video.setNomeVideo(entityDaVez.getNomeVideo());
            video.setOrdemVideo(entityDaVez.getOrdemVideo());
            video.setDataPostadoVideo(entityDaVez.getDataPostadoVideo());
            video.setIdVideo(entityDaVez.getIdVideo());
            video.setFkCurso(CursoMapper.toDomain(entityDaVez.getFkCurso()));
            video.setUrlVideo(entityDaVez.getUrlVideo());
            video.setVideoOculto(entityDaVez.getVideoOculto());

            videos.add(video);
        }
        return videos;
    }
}
