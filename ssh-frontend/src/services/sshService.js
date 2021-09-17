import axios from "axios"


export default class sshService {
    executeCommand(credential){
        return axios.post('http://localhost:8080/api/ssh',credential);
    }
    isConnected(id){
        return axios.get('http://localhost:8080/api/ssh/isConnect?credentialId='+id)
    }
    getEthernetAddress(id){
        return axios.get('http://localhost:8080/api/ssh/getEthernet?credentalId='+id)
    }
    getLocalAddress(id){
        return axios.get('http://localhost:8080/api/ssh/getLocal?credentialId='+id)
    }
    getSsh(){
        return axios.get("http://localhost:8080/api/ssh/getSsh")
    }
    addSsh(ssh){
        return axios.post("http://localhost:8080/api/ssh/add",ssh)
    }
    deleteSsh(id){
        return axios.post("http://localhost:8080/api/ssh/delete?id="+id)
    }
    getSshWithId(id){
        return axios.get("http://localhost:8080/api/ssh/getSshById?id="+id)
    }
    setCredentialCommand(id,command){
        return axios.post("http://localhost:8080/api/ssh/setCommand?command="+command+"&id="+id)
    }
    setStatus(){
        return axios.post("http://localhost:8080/api/ssh/setStatus");
    }
}