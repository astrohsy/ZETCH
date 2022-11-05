import './App.css';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Information from './components/Information';
import Rating from './components/Rating';
import Reviews from './components/Reviews';

function App() {
  return (
    <div className="App" style={{ textAlign: 'left' }}>
      <Box sx={{ width: '100%', padding: '10%' }}>
        <Typography variant="h3" gutterBottom>
          Museum Manager App
        </Typography>
        <Information />
        <Rating />
        <Reviews />
      </Box>
    </div>
  );
}

export default App;
