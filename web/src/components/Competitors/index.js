import { Box } from '@mui/material';
import Typography from '@mui/material/Typography';
import { useEffect, useState } from 'react';
import { getMuseums } from '../../utils/apiCalls';
import Information from '../Information';
import Rating from '../Rating';
import Reviews from '../Reviews';


const Competitors = (props) => {
    const { myLocations } = props;

    const [competitors, setCompetitors] = useState([]);


    useEffect(() => {
        const init = async () => {
            const museumsRes = await getMuseums();
            if (museumsRes) {
                setCompetitors(museumsRes.filter(museum => !myLocations.find(location => location.id === museum.id)));
            }
        };
        init();
    }, [myLocations])

    const renderCompetitors = () => {
        return competitors.map(competitor => {
            return <div key={`competitors_${competitor.id}`}>
                <Information location={competitor} editable={false} />
                <Rating location={competitor} />
                <Reviews location={competitor} editable={false} />
            </div>
        })
    }

    return (
        <Box>
            <Typography variant="h4" gutterBottom>
                Competitors
            </Typography>
            {renderCompetitors()}
        </Box>
    );
}

export default Competitors;


