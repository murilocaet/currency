import {environment} from '../environment';
import axios from 'axios';

const urlAPI = environment.getApi('rate');//http://localhost:8080/api/rate/findHistoricalData

export const findCurrencyData  = () => 
  axios.get(urlAPI + "/findCurrencyData" , environment.HEADER)
  .then(({data}) => data)
  .catch(e => {});

export const findHistoricalData  = (request) => 
  axios.post(urlAPI + "/findHistoricalData" , JSON.stringify(request), environment.HEADER)
  .then(({data}) => data)
  .catch(e => {});

export const findCurrencyExchangeRate  = (request) => 
  axios.post(urlAPI + "/findCurrencyExchangeRate" , JSON.stringify(request), environment.HEADER)
  .then(({data}) => data)
  .catch(e => {});

export const updateExchange2  = (request) => 
  axios.post(urlAPI + "/updateExchange" , JSON.stringify(request), environment.HEADER)
  .then(({data}) => data)
  .catch(e => {});

export const updateExchange  = (request) => 
  axios.put(urlAPI + "/rates" , JSON.stringify(request), {
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    },
    params: { 
      from: request.from,
      to: request.to
    }
  })
  .then(({data}) => data)
  .catch(e => {});

export const getPanelData  = () => 
  axios.get(urlAPI + "/getPanelData" , environment.HEADER)
  .then(({data}) => data)
  .catch(e => {});