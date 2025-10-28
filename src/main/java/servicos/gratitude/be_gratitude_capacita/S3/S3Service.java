package servicos.gratitude.be_gratitude_capacita.S3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import servicos.gratitude.be_gratitude_capacita.infraestructure.persistence.repository.ApostilaRepository;

@Service
public class S3Service {
        @Autowired
        private ApostilaRepository apostilaRepository;

        @Value("${aws.s3.bucket.bronze}")
        private String bucketBronze;

        @Value("${aws.s3.bucket.silver}")
        private String bucketSilver;

        @Value("${aws.s3.bucket.gold}")
        private String bucketGold;

        @Value("${aws.s3.bucket.images:gratitude-imagens-frontend}")
        private String bucketImages;

        @Value("${aws.s3.region}")
        private String region;

        @Value("${aws.accessKeyId:test}")
        private String accessKeyId;

        @Value("${aws.secretAccessKey:test}")
        private String secretAccessKey;

        /**
         * Envia arquivo para o bucket Bronze, Silver ou Gold.
         * 
         * @param file       arquivo a ser enviado
         * @param tipoBucket bronze, silver ou gold
         * @return URL do arquivo no S3
         * @throws IOException
         */
        public String uploadFile(MultipartFile file, String tipoBucket) throws IOException {
                AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
                S3Client s3 = S3Client.builder()
                                .region(Region.of(region))
                                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                                .build();

                // Extrai apenas o nome do arquivo, sem caminho
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null) {
                        throw new IllegalArgumentException("Nome do arquivo não pode ser nulo");
                }
                // Remove possíveis caminhos (Windows ou Linux)
                String filename = originalFilename.replaceAll("^.*[\\\\/]", "");
                // Remove caracteres inválidos para S3
                filename = filename.replaceAll("[^a-zA-Z0-9._-]", "_");
                // Adiciona UUID para unicidade
                String uniqueFilename = java.util.UUID.randomUUID() + "_" + filename;

                String bucket = switch (tipoBucket.toLowerCase()) {
                        case "bronze" -> bucketBronze;
                        case "silver" -> bucketSilver;
                        case "gold" -> bucketGold;
                        default -> throw new IllegalArgumentException("Tipo de bucket inválido");
                };

                s3.putObject(PutObjectRequest.builder()
                                .bucket(bucket)
                                .key(uniqueFilename)
                                .contentType(file.getContentType())
                                .build(),
                                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

                return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + uniqueFilename;
        }

        /**
         * Envia imagem de curso para o bucket dedicado (gratitude-imagens-frontend)
         * e retorna a URL pública.
         */
        public String uploadCourseImage(MultipartFile file) throws IOException {
            System.out.println("[S3Service] Iniciando upload de imagem de curso...");
            if (file == null || file.isEmpty()) {
                System.out.println("[S3Service] Falha: arquivo de imagem ausente ou vazio.");
                throw new IllegalArgumentException("Arquivo de imagem ausente");
            }

            System.out.println("[S3Service] Bucket de imagens: " + bucketImages);
            System.out.println("[S3Service] Região: " + region);
            System.out.println("[S3Service] Content-Type: " + file.getContentType());
            System.out.println("[S3Service] Tamanho do arquivo: " + file.getSize() + " bytes");

            AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
            S3Client s3 = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                    .build();

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                System.out.println("[S3Service] Falha: nome do arquivo nulo.");
                throw new IllegalArgumentException("Nome do arquivo não pode ser nulo");
            }
            String filename = originalFilename.replaceAll("^.*[\\\\/]", "");
            filename = filename.replaceAll("[^a-zA-Z0-9._-]", "_");
            String uniqueFilename = java.util.UUID.randomUUID() + "_" + filename;
            String key = "cursos/" + uniqueFilename;

            System.out.println("[S3Service] Nome do arquivo original: " + originalFilename);
            System.out.println("[S3Service] Nome do arquivo S3: " + key);

            try {
                s3.putObject(
                        PutObjectRequest.builder()
                                .bucket(bucketImages)
                                .key(key)
                                .contentType(file.getContentType())
                                .build(),
                        software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));
                String url = "https://" + bucketImages + ".s3." + region + ".amazonaws.com/" + key;
                System.out.println("[S3Service] Upload concluído. URL gerada: " + url);
                return url;
            } catch (Exception e) {
                System.out.println("[S3Service] Erro ao enviar para S3: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }

        /**
         * Atualiza apenas a URL do arquivo da apostila no banco de dados.
         * 
         * @param idApostila id da apostila
         * @param url        nova URL do arquivo
         */
        @Transactional
        public void patchAtualizarUrlArquivo(Integer idApostila, String url) {
                System.out.println("=== S3SERVICE.patchAtualizarUrlArquivo ===");
                System.out.println("Parâmetros recebidos:");
                System.out.println("  - idApostila: " + idApostila);
                System.out.println("  - url: " + url);

                try {
                        // Primeiro vamos verificar se a apostila existe
                        boolean exists = apostilaRepository.existsById(idApostila);
                        System.out.println("Apostila com ID " + idApostila + " existe? " + exists);

                        if (!exists) {
                                System.out.println("ERRO: Apostila com ID " + idApostila + " não encontrada!");
                                throw new RuntimeException("Apostila não encontrada com ID: " + idApostila);
                        }

                        System.out.println("Chamando apostilaRepository.atualizarUrlArquivo...");
                        int rowsAffected = apostilaRepository.atualizarUrlArquivo(idApostila, url);
                        System.out.println("Linhas afetadas pelo update: " + rowsAffected);

                        if (rowsAffected == 0) {
                                System.out.println(
                                                "ATENÇÃO: Nenhuma linha foi atualizada! Mesmo com a apostila existindo.");
                        } else {
                                System.out.println("Update executado com sucesso!");
                        }

                        // Vamos verificar se a URL foi realmente atualizada
                        var apostilaAtualizada = apostilaRepository.findById(idApostila);
                        if (apostilaAtualizada.isPresent()) {
                                System.out.println("URL atual na base após update: "
                                                + apostilaAtualizada.get().getUrlArquivo());
                        }

                } catch (Exception e) {
                        System.out.println("ERRO no repository: " + e.getMessage());
                        e.printStackTrace();
                        throw e;
                }
        }
}