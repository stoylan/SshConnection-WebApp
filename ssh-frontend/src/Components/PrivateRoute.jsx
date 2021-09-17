import React from 'react';
import { Route } from 'react-router-dom';
import AuthenticationService  from '../services/authenticationService';
import Swal from 'sweetalert2'

export const PrivateRoute = ({ component: Component, ...rest }) => (
    
    <Route {...rest} render={props => {
        let authenticationService = new AuthenticationService();
        const token = localStorage.getItem("token")
        let currentUser = authenticationService.tokenIsValid(token)

        function opensweetalertdanger()
        {
          Swal.fire({
            title: 'UNAUTHORIZED',
            text: "You must login to reach this functionality.",
            type: 'error',
            confirmButtonText: 'OK'
          }).then((result) => {
            window.location.reload(true);
          })
    }

        Promise.resolve(currentUser).then(function(value){
            if (value.data.success === false) {
                opensweetalertdanger()
                props.history.push("/login")
                //return <Redirect push to='/login'  />
            }
        },function(value){

        });

        

        // authorised so return component
        return <Component {...props} />
    }} />
)
