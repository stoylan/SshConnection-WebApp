import React, { useState } from 'react'
import { Formik, Form, Field, ErrorMessage } from 'formik'
import * as Yup from "yup"
import { FormField, Button, Label, Grid } from 'semantic-ui-react'
import LoginService from "../services/loginService"
import jwt from 'jsonwebtoken'
import axios from 'axios'
import '../App.css';
import { useHistory } from "react-router";
import AuthenticationService from '../services/authenticationService'


export default function LoginLayout() {
    let loginService = new LoginService();
    const history = useHistory()
    let authenticationService = new AuthenticationService();
    const [errorMessage, seterrorMessage] = useState('')
    const [successMessage, setsuccessMessage] = useState('')


    const initialValues = {
        username: "",
        password: ""
    }
    const schema = Yup.object({
        username: Yup.string().required("Username should be filled."),
        password: Yup.string().required("No password provided.").min(5, 'Password is too short - should be 5 chars minimum.'),
    })


    return (
        <Formik
            initialValues={initialValues}
            validationSchema={schema}
            onSubmit={(values, { resetForm }) => {
                loginService.login(values).then((result) => {
                    localStorage.setItem('token', result.data)
                    var decoded = jwt.decode(result.data, { complete: true });
                    axios.defaults.headers.common["Authorization"] = `Bearer ${result.data}`;

                    if (result.data){
                        localStorage.setItem('issue',decoded.payload.sub)
                        history.push("/ssh")
                        window.location.reload(true);
                        
                    }
                })
                    .catch(err => {
                        console.log(err)
                        seterrorMessage("Hatalı giriş")
                        resetForm();

                    })
            }}

        >
            <Grid>
                <Grid.Row>
                    <Grid.Column width={5}>
                    </Grid.Column>
                    <Grid.Column width={6}>
                        <Form className="ui form">
                            <FormField width="12" required className="loginForm">
                                <label style={{ color: 'white' }}>Username</label>
                                <Field name="username" placeholder="Username" ></Field>
                                <ErrorMessage
                                    name="username"
                                    render={error =>
                                        <Label pointing basic color="red" content={error}></Label>
                                    }>

                                </ErrorMessage>
                            </FormField>
                            <FormField width="12" required className="loginForm">
                                <label style={{ color: 'white' }}>Password</label>

                                <Field name="password" placeholder="Password" type="password"></Field>
                                <ErrorMessage
                                    name="password"
                                    render={error =>
                                        <Label pointing basic color="red" content={error}></Label>
                                    }>

                                </ErrorMessage>
                            </FormField>
                            <Button color="green" type="submit">Login</Button>
                            {errorMessage && (
                                <p className="error"> {errorMessage} </p>
                            )}
                            {successMessage && (
                                <p className="success"> {successMessage} </p>
                            )}
                        </Form>
                    </Grid.Column>
                    <Grid.Column width={5}>

                    </Grid.Column>
                </Grid.Row>

            </Grid>


        </Formik>
    )
};
