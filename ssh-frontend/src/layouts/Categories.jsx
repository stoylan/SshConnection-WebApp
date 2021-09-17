import React from 'react'
import { Menu,Icon } from 'semantic-ui-react'
import { Link } from "react-router-dom";


export default function Categories() {
  return (
    <div>
      <Menu secondary vertical inverted size="big" position='left' >
      <Menu.Item as={Link} to='/home'>
          <Icon name='home' color='white'></Icon>
          <label style={{color:'white'}}>Home</label>
        </Menu.Item>
        
        <Menu.Item as={Link} to='/ssh/server'>
          <Icon name='terminal' color="white" />
          <label style={{color:'white'}}>          SSH Connections
</label>
        </Menu.Item>

        <Menu.Item as={Link} to='/ssh/list'>
          <Icon name='terminal' color="white" />
          <label style={{color:'white'}}>          SSH List
</label>
        </Menu.Item>



      </Menu>
    </div>
  )
}
