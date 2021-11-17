import {environment} from '../environment';
import axios from 'axios';

//const urlAPI = environment.getApi('currency');
const url_test = "https://www.alphavantage.co/query?function=FX_DAILY&from_symbol=EUR&to_symbol=USD&apikey=demo";

export const findHistoricalData  = () => 
  axios.get(url_test, environment.HEADER)
  .then(({data}) => data)
  .catch(e => {});