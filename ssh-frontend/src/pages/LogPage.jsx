import React, { useState, useEffect } from 'react'
import { Accordion, Icon, Tab, Table,Menu,Pagination,Button } from 'semantic-ui-react'
import { useParams } from "react-router";
import CommandService from "../services/commandService"
import SshService from "../services/sshService"
export default function LogPage() {

  const [log, setLog] = useState([]);
  const [state, setState] = useState([0])
  const [ssh, setssh] = useState("")
  let commandService = new CommandService();
  let sshService = new SshService(); 
  let { id } = useParams();
  let count = 0;
  const [activePage, setActivePage] = useState(1);
  const [pageSize, setPageSize] = useState(4);

  function setCurrentSsh(id){
    sshService.getSshWithId(id).then(result => (
      setssh(result.data.data)
    ))
  }

  let pageAble = (pageNo) => {
    setPageSize(pageNo);
  };

  const handleClick = (e, titleProps) => {
    const { index } = titleProps
    const { activeIndex } = state
    const newIndex = activeIndex === index ? -1 : index

    setState({ activeIndex: newIndex })
  }

  const onChange = (e, pageInfo) => {
    setActivePage(pageInfo.activePage);
  };

  useEffect(() => {
    commandService.getAllCommandByCredentialIdAndPageable(id,activePage,pageSize).then(result =>
      setLog(result.data.data),
    )
  }, [activePage,pageSize])

  useEffect(() => {
    setCurrentSsh(id);
  }, [])
  const { activeIndex } = state
  return (

    <div>
      <div>
        <Table celled inverted selectable>
          <Table.Header>
            <Table.Row>
              <Table.HeaderCell>Hostname : {ssh.host} &emsp;&emsp;&emsp;&emsp; Username : {ssh.username} &emsp;&emsp;&emsp;&emsp; Port Address : {ssh.port}</Table.HeaderCell>

            </Table.Row>
          </Table.Header>
          <Table.Body>
          {

            log.map(logList => (
              <div>
              <Table.Row>
                <Table.Cell>
                <Accordion>
                <Accordion.Title
                  active={activeIndex ===  count++}
                  index={count}
                  onClick={handleClick}
                >
                  <Icon name='angle right' color="green" />
                  <label style={{ color: 'white' }}>Command : {logList.command}</label>
                  <br></br>
                  <label style={{ color: 'white' }}>Time : {logList.executedDate} </label>
                  <br></br>
                  <label style={{ color: 'white' }}>Issued by : {logList.issuedBy}</label>
                </Accordion.Title>
                <Accordion.Content active={activeIndex === count}>
                  <textarea class="divtext" style={{ color: 'lime' }}>{logList.response}</textarea>

                </Accordion.Content>
              </Accordion>
                </Table.Cell>
              </Table.Row>



              </div>
              
              

            ))
          }
         </Table.Body>
         <Table.Footer>
          <Table.Row >
            <Table.HeaderCell colSpan="8" inverted>
              <Menu pagination color='blue'>
                <Menu.Item as="a" icon>
                  <Pagination
                    activePage={activePage}
                    onPageChange={onChange}
                    totalPages={10}
                  />
                  &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
                  <Button.Group style={{ borderRadius: 10}}>
                    <Button onClick={() => pageAble(3)}>3</Button>
                    <Button onClick={() => pageAble(5)}>5</Button>
                    <Button onClick={() => pageAble(7)}>7</Button>
                    <Button onClick={() => pageAble(100)}>100</Button>

                  </Button.Group>
                </Menu.Item>
              </Menu>
            </Table.HeaderCell>
          </Table.Row>
        </Table.Footer>
             </Table>

      </div>
    </div>

      )
}
