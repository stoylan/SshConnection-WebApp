import React from 'react'
import Categories from '../layouts/Categories'
import { Grid } from "semantic-ui-react";
import { Route } from 'react-router';
import LoginLayout from '../layouts/LoginLayout'
import { Ssh } from '../pages/Ssh';
import { PrivateRoute } from '../Components/PrivateRoute';
import homepage from '../pages/HomePage';
import SshListPage from '../pages/SshListPage';
import SshAdd from '../pages/SshAdd';
import ExecuteCommand from '../pages/ExecuteCommand';
import LogPage from '../pages/LogPage';


export default function Dashboard() {

    return (
        <div>
            <Grid>
                <Grid.Row>
                    <Grid.Column width={4}>
                        <Route path="/ssh" >
                            <Categories></Categories>
                        </Route>
                    </Grid.Column>
                    <Grid.Column width={12}>
                    <PrivateRoute exact path="/ssh/server" component={Ssh} />
                    <PrivateRoute exact path="/ssh/list" component={SshListPage} />
                    <PrivateRoute exact path="/ssh/add" component={SshAdd} />
                    <PrivateRoute exact path="/ssh/execute/:id" component={ExecuteCommand} />
                    <PrivateRoute exact path="/ssh/log/:id" component={LogPage}></PrivateRoute>

                    </Grid.Column>
                    <Grid.Column width={16} >
                        <Route exact path="/login" component={LoginLayout} />
                        <Route exact path="/Home" component={homepage}/>
                    </Grid.Column>


                </Grid.Row>

            </Grid>
        </div>
    )
}
