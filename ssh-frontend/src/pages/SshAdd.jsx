import React from 'react'
import SshService from '../services/sshService';
import { Formik, Form, Field, ErrorMessage } from 'formik'
import * as Yup from "yup"
import { FormField, Button, Label } from 'semantic-ui-react'
import '../App.css';
import { Grid } from "semantic-ui-react";
import Swal from 'sweetalert2'
import { useHistory } from "react-router";


export default function SshAdd() {
    let sshService = new SshService();
    const history = useHistory()


    const initialValues = {
        host: "",
        port: "",
        username: "",
        password: "",
        sessionTimeout: "",
        channelTimeout: "",
    }
    const schema = Yup.object({
        username: Yup.string().required("Username should be filled."),
        channelTimeout: Yup.string()
            .matches(/^[0-9]+$/, "Must be only digits"),
        host: Yup.string().required(),
        password: Yup.string().required(),
        port: Yup.string().matches(/^[0-9]+$/, "Must be only digits"),
        sessionTimeout: Yup.string().matches(/^[0-9]+$/, "Must be only digits"),
    })
    return (
        <div>
            <Grid>
                <Grid.Row>
                <Grid.Column width={3}>
                    </Grid.Column>
                    <Grid.Column width={6}>
                        <Formik
                            initialValues={initialValues}
                            validationSchema={schema}
                            //onSubmit={handleSubmit}
                            onSubmit={(values) => {
                                sshService.addSsh(values);
                                Swal.fire({
                                    title: 'Succesfully added new connection.',
                                    type: 'success',
                                    confirmButtonText: 'OK'
                                  }).then((result) => {
                                    history.push("/ssh/list")
                                  })
                            }}

                        >

                            <Form className="ui form">
                                <FormField width="16" required >
                                    <label style={{ color: 'white' }}>Username</label>
                                    <Field name="username" placeholder="Username"></Field>
                                    <ErrorMessage
                                        name="username"
                                        render={error =>
                                            <Label pointing basic color="red" content={error}></Label>
                                        }>

                                    </ErrorMessage>
                                </FormField>
                                <FormField width="16" required>
                                    <label style={{ color: 'white' }}>Password</label>

                                    <Field name="password" placeholder="Password" type="password"></Field>
                                    <ErrorMessage
                                        name="password"
                                        render={error =>
                                            <Label pointing basic color="red" content={error}></Label>
                                        }>

                                    </ErrorMessage>
                                </FormField>

                                <FormField width="16" required>
                                    <label style={{ color: 'white' }}>host</label>

                                    <Field name="host" placeholder="host"></Field>
                                    <ErrorMessage
                                        name="host"
                                        render={error =>
                                            <Label pointing basic color="red" content={error}></Label>
                                        }>

                                    </ErrorMessage>
                                </FormField>

                                <FormField width="16" required>
                                    <label style={{ color: 'white' }}>Port</label>

                                    <Field name="port" placeholder="port"></Field>
                                    <ErrorMessage
                                        name="port"
                                        render={error =>
                                            <Label pointing basic color="red" content={error}></Label>
                                        }>

                                    </ErrorMessage>
                                </FormField>

                                <FormField width="16">
                                    <label style={{ color: 'white' }}>channelTimeout</label>

                                    <Field name="channelTimeout" placeholder="channelTimeout"></Field>
                                    <ErrorMessage
                                        name="channelTimeout"
                                        render={error =>
                                            <Label pointing basic color="red" content={error}></Label>
                                        }>

                                    </ErrorMessage>
                                </FormField>

                                <FormField width="16">
                                    <label style={{ color: 'white' }}>sessionTimeout</label>

                                    <Field name="sessionTimeout" placeholder="sessionTimeout"></Field>
                                    <ErrorMessage
                                        name="sessionTimeout"
                                        render={error =>
                                            <Label pointing basic color="red" content={error}></Label>
                                        }>

                                    </ErrorMessage>
                                </FormField>

                               
                                <Button color="linkedin" type="submit">Add Ssh Connection</Button>
                            </Form>
                        </Formik>
                    </Grid.Column>
                  


                </Grid.Row>
            </Grid>
        

        </div>

    )
}
