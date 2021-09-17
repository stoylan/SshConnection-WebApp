import React, { useState, useEffect } from 'react'
import SshService from '../services/sshService';
import { Formik, Form, Field, ErrorMessage } from 'formik'
import * as Yup from "yup"
import { FormField, Button, Label, Icon } from 'semantic-ui-react'
import '../App.css';
import { Grid } from "semantic-ui-react";



export function Ssh(props) {

    let sshService = new SshService();
    const [name, setName] = useState("");
    const [color,setColor]=useState('red');
    const [ipColor,setipColor]=useState('white');

    useEffect(() => {
        if(name.data === undefined){
            setName({data : "" , message : ""})            
        }
    }, [name.data])

    
    function isConnect() {
        Promise.resolve(sshService.isConnected()).then(function(value){
            if (value.data.success === false) {
                setColor("red")
                setipColor("red")

            }else{
            setColor("MediumSeaGreen")
            setipColor("white")
        }
        },function(value){

        });
    }

    function ethernetAddress() {
        
        Promise.resolve(sshService.getEthernetAddress()).then(function(value){
            if (value.data.data != null) {
                console.log(ipColor)
                
                // get your input
                document.getElementById('ethIp').innerHTML = value.data.data;

                }
                else{
                    document.getElementById('ethIp').innerHTML = "Ethernet Ip Not Found";
                    document.getElementById('localIp').innerHTML = "Local Ip Not Found";    

                }
        },function(value){

        });
    }

    function localAddress() {
        Promise.resolve(sshService.getLocalAddress()).then(function(value){
            console.log(value.data.data)  
            if (value.data.data != null) {
                // get your input
                document.getElementById('localIp').innerHTML = value.data.data;

            }
        },function(value){

        });
    }


    const initialValues = {
        channelTimeout: "",
        commandRequest: "",
        host: "",
        password: "",
        port: "22",
        sessionTimeout: "",
        username: "",
    }
    const schema = Yup.object({
        username: Yup.string().required("Username should be filled."),
        channelTimeout: Yup.string()
            .matches(/^[0-9]+$/, "Must be only digits"),
        commandRequest: Yup.string(),
        host: Yup.string().required(),
        password: Yup.string().required(),
        port: Yup.string().matches(/^[0-9]+$/, "Must be only digits"),
        sessionTimeout: Yup.string().matches(/^[0-9]+$/, "Must be only digits"),
    })


    return (
        <div>

            <Grid>
                <Grid.Row>
                    <Grid.Column width={6}>
                        <Formik
                            initialValues={initialValues}
                            validationSchema={schema}
                            onSubmit={(values) => {
                                
                                sshService.executeCommand(values).then((result) => {
                                    setName(result.data);
                                    isConnect();
                                    ethernetAddress();
                                    localAddress();
                                })
                                    .catch(err => {
                                        console.log(err);
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

                                <FormField width="16" required>
                                    <label style={{ color: 'white' }}>command</label>

                                    <Field name="commandRequest" placeholder="E.g. : ifconfig ; ls -l ; ..."></Field>
                                    <ErrorMessage
                                        name="commandRequest"
                                        render={error =>
                                            <Label pointing basic color="red" content={error}></Label>
                                        }>

                                    </ErrorMessage>
                                </FormField>
                                <Button color="linkedin" type="submit">Execute command</Button>
                            </Form>
                        </Formik>
                    </Grid.Column>
                    <Grid.Column width={1}>
                        <form>
                            <label for="pad">
                                <textarea
                                    className="commandList"
                                    rows="32"
                                    name="pad"
                                    value={name.message + "\n\n" + name.data}
                                ></textarea>
                            </label>
                        </form>
                    </Grid.Column>
                    <Grid.Column width={3}>

                    </Grid.Column>

                    
                </Grid.Row>
            </Grid>
            <div style={{marginLeft:"-15em",marginTop:"-28em"}}>
                <label className="label-text">Server Status</label>
                <br/><br/>
                <Icon name="circle" size="big" style={{marginLeft:"22px",color:color}} ></Icon>
                <br/><br/>

                <label className="label-text"style={{marginLeft:"-1em"}} >Ethernet ip address</label>
                <br/><br/>
                <label id="ethIp" style={{marginLeft:"7px",color:ipColor}}></label>
                <br/><br/>

                <label className="label-text"style={{marginLeft:"-4px"}} >Local ip address</label>
                <br/><br/>
                <label id="localIp" style={{marginLeft:"7px",color:ipColor}}></label>
                <br/>
                </div>

        </div>

    )
}