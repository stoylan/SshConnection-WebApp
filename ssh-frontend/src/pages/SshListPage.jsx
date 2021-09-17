import React, { useState, useEffect } from "react";
import { Icon, Table, Button } from 'semantic-ui-react'
import SshService from "../services/sshService";
import { Link } from "react-router-dom";
import Swal from 'sweetalert2'

export default function SshListPage() {
    const [ssh, setSsh] = useState([]);
    let sshService = new SshService();
    const [color,setColor]=useState('red');

    useEffect(() => {
        
        sshService.getSsh().then(result => 
            setSsh(result.data.data)
        )
    }, [])
    useEffect(() => {
        sshService.setStatus()
    
    }, [])
    function isConnect(isConnect){
        
        if(isConnect === true){
            return(
                <Icon name="circle" size="big" style={{color:'MediumSeaGreen'}} ></Icon>
            )
        }
        else{

            return(
                <Icon name="circle" size="big"style={{color:'red'}} ></Icon>
            )
        }
    }
  
    function deleteSsh(id) {
        Swal.fire({
            title: 'Are you sure?',
            title: 'If you delete the ssh connection all information will be lost.',
            text: 'Are you sure to delete ?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then((result) => {
            if (result.value === true) {
                sshService.deleteSsh(id);
  
              Swal.fire(
                'Deleted!',
                'Your file has been deleted.',
                'success'
              ).then((result) => {
                window.location.reload();
              })
            }
          })
    }
    return (
        <div>
            <div>
                <Table celled inverted selectable>
                    <Table.Header>
                        <Table.Row>
                            <Table.HeaderCell>Hostname</Table.HeaderCell>
                            <Table.HeaderCell>Username</Table.HeaderCell>
                            <Table.HeaderCell>Password</Table.HeaderCell>
                            <Table.HeaderCell>Port Address</Table.HeaderCell>
                            <Table.HeaderCell>Status</Table.HeaderCell>
                            <Table.HeaderCell>Run Command</Table.HeaderCell>
                            <Table.HeaderCell>Logs</Table.HeaderCell>
                            <Table.HeaderCell>Delete</Table.HeaderCell>
                            

                        </Table.Row>
                    </Table.Header>

                    <Table.Body>{

                        ssh.map(sshList => (
                            <Table.Row>
                                <Table.Cell>{sshList.host}</Table.Cell>
                                <Table.Cell>{sshList.username}</Table.Cell>
                                <Table.Cell >{sshList.password}</Table.Cell>
                                <Table.Cell>{sshList.port}</Table.Cell>
                                <Table.Cell>{isConnect(sshList.connection)}</Table.Cell>
                                <Table.Cell ><Link to={"execute/" + sshList.id}><Button primary floated='right' >
                                    Execute
                                    <Icon name='right chevron' />
                                </Button></Link></Table.Cell>
                                <Table.Cell><Link to={"log/"+sshList.id}><Button  floated='right' color='green'>
                                    Logs
                                    <Icon name='right chevron' />
                                </Button></Link></Table.Cell>
                                <Table.Cell><Button  floated='right' color='red' onClick={() => deleteSsh(sshList.id)}>
                                    Delete
                                    <Icon name='right chevron' />
                                </Button></Table.Cell>

                                
                            </Table.Row>

                        ))}

                    </Table.Body>

                </Table>
                <Link to="/ssh/add">
                    <Button positive divided floated='right'>New Connection</Button>
                </Link>

            </div>

        </div>
    )
}
