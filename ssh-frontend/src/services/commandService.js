import axios from "axios";


export default class commandService {
    getAllCommandByCredentialIdAndPageable(id,pageNo,pageSize){
        return axios.get("http://localhost:8080/api/command/getCommandByCredentialId?credentialId="+id+"&pageNo="+pageNo+"&pageSize="+pageSize)
    }
}
