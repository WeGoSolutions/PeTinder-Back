# =============================================================
#  start-local.ps1 — inicia o backend PeTinder localmente
#  Execute no PowerShell a partir da raiz de PeTinder-Back:
#      .\start-local.ps1
# =============================================================
#
# PRÉ-REQUISITOS (execute uma única vez):
#   1. MySQL 8 rodando na porta 3306, schema "WeGo" criado:
#        CREATE DATABASE IF NOT EXISTS WeGo
#            CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
#
#   2. RabbitMQ + Redis via Docker:
#        docker-compose up -d
#      (basta rodar uma vez; os containers sobem junto com o Docker)
#
#   3. Pasta de imagens locais:
#        New-Item -ItemType Directory -Force "C:\temp\imagens"
# =============================================================

# ── Configura Java 21 apenas para esta sessão ──────────────
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
$env:Path      = "$env:JAVA_HOME\bin;" + $env:Path

# ── Senha do MySQL (edite aqui ou defina MYSQL_PASSWORD antes de rodar) ──
if (-not $env:MYSQL_PASSWORD) {
    $env:MYSQL_PASSWORD = "root"   # <── troque pela sua senha do MySQL
}

# ── Chave Gemini (opcional — deixe vazio se não usar) ───────
if (-not $env:GEMINI_API_KEY) {
    $env:GEMINI_API_KEY = ""
}

# ── Senha do e-mail (opcional — necessário só para reset de senha) ──
# $env:SENHA_PETINDER_EMAIL = "sua_senha_de_app_gmail"

Write-Host ""
Write-Host "==> Java:  $(& java -version 2>&1 | Select-Object -First 1)"
Write-Host "==> Perfil: local"
Write-Host "==> Porta:  8080"
Write-Host ""

# Usa a propriedade do plugin (spring-boot.run.profiles). O -Dspring.profiles.active
# ficaria so no JVM do Maven e NAO chegaria na aplicacao (que roda forkada),
# resultando em "no profiles are currently active" e falha do DataSource.
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
