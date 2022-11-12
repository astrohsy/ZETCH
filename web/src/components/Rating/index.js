import Typography from '@mui/material/Typography';
import { getAverageRatingOfMuseum, getRatingHistogramOfMuseum } from '../../utils/apiCalls';
import { useEffect, useState } from 'react';
import { Box } from '@mui/material';

const Ratings = (props) => {
    //TODO: display average rating
    //TODO: display ratings histogram

    const [averageRating, setAverageRating] = useState(null);
    const [ratingHistogram, setRatingHistogram] = useState(null);

    const init = async () => {
        const avgRatingRes = await getAverageRatingOfMuseum("Columbia Museum");
        if (avgRatingRes) {
            setAverageRating(avgRatingRes.average_rating);
        }
        const histogramRes = await getRatingHistogramOfMuseum("Columbia Museum");
        if (histogramRes) {
            setRatingHistogram(histogramRes.rating_histogram);
        }
    };

    useEffect(() => {
        init();
    }, [])

    return (
        <Box>
            <Typography variant="h6" gutterBottom>
                Rating: {averageRating}
            </Typography>
            <Typography variant="h6" gutterBottom>
                Histogram: {JSON.stringify(ratingHistogram)}
            </Typography>
        </Box>
    );
}

export default Ratings;


