[Read in English](https://github.com/murilocaet/currency/blob/master/README.md)

[Ideias e tomadas de decisão do projeto](https://github.com/murilocaet/currency/blob/master/Project-Brainstorming-ptbr.md)

# Visão geral sobre o projeto

Este projeto trata da criação de uma API rest para Conversão de câmbio. Foi usado como Arquitetura para o Sistema:

**- ReactJS como front-end**
	 
**- Spring Boot como back-end**
	 
**- Redis para armazenar dados em cache**
	  
**- Imagens Docker**
	 
**- Docker-compose para iniciar o ambiente**
	 
**- Terraform para criar, instanciar e instalar todos os pacotes necessários para executar este projeto em uma VM Ubuntu (versão 18.4) no AWS.**

**-Swagger para documentar a API**

![ReactJS Front-end](https://github.com/murilocaet/currency/blob/master/img-project.png?raw=true)

[Projeto em execução na AWS](http://ec2-34-227-142-123.compute-1.amazonaws.com).

[Documentação da API](http://ec2-34-227-142-123.compute-1.amazonaws.com:8100/swagger).

## Scripts Disponíveis

No projeto, você verá 4 diretórios: **backend-java**, **frontend-react**, **terraform** e **arquivos**, 2 **dockerfile** e o **docker-compose.yml**.

## Como rodar?

Basta executar o Docker-compose no caminho do arquivo `docker-compose.yml path> docker-compose up -d` e abrir o [navegador em localhost](http://localhost/).

* Você encontrará links sobre Docker e Terraform abaixo


### frontend-react

Você também pode iniciar o frontend manualmente. Entre no **frontend-react** pelo terminal e execute `npm start` para executar o aplicativo.
Abra [http://localhost:3000](http://localhost:3000) para visualizá-lo no navegador.

#### Host settings

Você pode alterar as configurações de **API Host and Port** no `environment.jsx`

`const HOST = "http://192.168.1.7:";`

`const PORT_API = "8081";`

#### Dashboard

Além do Conversor de Câmbio, foi implantado um painel para visualização dos dados armazenados no Redis e seus cronogramas de atualização. Passe o mouse sobre cada bloco de painel para ver sua descrição.

### backend-java

Abra o Projeto Maven em um IDE de sua preferência e execute-o.

Os serviços estão sendo executados em http://your-server-host:8081/api/customers/

*Executando local vai preciar alterar as configurações do Redis no `RedisConfig.java` e no `application.yml`.

#### Endpoints

Todos os endpoints foram implementados para enviar apenas os dados necessários para o front-end

**GET findCurrencyData**
.../api/rates/findCurrencyData

**Response**

	{
		"currencies": [

			{
				"key": "USD",
				"currencyName": "United States Dollar"
			},
			{
				"key": "EUR",
				"currencyName": "Euro"
			},
			{
				"key": "GBP",
				"currencyName": "British Pound"
			},
			{
				"key": "BRL",
				"currencyName": "Brazilian Real"
			},
			...
		],
		"note": ""
	}


**GET findHistoricalData**
.../api/rates/findHistoricalData?functionRate=FX_INTRADAY&refRate=FIVE_MINUTES&from=USD&to=EUR

functionRate: `FX_INTRADAY, FX_DAILY, FX_WEEKLY, FX_MONTHLY`

refRate: `FIVE_MINUTES, SIXTY_MINUTES, DAILY, WEEKLY, MONTHLY, YEARLY`

**Response**

	{
		"min": 0.8785,
		"max": 0.8858,
		"rates": [
			{
				"key": "2021-11-18 16:05:00",
				"value": 0.8808
			},
			{
				"key": "2021-11-18 16:10:00",
				"value": 0.8808
			},
			{
				"key": "2021-11-18 16:15:00",
				"value": 0.8808
			},
			...
		],
		"note": ""
	}

**GET findCurrencyExchangeRate**
.../api/rates/findCurrencyExchangeRate?from=USD&to=EUR
	
**Response**

	{
		"currencyExchange": {
			"fromCurrency": "USD",
			"fromCurrencyName": "United States Dollar",
			"toCurrency": "EUR",
			"toCurrencyName": "Euro",
			"exchangeRate": 0.8848,
			"lastRefreshed": "2021-11-19 11:15:01",
			"timeZone": "UTC",
			"bidPrice": 0.88479,
			"askPrice": 0.88482
		},
		"note": "",
		"success": "",
		"error": ""
	}

**GET panelData**
.../api/rates/panelData

**Response**

	{
		"exchangePoolList": [
			"USDTOEUR",
			"USDTOBRL",
			"USDTOCLP"
		],
		"exchangePoolJobList": [
			"USDTOBRL",
			"USDTOEUR"
		],
		"exchangePoolJobForcedList": [
			"USDTOBRL"
		],
		"historicalPoolList": [
			"USDTOEUR::FX_INTRADAY::FIVE_MINUTES",
			"USDTOBRL::FX_DAILY::DAILY",
			"USDTOBRL::FX_WEEKLY::WEEKLY",
			"USDTOEUR::FX_INTRADAY::SIXTY_MINUTES",
			"USDTOBRL::FX_INTRADAY::FIVE_MINUTES",
			"USDTOBRL::FX_MONTHLY::MONTHLY",
			"USDTOBRL::FX_MONTHLY::YEARLY"
		],
		"historicalPoolJobList": [
			"USDTOBRL::FX_INTRADAY::SIXTY_MINUTES",
			"USDTOEUR::FX_INTRADAY::FIVE_MINUTES",
		],
		"note": ""
	}

**PUT updateExchange**
.../api/rates/updateExchange

**payload**

	{
		"from": "USD",
		"to": "BRL"
	}
	
**Response**

	{
		"currencyExchange": null,
		"note": "",
		"success": "Currency Exchange added successfully!",
		"error": ""
	}

**PUT updateRates**
.../api/rates?from=USD&to=BRL

**Response**

	{
		"currencyExchange": null,
		"note": "",
		"success": "Currency Exchange added successfully!",
		"error": ""
	}

#### Tarefas

Existem 5 tarefas de agendamento: 

**ListenerSchedule:** Coleta dados de moedas previamente selecionadas pelos usuários e envie para o pool de atualização. `Executa a cada 15 minutos.`

**JobSchedule:** Consome o pool de atualização. Aciona a busca por novas taxas e as armazena no Redis. `Executa a cada 5 minutos.`

**JobForcedSchedule:** Consome o pool de inserção. Aciona a busca por novas taxas e as armazena no Redis. O usuário pode enviar solicitações a essa fila pelo formulário da tela ou pelo terminal da API. `Executa a cada minuto.`

**ListenerHistoricalSchedule:** Coleta o histórico de câmbio de moedas previamente selecionado pelos usuários e envia para o pool de atualização. `Executa a cada 1 hora.`

**JobHistoricalSchedule:** Consome o pool de atualização histórica. Aciona a busca por novas taxas históricas e as armazena no Redis. `Executa a cada 20 minutos.`

A fila consumirá apenas um item por vez, sempre o primeiro, e o removerá da lista.

## APIs used in this project

**ALPHA VANTAGE API**, leia mais [aqui](https://www.alphavantage.co/).
[Documentação da API](https://www.alphavantage.co/documentation/)

**A API mais simples para conversão de moeda**, leia mais [aqui](https://www.currencyconverterapi.com/).
[Documentação da API](https://www.currencyconverterapi.com/docs)


#### Redis

Todos os dados que a API envia ao front-end vêm do Redis. O sistema só vai para a 'API de Terceiros' quando é necessário alterar um valor ou inserir um novo.
Essa estratégia mantém o sistema mais rápido do que acessar a 'API de Terceiros' o tempo todo. Em caso de alteração de dados, um sinalizador é criado e o Redis atualiza seu Cache.

**Redis**, leia mais [aqui](https://redis.io/).


### Terraform

Terraform é uma ferramenta de infraestrutura como código (IaC) que permite criar, alterar e criar versões de infraestrutura com segurança e eficiência. Isso inclui ambos os de baixo nível
componentes como instâncias de computação, armazenamento e rede, bem como componentes de alto nível, como entradas DNS e recursos SaaS.

**Terraform CLI Documentation**, leia mais [aqui](https://www.terraform.io/docs/cli/index.html).


### Docker / Docker Compose

O Docker permite que você separe seus aplicativos de sua infraestrutura para que possa entregar o software rapidamente.

**The Docker platform**, leia mais [aqui](https://docs.docker.com/get-started/overview/).

Compose é uma ferramenta para definir e executar aplicativos Docker de vários contêineres. Com o Compose, você usa um arquivo YAML para configurar os serviços do seu aplicativo.
Então, com um único comando, você cria e inicia todos os serviços de sua configuração.

**Docker Compose**, leia mais [aqui](https://docs.docker.com/compose/).


### Docker Hub

Docker Hub é um serviço fornecido pela Docker para localizar e compartilhar imagens de contêineres com sua equipe.

**Docker Hub**, leia mais [aqui](https://docs.docker.com/docker-hub/).


### Swagger

Swagger pode ajudá-lo a projetar e documentar suas APIs em escala.

**Swagger API - Ferramentas**, leia mais [aqui](https://swagger.io/tools/).

**Swagger - Documentação da API**, leia mais [aqui](https://swagger.io/solutions/api-documentation/).


## Saber mais

Você pode saber mais na [documentação do aplicativo Criar React] (https://facebook.github.io/create-react-app/docs/getting-started).

Para aprender o React, verifique a [documentação do React](https://reactjs.org/).

### Divisão de Código

Esta seção foi movida para aqui: [https://facebook.github.io/create-react-app/docs/code-splitting](https://facebook.github.io/create-react-app/docs/code-splitting)

### Analisando o tamanho do pacote

Esta seção foi movida para aqui: [https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size](https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size)

### Criando um Progressive Web App

Esta seção foi movida para aqui: [https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app](https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app)

### Configuração avançada

Esta seção foi movida para aqui: [https://facebook.github.io/create-react-app/docs/advanced-configuration](https://facebook.github.io/create-react-app/docs/advanced-configuration)

### Implantação

Esta seção foi movida para aqui: [https://facebook.github.io/create-react-app/docs/deployment](https://facebook.github.io/create-react-app/docs/deployment)

### `npm run build` fails to minify

Esta seção foi movida para aqui: [https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify](https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify)
