import { Button } from '@mui/material';
import Box from '@mui/material/Box';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import Select from '@mui/material/Select';
import Typography from '@mui/material/Typography';
import './App.css';
import Information from './components/Information';
import Rating from './components/Rating';
import Reviews from './components/Reviews';
import Competitors from './components/Competitors';
import { getAuthToken, getCognitoUser, getMuseumByName, getMyLocations, getUser } from './utils/apiCalls';
import getParameterByName from './utils/getParameterByName';

import { useEffect, useState } from 'react';



const clientId = process.env.REACT_APP_COGNITO_CLIENT_ID;
const authorize_url_base = process.env.REACT_APP_AUTHORIZE_URL;
const redirect_uri = "http%3A%2F%2Flocalhost%3A3000"
const authorize_url = `${authorize_url_base}?response_type=code&client_id=${clientId}&redirect_uri=${redirect_uri}`;

function App() {

  const accessToken = sessionStorage.getItem("access_token");

  const [user, setUser] = useState(null);
  const [locations, setLocations] = useState(null);
  const [selectedLocation, setSelectedLocation] = useState(null);


  useEffect(() => {
    const code = getParameterByName("code");
    if (code) {
      const getToken = async () => {
        const res = await getAuthToken(code);

        if (res.access_token) {
          sessionStorage.setItem("access_token", res.access_token);
          window.location.href = "http://localhost:3000";
        }


      }
      getToken();
    }
  }, []);

  useEffect(() => {
    const initUser = async () => {
      try {
        const { Username: username } = await getCognitoUser(accessToken);
        const user = await getUser(username);
        const locations = await getMyLocations();

        setUser(user);
        setLocations(locations);
      } catch (e) {
        console.log(e);
      }
    }
    if (accessToken) {
      initUser();
    }
  }, [accessToken]);

  const handleLocationChange = async (event) => {
    const res = await getMuseumByName(event.target.value);
    setSelectedLocation(res);
  }

  const renderSelectLocation = () => {
    return <FormControl fullWidth style={{paddingBottom: "10%"}}>
      <InputLabel id="select-loation-label">Select Museum</InputLabel>
      <Select
        labelId="select-location-label"
        id="select-location"
        value={selectedLocation ? selectedLocation.name : ''}
        label="Select Museum"
        onChange={handleLocationChange}
      >
        {locations.map((location) => {
          return <MenuItem value={location.name}>{location.name}</MenuItem>
        })}

      </Select>
    </FormControl>
  }

  const onEditLocation = async (locationName) => {
    const res = await getMuseumByName(locationName);
    setSelectedLocation(res);
  }

  return (
    <div className="App" style={{ textAlign: 'left' }}>
      <Box sx={{ width: '80%', padding: '10%' }}>
        <Typography variant="h3" gutterBottom>
          Museum Manager App
        </Typography>
        {
          locations ? renderSelectLocation() : null
        }
        {selectedLocation ?
          [<Information location={selectedLocation} onEditLocation={onEditLocation} editable={true}/>,
          <Rating location={selectedLocation} editable={true}/>,
          <Reviews location={selectedLocation} user={user} editable={true}/>] : null}
        {!accessToken ? <Button variant="contained" onClick={() => { window.location.href = authorize_url; }}>Login</Button> : null}

        {locations ? <><Competitors myLocations={locations} /><hr /></> : null}

        {
          user ?
            <Typography variant="body1" gutterBottom>Logged in as: {user.display_name}</Typography> :
            null
        }
        {accessToken ? <Button variant="contained" onClick={() => { sessionStorage.removeItem("access_token"); window.location.reload(); }}>Logout</Button> : null}
      </Box>
    </div>
  );
}

export default App;
