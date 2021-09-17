import React from 'react'
import { Button, Menu } from 'semantic-ui-react'
import { useHistory } from "react-router";


export default function SignedOut({signIn}) {


    const history = useHistory()

    return (
        <div>
            <Menu.Item>
               <Button  onClick={signIn} primary>Login</Button>
            </Menu.Item>
            
        </div>
    )
}
