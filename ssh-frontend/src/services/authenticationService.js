import axios from "axios"


export default class authenticationService {
    tokenIsValid(token){
        return axios.get("http://localhost:8080/api/valid?token="+token);
    }
    tokenIsExpired(token){
        return axios.get("http://localhost:8080/api/isExpired?token="+token);
    }
}
