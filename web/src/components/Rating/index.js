import Typography from '@mui/material/Typography';
import { getAverageRatingOfMuseum, getRatingHistogramOfMuseum } from '../../utils/apiCalls';
import { useEffect, useState } from 'react';
import { Box } from '@mui/material';
import ReactECharts from 'echarts-for-react';


const Ratings = (props) => {
    const { location } = props;
    //TODO: display average rating
    //TODO: display ratings histogram

    const [averageRating, setAverageRating] = useState(null);
    const [ratingHistogram, setRatingHistogram] = useState(null);



    useEffect(() => {
        const init = async () => {
            const avgRatingRes = await getAverageRatingOfMuseum(location.name);
            if (avgRatingRes) {
                setAverageRating(avgRatingRes.average_rating);
            }
            const histogramRes = await getRatingHistogramOfMuseum(location.name);
            if (histogramRes) {
                setRatingHistogram(histogramRes.rating_histogram);
            }
        };

        init();
    }, [location.name])

    const renderHistogram = () => {
        if (!ratingHistogram) return null;
        const option = {
            xAxis: {
                type: 'category',
                data: ['1', '2', '3', '4', '5']
            },
            yAxis: {
                type: 'value',
                minInterval: 1
            },
            series: [
                {
                    data: [ratingHistogram["1"], ratingHistogram["2"], ratingHistogram["3"], ratingHistogram["4"], ratingHistogram["5"]],
                    type: 'bar'
                }
            ]
        };

        return <ReactECharts option={option} />

    }

    return (
        <Box>
            <Typography variant="h6" gutterBottom>
                Average Rating: {averageRating}
            </Typography>
            <Typography variant="h6" >
                Ratings Histogram
            </Typography>
            {renderHistogram()}
        </Box>
    );
}

export default Ratings;


