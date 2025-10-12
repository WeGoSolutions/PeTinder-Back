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

## mvn funciona no PowerShell mas não no Git Bash / WSL

Cenário comum: o Maven foi instalado via Chocolatey e está disponível no PATH do Windows (ver `where mvn`), mas Git Bash ou WSL não o encontram.

- Verifique onde o mvn está no Windows (PowerShell/Command Prompt):
  where mvn
  mvn -v

- Git Bash (MSYS2/Cygwin)
  1. Teste:
     which mvn
     echo $PATH
  2. Se não aparecer, adicione o bin do Chocolatey ao PATH (ajuste conforme sua instalação):
     export PATH="/c/ProgramData/chocolatey/bin:$PATH"
     # ou diretamente o bin do Maven
     export PATH="/c/ProgramData/chocolatey/lib/maven/apache-maven-3.9.11/bin:$PATH"
  3. Para persistir, adicione as linhas acima em ~/.bashrc ou ~/.bash_profile e reinicie o Git Bash:
     echo 'export PATH="/c/ProgramData/chocolatey/bin:$PATH"' >> ~/.bashrc
     source ~/.bashrc

- WSL (Ubuntu/etc.)
  - A instalação do Maven no Windows NÃO garante que o WSL o veja. Instale no WSL:
    sudo apt update && sudo apt install maven -y
    mvn -v

- Teste final (em cada shell):
  which mvn   # linux/git-bash/wsl
  where mvn   # windows cmd/powershell
  mvn -v

- Alternativa recomendada (evita problemas de PATH): use o Maven Wrapper no projeto:
  - Linux/macOS: ./mvnw -B clean package -DskipTests
  - Windows (cmd): mvnw.cmd -B clean package -DskipTests
