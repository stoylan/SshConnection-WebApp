import React,{useEffect, useState} from 'react'
import { Menu, Segment } from 'semantic-ui-react'
import { Link } from "react-router-dom";
import SignedOut from "./SignedOut";
import SignedIn from "./SignedIn";
import { useHistory } from "react-router";
import LoginService from '../services/loginService';
import AuthenticationService from '../services/authenticationService.js'

export default function Navi() {
    const [isAuthenticated, setIsAuthenticated] = useState(false)
    const history = useHistory()
    let loginService = new LoginService();
    let authenticationService = new AuthenticationService();

    function handleSignOut() {
        setIsAuthenticated(false)
        loginService.logout();
        history.push("/")
    }

    function handleSignIn() {
        history.push("/login")
    }

    useEffect(() => {
        const token = localStorage.getItem("token");

        const isAuth = authenticationService.tokenIsValid(token)

        var isExpired = authenticationService.tokenIsExpired(localStorage.getItem("token"));

        Promise.resolve(isAuth).then(function(value){  
            console.log(value.data.success)
            if (value.data.success === false) {
                setIsAuthenticated(false)
                Promise.resolve(isExpired).then(function(isExpired) {
                    console.log(token)
                    if(isExpired.data.success === false && token !== null){
                        localStorage.removeItem("token")
                        localStorage.removeItem("issue")
                        window.location.reload(true);
                        }
                })
                //return <Redirect push to='/login'  />
            }else{setIsAuthenticated(true)}
        },function(value){

        });
    }, [])

    return (

        <div>
            <Segment inverted>
                <Menu inverted pointing fixed="top" size="huge">

                    <Menu.Item as={Link} to='/Home'
                        name='Home'
                    />
                    <Menu.Item as={Link} to='/Ssh'
                        name='Ssh'
                    />


                    <Menu.Menu position='right'>
                        {isAuthenticated ? <SignedIn signOut={handleSignOut} />
                            : <SignedOut signIn={handleSignIn} />}
                    </Menu.Menu>


                </Menu>
            </Segment>
        </div>

    )
}
