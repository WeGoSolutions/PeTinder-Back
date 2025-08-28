# Plano de Refatoração - Módulo Users

## Problemas Identificados

### 1. UserService (God Class) - 372 linhas
**Responsabilidades atuais:**
- CRUD de usuários
- Autenticação e autorização
- Gerenciamento de senhas
- Upload/manipulação de imagens
- Envio de emails
- Validações de negócio
- Gerenciamento de endereços

### 2. Entidade User com múltiplas responsabilidades
- Dados pessoais
- Endereço
- Imagem
- Estado do usuário

## Estrutura Proposta

### A. Divisão por Domínios (Domain-Driven Design)

```
Users/
├── domain/
│   ├── model/
│   │   ├── User.java (core user data)
│   │   ├── UserProfile.java (profile specific data)
│   │   └── UserStatus.java (user state management)
│   ├── service/
│   │   ├── UserDomainService.java (core business rules)
│   │   └── UserValidator.java (business validations)
│   └── repository/
│       └── UserRepository.java
├── application/
│   ├── service/
│   │   ├── UserApplicationService.java (orchestration)
│   │   ├── UserRegistrationService.java (registration flow)
│   │   ├── UserProfileService.java (profile management)
│   │   └── UserQueryService.java (read operations)
│   └── usecase/
│       ├── CreateUserUseCase.java
│       ├── UpdateUserUseCase.java
│       └── AuthenticateUserUseCase.java
├── infrastructure/
│   ├── persistence/
│   │   ├── entity/
│   │   │   ├── UserEntity.java
│   │   │   ├── UserProfileEntity.java
│   │   │   └── AddressEntity.java
│   │   └── repository/
│   │       └── UserJpaRepository.java
│   ├── security/
│   │   ├── UserAuthenticationService.java
│   │   └── PasswordService.java
│   └── notification/
│       └── UserNotificationService.java
└── presentation/
    ├── controller/
    │   ├── UserController.java (simplified)
    │   ├── UserProfileController.java
    │   └── UserAuthController.java
    └── dto/
        ├── request/
        └── response/
```

### B. Separação de Responsabilidades

#### 1. **UserDomainService** (Business Logic Core)
- Regras de negócio puras
- Validações de domínio
- Não depende de infraestrutura

#### 2. **UserApplicationService** (Orchestration)
- Coordena operações entre diferentes serviços
- Gerencia transações
- Publica eventos

#### 3. **UserRegistrationService** (Registration Flow)
- Fluxo específico de cadastro
- Integração com email
- Validações de registro

#### 4. **UserProfileService** (Profile Management)
- Gerenciamento de perfil
- Atualização de dados pessoais
- **SEM** gerenciamento de imagens (será movido para Python)

#### 5. **UserAuthenticationService** (Security)
- Autenticação
- Autorização
- Gerenciamento de tokens

#### 6. **PasswordService** (Password Management)
- Criptografia de senhas
- Validação de senhas
- Reset de senhas

### C. Benefícios da Reestruturação

1. **Single Responsibility Principle**: Cada classe tem uma responsabilidade específica
2. **Baixo Acoplamento**: Serviços independentes e testáveis
3. **Alta Coesão**: Funcionalidades relacionadas agrupadas
4. **Facilidade de Teste**: Classes menores e mais focadas
5. **Manutenibilidade**: Mudanças isoladas em domínios específicos
6. **Escalabilidade**: Fácil adição de novas funcionalidades

### D. Migração das Imagens para Python

#### Problemas Atuais com Imagens no Java:
- Código complexo de manipulação
- Conversão Base64 confusa
- Múltiplas estratégias de armazenamento
- Validações espalhadas

#### Proposta:
1. Criar microserviço Python para imagens
2. API REST para upload/download
3. Processamento assíncrono
4. Validações centralizadas
5. Possível migração futura para AWS Lambda

## Próximos Passos

1. Implementar UserDomainService
2. Criar UserApplicationService
3. Separar autenticação em service específico
4. Refatorar DTOs para novos serviços
5. Implementar testes unitários
6. Remover dependências de imagem gradualmente
