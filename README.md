Manual de Instalação - Projeto PeTinder
Pré-requisitos
Antes de rodar o projeto, instale os seguintes softwares:


Java JDK 17 ou superior


Download Java JDK
Após instalar, configure a variável de ambiente JAVA_HOME.
Apache Maven


Download Maven
Adicione o Maven ao PATH do sistema.
Git (opcional, para clonar os repositórios)


Download Git
Banco de Dados


O projeto utiliza um banco de dados relacional (ex: MySQL ou PostgreSQL).
Para baixar o banco de dados utilizado pelo projeto, clone o repositório:
git clone https://github.com/WeGoSolutions/PeTinder-DB.git
Siga as instruções no repositório acima para configurar o banco.
IDE recomendada:


IntelliJ IDEA
ou
VS Code
Passos para Instalação
Clone o repositório do projeto


git clone https://github.com/WeGoSolutions/PeTinder-Back.git
cd PeTinder-Back
Configure o banco de dados


Crie um banco de dados no MySQL/PostgreSQL.
Atualize as configurações de acesso no arquivo src/main/resources/application.properties:
spring.datasource.url=jdbc:mysql://localhost:3306/seu_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
Instale as dependências


mvn clean install
Execute o projeto


mvn spring-boot:run
Ou, rode a classe principal pela sua IDE.


Acesse a API


Acesse a documentação Swagger em:
http://localhost:8080/swagger-ui.html
ou
http://localhost:8080/swagger-ui/index.html