import './App.css';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Information from './components/Information';
import Rating from './components/Rating';
import Reviews from './components/Reviews';
import { Button } from '@mui/material';
import { getAuthToken } from './utils/apiCalls';
import getParameterByName from './utils/getParameterByName';

import { useEffect } from 'react';



const clientId = process.env.REACT_APP_COGNITO_CLIENT_ID;
const redirect_uri = "http%3A%2F%2Flocalhost%3A8080"
const authorize_url = `https://zetch-app-4.auth.us-east-1.amazoncognito.com/login?response_type=code&client_id=${clientId}&redirect_uri=${redirect_uri}`;

function App() {

  const token = sessionStorage.getItem("token");

  useEffect(() => {
    const code = getParameterByName("code");
    if (code) {
      const getToken = async () => {
        const res = await getAuthToken(code);

        if (res.access_token) {
          sessionStorage.setItem("token", res.access_token);
          window.location.href = "http://localhost:8080";
        }

      }
      getToken();
    }
  }, []);


  return (
    <div className="App" style={{ textAlign: 'left' }}>
      <Box sx={{ width: '100%', padding: '10%' }}>
        <Typography variant="h3" gutterBottom>
          Museum Manager App
        </Typography>

        {token ?
          [<Information />,
          <Rating />,
          <Reviews />] : null}
        {!token ? <Button variant="contained" onClick={() => { console.log("clicked"); window.location.href = authorize_url; }}>Login</Button> : null}
      </Box>
    </div>
  );
}

export default App;
