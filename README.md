# SGBD LOG
Trabalho Prático - BDII

**Índice:**
1. [Descrição](#about)
	+ [Progresso](#progress)
2. [Compilar projeto](#project)
	+ [Instalar dependências](#install)
	+ [Abrir & Compilar](#compile)
3. [Executar](#run)
4. [Parâmetros de linha de comando](#args)

## Descrição <a name="about"></a>

+ Objetivo: implementar o mecanismo de log "REDO" com checkpoint usando o SGBD.

+ Funcionamento: o software deverá ser capaz de ler o arquivo de log (entrada_log.txt),
o arquivo de metadados (metadado.json) e validar as informações no banco de dados através
do modelo "REDO".

+ Saída: O código deverá mostrar o nome das transações que irão sofrer "REDO" e o valor dos
dos dados/variáveis que foram atualizados.

Exemplo: <br>
> Transação T2 realizou REDO. <br>
> Transação T3 não realizou REDO. <br>
> Transação T4 não realizou REDO.
> 
> Valores: <br>
> A: [500, 20] <br>
> B: [20, 30]

Para uma descrição mais detalhada, leia o arquivo [Projeto.pdf](https://github.com/chrisGrando/sgbd-log/blob/main/database/Projeto.pdf).

### Progresso <a name="progress"></a>

(✅) Ler arquivo de log (entrada_log.txt). <br>
(✅) Ler arquivo JSON (metadado.json). <br>
(✅) Conectar-se ao PostgreSQL. <br>
(✅) Criar tabela com os dados do arquivo JSON no SGBD. <br>
(✅) Simular operação de "REDO" no SGBD. <br>
(❌) Interface gráfica.

## Compilar projeto <a name="project"></a>

Abaixo seguirão as instruções dos softwares e ferramentas necessárias para compilar o projeto.

### Instalar dependências <a name="install"></a>

- [PostgreSQL](https://www.postgresql.org/download/) <br>
(SGBD usado neste projeto)

- [Liberica JDK 17](https://bell-sw.com/pages/downloads/#/java-17-lts) <br>
(Versão modificada do Java JDK usada nesse projeto, requer versão 17)

- [Apache NetBeans](https://netbeans.apache.org/download/index.html) <br>
(IDE usada nesse projeto, requer versão 15 ou superior)

> OBS.: Certifique-se que possua o item "JDK 17" listado como plataforma entrando em *Tools -> Java Platforms*.
> Caso não esteja listado, utilize o seguinte guia para adicioná-lo na lista:
>
>[Visão Geral do Suporte a JDK 8 no NetBeans IDE](https://netbeans.apache.org/kb/docs/java/javase-jdk8_pt_BR.html) <br>
>(Este guia está um pouco desatualizado, mas o esquema ainda é o mesmo. Apenas troque o "JKD 8" pelo "JDK 17")

### Abrir & Compilar <a name="compile"></a>
Supondo que as dependências já estejam corretamente instaladas e a IDE aberta:

1. Clique em *File -> Open Project...*

2. Selecione a pasta do projeto *sgbd-log* e clique em *Open Project*.

3. Após o projeto ser carregado, vá na aba *Projects* no painel à esquerda, clique com o botão direito na pasta *Dependencies*,
selecione a opção *Download Declared Dependencies* e aguarde.

4. (Opcional) Ainda na aba *Projects*, clique com o botão direito na pasta *Dependencies* e selecione a opção *Download Javadoc*
e aguarde.

5. Clique no ícone 🔨 *Build Project* para compilar pela primeira vez.

Se a compilação for bem sucedida, então o projeto já está corretamente configurado e pronto para usar.<br>
Os arquivos compilados estarão disponíveis na pasta *target*.

> OBS.: Na primeira vez que o software é compilado, o processo pode demorar entre 5 ~ 10 minutos.
> É necessário ter conexão com a internet.

## Executar <a name="run"></a>

- Dentro da IDE: <br>
Clique no botão ▶️ *Run Project*.

- Na linha de comando (com parâmetros): <br>
```
java -jar sgbd-log.jar [-v | -h] -host [NOME DO HOST] -port [NÚMERO DA PORTA] -database [NOME DO BANCO] -user [USUÁRIO] -password [SENHA] -log [SGBD LOG] -table [NOME DA TABELA] -json [TABELA JSON]
```

## Parâmetros de linha de comando <a name="args"></a>

* -v: Exibe a versão do aplicativo e sai.
* -h: Exibe esta lista de parâmetros e sai.
* -host [...]: Nome do host (padrão: localhost).
* -port [...]: Número da porta (padrão: 5432).
* -database [...]: Nome do banco de dados (padrão: postgres).
* -user [...]: Nome de usuário (padrão: postgres).
* -password [...]: Senha do usuário (padrão: postgres).
* -log [...]: Diretório do arquivo de log do SGBD.
* -table [...]: Nome da tabela do SGBD (padrão: homework).
* -json [...]: Diretório do arquivo JSON da tabela do SGBD.
