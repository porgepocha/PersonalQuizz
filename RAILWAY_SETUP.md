# Railway Setup

## 1. Criar o serviço do backend

No Railway:

- cria um projeto novo
- liga este repositório
- escolhe a raiz do projeto

Este projeto já tem `railway.json`, por isso o Railway deve usar:

- build: `mvn -pl gift-quiz-server -am package -DskipTests`
- start: `java -jar gift-quiz-server/target/gift-quiz-server-1.0.0-SNAPSHOT.jar`

## 2. Base de dados

O ideal é adicionares um serviço PostgreSQL no mesmo projeto Railway.

Depois define estas variáveis no serviço do backend:

- `DATABASE_URL`
- `DATABASE_USERNAME`
- `DATABASE_PASSWORD`
- `DATABASE_DRIVER=org.postgresql.Driver`
- `H2_CONSOLE_ENABLED=false`

Se usares a integração do Railway Postgres, normalmente ele já te dá os valores de ligação.

## 3. Obter o URL público

Quando o deploy terminar, o Railway vai dar-te um domínio público tipo:

- `https://nome-da-tua-app.up.railway.app`

O endpoint da app fica:

- `https://nome-da-tua-app.up.railway.app/api/submissions`

## 4. Meter esse URL no cliente

Depois tens de trocar o `SUBMISSION_URL` em:

- `gift-quiz-client/src/main/java/com/jorge/constanca/client/ClientConfig.java`

de:

- `http://localhost:8080/api/submissions`

para:

- `https://o-teu-dominio.up.railway.app/api/submissions`

## 5. Recompilar a app para lhe mandares

Depois de trocares o URL:

```powershell
mvn test
powershell -ExecutionPolicy Bypass -File .\package-client.ps1
```

O executável final fica em:

- `dist/GiftAuraQuiz/GiftAuraQuiz.exe`

## 6. Ver resultados

No teu navegador:

- `https://o-teu-dominio.up.railway.app/api/submissions`

## Nota

Antes de lhe mandares a app, testa:

1. deploy do backend
2. abrir `/api/submissions`
3. trocar o URL no cliente
4. recompilar a app
5. fazer um quiz de teste
