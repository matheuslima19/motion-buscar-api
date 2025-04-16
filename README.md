# 🌐 Buscar - API

O **Buscar** é uma API desenvolvida com **Java 17** e **Spring Boot** que tem como objetivo conectar **clientes a oficinas cadastradas na plataforma Pitstop**. A aplicação permite que usuários localizem oficinas próximas com base em sua localização, façam avaliações, comentários e tenham acesso a detalhes importantes sobre os estabelecimentos.

Este repositório representa a API REST do projeto, estruturada com base na **Clean Architecture**.

Organização https://github.com/auto-motion-io
---

## 🎯 Principais funcionalidades

- 📍 **Geolocalização de oficinas**

  - Localização automática das oficinas mais próximas do usuário (Google Maps API)

- 👤 **Autenticação e autorização**

  - Login com **JWT** para garantir segurança nas requisições
  - Suporte a **SSO do Google** para login facilitado

- 🛠️ **Informações da oficina**

  - Exibição de especializações, informações de contato e imagens da oficina

- 💬 **Sistema de avaliação e comentários**

  - Clientes podem avaliar e comentar sobre as oficinas que visitaram

---

## ⚙️ Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring Security (JWT + Google OAuth2)
- Spring Data JPA
- MySQL
- Google Maps API (geolocalização)
- Clean Architecture
- Terraform
- AWS (EC2, RDS, etc.)
- NGINX
- CI/CD

---

## 🚀 Como executar localmente

> Requisitos:
>
> - Java 17
> - Maven ou Gradle
> - MySQL

```bash
# Clone o repositório
git clone https://github.com/Motion-Oficial/buscar-backend.git
cd buscar-backend

# Configure o application.properties ou application.yml com os dados do banco e credenciais OAuth2

# Execute a aplicação
./mvnw spring-boot:run
# ou
./gradlew bootRun
```

A API será iniciada em: `http://localhost:8080`

---

## 👥 Desenvolvedores

Projeto desenvolvido pelo grupo **Motion** – Estudantes de Análise e Desenvolvimento de Sistemas:

- Matheus Santos de Lima - @matheuslima19
- Thaisa Nobrega Costa - @nobregathsa
- David Silva - @Davidnmsilva
- Kauã Juhrs - @KauaJuhrs
- Leonardo Bento da Silva - @leopls07]

---

## 🏷️ Licença

Projeto acadêmico. Uso comercial sujeito à permissão dos autores.

