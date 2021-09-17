import React,{useEffect, useState} from 'react'
import { Dropdown, Menu,Image } from 'semantic-ui-react'
import AuthenticationService from '../services/authenticationService.js'

export default function SignedIn({signOut}) {

    const [isAuthenticated, setIsAuthenticated] = useState(false)
    let authenticationService = new AuthenticationService()


    useEffect(() => {
        const token = localStorage.getItem("token");

        const isAuth = authenticationService.tokenIsValid(token)
    
        Promise.resolve(isAuth).then(function(value){  
            if (value.data.success === false) {
                setIsAuthenticated(false)
                localStorage.removeItem("token")
                localStorage.removeItem("issue")
            }else{setIsAuthenticated(true)}
        },function(value){

        });
        
    }, [])

    return (
        <div>
            <Menu.Item>
                <Image avatar spaced="right" src="https://www.pedigreeall.com/images/avatar.png"/>
                <Dropdown pointing="top left" text="Admin">
                    <Dropdown.Menu>
                        <Dropdown.Item text="Bilgilerim" icon="info"/>
                        <Dropdown.Item onClick={signOut} text="Çıkış Yap" icon="sign-out"/>
                    </Dropdown.Menu>
                </Dropdown>
            </Menu.Item>
        </div>
    )
}
