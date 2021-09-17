import React,{ useState, useEffect }  from 'react'
import { useParams } from "react-router";
import { FormField, Button, Label,Grid,Icon } from 'semantic-ui-react'
import { Formik, Form, Field, ErrorMessage } from 'formik'
import SshService from '../services/sshService';
import * as Yup from "yup"


export default function ExecuteCommand() {
    let { id } = useParams();
    let sshService = new SshService();
    const [output, setOutput] = useState("");
    const [issuedBy,setIssuedBy] = useState("");
    const [color,setColor]=useState('red');
    const [ipColor,setipColor]=useState('white');
    const [success, setSuccess] = useState("");




    function isConnect() {
        sshService.isConnected(id).then(result => 
            setSuccess(result.data.success)
        )            
        if (success === false) {
                setColor("red")
                setipColor("red")

            }else{
            setColor("MediumSeaGreen")
            setipColor("white")
        }
        };
    


    useEffect(() => {
        if(output.data === undefined){
            setOutput({data : "" , message : ""})            
        }
        setIssuedBy(localStorage.getItem("issue"))
        isConnect()
    }, [output.data])

      
   

    const initialValues = {
        commandRequest: "",
    }
    const schema = Yup.object({
        commandRequest: Yup.string(),
    })

    return (
        <div>
            <Grid>
                <Grid.Row>
                    <Grid.Column width={6}>
                        <Formik
                            initialValues={initialValues}
                            validationSchema={schema}
                            //onSubmit={handleSubmit}
                            onSubmit={(values) => {
                                sshService.setCredentialCommand(id,values.commandRequest).then((result) => {
                                    sshService.executeCommand(result.data.data).then((result) => {
                                        setOutput(result.data)
                                    })
                                        .catch(err => {
                                            console.log(err);
                                        })
                                })
                                    .catch(err => {
                                        console.log(err);
                                    })
                                //setCredential(sshService.setCredentialCommand(id,values.commandRequest))
                                //executeCommand(credential)
                                
                               
                            }}
                        
                        >

                            <Form className="ui form">
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
                                    value={output.message + "\n\n" + output.data}
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
