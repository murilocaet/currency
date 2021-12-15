[Ler em Português-Brasil](https://github.com/murilocaet/currency/blob/master/README-ptbr.md)

[Project Brainstorming](https://github.com/murilocaet/currency/blob/master/Project-Brainstorming.md)

# Overview about the project

This project is about the creation of a Currency Exchange Converter. It was used as System Architecture:

**- React as Front-end**

**- Spring Boot as Back-end**

**- Redis to store data on cache**

**- Docker Images**

**- Docker-compose to start the environment**

**- Terraform to create, instantiate, and install all the necessary packages to run this project on a Ubuntu(version 18.4) VM on AWS.**

**- Swagger to document the API**

![React Front-end](https://github.com/murilocaet/currency/blob/master/img-project.png?raw=true)

[Project runing on AWS](http://ec2-34-227-142-123.compute-1.amazonaws.com).

[API documentation](http://ec2-34-227-142-123.compute-1.amazonaws.com:8100/swagger).

## Available Scripts

In the project you will see 4 directories: **backend-java**, **frontend-react**, **terraform** and **files**, 2 **dockerfile** and the **docker-compose.yml**.

## How to run?

Just execute the Docker-compose in the file path `docker-compose.yml path> docker-compose up -d` and open [localhost browser](http://localhost/).

*You can find links about Docker and Terraform below


### frontend-react

You can start the frontend manually as well. Enter into the **frontend-react** by the terminal and execute `npm start` to run the app.
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

#### Host settings

You can change settings the **API Host and Port** on the `environment.jsx`

`const HOST = "http://192.168.1.7:";`

`const PORT_API = "8081";`

#### Dashboard

In addition to the Currency Exchange Converter, a panel was implemented to visualize the data stored in Redis and its update schedules. Mouse over each panel block to see its description.

### backend-java

Open the Maven Project in an IDE of your preference and run it.

The services are running at http://your-server-host:8080/api/rate/

*Running locally will need to change the Redis settings in `RedisConfig.java` and `application.yml`.

#### Endpoints

All endpoints were coded to send only the necessary data to Front-end

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

#### Tasks

There are 5 Schedules tasks: 

**ListenerSchedule:** Collect Currencies Exchange previously selected by Users and send it to the updating pool. `Runs every 15 minutes.`

**JobSchedule:** Consumes the update pool. Triggers the search for new rates and store them in Redis. `Runs every 5 minutes.`

**JobForcedSchedule:** Consumes the insertion pool. Triggers the search for new rates and store them in Redis. The user sends requests to this queue by the page form or by the API endpoint. `Runs every minute.`

**ListenerHistoricalSchedule:** Collect the historical Currencies Exchange previously selected by Users and send it to the updating pool. `Runs every 1 hour.`

**JobHistoricalSchedule:** Consumes the historical update pool. Triggers the search for new historical rates and store them in Redis. `Runs every 20 minutes.`

The queue will consume only one item per time, always the first, and will remove it from the list.

## APIs used in this project

**ALPHA VANTAGE API**, you can read more [at here](https://www.alphavantage.co/).
Check out the [API Documentation](https://www.alphavantage.co/documentation/)

**The simplest API for currency conversion**, you can read more [at here](https://www.currencyconverterapi.com/).
Check out the [API Documentation](https://www.currencyconverterapi.com/docs)


#### Redis 

All data that the API sends to the front-end comes from Redis. The System only goes to 'third party APIs' when it is necessary to change a value or enter a new one. 
This strategy keeps the system faster than accessing the 'third party APIs' all the time. In case of any data change, a flag is created and Redis updates its Cache.

**Redis**, you can read more [at here](https://redis.io/).

### Terraform

Terraform is an infrastructure as code (IaC) tool that allows you to build, change, and version infrastructure safely and efficiently. This includes both low-level 
components like compute instances, storage, and networking, as well as high-level components like DNS entries and SaaS features.

**Terraform CLI Documentation**, you can read more [at here](https://www.terraform.io/docs/cli/index.html).


### Docker / Docker Compose

Docker enables you to separate your applications from your infrastructure so you can deliver software quickly.

**The Docker platform**, you can read more [at here](https://docs.docker.com/get-started/overview/).

Compose is a tool for defining and running multi-container Docker applications. With Compose, you use a YAML file to configure your application’s services. 
Then, with a single command, you create and start all the services from your configuration.

**Docker Compose**, you can read more [at here](https://docs.docker.com/compose/).


### Docker Hub

Docker Hub is a service provided by Docker for finding and sharing container images with your team.

**Docker Hub**, you can read more [at here](https://docs.docker.com/docker-hub/).


### Swagger

Swagger can help you design and document your APIs at scale.

**Swagger API Tools**, you can read more [at here](https://swagger.io/tools/).

**Swagger Documentation From Your API Design**, you can read more [at here](https://swagger.io/solutions/api-documentation/).


## Learn More

You can learn more in the [Create React App documentation](https://facebook.github.io/create-react-app/docs/getting-started).

To learn React, check out the [React documentation](https://reactjs.org/).

### Code Splitting

This section has moved here: [https://facebook.github.io/create-react-app/docs/code-splitting](https://facebook.github.io/create-react-app/docs/code-splitting)

### Analyzing the Bundle Size

This section has moved here: [https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size](https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size)

### Making a Progressive Web App

This section has moved here: [https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app](https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app)

### Advanced Configuration

This section has moved here: [https://facebook.github.io/create-react-app/docs/advanced-configuration](https://facebook.github.io/create-react-app/docs/advanced-configuration)

### Deployment

This section has moved here: [https://facebook.github.io/create-react-app/docs/deployment](https://facebook.github.io/create-react-app/docs/deployment)

### `npm run build` fails to minify

This section has moved here: [https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify](https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify)
