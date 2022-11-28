import Typography from '@mui/material/Typography';
import { getReviewsForMuseum, replyToReview, getRepliesToReview, deleteReply } from '../../utils/apiCalls';
import { useCallback, useEffect, useState } from 'react';
import { Box, Button } from '@mui/material';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';


const Reviews = (props) => {
    const { location, user, editable } = props;

    const [reviews, setReviews] = useState([]);



    useEffect(() => {
        const init = async () => {
            const reviewsRes = await getReviewsForMuseum(location.id);
            if (reviewsRes) {
                setReviews(reviewsRes);
            }
        };
        init();
    }, [location.id])

    const renderReviews = () => {
        return reviews.map(review => {
            return <div key={`review_${review.id}`}>
                <div>Comment: {review.comment}</div>
                <div>Rating: {review.rating}</div>
                <div style={{ paddingBottom: 10 }}>User: {review.user.display_name}</div>
                <ViewRepliesDialog key={`reply_dialog_${review.id}`} review={review} user={user} editable={editable} />
                <hr />
            </div>
        })
    }

    return (
        <Box>
            <Typography variant="h4" gutterBottom>
                Reviews
            </Typography>
            {renderReviews()}
        </Box>
    );
}

const ViewRepliesDialog = (props) => {
    const { review, user, editable } = props;

    const [open, setOpen] = useState(false);
    const [replies, setReplies] = useState([]);

    const init = useCallback(async () => {
        const repliesRes = await getRepliesToReview(review.id);
        if (repliesRes) {
            setReplies(repliesRes);
        }
    }, [review.id]);

    useEffect(() => {
        init();
    }, [init, review.id])

    const onPostReply = useCallback(() => {
        init()
    }, [init])


    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleDeleteReply = async (replyId) => {
        await deleteReply(replyId);
        init();
    }


    return (
        <div>
            <Button variant="contained" onClick={handleClickOpen}>
                View Replies
            </Button>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Replies</DialogTitle>
                <DialogContent>
                    {replies.length === 0 ? <DialogContentText>No existing replies</DialogContentText> :
                        replies.map(reply => {
                            return <div key={`reply_comment_${reply.id}`}>
                                <div style={{ flexDirection: "row", justifyContent: "space-between", display: "flex", alignItems: "center" }}>
                                    <div style={{ paddingRight: 20 }}>
                                        {reply.reply_comment}
                                    </div>
                                    {editable ? <Button variant="contained" onClick={() => handleDeleteReply(reply.id)}>Delete Reply</Button> : null}
                                </div>
                                <hr />
                            </div>
                        })}
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Close</Button>
                    {editable ? <PostReplyDialog key={`post_reply_${review.id}`} review={review} onPostReply={onPostReply} user={user}>Add Reply</PostReplyDialog> : null}
                </DialogActions>
            </Dialog>
        </div>
    );
}

const PostReplyDialog = (props) => {
    const { review, user, onPostReply } = props;

    const [open, setOpen] = useState(false);
    const [reply, setReply] = useState('');

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleSubmit = useCallback(async () => {
        await replyToReview(review.id, user.id, reply)
        onPostReply();
        handleClose();
    }, [onPostReply, reply, review.id, user.id])


    return (
        <div>
            <Button variant="contained" onClick={handleClickOpen}>
                Add Reply
            </Button>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Post Reply</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="reply"
                        label="Reply"
                        multiline
                        onChange={(e) => {
                            setReply(e.target.value);
                        }}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleSubmit}>Confirm</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}

export default Reviews;


