# 🚀 Setup GitHub Actions para Deploy no DigitalOcean

Este guia mostra como configurar o GitHub Actions para fazer deploy automático no Droplet da DigitalOcean.

---

## 📋 Pré-requisitos

- ✅ Droplet DigitalOcean rodando Ubuntu
- ✅ Backend já configurado e funcionando no Droplet
- ✅ Acesso SSH ao Droplet
- ✅ Repositório no GitHub

---

## 🔐 Passo 1: Configurar GitHub Secrets

No repositório **be-gratitude-capacita**, vá em:

**Settings → Secrets and variables → Actions → New repository secret**

### Secrets necessários:

#### 1. **DROPLET_HOST**
- **Valor:** IP do seu Droplet (ex: `157.245.112.40`)
- Como obter: Copie do painel da DigitalOcean ou do SSH

#### 2. **DROPLET_USER**
- **Valor:** `root` ou `ubuntu` (dependendo de como configurou)
- Recomendado: `root` (se rodou o script como root)

#### 3. **DROPLET_SSH_KEY**
- **Valor:** Conteúdo da sua chave privada SSH
- Como obter:

**No Linux/Mac:**
```bash
cat ~/.ssh/id_rsa
# OU se tiver chave específica:
cat ~/.ssh/digitalocean_key
```

**No Windows (PowerShell):**
```powershell
Get-Content ~\.ssh\id_rsa
```

**Exemplo do que copiar:**
```
-----BEGIN OPENSSH PRIVATE KEY-----
b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAABlwAAAAdzc2gtcn
NhAAAAAwEAAQAAAYEA1234567890abcdefghijklmnop...
... (várias linhas) ...
AAAAA1234567890==
-----END OPENSSH PRIVATE KEY-----
```

**⚠️ IMPORTANTE:** Copie TODO o conteúdo, incluindo as linhas `BEGIN` e `END`!

---

## 📝 Passo 2: Verificar se o serviço está configurado

No Droplet, verifique se o serviço `spring-app.service` existe:

```bash
ssh root@SEU_IP_DROPLET

# Verificar se o serviço existe
systemctl status spring-app.service

# Verificar se está habilitado para iniciar automaticamente
systemctl is-enabled spring-app.service

# Se não estiver habilitado, habilite:
sudo systemctl enable spring-app.service
```

---

## 🎯 Passo 3: Estrutura de diretórios no Droplet

Certifique-se de que existe:

```
/opt/app/
├── app.jar                    # JAR da aplicação
└── application.properties     # Configurações externas
```

Verifique:
```bash
ls -lh /opt/app/
```

Se não existir, crie:
```bash
sudo mkdir -p /opt/app
sudo chown -R ubuntu:ubuntu /opt/app
```

---

## 🔄 Passo 4: Testar o Deploy Manualmente

### No GitHub:

1. Vá em **Actions** no repositório
2. Clique em **Deploy Backend to DigitalOcean**
3. Clique em **Run workflow** → **Run workflow**
4. Aguarde a execução

### Logs para acompanhar:

**No Droplet:**
```bash
# Ver logs do deploy em tempo real
sudo journalctl -u spring-app.service -f

# Ver status do serviço
sudo systemctl status spring-app.service

# Ver últimas 50 linhas de log
sudo journalctl -u spring-app.service -n 50
```

---

## ✅ Passo 5: Verificar se funcionou

### 1. **No GitHub Actions:**
- Todos os steps devem estar ✅ (verde)
- Verifique os logs de cada step

### 2. **No Droplet:**
```bash
# Verificar se o serviço está rodando
systemctl is-active spring-app.service
# Deve retornar: active

# Verificar se a porta está ouvindo
sudo netstat -tulpn | grep 8081
# Deve mostrar Java ouvindo na porta 8081

# Testar endpoint
curl http://localhost:8081/usuarios/login
# Deve retornar resposta do backend
```

---

## 🐛 Troubleshooting

### ❌ Erro: "Permission denied (publickey)"

**Causa:** Chave SSH incorreta ou não configurada no Droplet

**Solução:**
1. Verifique se a chave no secret `DROPLET_SSH_KEY` está correta
2. Verifique se a chave pública está em `~/.ssh/authorized_keys` no Droplet:

```bash
ssh root@SEU_IP_DROPLET
cat ~/.ssh/authorized_keys
```

3. Se necessário, adicione a chave pública:

```bash
# No seu PC, copie a chave pública:
cat ~/.ssh/id_rsa.pub

# No Droplet, adicione ao authorized_keys:
echo "SUA_CHAVE_PUBLICA_AQUI" >> ~/.ssh/authorized_keys
chmod 600 ~/.ssh/authorized_keys
```

---

### ❌ Erro: "Connection refused" ou "Could not connect"

**Causa:** IP do Droplet incorreto ou firewall bloqueando

**Solução:**
1. Verifique o IP no secret `DROPLET_HOST`
2. Verifique firewall no Droplet:

```bash
# Ver regras de firewall
sudo ufw status

# Se SSH não estiver permitido, adicione:
sudo ufw allow OpenSSH
sudo ufw enable
```

---

### ❌ Erro: "systemctl: command not found"

**Causa:** Usuário sem permissões sudo

**Solução:**
Use `root` como `DROPLET_USER`, ou configure sudo sem senha para `ubuntu`:

```bash
# No Droplet como root:
echo "ubuntu ALL=(ALL) NOPASSWD:ALL" | sudo tee /etc/sudoers.d/ubuntu
```

---

### ❌ Serviço não reinicia

**Causa:** JAR corrompido ou erro no download

**Solução:**
```bash
# Ver logs detalhados
sudo journalctl -u spring-app.service -n 100 --no-pager

# Verificar tamanho do JAR
ls -lh /opt/app/app.jar

# Restaurar backup se necessário
sudo cp /opt/app/app.jar.backup-XXXXXXXX /opt/app/app.jar
sudo systemctl restart spring-app.service
```

---

## 🎨 Customizações Opcionais

### 1. **Fazer build no GitHub e fazer upload direto (sem usar Release)**

Edite o `deploy.yml`:

```yaml
      - name: Upload JAR to Droplet
        uses: appleboy/scp-action@master
        with:
          host: ${{ secrets.DROPLET_HOST }}
          username: ${{ secrets.DROPLET_USER }}
          key: ${{ secrets.DROPLET_SSH_KEY }}
          source: "target/*.jar"
          target: "/opt/app/"
          strip_components: 1
          overwrite: true
```

### 2. **Notificação no Slack/Discord**

Adicione ao final do `deploy.yml`:

```yaml
      - name: Notify Slack
        if: always()
        uses: 8398a7/action-slack@v3
        with:
          status: ${{ job.status }}
          text: 'Deploy to DigitalOcean completed!'
          webhook_url: ${{ secrets.SLACK_WEBHOOK }}
```

### 3. **Rollback automático em caso de falha**

Adicione step de verificação:

```yaml
      - name: Health Check
        uses: appleboy/ssh-action@master
        with:
          host: ${{ secrets.DROPLET_HOST }}
          username: ${{ secrets.DROPLET_USER }}
          key: ${{ secrets.DROPLET_SSH_KEY }}
          script: |
            sleep 10
            if ! systemctl is-active --quiet spring-app.service; then
              echo "Deploy falhou! Fazendo rollback..."
              sudo cp /opt/app/app.jar.backup-* /opt/app/app.jar
              sudo systemctl restart spring-app.service
              exit 1
            fi
```

---

## 📊 Fluxo Completo de Deploy

```
1. Desenvolvedor faz push na branch main
   ↓
2. GitHub Actions detecta o push
   ↓
3. Actions faz checkout do código
   ↓
4. Configura JDK 21
   ↓
5. Compila o projeto com Maven
   ↓
6. Conecta via SSH no Droplet
   ↓
7. Para o serviço Spring Boot
   ↓
8. Faz backup do JAR atual
   ↓
9. Baixa novo JAR do GitHub Release
   ↓
10. Ajusta permissões
   ↓
11. Reinicia o serviço
   ↓
12. Verifica se está rodando
   ↓
13. ✅ Deploy concluído!
```

---

## 📞 Comandos Úteis

### Verificar logs do GitHub Actions no Droplet:
```bash
# Ver quando foi o último deploy
ls -lht /opt/app/

# Ver histórico de backups
ls -lht /opt/app/*.backup*

# Ver versão do JAR atual
sudo jar -xf /opt/app/app.jar META-INF/MANIFEST.MF
cat META-INF/MANIFEST.MF
```

### Forçar novo deploy:
```bash
# Trigger manual no GitHub:
# Actions → Deploy Backend to DigitalOcean → Run workflow

# Ou fazer commit vazio:
git commit --allow-empty -m "Trigger deploy"
git push origin main
```

---

## ✅ Checklist Final

- [ ] Secrets configurados no GitHub
- [ ] Serviço `spring-app.service` rodando no Droplet
- [ ] SSH funcionando (testado manualmente)
- [ ] Diretório `/opt/app/` existe e tem permissões corretas
- [ ] Firewall permite SSH (porta 22)
- [ ] Deploy manual testado e funcionando
- [ ] Logs verificados após deploy

---

**Pronto! Deploy automático configurado! 🚀**

Qualquer push na branch `main` vai disparar o deploy automaticamente!
