#Kemecom#

Projeto POC que utiliza HTML5, GruntJS, Ajax, SPA, Web Components, JEE 6, Resteasy, Logback, Google WebUtilities, MongoDB, Facebook Login, Servidor de email SMTP e Memcached.

##Pré-requisitos##

* Java SE 7
* JBoss EAS 6.1
* Netbeans (Recomendo: 7.3+)
	+ Maven
* Git
* NodeJS
	+ GruntJS
* MongoDB
* Memcached

##Preparação de Ambiente##

Para que o projeto compile, tenha todos os pré-requisitos instalados e execute os seguintes passos:

1. Clone o projeto **Kemecom** no Git:

		git clone https://Barenko@bitbucket.org/Barenko/kemecom.git

    O projeto está dividido em dois sub-projetos:
	* **Kemecom-web**: Contém toda a parte do servidor
	* **Kemecom-static**: Contém toda a parte do front-end

2. No terminal, faça:

	2.1. Abra o diretório do projeto **Kemecom-static**

	2.2. Execute os seguintes comandos para baixar as dependências do grunt para o projeto (será criada uma pasta node_modules)

    	npm install grunt-contrib-clean --save-dev
    	npm install grunt-contrib-less --save-dev
    	npm install grunt-contrib-jshint --save-dev
    	npm install grunt-contrib-uglify --save-dev
    	npm install grunt-contrib-cssmin --save-dev
    	npm install grunt-contrib-htmlmin --save-dev
    	npm install grunt-contrib-imagemin --save-dev
    	npm install grunt-contrib-watch --save-dev
    	npm install grunt-contrib-copy --save-dev
    	npm install grunt-text-replace --save-dev

	2.3. Construa os pacotes estáticos do **Kemecom-static** para o **Kemecom-web** (estou usando assim para simplificar o deploy do ambiente de desenvolvimento - Recomendo redirecionar o build para seu servidor estático caso precise trabalhar muito com conteúdo estático).

		grunt --force

	2.4. Suba o daemon do MongoDB

		mongod &

	2.5 Suba o memcached

		memcached &

3. No Netbeans, faça:

	3.1. Abra o projeto **Kemecom** no Netbeans

	3.2. Abra o arquivo **config.properties** e altere as configurações do servidor de email, conexão com o MongoDB, Memcached, tempo de sessão, etc.

	3.2. Execute o maven build com dependências (botao direito no projeto - Build with dependencies - isso demora um pouco...)

	3.3. Configure o JBoss EAP 6.1 nos Servers do Netbeans

	3.4. Execute o projeto

###Deploy de produção###

Para o deploy de produção, recomendo que seja executada a task *dist* do Grunt, que irá efetuar diversos aperfeiçoamentos adicionais aos arquivos estáticos, tais como a minificação de CSS, Javascript, Html e imagens e a troca das bibliotecas javascript locais para versões disponibilizadas em CDN's.

		grunt dist --force


