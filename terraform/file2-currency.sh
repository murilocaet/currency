#!/bin/bash

cd /var/www/currency/files/swagger

sudo bash -c "echo \"{
  'openapi': '3.0.1',
  'info': {
    'title': 'Currency Exchange Converter',
    'description': 'This project is about the creation of a Currency Exchange Converter. It was used as System Architecture:\n- React as Front-end\n- Spring Boot as Back-end\n- Redis to store data on cache\n- Docker Images\n- Docker-compose to start the environment\n- Terraform to create, instantiate, and install all the necessary packages to run this project on a Ubuntu(version 18.4) VM on AWS.\n- Swagger to document the API',
    'termsOfService': 'https://github.com/murilocaet/currency',
    'contact': {
      'name': 'Murilo Costa',
      'email': 'murilo.caet@gmail.com',
      'url': 'https://github.com/murilocaet'
    },
    'license': {
      'name': 'Software livre',
      'url': 'https://github.com/murilocaet/currency'
    },
    'version': '1.0.0'
  },
  'servers': [
    {
      'url': 'http://ec2-54-152-102-37.compute-1.amazonaws.com:8081/api'
    },
    {
      'url': 'http://localhost:8081/api'
    }
  ],
  'tags': [
    {
      'name': 'Currency',
      'description': 'Everything about Currency Exchange'
    }
  ],
  'paths': {
    '/rates/findCurrencyData': {
      'get': {
        'tags': [
          'Currency'
        ],
        'summary': 'Find all Currencies',
        'description': 'Returns all Currencies data.',
        'operationId': 'findCurrencyData',
        'responses': {
          '200': {
            'description': 'successful operation',
            'content': {
              'application/json': {
                'schema': {
                  'items': {
                    '$ref': '#/components/schemas/CurrencyResponse'
                  }
                }
              }
            }
          },
          '204': {
            'description': 'No Content',
            'content': {
              'application/json': {
                'example': 'No Content'
              }
            }
          },
          '500': {
            'description': 'Error loading data',
            'content': {
              'application/json': {
                'example': 'content-length: 0'
              }
            }
          }
        }
      }
    },
    '/rates/findCurrencyExchangeRate': {
      'get': {
        'tags': [
          'Currency'
        ],
        'summary': 'Find Currency Exchange by FROM and TO',
        'description': 'Returns a single Currency Exchange',
        'operationId': 'findCurrencyExchangeRate',
        'parameters': [
          {
            'name': 'from',
            'in': 'query',
            'description': 'FROM Currency',
            'required': true,
            'schema': {
              'type': 'string',
              'default': 'USD'
            }
          },
          {
            'name': 'to',
            'in': 'query',
            'description': 'TO Currency',
            'required': true,
            'schema': {
              'type': 'string',
              'default': 'EUR'
            }
          }
        ],
        'responses': {
          '200': {
            'description': 'successful operation',
            'content': {
              'application/json': {
                'schema': {
                  'items': {
                    '$ref': '#/components/schemas/CurrencyExchangeResponse'
                  }
                }
              }
            }
          },
          '204': {
            'description': 'No Content',
            'content': {
              'application/json': {
                'example': 'No Content'
              }
            }
          },
          '500': {
            'description': 'Error loading data',
            'content': {
              'application/json': {
                'example': 'content-length: 0'
              }
            }
          }
        }
      }
    },
    '/rates/findHistoricalData': {
      'get': {
        'tags': [
          'Currency'
        ],
        'summary': 'Find Historical Data',
        'description': 'Returns historical data',
        'operationId': 'findHistoricalData',
        'parameters': [
          {
            'name': 'functionRate',
            'in': 'query',
            'description': 'Rate function',
            'required': true,
            'schema': {
              'type': 'array',
              'items': {
                'type': 'string',
                'default': 'FX_INTRADAY',
                'enum': [
                  'FX_INTRADAY',
                  'FX_DAILY',
                  'FX_WEEKLY',
                  'FX_MONTHLY'
                ]
              }
            }
          },
          {
            'name': 'refRate',
            'in': 'query',
            'description': 'Rate type',
            'required': true,
            'schema': {
              'type': 'array',
              'items': {
                'type': 'string',
                'default': 'FIVE_MINUTES',
                'enum': [
                  'FIVE_MINUTES',
                  'SIXTY_MINUTES',
                  'DAILY',
                  'WEEKLY',
                  'MONTHLY',
                  'YEARLY'
                ]
              }
            }
          },
          {
            'name': 'from',
            'in': 'query',
            'description': 'FROM Currency',
            'required': true,
            'schema': {
              'type': 'string',
              'default': 'USD'
            }
          },
          {
            'name': 'to',
            'in': 'query',
            'description': 'TO Currency',
            'required': true,
            'schema': {
              'type': 'string',
              'default': 'EUR'
            }
          }
        ],
        'responses': {
          '200': {
            'description': 'successful operation',
            'content': {
              'application/json': {
                'schema': {
                  'items': {
                    '$ref': '#/components/schemas/RateResponse'
                  }
                }
              }
            }
          },
          '204': {
            'description': 'No Content',
            'content': {
              'application/json': {
                'example': 'No Content'
              }
            }
          },
          '500': {
            'description': 'Error loading data',
            'content': {
              'application/json': {
                'example': 'content-length: 0'
              }
            }
          }
        }
      }
    },
    '/rates/panelData': {
      'get': {
        'tags': [
          'Currency'
        ],
        'summary': 'Find all Panel Data',
        'description': 'Returns all panel data.',
        'operationId': 'panelData',
        'responses': {
          '200': {
            'description': 'successful operation',
            'content': {
              'application/json': {
                'schema': {
                  'items': {
                    '$ref': '#/components/schemas/PanelResponse'
                  }
                }
              }
            }
          },
          '204': {
            'description': 'No Content',
            'content': {
              'application/json': {
                'example': 'No Content'
              }
            }
          },
          '500': {
            'description': 'Error loading data',
            'content': {
              'application/json': {
                'example': 'content-length: 0'
              }
            }
          }
        }
      }
    },
    '/rates/updateExchange': {
      'put': {
        'tags': [
          'Currency'
        ],
        'summary': 'Update an Exchange Data',
        'description': 'Update an exchange data',
        'operationId': 'updateExchange',
        'requestBody': {
          'description': 'Currency object that needs to be updated',
          'content': {
            'application/json': {
              'schema': {
                '$ref': '#/components/schemas/CurrencyExchangeRequest'
              }
            }
          },
          'required': true
        },
        'responses': {
          '202': {
            'description': 'successful operation',
            'content': {
              'application/json': {
                'schema': {
                  'items': {
                    '$ref': '#/components/schemas/CurrencyExchangeResponse'
                  }
                }
              }
            }
          },
          '404': {
            'description': 'Not Found',
            'content': {
              'application/json': {
                'example': 'Not Found'
              }
            }
          },
          '500': {
            'description': 'Error updating data',
            'content': {
              'application/json': {
                'example': 'Internal Server Error'
              }
            }
          }
        }
      }
    },
    '/rates': {
      'put': {
        'tags': [
          'Currency'
        ],
        'summary': 'Update Rates',
        'description': 'Update rates',
        'operationId': 'updateRates',
        'parameters': [
          {
            'name': 'from',
            'in': 'query',
            'description': 'FROM Currency',
            'required': true,
            'schema': {
              'type': 'string',
              'default': 'USD'
            }
          },
          {
            'name': 'to',
            'in': 'query',
            'description': 'TO Currency',
            'required': true,
            'schema': {
              'type': 'string',
              'default': 'EUR'
            }
          }
        ],
        'responses': {
          '202': {
            'description': 'successful operation',
            'content': {
              'application/json': {
                'schema': {
                  'items': {
                    '$ref': '#/components/schemas/CurrencyExchangeResponse'
                  }
                }
              }
            }
          },
          '404': {
            'description': 'Not Found',
            'content': {
              'application/json': {
                'example': 'Not Found'
              }
            }
          },
          '500': {
            'description': 'Error updating data',
            'content': {
              'application/json': {
                'example': 'Internal Server Error'
              }
            }
          }
        }
      }
    }
  },
  'components': {
    'schemas': {
      'Currency': {
        'type': 'object',
        'properties': {
          'key': {
            'type': 'string'
          },
          'currencyName': {
            'type': 'string'
          }
        }
      },
      'RateDTO': {
        'type': 'object',
        'properties': {
          'key': {
            'type': 'string'
          },
          'value': {
            'type': 'number'
          }
        }
      },
      'CurrencyExchange': {
        'type': 'object',
        'properties': {
          'fromCurrency': {
            'type': 'string'
          },
          'fromCurrencyName': {
            'type': 'string'
          },
          'toCurrency': {
            'type': 'string'
          },
          'toCurrencyName': {
            'type': 'string'
          },
          'exchangeRate': {
            'type': 'number'
          },
          'lastRefreshed': {
            'type': 'string'
          },
          'timeZone': {
            'type': 'string'
          },
          'bidPrice': {
            'type': 'number'
          },
          'askPrice': {
            'type': 'number'
          }
        }
      },
      'CurrencyResponse': {
        'type': 'object',
        'properties': {
          'currencies': {
            'type': 'array',
            'items': {
              '$ref': '#/components/schemas/Currency'
            }
          },
          'note': {
            'type': 'string'
          }
        }
      },
      'CurrencyExchangeResponse': {
        'type': 'object',
        'properties': {
          'currencyExchange': {
            '$ref': '#/components/schemas/CurrencyExchange'
          },
          'note': {
            'type': 'string'
          },
          'success': {
            'type': 'string'
          },
          'error': {
            'type': 'string'
          }
        }
      },
      'PanelResponse': {
        'type': 'object',
        'properties': {
          'exchangePoolList': {
            'type': 'array',
            'items': {
              'type': 'string'
            }
          },
          'exchangePoolJobList': {
            'type': 'array',
            'items': {
              'type': 'string'
            }
          },
          'exchangePoolJobForcedList': {
            'type': 'array',
            'items': {
              'type': 'string'
            }
          },
          'historicalPoolList': {
            'type': 'array',
            'items': {
              'type': 'string'
            }
          },
          'historicalPoolJobList': {
            'type': 'array',
            'items': {
              'type': 'string'
            }
          },
          'note': {
            'type': 'string'
          }
        }
      },
      'RateResponse': {
        'type': 'object',
        'properties': {
          'min': {
            'type': 'number'
          },
          'max': {
            'type': 'number'
          },
          'rates': {
            'type': 'array',
            'items': {
              '$ref': '#/components/schemas/RateDTO'
            }
          },
          'note': {
            'type': 'string'
          }
        }
      },
      'RateRequest': {
        'type': 'object',
        'properties': {
          'functionRate': {
            'type': 'string',
            'default': 'FX_INTRADAY'
          },
          'refRate': {
            'type': 'string',
            'default': 'FIVE_MINUTES'
          },
          'outputSize': {
            'type': 'string',
            'default': 'COMPACT'
          },
          'fromSymbol': {
            'type': 'string',
            'default': 'USD'
          },
          'toSymbol': {
            'type': 'string',
            'default': 'EUR'
          }
        }
      },
      'CurrencyExchangeRequest': {
        'type': 'object',
        'properties': {
          'from': {
            'type': 'string',
            'default': 'USD'
          },
          'to': {
            'type': 'string',
            'default': 'EUR'
          }
        }
      }
    }
  }
}\" > openapi.json" 

sudo chmod 666 /var/www/currency/files/swagger/openapi.json