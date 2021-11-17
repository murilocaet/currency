
const HOST = "http://192.168.1.6:";
const PORT_API = "8081";
const HOST_API = HOST + PORT_API + "/";

const HEADER = {
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
}

export const environment = {
    getApi,
    HEADER
};

function getApi(path) {
    return HOST_API + "api/" + path;
}