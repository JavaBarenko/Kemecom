#Kemecom - Projeto de Sistema de cadastro de clientes#

Projeto POC que utiliza HTML5, GruntJS, Ajax, SPA, Web Components, JEE 6, Resteasy, Logback, Google WebUtilities, MongoDB, Facebook Login, Servidor de email SMTP e Memcached.

##Suposições##

* Clientes podem se cadastrar usando email/senha ou facebook
* Clientes podem se logar no sistema usando email/senha ou facebook. Ao se logar, a tela inicial de login será a profile do cliente.
* Clientes podem resetar a senha com "lembrar senha" na tela de login, sendo necessário fornecer o email. Caso email seja de um cliente válido, o sistema gerará uma nova senha para o cliente e a enviará ao email fornecido.
* Após estar logado, cliente pode adastrar seu endereço utilizando o CEP e o número. (cliente deverá colocar o CEP e clicar no botão para pesquisar cep, no qual preencherá o logradouro, bairro, cidade e estado)

##Design##

O projeto foi arquitetado em dois sub-projetos:
* *Kemecom-war*: Compõe a camada de serviços, segurança, cache e persistência (back-end)
* *Kemecom-static*: Compõe a camada web com Html, Js e Css (Front-end)

###Kemecom-war###

* Desenvolvido em JEE 6 e Resteasy para fornecer os serviços de login e usuário.
* Possui interceptors para logging, segurança e cache, de forma que essas funcionalidades fiquem fora da lógica do negócio
* A validação de login é feita através do KemecomToken, que é o objectId do documento de autenticacao salvo no mongodb. Esse documento contém o email e o ip do ciente. O token deve ser devolvido no header para todas as requisições a serviços que estejam mapeadas com a anotação @Secure.
* Foi utilizado o framework morphia para conexão com a base de dados, devido a facilidade gerar documentos as anotações do JPA e outras específicas para o MongoDB.
* Todas as respostas de serviços do sistema são tratadas com a classe Message e retornam como ajax. A classe Message possui as seguintes informações:
	+ O flag 'successful', que indica se ouve falha ou sucesso no processo do ponto de vista do negócio;
	+ A string 'message', que indica uma mensagem de retorno para o usuário (utilizada em alguns serviços e para erros)
	+ Um objeto: que contém o retorno real do serviço
* Todas as exceptions do sistema são capturadas pelo GenericExceptionMapper, encapsuladas em uma Message e retornadas para o solicitante rest.

###Kemecom-static###

* Desenvolvido como SPA, com less, html5, web components (polymer) e javascript.
* Todo conteúdo principal é renderizado dentro do "article.content"
* Possui a api 'K', na qual é responsável pelo bootstrap da página, navegação do site, mensagens, controle de token, entre outros.
* O javascript foi todo modularizado, sendo que apenas a variável 'K' foi disponibilizada no contexto da window para uso dos web components.
* A validação de formulários é realizada com parsley.
* A consulta de CEP é realizada inteiramente no lado do cliente, através do script k-zipcode.js.
* O grunt realiza a construção dos html, css e js, realizando diversas tarefas automatizadas tanto para ambiente de desenvolvimento quanto para produção.
	+ O maior destaque é a construção para ambiente de produção, no qual o grunt minifica html, utiliza CDN nos arquivos js de terceiros, gera css a apartir do less e os minifica, aplica uglify nos arquivos js locais e minifica imagens.

##Pré-requisitos##

* Java SE 7
* JBoss EAP 6.1
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


