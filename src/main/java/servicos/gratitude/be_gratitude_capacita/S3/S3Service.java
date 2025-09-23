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
        private final S3Client s3Client = S3Client.create();

        @Autowired
        private ApostilaRepository apostilaRepository;

        @Value("${aws.s3.bucket.bronze}")
        private String bucketBronze;

        @Value("${aws.s3.bucket.silver}")
        private String bucketSilver;

        @Value("${aws.s3.bucket.gold}")
        private String bucketGold;

        @Value("${aws.s3.region}")
        private String region;

        /**
         * Envia arquivo para o bucket Bronze, Silver ou Gold.
         * 
         * @param file       arquivo a ser enviado
         * @param tipoBucket bronze, silver ou gold
         * @return URL do arquivo no S3
         * @throws IOException
         */
        public String uploadFile(MultipartFile file, String tipoBucket) throws IOException {
                S3Client s3 = S3Client.builder()
                                .region(Region.of(region))
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