# Overview about the project

This project is about the creation of a Currency Exchange Converter using React as Front-end, Spring Boot JAVA as Backend, and Redis to store the data in cache.

## Available Scripts

In the project you will see 2 directories: **backend-java** and **frontend-react**

### frontend-react

Enter into the **frontend-react** by the terminal and execute `npm start` to run the app.
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

#### Dashboard

In addition to the Currency Exchange Converter, a panel was implemented to visualize the data stored in Redis and its update schedules. Mouse over each panel block to see its description.

### backend-java

Open the Maven Project in an IDE of your preference and run it.

In case of need Redis configuration, follow theses steps:

**Step 1: **Go into backend-java directory and unzip the file **Redis-x64-3.2.100.zip**
**Step 2: **Move it to another directory and run **redis-server.exe**
**Step 3: **Now run **redis-cli.exe** and type ping to test the connection

The services are running at http://your-server-host:8080/api/rate/

#### Endpoints

All endpoints were coded to send only the necessary data to Front-end

**GET findCurrencyData**
.../api/rate/findCurrencyData
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

**POST findHistoricalData**
.../api/rate/findHistoricalData
**payload**
{
	"functionRate": "FX_INTRADAY", # =>FX_INTRADAY, FX_DAILY, FX_WEEKLY, FX_MONTHLY
	"refRate": "FIVE_MINUTES",  # => FIVE_MINUTES, SIXTY_MINUTES, DAILY, WEEKLY, MONTHLY, YEARLY
	"fromSymbol": "USD",
	"toSymbol": "EUR"
}
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

**POST findCurrencyExchangeRate**
.../api/rate/findCurrencyExchangeRate
**payload**
{
	"from": "USD",
	"to": "EUR"
}
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

**GET getPanelData**
.../api/rate/getPanelData
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
.../api/rate/updateExchange
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

**PUT rates**
.../api/rate/rates?from=USD&to=BRL
**Response**
{
	"currencyExchange": null,
	"note": "",
	"success": "Currency Exchange added successfully!",
	"error": ""
}

#### Tasks

There are 5 Schedules tasks: 

**ListenerSchedule: **Collect Currencies Exchange previously selected by Users and send it to the updating pool. Runs every 15 minutes.
**JobSchedule: **Consumes the update pool. Triggers the search for new rates and store them in Redis. Runs every 5 minutes.
**JobForcedSchedule: **Consumes the insertion pool. Triggers the search for new rates and store them in Redis. The user sends requests to this queue by the page form or by the API endpoint. Runs every minute.
**ListenerHistoricalSchedule**Collect the historical Currencies Exchange previously selected by Users and send it to the updating pool. Runs every 1 hour.
**JobHistoricalSchedule**Consumes the historical update pool. Triggers the search for new historical rates and store them in Redis. Runs every 20 minutes.

The queue will consume only one item per time, always the first, and will remove it from the list.

## APIs used in this project

**ALPHA VANTAGE API**, you can read more [at here](https://www.alphavantage.co/).
Check out the [API Documentation](https://www.alphavantage.co/documentation/)

**The simplest API for currency conversion**, you can read more [at here](https://www.currencyconverterapi.com/).
Check out the [API Documentation](https://www.currencyconverterapi.com/docs)


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
