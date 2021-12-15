import {environment} from '../environment';
import axios from 'axios';

const urlAPI = environment.getApi('rates');

export const findCurrencyData  = () => 
  axios.get(urlAPI + "/findCurrencyData" , environment.HEADER)
  .then(({data}) => data)
  .catch(e => {});

export const findHistoricalData  = (request) => 
  axios.get(urlAPI + "/findHistoricalData", 
    { 
      data: {}, 
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      params: { 
        functionRate: request.functionRate,
        refRate: request.refRate,
        fromSymbol: request.fromSymbol,
        toSymbol: request.toSymbol
      }
  })
  .then(({data}) => data)
  .catch(e => {});

export const findCurrencyExchangeRate  = (request) => 
  axios.get(urlAPI + "/findCurrencyExchangeRate", 
    { 
      data: {}, 
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

export const updateExchange  = (request) => 
  axios.put(urlAPI + "/updateExchange" , JSON.stringify(request), environment.HEADER)
  .then(({data}) => data)
  .catch(e => {});

export const updateExchange_Op2  = (request) => 
  axios.put(urlAPI, null, {
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
  axios.get(urlAPI + "/panelData" , environment.HEADER)
  .then(({data}) => data)
  .catch(e => {});