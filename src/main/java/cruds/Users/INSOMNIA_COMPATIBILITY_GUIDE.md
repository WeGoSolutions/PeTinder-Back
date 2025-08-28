# 🧪 Mapeamento Insomnia → Controller Endpoints

## 📋 Compatibilidade com Testes do Insomnia

Este documento mapeia os endpoints da coleção Insomnia para os endpoints implementados no `UserController.java`.

## 🔄 **Endpoints Mapeados**

### ✅ **Endpoints Funcionais (Compatíveis)**

| Insomnia Request | Método | Endpoint Atual | Status | Observações |
|------------------|--------|----------------|---------|-------------|
| **Cadastrar User** | `POST` | `/users` | ✅ | Totalmente compatível |
| **Pegar lista de Usuarios** | `GET` | `/users` | ✅ | Totalmente compatível |
| **Pegar Usuario por Id** | `GET` | `/users/{id}` | ⚠️ | **Requer UUID em vez de Integer** |
| **Deletar Usuario** | `DELETE` | `/users/{id}` | ⚠️ | **Requer UUID em vez de Integer** |
| **Cadastrado Extra** | `PUT` | `/users/{id}/optional` | ⚠️ | **Requer UUID em vez de Integer** |
| **Login** | `POST` | `/users/login` | ✅ | Totalmente compatível |
| **Atualizar um usuario** | `PATCH` | `/users/{id}` | ⚠️ | **Requer UUID em vez de Integer** |
| **Valida o email** | `GET` | `/users/{email}/validar-email` | ✅ | Novo endpoint adicionado |
| **Atualizar user novo** | `PATCH` | `/users/{id}/user-novo` | ⚠️ | **Requer UUID em vez de Integer** |
| **Att senha seguranca** | `PATCH` | `/users/{id}/senha` | ⚠️ | **Requer UUID em vez de Integer** |
| **Atualizar senha** | `PATCH` | `/users/senha` | ✅ | Endpoint via email |
| **Deletar Todos Usuario** | `DELETE` | `/users/teste` | ✅ | Adicionado (⚠️ só para dev) |

### ⚠️ **Endpoints de Imagem (Deprecated - Compatibilidade Temporária)**

| Insomnia Request | Método | Endpoint Atual | Status | Observações |
|------------------|--------|----------------|---------|-------------|
| **Colocar foto de perfil** | `POST` | `/users/{id}/imagem` | 🔶 | **@Deprecated - Migração Python** |
| **Mudando foto de perfil** | `PUT` | `/users/{id}/imagem` | 🔶 | **@Deprecated - Migração Python** |
| **pegando a imagem de perfil** | `GET` | `/users/{id}/imagens/{indice}` | 🔶 | **@Deprecated - Migração Python** |
| **Pega imagem user** | `GET` | `/users/{id}/imagem` | 🔶 | **@Deprecated - Migração Python** |

## 🔧 **Ajustes Necessários nos Testes**

### 1. **Migração de IDs Integer → UUID**

#### ❌ **Formato Antigo (Insomnia):**
```
GET localhost:8080/users/1
PUT localhost:8080/users/1/optional
DELETE localhost:8080/users/11
```

#### ✅ **Formato Novo (Requerido):**
```
GET localhost:8080/users/550e8400-e29b-41d4-a716-446655440000
PUT localhost:8080/users/550e8400-e29b-41d4-a716-446655440000/optional
DELETE localhost:8080/users/6ba7b810-9dad-11d1-80b4-00c04fd430c8
```

### 2. **Novos Endpoints Adicionados**

#### ✅ **Validação de Email com Path Variable:**
```
GET localhost:8080/users/usuario@exemplo.com/validar-email
```

#### ✅ **Atualização de Senha Segura:**
```
PATCH localhost:8080/users/{uuid}/senha
Content-Type: application/json
{
  "senhaAtual": "senha123",
  "novaSenha": "novaSenha456"
}
```

#### ✅ **Endpoint de Teste (Desenvolvimento):**
```
DELETE localhost:8080/users/teste
```

## 🎯 **Como Atualizar os Testes do Insomnia**

### **1. Criar Usuário e Obter UUID:**
```json
POST /users
{
  "nome": "João Silva",
  "email": "joao@exemplo.com",
  "senha": "senha123",
  "dataNasc": "1990-01-01"
}

Response:
{
  "id": "550e8400-e29b-41d4-a716-446655440000",  ← Use este UUID
  "nome": "João Silva",
  "email": "joao@exemplo.com"
}
```

### **2. Usar UUID nos Testes Subsequentes:**
```json
GET /users/550e8400-e29b-41d4-a716-446655440000
PUT /users/550e8400-e29b-41d4-a716-446655440000/optional
PATCH /users/550e8400-e29b-41d4-a716-446655440000
DELETE /users/550e8400-e29b-41d4-a716-446655440000
```

### **3. Variáveis de Ambiente no Insomnia:**
Crie uma variável de ambiente `USER_ID` e use nos testes:
```
GET /users/{{ USER_ID }}
PUT /users/{{ USER_ID }}/optional
```

## 🔄 **Exemplos de Requisições Atualizadas**

### **Cadastrar Usuário:**
```http
POST localhost:8080/users
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao@exemplo.com",
  "senha": "senha123",
  "dataNasc": "1990-01-01",
  "userNovo": true
}
```

### **Buscar Usuário (UUID):**
```http
GET localhost:8080/users/550e8400-e29b-41d4-a716-446655440000
```

### **Atualizar Informações Opcionais:**
```http
PUT localhost:8080/users/550e8400-e29b-41d4-a716-446655440000/optional
Content-Type: application/json

{
  "cpf": "12345678901"
}
```

### **Login:**
```http
POST localhost:8080/users/login
Content-Type: application/json

{
  "email": "joao@exemplo.com",
  "senha": "senha123"
}
```

### **Validar Email:**
```http
GET localhost:8080/users/joao@exemplo.com/validar-email
```

### **Atualizar Senha:**
```http
PATCH localhost:8080/users/550e8400-e29b-41d4-a716-446655440000/senha
Content-Type: application/json

{
  "senhaAtual": "senha123",
  "novaSenha": "novaSenha456"
}
```

## 🚨 **Endpoints Deprecados (Usar com Cuidado)**

### **Imagens (Serão Removidos):**
```http
# ⚠️ DEPRECATED - Use apenas para testes de compatibilidade
POST localhost:8080/users/550e8400-e29b-41d4-a716-446655440000/imagem
PUT localhost:8080/users/550e8400-e29b-41d4-a716-446655440000/imagem
GET localhost:8080/users/550e8400-e29b-41d4-a716-446655440000/imagem
DELETE localhost:8080/users/550e8400-e29b-41d4-a716-446655440000/imagem
```

### **Deletar Todos (Apenas Desenvolvimento):**
```http
# ⚠️ PERIGO - Remove todos os usuários
DELETE localhost:8080/users/teste
```

## ✅ **Resumo de Compatibilidade**

- ✅ **11 endpoints principais** implementados
- ✅ **5 endpoints de imagem** (deprecated mas funcionais)
- ✅ **1 endpoint de teste** para desenvolvimento
- ⚠️ **Migração UUID necessária** para testes com ID
- 🔶 **Endpoints de imagem** serão migrados para Python

**Total: 17 endpoints disponíveis para testes no Insomnia!** 🎯
