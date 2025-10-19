# 📨 Sistema de Notificações via RabbitMQ

## 📑 Índice
1. [Visão Geral](#visão-geral)
2. [Arquitetura](#arquitetura)
3. [Componentes](#componentes)
4. [Fluxo de Funcionamento](#fluxo-de-funcionamento)
5. [Configuração](#configuração)
6. [Configuração SMTP (Importante!)](#configuração-smtp-importante)
7. [API de Publicação](#api-de-publicação)
8. [Ambientes](#ambientes)
9. [Monitoramento](#monitoramento)
10. [Troubleshooting](#troubleshooting)

---

## 🎯 Visão Geral

O sistema de notificações do **Gratitude Capacita** permite que administradores publiquem novos cursos e notifiquem automaticamente os usuários via email usando a arquitetura de **Message Queue** com **RabbitMQ**.

### Fluxo Básico:
```
Admin Publica Curso → Message Producer → RabbitMQ Queue → Message Consumer → Email Enviado
```

---

## 🏗️ Arquitetura

### Componentes Principais:

```
┌─────────────────────────────────────────────────────────────────┐
│                    Camada de Apresentação                        │
│                    (REST Controllers)                            │
│                    /cursos/{id}/publicar                        │
└────────────────────────────┬────────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────────┐
│                  Camada de Aplicação                             │
│              PublicarCursoUseCase                               │
│  - Valida curso                                                  │
│  - Desculta curso no BD                                          │
│  - Busca usuários para notificar                                 │
│  - Chama NotificacaoProducer                                     │
└────────────────────────────┬────────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────────┐
│              NotificacaoProducer (Service)                       │
│  - Cria DTOs de notificação                                      │
│  - Envia para RabbitTemplate                                     │
│  - Registra logs                                                 │
└────────────────────────────┬────────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────────┐
│                  RabbitMQ (Message Broker)                       │
│  ┌──────────────────────────────────────────┐                   │
│  │ Exchange: exchange.notificacoes           │                   │
│  │ (DirectExchange)                          │                   │
│  └────────────────┬─────────────────────────┘                   │
│                  │ routing.notificacao.cursos                    │
│  ┌────────────────▼─────────────────────────┐                   │
│  │ Queue: notificacao-cursos-lancados       │                   │
│  │ (Durable: true, Auto-delete: false)      │                   │
│  └────────────────┬─────────────────────────┘                   │
└────────────────────────────┬────────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────────┐
│              NotificacaoConsumer (Service)                       │
│  - @RabbitListener na fila                                       │
│  - Deserializa NotificacaoEmailDTO                               │
│  - Constrói corpo do email                                       │
│  - Envia via JavaMailSender                                      │
│  - Registra logs                                                 │
└────────────────────────────┬────────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────────┐
│              SMTP Server (Gmail/MailHog)                         │
│              Email enviado ao usuário ✉️                         │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🔧 Componentes

### 1. **RabbitMQConfig** 
**Arquivo:** `infraestructure/config/RabbitMQConfig.java`

Responsável pela configuração inicial das filas e exchanges do RabbitMQ.

```java
@Configuration
public class RabbitMQConfig {
    // Define a fila com nome "notificacao-cursos-lancados"
    @Bean
    public Queue filaNotificacao() {
        return new Queue(filaNotificacao, true, false, false);
        //                              ↑    ↑    ↑    ↑
        //                            nome durable exclusive autoDelete
    }

    // Define o exchange do tipo Direct
    @Bean
    public DirectExchange exchangeNotificacao() {
        return new DirectExchange(exchangeNotificacao, true, false);
    }

    // Faz o binding (conexão) entre fila e exchange
    @Bean
    public Binding bindingNotificacao(Queue filaNotificacao, DirectExchange exchangeNotificacao) {
        return BindingBuilder.bind(filaNotificacao)
                .to(exchangeNotificacao)
                .with(routingKeyNotificacao);
    }

    // Conversor de mensagens para JSON
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Template para enviar mensagens
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
```

**Configurações:**
- **Fila:** `notificacao-cursos-lancados` (durable=true, não é apagada ao reconectar)
- **Exchange:** `exchange.notificacoes` (tipo DirectExchange)
- **Routing Key:** `routing.notificacao.cursos`

---

### 2. **NotificacaoProducer**
**Arquivo:** `infraestructure/messaging/producer/NotificacaoProducer.java`

Responsável por enviar mensagens para a fila do RabbitMQ.

```java
@Service
public class NotificacaoProducer {
    @Value("${rabbitmq.exchange.notificacao}")
    private String exchange;

    @Value("${rabbitmq.routing.key.notificacao}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public void enviarNotificacao(NotificacaoEmailDTO notificacao) {
        try {
            // Envia a mensagem para o exchange com a routing key
            rabbitTemplate.convertAndSend(exchange, routingKey, notificacao);
            
            logger.info("Notificação enviada para fila: {} | Aluno: {} | Curso: {}", 
                routingKey, notificacao.getNomeAluno(), notificacao.getTituloCurso());
        } catch (Exception e) {
            logger.error("Erro ao enviar notificação para fila: {}", e.getMessage(), e);
        }
    }
}
```

**Responsabilidades:**
- Recebe um `NotificacaoEmailDTO`
- Envia via `RabbitTemplate` usando exchange e routing key
- Registra logs de sucesso e erro
- Não lança exceção (failure handling)

---

### 3. **NotificacaoConsumer**
**Arquivo:** `infraestructure/messaging/consumer/NotificacaoConsumer.java`

Responsável por receber e processar mensagens da fila RabbitMQ.

```java
@Service
public class NotificacaoConsumer {
    @RabbitListener(queues = "${rabbitmq.queue.notificacao}")
    public void receberNotificacao(NotificacaoEmailDTO notificacao) {
        try {
            logger.info("Processando notificação para: {} | Curso: {}", 
                notificacao.getEmailAluno(), notificacao.getTituloCurso());

            enviarEmail(notificacao);

            logger.info("Email enviado com sucesso para: {}", notificacao.getEmailAluno());
        } catch (Exception e) {
            logger.error("Erro ao processar notificação: {}", e.getMessage(), e);
            // Em produção: implementar Dead Letter Queue (DLQ) ou retry
        }
    }

    private void enviarEmail(NotificacaoEmailDTO notificacao) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(notificacao.getEmailAluno());
        message.setSubject("🎓 Novo Curso Lançado: " + notificacao.getTituloCurso());
        message.setText(construirCorpoEmail(notificacao));
        mailSender.send(message);
    }
}
```

**Responsabilidades:**
- Escuta a fila usando `@RabbitListener`
- Recebe `NotificacaoEmailDTO` automaticamente deserializado
- Constrói e envia email via `JavaMailSender`
- Registra logs de processamento

---

### 4. **NotificacaoEmailDTO**
**Arquivo:** `infraestructure/messaging/dto/NotificacaoEmailDTO.java`

Data Transfer Object que representa uma notificação de email.

```java
public class NotificacaoEmailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer idCurso;
    private String tituloCurso;
    private String descricaoCurso;
    private String emailAluno;
    private String nomeAluno;
    private Long dataEnvio;  // timestamp

    // Constructors, Getters e Setters
}
```

**Campos:**
- `idCurso`: ID do curso publicado
- `tituloCurso`: Nome do curso
- `descricaoCurso`: Descrição do curso
- `emailAluno`: Email do aluno a notificar
- `nomeAluno`: Nome do aluno
- `dataEnvio`: Timestamp do envio

---

### 5. **PublicarCursoUseCase**
**Arquivo:** `core/application/usecase/curso/PublicarCursoUseCase.java`

Use Case que orquestra o fluxo de publicação de curso e envio de notificações.

```java
public class PublicarCursoUseCase {
    private final CursoGateway cursoGateway;
    private final MatriculaGateway matriculaGateway;
    private final UsuarioGateway usuarioGateway;
    private final NotificacaoProducer notificacaoProducer;

    public Curso execute(PublicarCursoCommand command) {
        // 1. Validar comando
        if (Objects.isNull(command) || Objects.isNull(command.idCurso())) {
            throw new IllegalArgumentException("ID do curso é obrigatório");
        }

        // 2. Buscar curso
        Curso curso = cursoGateway.findById(command.idCurso());
        if (Objects.isNull(curso)) {
            throw new NaoEncontradoException("Curso não encontrado");
        }

        // 3. Descultar curso (publicar)
        curso.setOcultado(false);
        Curso cursoPublicado = cursoGateway.save(curso);

        // 4. Enviar notificações
        if (command.notificarTodos()) {
            enviarNotificacoesTodos(cursoPublicado);
        } else {
            enviarNotificacoesSelecionados(cursoPublicado, command.idsAlunosSelecionados());
        }

        logger.info("Curso {} publicado com sucesso", cursoPublicado.getIdCurso());
        return cursoPublicado;
    }

    private void enviarNotificacoesTodos(Curso curso) {
        // Busca todas as matrículas do curso
        List<Matricula> matriculas = matriculaGateway.findByCurso(curso);
        
        for (Matricula matricula : matriculas) {
            criarEEnviarNotificacao(curso, matricula.getUsuario());
        }
    }

    private void enviarNotificacoesSelecionados(Curso curso, List<Integer> idsAlunos) {
        // Busca apenas os usuários selecionados
        for (Integer idAluno : idsAlunos) {
            Usuario usuario = usuarioGateway.findById(idAluno);
            criarEEnviarNotificacao(curso, usuario);
        }
    }

    private void criarEEnviarNotificacao(Curso curso, Usuario usuario) {
        NotificacaoEmailDTO notificacao = new NotificacaoEmailDTO(
            curso.getIdCurso(),
            curso.getTituloCurso(),
            curso.getDescricao(),
            usuario.getEmail(),
            usuario.getNome(),
            System.currentTimeMillis()
        );
        notificacaoProducer.enviarNotificacao(notificacao);
    }
}
```

**Fluxo:**
1. Valida comando
2. Busca o curso no BD
3. Desculta (publicar) o curso
4. Busca usuários a notificar (todos ou selecionados)
5. Cria DTOs e envia para fila via producer

---

## 🔄 Fluxo de Funcionamento

### Caso 1: Publicar Curso para TODOS os Alunos

```
POST /cursos/1/publicar
{
  "notificarTodos": true,
  "idsAlunosSelecionados": []
}
    ↓
PublicarCursoUseCase.execute()
    ↓
[Validações] → [Busca Curso] → [Desculta] → [Salva BD]
    ↓
enviarNotificacoesTodos()
    ↓
Busca todas as matrículas do curso
    ↓
Para cada matrícula:
    ├─ Cria NotificacaoEmailDTO
    ├─ NotificacaoProducer.enviarNotificacao()
    ├─ RabbitTemplate.convertAndSend()
    ├─ Mensagem vai para: exchange.notificacoes
    ├─ Routing: routing.notificacao.cursos
    └─ Chega na fila: notificacao-cursos-lancados
        ↓
RabbitMQ guarda mensagem na fila
    ↓
NotificacaoConsumer detecta nova mensagem
    ↓
@RabbitListener desserializa NotificacaoEmailDTO
    ↓
Constrói corpo do email
    ↓
JavaMailSender.send() envia email SMTP
    ↓
✉️ Email chega na caixa do aluno
```

### Caso 2: Publicar Curso para ALUNOS SELECIONADOS

```
POST /cursos/1/publicar
{
  "notificarTodos": false,
  "idsAlunosSelecionados": [1, 2, 3]
}
    ↓
PublicarCursoUseCase.execute()
    ↓
[Validações] → [Busca Curso] → [Desculta] → [Salva BD]
    ↓
enviarNotificacoesSelecionados([1, 2, 3])
    ↓
Para cada ID de aluno:
    ├─ Busca usuário no BD
    ├─ Cria NotificacaoEmailDTO
    ├─ NotificacaoProducer.enviarNotificacao()
    └─ Vai para fila...
        ↓
    (mesmo processo acima)
```

---

## ⚙️ Configuração

### 1. **application.properties**

```properties
# ========== RabbitMQ Configuration ==========
spring.rabbitmq.host=${RABBITMQ_HOST:localhost}
spring.rabbitmq.port=${RABBITMQ_PORT:5672}
spring.rabbitmq.username=${RABBITMQ_USER:guest}
spring.rabbitmq.password=${RABBITMQ_PASSWORD:guest}
spring.rabbitmq.virtual-host=${RABBITMQ_VHOST:/}

# ========== Email Configuration ==========
spring.mail.host=${MAIL_HOST:smtp.gmail.com}
spring.mail.port=${MAIL_PORT:587}
spring.mail.username=${MAIL_USERNAME:seu-email@gmail.com}
spring.mail.password=${MAIL_PASSWORD:sua-senha}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

# ========== RabbitMQ Queue Configuration ==========
rabbitmq.queue.notificacao=notificacao-cursos-lancados
rabbitmq.exchange.notificacao=exchange.notificacoes
rabbitmq.routing.key.notificacao=routing.notificacao.cursos
```

### 2. **Variáveis de Ambiente (Docker/K8s)**

```bash
# RabbitMQ
SPRING_RABBITMQ_HOST=rabbitmq
SPRING_RABBITMQ_PORT=5672
SPRING_RABBITMQ_USERNAME=guest
SPRING_RABBITMQ_PASSWORD=guest

# Email (Gmail)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=seu-email@gmail.com
MAIL_PASSWORD=sua-senha-app  # Usar "Senha de App" no Gmail!
```

---

## ⚠️ Configuração SMTP (Importante!)

> **ADVERTÊNCIA CRÍTICA**: Sem um servidor SMTP configurado, as mensagens chegarão corretamente na fila RabbitMQ, MAS o **Consumer não conseguirá enviar os emails** e resultará em erro!

### Fluxo Incompleto (SEM SMTP):
```
✅ Admin publica curso
   ↓
✅ Mensagem enviada para fila RabbitMQ
   ↓
❌ Consumer tenta processar e falha (sem SMTP)
   ↓
❌ Email nunca é enviado
```

### Opção 1: Gmail (Recomendado para Testes)

1. **Habilitar "Senhas de App"**:
   - Acesse: https://myaccount.google.com/apppasswords
   - Selecione: **Mail** > **Windows Computer** (ou seu OS)
   - Copie a senha gerada (16 caracteres com espaços)

2. **Configurar no `.env` (Produção)**:
```bash
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=seu-email@gmail.com
MAIL_PASSWORD=xxxx xxxx xxxx xxxx  # Senha de App (com espaços!)
```

3. **Ou em `application.properties` (Testes)**:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=seu-email@gmail.com
spring.mail.password=xxxx xxxx xxxx xxxx
```

### Opção 2: MailHog (Melhor para Desenvolvimento)

MailHog é um servidor SMTP fake que intercepta emails sem realmente enviá-los. Perfeito para testes!

```bash
# Iniciar MailHog com Docker
docker run -d \
  --name mailhog \
  --network capacita-net \
  -p 1025:1025 \
  -p 8025:8025 \
  mailhog/mailhog
```

**Configurar em `application.properties`**:
```properties
spring.mail.host=mailhog
spring.mail.port=1025
```

**Acessar Dashboard**: http://localhost:8025

---

### Opção 3: SendGrid / Mailgun (Produção)

Para ambientes de produção, use serviços SMTP gerenciados:

```properties
# SendGrid
spring.mail.host=smtp.sendgrid.net
spring.mail.port=587
spring.mail.username=apikey
spring.mail.password=${SENDGRID_API_KEY}
```

---

### 3. **Depêndencias Maven (pom.xml)**

```xml
<!-- RabbitMQ -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>

<!-- Email -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>

<!-- JSON Processing -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
```

---

## 📡 API de Publicação

### Endpoint: Publicar Curso

**URL:** `POST /cursos/{idCurso}/publicar`

**Headers:**
```http
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>
```

**Body - Opção 1: Notificar TODOS**
```json
{
  "notificarTodos": true,
  "idsAlunosSelecionados": []
}
```

**Body - Opção 2: Notificar SELECIONADOS**
```json
{
  "notificarTodos": false,
  "idsAlunosSelecionados": [1, 2, 3, 4, 5]
}
```

**Response (200 OK):**
```json
{
  "idCurso": 1,
  "tituloCurso": "Regularização Fundiária: Fundamentos",
  "descricao": "Conceitos, etapas e objetivos da Reurb (S/E).",
  "imagem": "https://example.com/img.jpg",
  "ocultado": false,
  "duracaoEstimada": 20
}
```

**Errors:**
- `404 Not Found`: Curso não existe
- `400 Bad Request`: Dados inválidos
- `500 Internal Server Error`: Erro ao conectar RabbitMQ

---

## 📊 Monitoramento

### 1. **RabbitMQ Management Console**

**URL:** `http://localhost:15672`

**Credenciais:** `guest` / `guest`

**O que monitorar:**
- **Queues > notificacao-cursos-lancados**
  - `Ready`: mensagens aguardando processamento
  - `Unacked`: mensagens sendo processadas
  - `Total`: total de mensagens
  
- **Connections**: conexões ativas
- **Channels**: canais AMQP
- **Message Rates**: taxa de publicação/consumo

### 2. **Logs da Aplicação**

```bash
# Producer enviou mensagem
[INFO] NotificacaoProducer: Notificação enviada para fila: routing.notificacao.cursos | Aluno: João Silva | Curso: Curso Teste

# Consumer processando
[INFO] NotificacaoConsumer: Processando notificação para: joao@email.com | Curso: Curso Teste

# Email enviado
[INFO] NotificacaoConsumer: Email enviado com sucesso para: joao@email.com
```

### 3. **Verificar Fila via Docker**

```bash
# Conectar ao container RabbitMQ
docker exec -it rabbitmq bash

# Listar filas
rabbitmqctl list_queues name messages consumers

# Verificar detalhes de uma fila
rabbitmqctl list_queue_details notificacao-cursos-lancados
```

---

## 🐛 Troubleshooting

### Problema: Fila Vazia (Mensagens Não Chegam)

**Causas Possíveis:**
1. RabbitMQ não está rodando
2. Aplicação não conecta no RabbitMQ
3. Variáveis de ambiente incorretas
4. Consumer não está rodando

**Solução:**
```bash
# Verificar se RabbitMQ está rodando
docker ps | grep rabbitmq

# Verificar logs do RabbitMQ
docker logs rabbitmq

# Verificar se aplicação conectou
# Ver logs: "o.s.a.r.c.CachingConnectionFactory"

# Reconectar containers
docker stop rabbitmq app-gratitude
docker start rabbitmq app-gratitude
```

---

### Problema: Email Não Chega

**Causas Possíveis:**
1. Credenciais SMTP incorretas
2. Porta SMTP bloqueada
3. Gmail: Autenticação 2FA ativa (precisa "Senha de App")
4. Firewall bloqueando

**Solução:**
```bash
# Testar credenciais localmente
docker run -it mailhog/mailhog  # MailHog para testes

# Configurar para MailHog
spring.mail.host=localhost
spring.mail.port=1025

# Acessar interface: http://localhost:8025
```

---

### Problema: Erro de Serialização

**Erro:**
```
com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException: 
Unrecognized field "xyz" (class NotificacaoEmailDTO)
```

**Solução:**
- Certifique-se que `NotificacaoEmailDTO` tem `@JsonProperty` em todos os campos
- Implemente `Serializable` com `serialVersionUID`
- Versione o DTO se alterar estrutura

---

### Problema: Connection Refused

**Erro:**
```
Exception: Connection refused: rabbitmq:5672
```

**Solução:**
```bash
# Verificar rede Docker
docker network ls

# Recrear container na rede correta
docker run -d --name app-gratitude \
  --network capacita-net \
  -e SPRING_RABBITMQ_HOST=rabbitmq \
  ...
```

---

## 📈 Casos de Uso Avançados

### 1. **Dead Letter Queue (DLQ)**
Para mensagens que falham permanentemente:

```java
@Bean
public Queue dlq() {
    return new Queue("notificacao-dlq");
}

@Bean
public DirectExchange dlqExchange() {
    return new DirectExchange("exchange.dlq");
}

// Configurar em RabbitMQConfig
```

### 2. **Retry Policy**
Reprocessar mensagens com erro:

```java
@RabbitListener(queues = "notificacao-cursos-lancados",
    retryTemplate = retryTemplate())
public void receberNotificacao(NotificacaoEmailDTO notificacao) {
    // ...
}
```

### 3. **Monitoramento com Prometheus**
Expor métricas de RabbitMQ:

```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

---

## 🏗️ Ambientes

### Desenvolvimento Local

```yaml
# docker-compose.yml (testes locais)
version: '3.8'
services:
  rabbitmq:
    image: rabbitmq:3-management
    ports:
      - "5672:5672"    # AMQP
      - "15672:15672"  # Management Console
    environment:
      RABBITMQ_DEFAULT_USER: guest
      RABBITMQ_DEFAULT_PASS: guest

  mysql:
    image: mysql:8.0
    ports:
      - "3306:3306"
    environment:
      MYSQL_DATABASE: capacita
      MYSQL_ROOT_PASSWORD: admin

  mailhog:  # Para testar emails sem enviar de verdade
    image: mailhog/mailhog
    ports:
      - "1025:1025"
      - "8025:8025"
```

**Iniciar tudo:**
```bash
docker-compose up -d
```

---

### Produção (DigitalOcean)

A arquitetura de produção usa **GitHub Actions + Docker Compose no servidor**:

```
GitHub Actions
    ↓
1. Faz checkout do código
2. Compila com Maven
3. Gera JAR em target/app_loko.jar
    ↓
SSH/rsync
    ↓
Copia JAR para servidor em /usr/share/api/
    ↓
Servidor (DigitalOcean Droplet)
    ↓
docker compose down/up
    ↓
Containers:
- Java (lê JAR de /usr/share/api/)
- MySQL
- RabbitMQ
```

**Arquivos no servidor:**
- `/root/compose.yaml` - Docker compose
- `/root/.env` - Variáveis de ambiente (criadas pelo GitHub Actions)
- `/usr/share/api/app_loko.jar` - JAR da aplicação

**Variáveis necessárias no `.env`:**
```bash
DATABASE_NAME=capacita
DATABASE_USER=root
DATABASE_PASSWORD=...

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=seu-email@gmail.com
MAIL_PASSWORD=...  # Senha de App

RABBITMQ_HOST=rabbitmq
RABBITMQ_PORT=5672
RABBITMQ_USER=guest
RABBITMQ_PASSWORD=guest
```

---

## 🔒 Segurança

- ✅ RabbitMQ: Alterar credenciais padrão em produção
- ✅ Email: Usar "Senha de App" do Gmail, não senha real
- ✅ Variáveis: Não commitar credenciais no Git
- ✅ SSL/TLS: Ativar conexão criptografada com RabbitMQ
- ✅ Validação: Sempre validar dados antes de processar

---

## 📚 Referências

- [Spring AMQP Documentation](https://spring.io/projects/spring-amqp)
- [RabbitMQ Tutorials](https://www.rabbitmq.com/getstarted.html)
- [RabbitMQ Management Plugin](https://www.rabbitmq.com/management.html)

---

**Última atualização:** 18/10/2025  

