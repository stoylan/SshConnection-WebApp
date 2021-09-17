import axios from "axios"


export default class loginService {
    login(loginRequest){
        return axios.post("http://localhost:8080/api/login",loginRequest);
    };

    logout(){
        localStorage.removeItem("token")
        localStorage.removeItem("issue")
    };
    
    getCurrentUser(){
        return JSON.parse(localStorage.getItem("token"));
    };
}
