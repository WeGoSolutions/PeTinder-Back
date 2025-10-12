# Manual de Instalação - Projeto PeTinder

## Pré-requisitos

Antes de iniciar, certifique-se de instalar os seguintes softwares:

- **Java JDK 17 ou superior**  
  [Download Java JDK](https://www.oracle.com/java/technologies/downloads/)  
  Após a instalação, configure a variável de ambiente `JAVA_HOME`.

- **Apache Maven**  
  [Download Maven](https://maven.apache.org/download.cgi)  
  Adicione o Maven ao `PATH` do sistema.

- **Git** (opcional, para clonar os repositórios)  
  [Download Git](https://git-scm.com/downloads)

- **Banco de Dados Relacional (MySQL ou PostgreSQL)**  
  O projeto utiliza um banco de dados relacional.  
  Para obter o banco de dados utilizado:
  ```sh
  git clone https://github.com/WeGoSolutions/PeTinder-DB.git
  ```
  Siga as instruções do repositório acima para configurar o banco.

- **IDE Recomendada:**  
  - IntelliJ IDEA  
  - VS Code  

---

## Passos para Instalação

### 1. Clone o repositório do projeto

```sh
git clone https://github.com/WeGoSolutions/PeTinder-Back.git
cd PeTinder-Back
```

### 2. Configure o banco de dados

- Crie um banco de dados no MySQL ou PostgreSQL.
- Edite o arquivo `src/main/resources/application.properties` com as informações do seu banco:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/seu_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 3. Instale as dependências

```sh
mvn clean install
```

### 4. Execute o projeto

```sh
mvn spring-boot:run
```
Ou, se preferir, execute a classe principal pela sua IDE.

---

## Acesse a API

A documentação da API (Swagger) pode ser acessada em:

- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
  ou  
- [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## Se aparecer "mvn: command not found"

Opções rápidas:

- Usar Maven (Linux - Ubuntu):
  ```sh
  sudo apt update
  sudo apt install maven -y
  mvn -v
  ```

- macOS (Homebrew):
  ```sh
  brew install maven
  mvn -v
  ```

- Windows:
  - Usando Chocolatey (utilize o terminal como administrador):
    choco install maven
  - Ou instalar manualmente pelo site: https://maven.apache.org/download.cgi

- Alternativa (recomendado em projetos): usar o Maven Wrapper se presente no repositório:
  - Linux/macOS: ./mvnw -B clean package -DskipTests
  - Windows (cmd): mvnw.cmd -B clean package -DskipTests

Se preferir eu adiciono o Maven Wrapper (mvnw) ao repositório para evitar necessidade de instalação local.

---

## Erro: permission denied ao acessar /var/run/docker.sock (self-hosted runner)

Causa: o usuário que executa o self-hosted runner não tem permissão para acessar o socket do Docker.

Solução recomendada (executar no host onde o runner está instalado):

1) Identifique o usuário do runner (ex.: `runner`, `ubuntu`, `deploy`, etc.) e execute:
   ```sh
   sudo usermod -aG docker <runner_user>
   ```

2) Reinicie o Docker:
   ```sh
   sudo systemctl restart docker
   ```

3) Reinicie o self-hosted runner para que a nova membership do grupo seja aplicada. Exemplo (dependendo da instalação):
   - Se o runner foi instalado como serviço systemd:
     ```sh
     sudo systemctl restart actions.runner.<OWNER>-<REPO>.<RUNNER_NAME>.service
     ```
     (substitua OWNER/REPO/RUNNER_NAME pelos valores corretos)
   - Ou, no diretório do runner:
     ```sh
     cd /path/to/actions-runner
     ./svc.sh stop
     ./svc.sh start
     ```

4) Teste como o usuário do runner:
   ```sh
   docker ps
   docker pull <seu_usuario>/petinder:back
   ```

Alternativas temporárias (menos recomendadas)
- Permitir uso de sudo nas etapas do workflow (só se o runner aceitar sudo sem senha):
  - Exemplo de step no workflow:
    ```yaml
    - name: Pull image (com sudo)
      run: sudo docker pull ${{ secrets.DOCKER_USERNAME }}/petinder:back
    ```
- Usar SSH para executar docker pull no servidor de destino em vez de executar diretamente no runner.

CUIDADO: alterar permissões do socket (ex.: chmod 666 /var/run/docker.sock) resolve o problema rapidamente, mas reduz a segurança. Prefira adicionar o usuário ao grupo docker.
